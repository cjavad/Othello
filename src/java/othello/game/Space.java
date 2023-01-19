package othello.game;


import java.util.Objects;

/**
 * Not the actual Board Element but a representation of a reference to an existing board element.
 * We store the board as a 1D array for performance reasons.
 */

public class Space implements othello.game.interfaces.Space {
    public final int column;
    public final int row;

    // Make read only copies of column and row called x and y
    public final int x;
    public final int y;

    public Space(int column, int row) {
        this.column = column;
        this.row = row;
        this.x = column;
        this.y = row;
    }

    public String toString() {
        // Convert to A1 notation
        return String.format("%c%d", (char) (this.column + 65), this.row + 1);
    }

    @Override
    public int compareTo(Space space) {
        if (space == null) return 1;
        // If the piece appears before the other piece, return -1
        if (this.row < space.row) {
            return -1;
        } else if (this.row > space.row) {
            return 1;
        } else if (this.column < space.column) {
            return -1;
        } else if (this.column > space.column) {
            return 1;
        }

        return 0;
    }

    public boolean equals(Space space) {
        return this.compareTo(space) == 0;
    }

    public int compareToColumn(Space space) {
        // If the piece appears before the other piece, return -1
        if (this.column < space.column) {
            return -1;
        } else if (this.column > space.column) {
            return 1;
        }

        return 0;
    }

    public int compareToRow(Space space) {
        // If the piece appears before the other piece, return -1
        if (this.row < space.row) {
            return -1;
        } else if (this.row > space.row) {
            return 1;
        }

        return 0;
    }

    public int distanceTo(Space space) {
        return Math.max(Math.abs(this.row - space.row), Math.abs(this.column - space.column));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Space space = (Space) o;
        return column == space.column && row == space.row;
    }

    @Override
    public int hashCode() {
        return Objects.hash(column, row);
    }
}