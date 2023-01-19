package othello.game;

public class Change implements othello.game.interfaces.Change {
    private final Space space;

    private final int playerId;
    private final int prevPlayerId;



    public Change(Space space, int playerId, int prevPlayerId) {
        this.space = space;
        this.playerId = playerId;
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

    public int getPlayerId() {
        return this.playerId;
    }

    public String toString() {
        return String.format("Space %s flipped from Player %d to Player %d", this.space, this.prevPlayerId, this.playerId);
    }
}
