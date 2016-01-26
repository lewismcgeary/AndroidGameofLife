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
        List<GridCoordinates> userSelectedCells = worldGridLayout.getUserSetLiveCellCoordinates();
        if(userSelectedCells.size() != 0) {
            worldGrid.setInitialLiveCells(userSelectedCells);
            startConstantUpdate();
            worldGridLayout.setEnabled(false);
        } else {
            worldGridLayout.noCellsWereSelected();
        }
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
        worldGridLayout.setEnabled(true);
    }

    private class CalculateUpdateTask extends AsyncTask<Void, List<GridCoordinates>, Void> {
        Handler asyncHandler = new Handler();
        Runnable asyncRunnable = new Runnable() {
            @Override
            public void run() {
                if(isCancelled()){
                    asyncHandler.removeCallbacks(asyncRunnable);
                    asyncRunnable = null;
                } else {
                    publishProgress(worldGrid.getLiveCells());
                }
                worldGrid.calculateNextStateOfCells();
                worldGrid.switchCellsToNextState();
                asyncHandler.postDelayed(asyncRunnable, moveDuration);
            }
        };

        @Override
        protected void onCancelled() {
            super.onCancelled();
            asyncHandler.removeCallbacks(asyncRunnable);
            asyncRunnable = null;
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
            worldGridLayout.setNewLiveCells(newListOfLiveCells[0]);
            if(newListOfLiveCells[0].size() == 0){
                worldGridLayout.cellsDiedGameOver();
                resetGrid();
            }
        }

    }
}
