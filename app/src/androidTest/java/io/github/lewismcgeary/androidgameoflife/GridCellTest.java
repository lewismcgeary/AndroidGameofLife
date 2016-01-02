package io.github.lewismcgeary.androidgameoflife;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Lewis on 15/11/15.
 */
public class GridCellTest {
    @Test
    public void testGetState(){
        GridCell c = new GridCell(false);
        Assert.assertTrue(c.getState());
    }
}
