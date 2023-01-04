package othello.game.state;

public class Change implements othello.game.state.interfaces.Change {
    private int row;
    private int column;
    private int prevPlayerId;

    public Change(int row, int column, int prevPlayerId) {
        this.row = row;
        this.column = column;
        this.prevPlayerId = prevPlayerId;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public int getPrevPlayerId() {
        return this.prevPlayerId;
    }
}
