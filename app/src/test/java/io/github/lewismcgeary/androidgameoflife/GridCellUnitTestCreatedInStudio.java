package io.github.lewismcgeary.androidgameoflife;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Lewis on 15/11/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GridCellUnitTestCreatedInStudio {
    @Test
    public void testGetState() throws Exception {
        GridCell c = new GridCell(true);
        assertTrue(c.getState());
    }

    @Test
    public void whenLessThanTwoLiveNeighbours_ThenNextStateOfCellShouldBeDead() throws Exception {
        GridCell cell = new GridCell(true);
        assertFalse(cell.applyLifeRules(1));
    }

    @Test
    public void whenMoreThanThreeLiveNeighboursCellShouldDie() throws Exception {
        GridCell cell = new GridCell(true);
        assertFalse(cell.applyLifeRules(4));
    }
    @Test
    public void givenTheCellIsDead_WhenCellHasThreeLivingNeighbours_ThenCellWillBecomeAliveNext() throws Exception{
        GridCell cell = new GridCell(false);
        assertTrue(cell.applyLifeRules(3));
    }


    @Test
    public void givenTheGridIsInitialised_WhenAllCellsStartDead_ThenTheyStayDead() throws Exception {
        Grid worldGrid = new Grid(20, 20);
        assertEquals(0, worldGrid.countLivingCells());
    }

    @Test
    public void givenTheGridHasOneLiveCell_WhenNoMovesAreMade_ThenGridStillHasOneLiveCell() throws Exception {
        Grid worldGrid = new Grid(20, 20);
        worldGrid.giveLifeToCell(10, 10);
        assertEquals(1, worldGrid.countLivingCells());
    }

    @Test
    public void givenTheGridIsSeededWithFiveLiveCells_WhenNoMovesAreMade_ThenGridStillHasFiveLiveCells() throws Exception {
        Grid worldGrid = new Grid(20, 20);
        worldGrid.setInitialLiveCells(Arrays.asList(new GridCoordinates(5,5),new GridCoordinates(6,5),new GridCoordinates(5,6),new GridCoordinates(7,5),new GridCoordinates(5,4)));
        assertEquals(5, worldGrid.countLivingCells());
    }

    @Test
    public void givenTheGridHasOneLiveCell_WhenAMoveIsMade_ThenGridHasNoLiveCells() throws Exception {
        Grid worldGrid = new Grid(20,20);
        worldGrid.giveLifeToCell(10, 10);
        worldGrid.calculateNextStateOfCells();
        worldGrid.switchCellsToNextState();
        assertEquals(0, worldGrid.countLivingCells());
    }

    @Test
    public void givenTheGridIsInitialised_WhenAllCellsAreDead_ThenACellsLiveNeighbourCountIsZero() throws Exception{
        Grid worldGrid = new Grid(20,20);
        assertEquals(0, worldGrid.getCell(10,10).countLivingNeighbours(worldGrid, 10, 10));
    }
    @Test
    public void givenTheGridIsInitialised_WhenACellHasOneLiveNeighbour_ThenTheCellsLiveNeighbourCountIsOne() throws Exception{
        Grid worldGrid = new Grid(20,20);
        worldGrid.giveLifeToCell(10, 10);
        assertEquals(1, worldGrid.getCell(9, 10).countLivingNeighbours(worldGrid, 9, 10));
    }
    @Test
    public void givenTheGridIsInitialised_WhenACellHasTwoLiveNeighbours_ThenTheCellsLiveNeighbourCountIsTwo() throws Exception{
        Grid worldGrid = new Grid(20,20);
        worldGrid.giveLifeToCell(10, 10);
        worldGrid.giveLifeToCell(9, 11);
        assertEquals(2, worldGrid.getCell(9, 10).countLivingNeighbours(worldGrid, 9, 10));
    }
    @Test
    public void givenALiveCellHasOneLiveNeighbour_WhenTheCellCalculatesItsNextState_ItWillBeDead() throws Exception{
        Grid worldGrid = new Grid(20,20);
        worldGrid.giveLifeToCell(10,10);
        worldGrid.giveLifeToCell(9,10);
        worldGrid.getCell(9,10).computeNextState(worldGrid, 9, 10);
        assertFalse(worldGrid.getCell(9, 10).getNextState());
    }



}
