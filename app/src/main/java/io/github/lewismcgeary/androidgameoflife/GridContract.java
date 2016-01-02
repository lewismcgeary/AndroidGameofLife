package io.github.lewismcgeary.androidgameoflife;

/**
 * Created by Lewis on 14/12/15.
 */
public interface GridContract {
    interface View {
        void displayNewGridState();
        void killAllCells();
    }
    interface GridModel {

    }
}
