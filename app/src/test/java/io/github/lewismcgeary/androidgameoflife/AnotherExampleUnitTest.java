package io.github.lewismcgeary.androidgameoflife;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class AnotherExampleUnitTest {
    @Test
    public void testGetState() throws Exception {
        GridCell c = new GridCell(true);
        assertTrue(c.getState());
    }
}