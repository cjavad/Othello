package othello.game;


/**
 * Not the actual Board Element but a representation of a reference to an existing board element.
 * We store the board as a 1D array for performance reasons.
 */

public class Space implements othello.game.interfaces.Space {
    public int column;
    public int row;

    public Space(int column, int row) {
        this.column = column;
        this.row = row;
    }

    public int getColumn() {
        return this.column;
    }

    public int getRow() {
        return this.row;
    }

    public String toString() {
        return "[" + this.column + ", " + this.row + "]";
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
}