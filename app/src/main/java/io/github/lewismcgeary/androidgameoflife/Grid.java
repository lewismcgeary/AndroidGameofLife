package io.github.lewismcgeary.androidgameoflife;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lewis on 14/11/15.
 */
public class Grid {

    private final int xLength;
    private final int yLength;

    private List<List<GridCell>> members;

    Grid(int xLength, int yLength) {
        this.xLength=xLength;
        this.yLength=yLength;

        members=new ArrayList<>();
        for(int x=0;x<xLength;x++) {
            members.add(new ArrayList<GridCell>());
            for(int y=0; y<yLength; y++) {
                members.get(x).add(new GridCell(false));
            }
        }


    }

    public void setInitialLiveCells(List<GridCoordinates> liveCellCoordinates) {
        for (int i =0; i < liveCellCoordinates.size(); i++){
            int x=liveCellCoordinates.get(i).getX();
            int y= liveCellCoordinates.get(i).getY();
            //check that coordinates are valid
            if (x < xLength && y < yLength) {
                //set specified cells to be alive
                members.get(liveCellCoordinates.get(i).getX()).get(liveCellCoordinates.get(i).getY()).setState(true);
            }
        }
    }

    public List<GridCoordinates> getLiveCells(){
        List<GridCoordinates> currentLiveCells = new ArrayList<>();
        for(int x=0; x<xLength; x++){
            for(int y=0; y<yLength; y++){
                if(members.get(x).get(y).getState()){
                    currentLiveCells.add(new GridCoordinates(x, y));
                }
            }
        }
        return currentLiveCells;
    }

    public int countLivingCells() {
        int liveCount=0;
        for(int x=0; x<xLength; x++){
            for(int y=0; y<yLength; y++){
                if(members.get(x).get(y).getState()){
                    liveCount++;
                }
            }
        }
        return liveCount;
    }

    public void killAllCells() {
        for(int x=0; x<xLength; x++){
            for(int y=0; y<yLength; y++){
                members.get(x).get(y).setState(false);
            }
        }
    }

    public void giveLifeToCell(int x, int y) {
        members.get(x).get(y).setState(true);
    }

    public List<List<GridCell>> getMembers() {
        return members;
    }

    public void setMembers(List<List<GridCell>> members) {
        this.members = members;
    }

    public void calculateNextStateOfCells() {
        for(int x=0; x<xLength; x++){
            for(int y=0; y<yLength; y++){
               getCell(x,y).computeNextState(this, x, y);
            }
        }
    }

    public void switchCellsToNextState() {
        for(int x=0; x<xLength; x++){
            for(int y=0; y<yLength; y++){
                getCell(x,y).switchToNextState();
            }
        }
    }

    public GridCell getCell(int x, int y) {
        return members.get(x).get(y);
    }

    public int getxLength() {
        return xLength;
    }

    public int getyLength() {
        return yLength;
    }


}
