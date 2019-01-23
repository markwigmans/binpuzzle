package com.capgemini.binpuzzle.board;

import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;

public class BoardTest {
    
    /**
     * Tests if the {@code Board.create} method creates the Board correctly.
     */
    @Test
    public void testCreateBoard() {
        Board board = Board.create("01", "  ");
        Assert.assertEquals(State.ZERO, board.get(0, 0));
        Assert.assertEquals(State.ONE, board.get(1, 0));
        Assert.assertEquals(State.EMPTY, board.get(0, 1));
        Assert.assertEquals(State.EMPTY, board.get(1, 1));
    }

    /**
     * Tests if the {@code set} method updates the Board instance.
     */
    @Test
    public void testGetSetState() {
        Board board = Board.create("10", "  ");
        Assert.assertEquals(State.EMPTY, board.get(0, 1));

        board.set(0, 1, State.ZERO);
        Assert.assertEquals(State.ZERO, board.get(0, 1));
    }

    /**
     * Tests if the {@code isValidCell(int,int)} method changes its result on
     * the correct boundaries.
     */
    @Test
    public void testIsValidCell() {
        Board board = new Board(3);

        // Boundary testing of x coordinate.
        Assert.assertFalse(board.isValidCell(-1, 0));
        Assert.assertTrue(board.isValidCell(0, 0));
        Assert.assertTrue(board.isValidCell(2, 0));
        Assert.assertFalse(board.isValidCell(3, 0));

        // Boundary testing of y coordinate.
        Assert.assertFalse(board.isValidCell(0, -1));
        Assert.assertTrue(board.isValidCell(0, 0));
        Assert.assertTrue(board.isValidCell(0, 2));
        Assert.assertFalse(board.isValidCell(0, 3));
    }

    /**
     * Tests if a completed Board returns true for {@code isComplete()}.
     */
    @Test
    public void testIsCompleteTrueCase() {
        Assert.assertTrue(Board.create("10", "01").isCompleteAndCorrect());
    }

    /**
     * Tests if an incomplete Board returns false for {@code isComplete()}.
     */
    @Test
    public void testIsCompleteFalseCase() {
        Assert.assertFalse(Board.create(" 0", "01").isCompleteAndCorrect());
        Assert.assertFalse(Board.create(".0", "01").isCompleteAndCorrect());
    }

    /**
     * Tests if creating a copy makes the Board behave independent from the
     * original Board instance.
     */
    @Test
    public void testCopy() {
        Board board = Board.create("10", "0 ");
        Board copy = board.copy();

        Assert.assertEquals(board, board);
        Assert.assertEquals(board, copy);

        copy.set(1, 1, State.ONE);
        Assert.assertNotEquals(board, copy);
    }

    /**
     * Tests if the {@code toString()} method formats the Board as expected.
     */
    @Test
    public void testToString() {
        Board board = Board.create("0101", "10 1", "   0", " 1  ");
        String expected = "0101\n10.1\n...0\n.1..\n".replace("\n", System.getProperty("line.separator"));
        String actual = board.toString();
        Assert.assertEquals(expected, actual);
    }
}
