package io.github.lewismcgeary.androidgameoflife;

/**
 * Created by Lewis on 26/01/16.
 */
public interface GameStateCallback {

    void gameOver();

    void noCellsWereSelected();

    void cellDrawingInProgress();

    void cellDrawingFinished();
}
