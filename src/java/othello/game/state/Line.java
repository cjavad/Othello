package othello.game.state;

import java.util.ArrayList;
import java.util.Iterator;

public class Line implements othello.game.state.interfaces.Line {
    private int maxRow;
    private int maxColumn;
    private Space start;
    private Space end;
    private int dr;
    private int dc;

    public Line(Space start, Space end, int maxRow, int maxColumn) {
        this.start = start;
        this.end = end;
        this.maxRow = maxRow;
        this.maxColumn = maxColumn;

        /*
            A line always has an initial direction, which can not be changed.
        */

        this.dr = end.compareToRow(start);
        this.dc = end.compareToColumn(start);
    }

    public Line(Space offset, String direction, int maxRow, int maxColumn) {
        switch (direction) {
            case "vertical":
                this.start = new Space(offset.getRow(), 0);
                this.end = new Space(offset.getRow(), maxColumn - 1);
                break;
            case "horizontal":
                this.start = new Space(0, offset.getColumn());
                this.end = new Space(maxRow - 1, offset.getColumn());
                break;
            case "diagonal":
                this.start = new Space(0, 0);
                this.end = new Space(maxRow - 1, maxColumn - 1);
                break;
            case "antiDiagonal":
                this.start = new Space(0, maxColumn - 1);
                this.end = new Space(maxRow - 1, 0);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction: " + direction);
        }

        this.maxRow = maxRow;
        this.maxColumn = maxColumn;

        this.dr = end.compareToRow(start);
        this.dc = end.compareToColumn(start);
    }

    public Space at(int i) {
        int row = this.start.getRow() + (i * this.dr);
        int column = this.start.getColumn() + (i * this.dc);

        // Check if the space is out of bounds
        if (row < 0 || row >= this.maxRow || column < 0 || column >= this.maxColumn) {
            return null;
        }

        return new Space(row, column);
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
        return space.getRow() >= this.start.getRow() && space.getRow() <= this.end.getRow() && space.getColumn() >= this.start.getColumn() && space.getColumn() <= this.end.getColumn();
    }

    public int length() {
        // We can always assume that the line is horizontal or vertical or diagonal
        // If the line is horizontal or vertical, the length is the difference between the start and end
        // If the line is diagonal, the length is the difference between the start and end
        if (this.start == null || this.end == null) return 0;
        return Math.abs(this.start.getRow() - this.end.getRow()) + Math.abs(this.start.getColumn() - this.end.getColumn());
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Line between " + this.start + " and " + this.end + " c: " + this.length());

        for (int i = 0; i < this.length(); i++) {
            Space space = this.at(i);
            if (space == null) continue;
            s.append(" ").append(space);
        }

        return s.toString();
    }
}
