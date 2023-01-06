package othello.game.state;

public class Space {
    public int row;
    public int column;

    public Space(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Space getNeighbor(int stepsX, int stepsY, int maxX, int maxY) {
        int row = this.row + stepsY;
        int column = this.column + stepsX;

        if (row < 0 || row >= maxY || column < 0 || column >= maxX) {
            return null;
        }

        return new Space(row, column);
    }
}