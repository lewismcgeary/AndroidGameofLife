package io.github.lewismcgeary.androidgameoflife;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class LifeGameActivity extends AppCompatActivity implements LifeGridFragment.OnFragmentInteractionListener {
    GridPresenter worldGridPresenter;
    LifeGridLayout worldGridLayout;
    FloatingActionButton startResetFab;
    String startButtonText;
    String resetButtonText;
    Drawable playIcon;
    Drawable resetIcon;
    Snackbar snack;
    LifeGridFragment lifeGridFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFixedScreenOrientation();
        setContentView(R.layout.activity_life_game);
        /** worldGridLayout = (LifeGridLayout)findViewById(R.id.life_grid_layout);
        worldGridLayout.setCallback(this);
        worldGridLayout.post(new Runnable() {
            @Override
            public void run() {
                setUpGrid();
            }
        }); */
        FrameLayout container = (FrameLayout)findViewById(R.id.container);
        lifeGridFragment = LifeGridFragment.newInstance(null, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, lifeGridFragment);
        fragmentTransaction.commit();
        startResetFab = (FloatingActionButton)findViewById(R.id.start_reset_fab);
        startButtonText = getString(R.string.start_button_text);
        resetButtonText = getString(R.string.reset_button_text);
        playIcon = getDrawable(R.drawable.ic_play_arrow_24dp);
        resetIcon = getDrawable(R.drawable.ic_replay_24dp);
        startResetFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startResetFab.getTag().equals(startButtonText)) {
                    showButtonInResetMode();
                    lifeGridFragment.worldGridPresenter.passLiveCellsToModelAndStartGame();
                } else {
                    showButtonInStartMode();
                    lifeGridFragment.worldGridPresenter.resetGrid();
                }
            }
        });
    }

    private void setFixedScreenOrientation(){
        //game can be started in any orientation but once GameActivity is reached it will stay in
        //the orientation it starts in
        int orientation = getWindowManager().getDefaultDisplay().getRotation();

        switch(orientation) {
            case Surface.ROTATION_180:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
                break;
            case Surface.ROTATION_270:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                break;
            case  Surface.ROTATION_0:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case Surface.ROTATION_90:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
        }
    }

    private void setUpGrid() {
        int densityOfScreen = DisplayMetrics.DENSITY_DEFAULT;
        int cellSize = (int) getResources().getDimension(R.dimen.cell_size);
        int moveDuration = getResources().getInteger(R.integer.move_duration);
        //get the width and height of grid in dp
        int gridWidth = worldGridLayout.getMeasuredWidth() / (densityOfScreen / 160);
        int gridHeight = worldGridLayout.getMeasuredHeight() / (densityOfScreen / 160);
        //calculate number of columns and rows that will fit on screen
        int numberOfColumns = gridWidth/cellSize;
        int numberOfRows = gridHeight/cellSize;
        //calculate the margins needed to centre the cells in the layout
        float unusedWidthDp = gridWidth - numberOfColumns * cellSize;
        float unusedHeightDp = gridHeight - numberOfRows * cellSize;
        float unusedWidthPx = unusedWidthDp * (densityOfScreen / 160);
        float unusedHeightPx = unusedHeightDp * (densityOfScreen / 160);
        int leftRightMargin = (int) unusedWidthPx / 2;
        int topBottomMargin = (int) unusedHeightPx / 2;
        //apply the calculated values
        FrameLayout.LayoutParams worldGridLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        worldGridLayoutParams.setMargins(leftRightMargin, topBottomMargin, leftRightMargin, topBottomMargin);
        worldGridLayout.setLayoutParams(worldGridLayoutParams);
        worldGridLayout.setColumnCount(numberOfColumns);
        worldGridLayout.setRowCount(numberOfRows);
        worldGridPresenter = new GridPresenter(worldGridLayout, moveDuration);
        worldGridPresenter.setInitialState();
    }

    private void showButtonInStartMode(){
        startResetFab.setTag(startButtonText);
        startResetFab.setImageDrawable(playIcon);
    }

    private void showButtonInResetMode(){
        startResetFab.setTag(resetButtonText);
        startResetFab.setImageDrawable(resetIcon);
    }

    boolean fabShouldBeShown;
    //this listener deals with occasions where button remains in the wrong shown/hidden state
    //which happens if show or hide is called while it is still animating
    FloatingActionButton.OnVisibilityChangedListener fabVisibilityListener = new FloatingActionButton.OnVisibilityChangedListener() {
        @Override
        public void onShown(FloatingActionButton fab) {
            super.onShown(fab);
            if(!fabShouldBeShown){
                fab.hide();
            }
        }

        @Override
        public void onHidden(FloatingActionButton fab) {
            super.onHidden(fab);
            if(fabShouldBeShown){
                fab.show();
            }
        }
    };

    @Override
    public void cellDrawingInProgress() {
        fabShouldBeShown = false;
        startResetFab.hide(fabVisibilityListener);
    }

    @Override
    public void cellDrawingFinished() {
        fabShouldBeShown = true;
        //Fab can be stuck in wrong position when snackbar appears and disappears. this check ensures
        //fab is back in the correct location
        if(snack != null) {
            if(!snack.isShown()) {
                startResetFab.setTranslationY(0.0f);
            }
        }
        startResetFab.show(fabVisibilityListener);
    }

    @Override
    public void gameOver(){
        snack = Snackbar.make(startResetFab, R.string.game_over_snackbar_text, Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        snack.show();
        showButtonInStartMode();
    }

    @Override
    public void noCellsWereSelected() {
        snack = Snackbar.make(startResetFab, R.string.starting_blank_game_snackbar_text , Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        snack.show();
        showButtonInStartMode();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
