package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis on 17/11/15.
 */
public class LifeGridLayout extends GridLayout {
    Context context;
    AttributeSet attrs;
    LifeCellView lifeCell;
    float leftOrigin;
    float topOrigin;
    int cellPixelSize;
    boolean bringingCellsToLife;
    private GameStateCallback gameStateCallback;
    int delayBeforeFabDisappears = getResources().getInteger(R.integer.delay_before_fab_disappears);


    public LifeGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        //this.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
    }

    public void setCallback(GameStateCallback callback){
        gameStateCallback = callback;
    }


    public void initialiseLifeGridLayout(){
        for (int x=0; x<getColumnCount(); x++){
            for(int y=0; y<getRowCount(); y++){
                lifeCell = new LifeCellView(context, attrs);
                this.addView(lifeCell);
            }
        }
        lifeCell = (LifeCellView) getChildAt(0);
        final ViewTreeObserver.OnGlobalLayoutListener listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                leftOrigin = lifeCell.getX();
                topOrigin = lifeCell.getY();
                cellPixelSize = lifeCell.getWidth();
            }
        };
        lifeCell.getViewTreeObserver().addOnGlobalLayoutListener(listener);
        /**lifeCell.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                leftOrigin = lifeCell.getX();
                topOrigin = lifeCell.getY();
                cellPixelSize = lifeCell.getWidth();
            }
        });*/
        /**lifeCell.post(new Runnable() {
            @Override
            public void run() {
                //after layout is drawn, use first cell as an anchor to work out any coordinates

                leftOrigin = lifeCell.getX();
                topOrigin = lifeCell.getY();
                cellPixelSize = lifeCell.getWidth();
            }
        });*/
        this.setOnTouchListener(new OnTouchListener() {
            long touchStartTime;
            long touchEventCurrentTime;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //if cellPixelSize is zero, grid has not been fully initialised so don't continue
                //touch event
                if (cellPixelSize == 0){
                    return false;
                }
                //calculate the grid position based on finger location on the screen
                int xGridPosition = (int) (event.getX() - leftOrigin) / cellPixelSize;
                int yGridPosition = (int) (event.getY() - topOrigin) / cellPixelSize;
                //check that calculated positions are within range of grid
                if (0 <= xGridPosition && xGridPosition < getColumnCount()
                        && 0 <= yGridPosition && yGridPosition < getRowCount()) {

                    int touchedCellIndex = xGridPosition + yGridPosition * getColumnCount();
                    lifeCell = (LifeCellView) getChildAt(touchedCellIndex);
                    //each continuous touch movement will be either giving life to cells or killing them
                    //the first cell touched determines which action is being performed
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            lifeCell.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
                            touchStartTime = SystemClock.elapsedRealtime();
                            if (lifeCell.isCellAlive()) {
                                lifeCell.makeCellViewDead();
                                bringingCellsToLife = false;
                            } else {
                                lifeCell.makeCellViewLive();
                                bringingCellsToLife = true;
                            }
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (bringingCellsToLife) {
                                lifeCell.makeCellViewLive();
                            } else {
                                lifeCell.makeCellViewDead();
                            }
                            touchEventCurrentTime = SystemClock.elapsedRealtime();
                            //ensure series of short touch events don't cause button to repeatedly
                            //disappear and reappear, only sustained touch events trigger this
                            if(touchEventCurrentTime - touchStartTime > delayBeforeFabDisappears){
                                gameStateCallback.cellDrawingInProgress();
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            gameStateCallback.cellDrawingFinished();
                            return false;
                    }
                } else {
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        gameStateCallback.cellDrawingFinished();
                        return false;
                    }
                }
                return true;
            }
        });
    }

    public List<GridCoordinates> getUserSetLiveCellCoordinates() {
        List<GridCoordinates> userSelectedLiveCells = new ArrayList<>();
        for (int i=0; i < getChildCount(); i++) {
            lifeCell = (LifeCellView)getChildAt(i);
            if(lifeCell.isCellAlive()){
                int x = i % getColumnCount();
                int y = i/getColumnCount();
                userSelectedLiveCells.add(new GridCoordinates(x, y));
            }
        }
        return userSelectedLiveCells;
    }

    public void setNewLiveCells(List<GridCoordinates> newLiveCells) {
        List<Integer> indexOfLiveCells = new ArrayList<>();
        for (int j=0; j<newLiveCells.size(); j++) {
            indexOfLiveCells.add(newLiveCells.get(j).getX()+(getColumnCount()*newLiveCells.get(j).getY()));
        }
        for (int i=0; i < getChildCount(); i++) {
            if (indexOfLiveCells.contains(i)) {
                lifeCell = (LifeCellView)getChildAt(i);
                lifeCell.makeCellViewLive();
            } else {
                lifeCell = (LifeCellView)getChildAt(i);
                lifeCell.makeCellViewDead();
            }
        }
    }

    public void killAllCells(){
        for(int i=0; i<getChildCount(); i++) {
            lifeCell = (LifeCellView)getChildAt(i);
            lifeCell.makeCellViewDead();
        }
    }

    public void noCellsWereSelected(){
        gameStateCallback.noCellsWereSelected();
    }

    public void cellsDiedGameOver(){
        gameStateCallback.gameOver();
    }
    //can disable this as no scrolling needed
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

}
