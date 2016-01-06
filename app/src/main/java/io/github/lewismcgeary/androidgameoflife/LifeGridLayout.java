package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
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


    public LifeGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.attrs = attrs;
        this.setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
    }




    public void initialiseLifeGridLayout(){
        OnTouchListener cellTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LifeCellView touchedCell = (LifeCellView)v;
                //cells can be toggled between living or dead by user at start of game
                if(touchedCell.getState()) {
                    touchedCell.makeCellViewDead();
                } else {
                    touchedCell.makeCellViewLive();
                }
                return false;
            }
        };
        for (int x=0; x<getColumnCount(); x++){
            for(int y=0; y<getRowCount(); y++){
                lifeCell = new LifeCellView(context, attrs);
                lifeCell.setOnTouchListener(cellTouchListener);
                this.addView(lifeCell);
            }
        }

    }

    public List<GridCoordinates> getUserSetLiveCellCoordinates() {
        List<GridCoordinates> userSelectedLiveCells = new ArrayList<>();
        for (int i=0; i < getChildCount(); i++) {
            lifeCell = (LifeCellView)getChildAt(i);
            //disable clicking of cells during game
            lifeCell.setEnabled(false);
            if(lifeCell.getState()){
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
            //re-enable clicking of cells for next game
            lifeCell.makeCellViewDead();
            lifeCell.setEnabled(true);
        }
    }

    public void cellsDiedGameOver(){
        if(context instanceof MainActivity){
            MainActivity activity = (MainActivity)context;
            activity.gameOver();
        }
    }
    //can disable this as no scrolling needed
    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
