package io.github.lewismcgeary.androidgameoflife;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

/**
 * Created by Lewis on 16/11/15.
 */
public class GridPresenter {
    Grid worldGrid;
    LifeGridLayout worldGridLayout;
    int coordinates[][];
    //Context context;
    int moveDuration;

    public GridPresenter(Context context, LifeGridLayout newWorldGridLayout) {
       // this.context = context;
        worldGrid = new Grid(context.getResources().getInteger(R.integer.grid_width),context.getResources().getInteger(R.integer.grid_height));
        worldGridLayout = newWorldGridLayout;
        moveDuration = context.getResources().getInteger(R.integer.move_duration);

    }

    public Grid getWorldGrid() {
        return worldGrid;
    }

    public void setInitialState() {
        worldGridLayout.initialiseLifeGridLayout();
    }

    public void passLiveCellsToModelAndStartGame(){
        worldGrid.setInitialLiveCells(worldGridLayout.getUserSetLiveCellCoordinates());
        startConstantUpdate();
    }

    public List<GridCoordinates> getLiveCells(){
        return worldGrid.getLiveCells();
    }

    public void updateGrid(){
        new CalculateUpdateTask().execute();

    }

    public void resetGrid(){
        if(mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
        worldGrid.killAllCells();
        worldGridLayout.killAllCells();
    }

    Handler mHandler;
    public void startConstantUpdate() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, 0);
    }

    private Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            updateGrid();
            mHandler.postDelayed(mRunnable, moveDuration);
        }
    };

    private class CalculateUpdateTask extends AsyncTask<Void, Void, List<GridCoordinates>> {

        @Override
        protected List<GridCoordinates> doInBackground(Void... params) {
            worldGrid.calculateNextStateOfCells();
            worldGrid.switchCellsToNextState();
            return worldGrid.getLiveCells();
        }

        @Override
        protected void onPostExecute(List<GridCoordinates> newListOfCells) {
            super.onPostExecute(newListOfCells);
            worldGridLayout.setNewLiveCells(newListOfCells);

        }
    }
}
