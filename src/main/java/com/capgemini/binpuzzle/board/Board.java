package com.capgemini.binpuzzle.board;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.capgemini.binpuzzle.board.State.EMPTY;

@Slf4j
public class Board {

    @Getter
    private final int width;
    private final State[][] values;

    public Board(int width) {
        this.width = width;
        this.values = new State[width][width];
    }

    Board(int width, State[][] values) {
        this.width = width;
        this.values = new State[width][width];
        for (int i = 0; i < values.length; i++) {
            System.arraycopy(values[i], 0, this.values[i], 0, width);
        }
    }

    /**
     * Produces a copy of this Board instance.
     */
    public Board copy() {
        return new Board(width, values);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final Board that = (Board) obj;
            return Arrays.deepEquals(this.values, that.values);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(width);
        result = 31 * result + Arrays.hashCode(values);
        return result;
    }

    /**
     * Sets the cell at the given coordinates to the provided State.
     */
    public void set(int x, int y, State value) {
        Assert.notNull(value, "Illegal value");
        Assert.isTrue(isValidCell(x, y), "Cell coordinates out of range");
        values[x][y] = value;
    }

    public State get(int x, int y) {
        Assert.isTrue(isValidCell(x, y), "Cell coordinates out of range");
        return values[x][y];
    }

    private State safeGet(int x, int y) {
        if (isValidCell(x, y)) {
            return get(x, y);
        } else {
            return EMPTY;
        }
    }

    public boolean isValidCell(int x, int y) {
        return (x >= 0 && x < width) && (y >= 0 && y < width);
    }

    public boolean isCompleteAndCorrect() {
        boolean value = isComplete() && rowsInSync() && colsInSync();
        log.debug("isCompleteAndCorrect : {}", value);

        return value;
    }

    private boolean isComplete() {
        return Arrays.stream(values).flatMap(Arrays::stream).noneMatch(s -> s == EMPTY);
    }

    private boolean rowsInSync() {
        final Set<String> values = new HashSet<>();
        for (int x = 0; x < width; x++) {
            int c0 = 0, c1 = 0;
            final StringBuilder sb = new StringBuilder();
            for (int y = 0; y < width; y++) {
                switch (get(x, y)) {
                    case ZERO:
                        c0++;
                        break;
                    case ONE:
                        c1++;
                        break;
                }
                // check if group is ok
                if (!validGroup(safeGet(x, y - 1), safeGet(x, y), safeGet(x, y + 1))) {
                    return false;
                }
                sb.append(get(x, y).toString());
            }
            if (c0 != c1) {
                return false;
            }
            values.add(sb.toString());
        }

        return values.size() == width;
    }

    private boolean colsInSync() {
        final Set<String> values = new HashSet<>();
        for (int y = 0; y < width; y++) {
            int c0 = 0, c1 = 0;
            final StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {
                switch (get(x, y)) {
                    case ZERO:
                        c0++;
                        break;
                    case ONE:
                        c1++;
                        break;
                }
                // check if group is ok
                if (!validGroup(safeGet(x - 1, y), safeGet(x, y), safeGet(x + 1, y))) {
                    return false;
                }
                sb.append(get(x, y).toString());
            }
            if (c0 != c1) {
                return false;
            }
            values.add(sb.toString());
        }
        return values.size() == width;
    }

    private boolean validGroup(State s1, State s2, State s3) {
        return s1 == EMPTY || s1 != s2 ||
                s2 == EMPTY || s2 != s3 ||
                s3 == EMPTY || s3 != s1;
    }

    /**
     * Creates a game from the provided String representation.
     *
     * @param lines The Stream of individual lines, not null.
     * @return The Game encoded in the Stream.
     */
    public static Board create(Stream<String> lines) {
        Assert.notNull(lines, "Illegal value");
        return create(lines.collect(Collectors.toList()));
    }

    public static Board create(String... lines) {
        return Board.create(Arrays.asList(lines));
    }

    /**
     * Creates a Game from the provided String representation.
     *
     * @param lines The List of individual lines, not null.
     * @return The Game encoded in the List of Strings.
     */
    public static Board create(List<String> lines) {
        Assert.notNull(lines, "Illegal value");
        final int width = lines.size() - (int) lines.stream().filter(String::isEmpty).count();
        if (width == 0) {
            throw new IllegalArgumentException("Invalid board size: " + width);
        }
        final Board result = new Board(width);
        for (int y = 0; y < width; y++) {
            String row = lines.get(y);
            for (int x = 0; x < row.length(); x++) {
                result.set(x, y, State.fromChar(row.charAt(x)));
            }

            for (int x = row.length(); x < width; x++) {
                result.set(x, y, EMPTY);
            }
        }

        return result;
    }

    public Optional<Point> firstEmpty() {
        final List<Point> result = new ArrayList<>();
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                if (values[x][y] == EMPTY) {
                    return Optional.of(new Point(x, y));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(width * width);
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < width; x++) {
                sb.append(values[x][y].getSymbol());
            }
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }
}
