package io.github.lewismcgeary.androidgameoflife;

/**
 * Created by Lewis on 14/11/15.
 */
public class GridCell {
    private boolean state = false;
    private boolean nextState = false;

    public GridCell(boolean state) {
        this.state=state;
    }

    public boolean getState() {
        return state;
    }

    public boolean getNextState() {
        return nextState;
    }

    public void computeNextState(Grid worldGrid, int x, int y) {
        int liveCount = countLivingNeighbours(worldGrid, x, y);
        nextState = applyLifeRules(liveCount);

    }

    public void switchToNextState() {
        state = nextState;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int countLivingNeighbours(Grid worldGrid, int x, int y) {
        int liveNeighbourCount = 0;
        for(int i = Math.max(0, x-1);i < Math.min(worldGrid.getxLength(), x+2); i++){
            for(int j = Math.max(0, y-1); j < Math.min(worldGrid.getyLength(), y+2); j++){
                if (worldGrid.getCell(i,j).getState() && !(i==x && j==y)){
                    liveNeighbourCount++;
                }
            }

        }
        return liveNeighbourCount;
    }

    public boolean applyLifeRules(int liveCount) {
        boolean currentState = state;
        if(liveCount>3) {
            return false;
        }
        if(liveCount==3) {
            return true;
        }
        if(liveCount==2) {
            return currentState;
        }
        return false;
    }
}
