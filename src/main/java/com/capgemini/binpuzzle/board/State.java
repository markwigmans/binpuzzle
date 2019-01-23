package com.capgemini.binpuzzle.board;

public enum State {
    /**
     * The unknown State.
     */
    EMPTY('.'),

    /**
     * The State with the '0' value.
     */
    ZERO('0'),

    /**
     * The State with the '1' value.
     */
    ONE('1');

    private final char c;

    /**
     * Creates a State.
     */
    private State(char c) {
        this.c = c;
    }

    public static State fromChar(char x) {
        switch (x) {
            case '1':
                return ONE;
            case '0':
                return ZERO;
            default:
                return EMPTY;
        }
    }

    public static State invert(State s) {
        switch (s) {
            case EMPTY:
                return EMPTY;
            case ZERO:
                return ONE;
            case ONE:
                return ZERO;
            default:
                throw new AssertionError("Unreachable state");
        }
    }

    /**
     * Returns the character that represents the State.
     */
    public char getSymbol() {
        return c;
    }
}
