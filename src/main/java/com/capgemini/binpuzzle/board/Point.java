package com.capgemini.binpuzzle.board;

import lombok.Getter;

@Getter
public class Point {

    private final int row;
    private final int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
