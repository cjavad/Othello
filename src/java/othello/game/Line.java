package othello.game;

import java.util.Iterator;

public class Line implements othello.game.interfaces.Line {
    private final int maxRow;
    private final int maxColumn;
    private Space start;
    private Space end;
    private int dr;
    private int dc;

    private String label;

    public Line(Space start, Space end, int maxColumn, int maxRow) {
        this.start = start;
        this.end = end;
        this.maxRow = maxRow;
        this.maxColumn = maxColumn;

        /*
            A line always has an initial direction, which can not be changed.
            If it changes pass true to at() once to recalculate its direction.
        */

        this.dr = end.compareToRow(start);
        this.dc = end.compareToColumn(start);
    }

    public static Line[] getLinesFromOffset(Space offset, int maxColumn, int maxRow) {
        return new Line[] {
                new Line(offset, "verticalLeft", maxColumn, maxRow),
                new Line(offset, "verticalRight", maxColumn, maxRow),
                new Line(offset, "horizontalTop", maxColumn, maxRow),
                new Line(offset, "horizontalBottom", maxColumn, maxRow),
                new Line(offset, "diagonalTopLeft", maxColumn, maxRow),
                new Line(offset, "diagonalTopRight", maxColumn, maxRow),
                new Line(offset, "diagonalBottomLeft", maxColumn, maxRow),
                new Line(offset, "diagonalBottomRight", maxColumn, maxRow),
        };
    }

    public Line(Space offset, String direction, int maxColumn, int maxRow) {
        // Derive the start and end spaces from the offset and direction
        this.label = direction;

        final int min1 = Math.min(maxRow - offset.row - 1, offset.column);
        final int min2 = Math.min(offset.row, maxColumn - offset.column - 1);
        final int min3 = Math.min(maxRow - offset.row - 1, maxColumn - offset.column - 1);

        switch (direction) {
            case "antiDiagonal" -> {
                this.start = new Space(offset.column + min2, offset.row - min2);
                this.end = new Space(offset.column - min1, offset.row + min1);
            }
            // We split the above cases into two to avoid a default case
            case "verticalLeft" -> {
                this.start = new Space(0, offset.row);
                this.end = new Space(offset.column, offset.row);
            }
            case "verticalRight" -> {
                this.start = new Space(offset.column, offset.row);
                this.end = new Space(maxColumn - 1, offset.row);
            }
            case "horizontalTop" -> {
                this.start = new Space(offset.column, 0);
                this.end = new Space(offset.column, offset.row);
            }
            case "horizontalBottom" -> {
                this.start = new Space(offset.column, offset.row);
                this.end = new Space(offset.column, maxRow - 1);
            }
            case "diagonalTopLeft" -> {
                int min = Math.min(offset.column, offset.row);
                this.start = new Space(offset.column - min, offset.row - min);
                this.end = new Space(offset.column, offset.row);
            }
            case "diagonalTopRight" -> {
                this.start = new Space(offset.column, offset.row);
                this.end = new Space(offset.column + min2, offset.row - min2);
            }
            case "diagonalBottomLeft" -> {
                this.start = new Space(offset.column, offset.row);
                this.end = new Space(offset.column - min1, offset.row + min1);
            }
            case "diagonalBottomRight" -> {
                this.start = new Space(offset.column, offset.row);
                this.end = new Space(offset.column + min3, offset.row + min3);
            }
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        this.maxRow = maxRow;
        this.maxColumn = maxColumn;

        this.dr = end.compareToRow(start);
        this.dc = end.compareToColumn(start);
    }

    public Space at(int i) {
        return this.at(i, false);
    }

    public Space at(int i, boolean recalculate) {
        if (recalculate) {
            this.dr = end.compareToRow(start);
            this.dc = end.compareToColumn(start);
        }

        int row = this.start.row + (i * this.dr);
        int column = this.start.column + (i * this.dc);

        // Check if the space is out of bounds
        if (column < 0 || column >= this.maxColumn || row < 0 || row >= this.maxRow) {
            return null;
        }

        return new Space(column, row);
    }

    public Iterator<Space> iterator() {
        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < length();
            }

            @Override
            public Space next() {
                return at(i++);
            }
        };
    }

    public Space getStart() {
        return start;
    }

    public Space getEnd() {
        return end;
    }

    public void setStart(Space start) {
        this.start = start;
    }

    public void setEnd(Space end) {
        this.end = end;
    }

    public boolean contains(Space space) {
        // Check if the space is within the line
        for (Space s : this) {
            if (s == null) continue;
            if (s.equals(space)) {
                return true;
            }
        }

        return false;
    }

    public int length() {
        // We can always assume that the line is horizontal or vertical or diagonal
        // If the line is horizontal or vertical, the length is the difference between the start and end
        // If the line is diagonal, the length is the difference between the start and end
        if (this.start == null || this.end == null) return 0;
        return Math.max(Math.abs(this.start.row - this.end.row), Math.abs(this.start.column - this.end.column) ) + 1;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Line (" + this.label + ") between " + this.start + " and " + this.end + " c: " + this.length());

        for (Space space : this) {
            if (space == null) continue;
            s.append(" ").append(space);
        }

        return s.toString();
    }
}
