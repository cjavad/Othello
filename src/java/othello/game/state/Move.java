package othello.game.state;

public class Move implements othello.game.state.interfaces.Move {
    private int round;
    private int playerId;
    private Iterable<Change> changes;

    public Move(int round, int playerId, Iterable<Change> changes) {
        this.round = round;
        this.playerId = playerId;
        this.changes = changes;
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
}
