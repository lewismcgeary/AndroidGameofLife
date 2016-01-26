package io.github.lewismcgeary.androidgameoflife;

/**
 * Created by Lewis on 26/01/16.
 */
public interface GameStateCallback {

    public void gameOver();

    public void noCellsWereSelected();

    public void cellDrawingInProgress();

    public void cellDrawingFinished();
}
