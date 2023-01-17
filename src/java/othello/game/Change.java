package othello.game;

public class Change implements othello.game.interfaces.Change {
    private final Space space;
    private final int prevPlayerId;

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
