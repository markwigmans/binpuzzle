package com.capgemini.binpuzzle.Solver;

import com.capgemini.binpuzzle.Solver.Solver;
import com.capgemini.binpuzzle.board.Board;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

@Slf4j
public class SolverTest {

    @Test
    public void solveSimpleRules() {
        final Board board = Board.create(
                "1.1.....",
                ".10....1",
                "...1.0..",
                "..0.....",
                "....1.10",
                ".....0..",
                "......1.",
                "1..0.11.");

        final Board solution = Board.create(
                "10101.0.",
                ".1001.01",
                ".011001.",
                "..01010.",
                "..101010",
                "..011001",
                "..010110",
                "10100110");

        Solver solver = new Solver();
        Optional<Board> output = solver.solveSimpleRules(board.copy());
        Assert.assertTrue(output.isPresent());
        Assert.assertEquals(solution, output.get());
    }

    @Test
    public void solve12() {

        final Board board = Board.create(
                "....0.....1.",
                "..0..1....1.",
                ".0..........",
                "..1.0..1.1..",
                ".0..0.......",
                "..0....00...",
                ".....1.0...1",
                "1...0.....0.",
                ".....1..0.00",
                ".1.1.......0",
                "..0.........",
                "....0..1....");

        final Board solution = Board.create(
                "001100110110",
                "110011001010",
                "100110101001",
                "011001010110",
                "001101011001",
                "110010100110",
                "001011001011",
                "100100110101",
                "011011010100",
                "110100101010",
                "100110100101",
                "011001011001");

        final Solver solver = new Solver();
        Optional<Board> output = solver.solve(board.copy());
        Assert.assertTrue(output.isPresent());
        Assert.assertEquals(solution, output.get());
        log.info("Solution:\n{}", output);
    }

    @Test
    public void solve16() {
        final Board board = Board.create(
                "...11..0........",
                "00........0..0..",
                ".....0..0.......",
                ".1.1......00.1..",
                "..0......1......",
                ".....00...0.1..0",
                ".......1....1...",
                ".11...0...1...00",
                "......0.....0...",
                "....0...0......1",
                ".00..11......1.1",
                ".0........1.0...",
                "...0....1......1",
                "1.00.......1.1.1",
                "1......1.1......",
                ".0..0..10..0.0..");

        final Board solution = Board.create(
                "0101101010101100",
                "0010011011011010",
                "1010100100110011",
                "0101101011001100",
                "0101011001100101",
                "1010100110011010",
                "1101001101001001",
                "0110110010110100",
                "1001100100110110",
                "0110011001001011",
                "1001011010010101",
                "1001100101100110",
                "0110010110101001",
                "1100101010010101",
                "1011010101010010",
                "0010010101101011");

        final Solver solver = new Solver();
        Optional<Board> output = solver.solve(board.copy());
        Assert.assertTrue(output.isPresent());
        Assert.assertEquals(solution, output.get());
        log.info("Solution:\n{}", output);
    }

    @Test
    public void solveDoubleRulePerRow() {
        final Board board = Board.create(
                "...0.1.0....",
                "..1...00..10",
                "..1.........",
                ".....1.00.0.",
                "...........1",
                "......1.0.11",
                "011.........",
                ".......0.00.",
                "...11..0....",
                "..........00",
                "1..0.1..0...",
                "....11.1...0");

        final Board solution = Board.create(
                "...0.1.0....",
                "..1..1001.10",
                "..1.........",
                ".....110010.",
                "...........1",
                "......1.0011",
                "0110........",
                ".......01001",
                "..0110.0....",
                ".........100",
                "1..0.1..0...",
                "...01101...0");

        Solver solver = new Solver();
        Board input = board.copy();
        boolean output = solver.solveDoubleRulePerRow(input);
        Assert.assertTrue(output);
        Assert.assertEquals(solution, input);
    }

    @Test
    public void solveDoubleRulePerColumn() {
        final Board board = Board.create(
                "...0.1.0....",
                "..1...00..10",
                "..1.........",
                ".....1.00.0.",
                "...........1",
                "......1.0.11",
                "011.........",
                ".......0.00.",
                "...11..0....",
                "..........00",
                "1..0.1..0...",
                "....11.1...0");

        final Board solution = Board.create(
                "..00.1.0....",
                "..1...00..10",
                "..1....1....",
                "..0..1.00.00",
                "...........1",
                "......1.0.11",
                "011....1...0",
                ".......0.00.",
                "...11..0....",
                ".....0.1..00",
                "1..0.1..0...",
                "....11.1...0");

        Solver solver = new Solver();
        Board input = board.copy();
        boolean output = solver.solveDoubleRulePerColumn(input);
        Assert.assertTrue(output);
        Assert.assertEquals(solution, input);
    }

    @Test
    public void solveGapRulePerColumn() {
        final Board board = Board.create(
                "...0.1.0....",
                "..1...00..10",
                "..1.........",
                ".....1.00.0.",
                "...........1",
                "......1.0.11",
                "011.........",
                ".......0.00.",
                "...11..0....",
                "..........00",
                "1..0.1..0...",
                "....11.1...0");

        final Board solution = Board.create(
                "...0.1.0....",
                "..1...00..10",
                "..1....1....",
                ".....1.00.0.",
                "........1..1",
                "......1.0.11",
                "011.........",
                ".......0.00.",
                "...11..0..1.",
                "..........00",
                "1..0.1..0..1",
                "....11.1...0");

        Solver solver = new Solver();
        Board input = board.copy();
        boolean output = solver.solveGapRulePerColumn(input);
        Assert.assertTrue(output);
        Assert.assertEquals(solution, input);
    }

    @Test
    public void solveGapRulePerRow() {
        final Board board = Board.create(
                "...0.1.0....",
                "..1...00..10",
                "..1.........",
                ".....1.00.0.",
                "...........1",
                "......1.0.11",
                "011.........",
                ".......0.00.",
                "...11..0....",
                "..........00",
                "1..0.1..0...",
                "....11.1...0");

        final Board solution = Board.create(
                "...0.1.0....",
                "..1...00..10",
                "..1.........",
                ".....1.0010.",
                "...........1",
                "......1.0.11",
                "011.........",
                ".......0100.",
                "...11..0....",
                "..........00",
                "1..0.1..0...",
                "....1101...0");

        Solver solver = new Solver();
        Board input = board.copy();
        boolean output = solver.solveGapRulePerRow(input);
        Assert.assertTrue(output);
        Assert.assertEquals(solution, input);
    }

    @Test
    public void solveValueCountPerColumn() {
        final Board board = Board.create(
                "1...",
                "10..",
                ".01.",
                "..1.");

        final Board solution = Board.create(
                "110.",
                "100.",
                "001.",
                "011.");

        Solver solver = new Solver();
        Board input = board.copy();
        boolean output = solver.solveValueCountPerColumn(input);
        Assert.assertTrue(output);
        Assert.assertEquals(solution, input);
    }

    @Test
    public void solveValueCountPerRow() {
        final Board board = Board.create(
                "11..",
                ".00.",
                "..11",
                "....");


        final Board solution = Board.create(
                "1100",
                "1001",
                "0011",
                "....");

        Solver solver = new Solver();
        Board input = board.copy();
        boolean output = solver.solveValueCountPerRow(input);
        Assert.assertEquals(solution, input);
    }
}