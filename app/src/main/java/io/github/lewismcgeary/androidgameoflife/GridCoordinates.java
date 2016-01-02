package io.github.lewismcgeary.androidgameoflife;

/**
 * Created by Lewis on 17/11/15.
 */
public class GridCoordinates {
    int x;
    int y;

    public GridCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
