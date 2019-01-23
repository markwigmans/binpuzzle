package com.capgemini.binpuzzle.Solver;

import com.capgemini.binpuzzle.board.Board;
import com.capgemini.binpuzzle.board.Point;
import com.capgemini.binpuzzle.board.State;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

import static com.capgemini.binpuzzle.board.State.*;

@Slf4j
public class Solver {

    public Optional<Board> solve(Board board) {
        return solveSimpleRules(board.copy()).flatMap(b -> findGuess(b));
    }

    Optional<Board> findGuess(Board board) {
        if (board.isCompleteAndCorrect()) {
            return Optional.of(board);
        } else {
            final Point point = board.firstEmpty().orElse(null);
            if (point != null) {
                Optional<Board> b0 = guessSolve(board.copy(), point.getRow(), point.getCol(), ZERO);
                if (b0.isPresent() && b0.get().isCompleteAndCorrect()) {
                    log.info("found with ZERO");
                    return b0;
                }
                Optional<Board> b1 = guessSolve(board.copy(), point.getRow(), point.getCol(), ONE);
                if (b1.isPresent() && b1.get().isCompleteAndCorrect()) {
                    log.info("found with ONE");
                    return b1;
                }
            }
        }

        // no solution is found
        log.debug("findGuess : no solution");
        return Optional.empty();
    }

    private Optional<Board> guessSolve(Board board, int x, int y, State state) {
        log.debug("guess ({},{}) : {}", x, y, state);
        if (checkedUpdate(board, x, y, state)) {
            return solveSimpleRules(board).flatMap(b -> findGuess(b));
        }
        log.debug("guess ({},{}) : {} not found", x, y, state);
        return Optional.empty();
    }

    /**
     * Solve board by simple rules as long this is possible.
     */
    Optional<Board> solveSimpleRules(Board board) {
        Board startBoard;
        boolean result = true;
        // loop as long as there changes are made
        do {
            // loop as long as there changes are made
            startBoard = board.copy();
            result = result && solveDoubleRulePerColumn(board);
            result = result && solveDoubleRulePerRow(board);
            result = result && solveGapRulePerColumn(board);
            result = result && solveGapRulePerRow(board);
            result = result && solveValueCountPerColumn(board);
            result = result && solveValueCountPerRow(board);
        }
        while (result && (!board.equals(startBoard)));

        log.debug("solveSimpleRules({})\n{}", result, board);

        if (result) {
            return Optional.of(board);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Fills {@code _11_} and {@code _00_} patterns in rows.
     *
     * @param board The Board
     * @return A (partial) solution
     */
    boolean solveDoubleRulePerRow(Board board) {
        boolean result = true;
        for (int y = 0; y < board.getWidth(); y++) {
            for (int x = 0; x < board.getWidth() - 1; x++) {
                State s = board.get(x, y);
                if (s != EMPTY && s == board.get(x + 1, y)) {
                    State inverse = invert(s);
                    result = result && checkedUpdate(board, x - 1, y, inverse);
                    result = result && checkedUpdate(board, x + 2, y, inverse);
                }
            }
        }
        return result;
    }

    /**
     * Fills {@code 1_1} and {@code 0_0} patterns in rows.
     *
     * @param board The Board
     * @return A (partial) solution
     */
    boolean solveGapRulePerRow(Board board) {
        boolean result = true;
        for (int y = 0; y < board.getWidth(); y++) {
            for (int x = 1; x < board.getWidth() - 1; x++) {
                State s = board.get(x, y);
                if (s == EMPTY && board.get(x - 1, y) == board.get(x + 1, y)) {
                    State inverse = invert(board.get(x - 1, y));
                    result = result && checkedUpdate(board, x, y, inverse);
                }
            }
        }
        return result;
    }

    /**
     * Fills {@code _11_} and {@code _00_} patterns in columns.
     *
     * @param board The Board
     * @return A (partial) solution
     */
    boolean solveDoubleRulePerColumn(Board board) {
        boolean result = true;
        for (int y = 0; y < board.getWidth() - 1; y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                State s = board.get(x, y);
                if (s != EMPTY && s == board.get(x, y + 1)) {
                    State inverse = invert(s);
                    result = result && checkedUpdate(board, x, y - 1, inverse);
                    result = result && checkedUpdate(board, x, y + 2, inverse);
                }
            }
        }
        return result;
    }

    /**
     * Fills {@code 1_1} and {@code 0_0} patterns in columns.
     *
     * @param board The Board
     * @return A (partial) solution
     */
    boolean solveGapRulePerColumn(Board board) {
        boolean result = true;
        for (int y = 1; y < board.getWidth() - 1; y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                State s = board.get(x, y);
                if (s == EMPTY && board.get(x, y - 1) == board.get(x, y + 1)) {
                    State inverse = invert(board.get(x, y + 1));
                    result = result && checkedUpdate(board, x, y, inverse);
                }
            }
        }
        return result;
    }

    /**
     * Fills the 0 and 1 counting patterns in rows.
     *
     * @param board The Board
     * @return A (partial) solution
     */
    boolean solveValueCountPerRow(Board board) {
        boolean result = true;
        int w = board.getWidth(), h = board.getWidth();
        for (int y = 0; y < h; y++) {
            int c0 = 0, c1 = 0;
            for (int x = 0; x < w; x++) {
                State s = board.get(x, y);
                if (s == ZERO) {
                    c0++;
                } else if (s == ONE) {
                    c1++;
                }
            }
            if (2 * c0 >= w) {
                result = result && fillRemainingRow(board, y, ONE);
            } else if (2 * c1 >= w) {
                result = result && fillRemainingRow(board, y, ZERO);
            }
        }
        return result;
    }

    /**
     * Fills the 0 and 1 counting patterns in columns.
     *
     * @param board The Board
     * @return A (partial) solution
     */
    boolean solveValueCountPerColumn(Board board) {
        boolean result = true;
        int w = board.getWidth(), h = board.getWidth();
        for (int x = 0; x < w; x++) {
            int c0 = 0, c1 = 0;
            for (int y = 0; y < h; y++) {
                State s = board.get(x, y);
                if (s == ZERO) {
                    c0++;
                } else if (s == ONE) {
                    c1++;
                }
            }
            if (2 * c0 >= h) {
                result = result && fillRemainingColumn(board, x, ONE);
            } else if (2 * c1 >= h) {
                result = result && fillRemainingColumn(board, x, ZERO);
            }
        }
        return result;
    }


    boolean fillRemainingColumn(Board board, int col, State value) {
        boolean result = true;
        for (int y = 0; y < board.getWidth(); y++) {
            State s = board.get(col, y);
            if (s == EMPTY) {
                result = result && checkedUpdate(board, col, y, value);
            }
        }
        return result;
    }

    boolean fillRemainingRow(Board board, int row, State value) {
        boolean result = true;
        for (int x = 0; x < board.getWidth(); x++) {
            State s = board.get(x, row);
            if (s == EMPTY) {
                result = result && checkedUpdate(board, x, row, value);
            }
        }
        return result;
    }

    /**
     * perform update and return if successful
     */
    boolean checkedUpdate(Board g, int x, int y, State s) {
        if (!g.isValidCell(x, y)) {
            // Cannot update, ignore.
            return true;
        }

        State current = g.get(x, y);
        if (current == s) {
            return true;
        } else if (current == EMPTY) {
            g.set(x, y, s);
        } else {
            // Collision!
            log.debug("Collision ({},{}) : {} -> {} ", x, y, current.name(), s.name());
            return false;
        }
        return true;
    }
}
