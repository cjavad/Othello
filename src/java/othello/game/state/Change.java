package othello.game.state;

public class Change implements othello.game.state.interfaces.Change {
    private Space space;
    private int prevPlayerId;

    public Change(Space space, int prevPlayerId) {
        this.space = space;
        this.prevPlayerId = prevPlayerId;
    }

    public int getRow() {
        return this.space.row;
    }

    public int getColumn() {
        return this.space.column;
    }

    public int getPrevPlayerId() {
        return this.prevPlayerId;
    }
}
