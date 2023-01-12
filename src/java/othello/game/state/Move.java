package othello.game.state;

public class Move implements othello.game.state.interfaces.Move {
    private int round;
    private int playerId;

    private Space placementSpace;
    private Iterable<Change> changes;

    public Move(int round, int playerId, Space space, Iterable<Change> changes) {
        this.round = round;
        this.playerId = playerId;
        this.changes = changes;
        this.placementSpace = space;
    }

    public int getRound() {
        return this.round;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public Iterable<Change> getChanges() {
        return this.changes;
    }

    public void setChanges(Iterable<Change> changes) {
        this.changes = changes;
    }
}
