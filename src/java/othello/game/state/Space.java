package othello.game.state;


/**
 * Not the actual Board Element but a representation of a reference to an existing board element.
 * We store the board as a 1D array for performance reasons.
 */

public class Space implements othello.game.state.interfaces.Space {
    public int row;
    public int column;

    public Space(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    /**
     *
     * @param stepsX The number of steps to move in the X direction.
     * @param stepsY The number of steps to move in the Y direction.
     * @param maxX The maximum value of X, typically the max width or row length.
     * @param maxY The maximum value of Y, typically the max height or column length.
     * @return The neighboring space, or null if the space is out of bounds.
     */

    public Space getRelativeSpace(int stepsX, int stepsY, int maxX, int maxY) {
        int row = this.row + stepsY;
        int column = this.column + stepsX;

        if (row < 0 || row >= maxY || column < 0 || column >= maxX) {
            return null;
        }

        return new Space(row, column);
    }
}