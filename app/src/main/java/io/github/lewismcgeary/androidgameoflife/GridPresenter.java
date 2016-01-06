package io.github.lewismcgeary.androidgameoflife;

import android.os.AsyncTask;
import android.os.Handler;

import java.util.List;

/**
 * Created by Lewis on 16/11/15.
 */
public class GridPresenter {
    Grid worldGrid;
    LifeGridLayout worldGridLayout;
    int moveDuration;
    private CalculateUpdateTask calculateUpdateTask;

    public GridPresenter(LifeGridLayout newWorldGridLayout, int moveDuration) {
        worldGridLayout = newWorldGridLayout;
        worldGrid = new Grid(worldGridLayout.getColumnCount(), worldGridLayout.getRowCount());
        this.moveDuration = moveDuration;

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

    public void startConstantUpdate() {
        calculateUpdateTask = new CalculateUpdateTask();
        calculateUpdateTask.execute();
    }

    public void resetGrid(){
        calculateUpdateTask.cancel(true);
        worldGrid.killAllCells();
        worldGridLayout.killAllCells();
    }

    private class CalculateUpdateTask extends AsyncTask<Void, List<GridCoordinates>, Void> {
        Handler asyncHandler = new Handler();
        final Runnable asyncRunnable = new Runnable() {
            @Override
            public void run() {
                worldGrid.calculateNextStateOfCells();
                worldGrid.switchCellsToNextState();
                if(isCancelled()){
                    asyncHandler.removeCallbacks(asyncRunnable);
                } else {
                    publishProgress(worldGrid.getLiveCells());
                }
                asyncHandler.postDelayed(asyncRunnable, moveDuration);
            }
        };

        @Override
        protected void onCancelled() {
            super.onCancelled();
            asyncHandler.removeCallbacks(asyncRunnable);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            asyncHandler.postDelayed(asyncRunnable, 0);
            return null;

        }

        @Override
        protected void onProgressUpdate(List<GridCoordinates>... newListOfLiveCells) {
            super.onProgressUpdate(newListOfLiveCells);
            if(newListOfLiveCells[0].size() == 0){
                resetGrid();
                worldGridLayout.cellsDiedGameOver();
            } else {
                worldGridLayout.setNewLiveCells(newListOfLiveCells[0]);
            }
        }

    }
}
