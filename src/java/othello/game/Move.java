package othello.game;

public class Move implements othello.game.interfaces.Move {
    private final int round;
    private final int playerId;

    private final Space placementSpace;

    // Lines this move captures
    private final Line[] lines;
    private Iterable<Change> changes;

    public Move(int round, int playerId, Space space, Line[] lines, Iterable<Change> changes) {
        this.round = round;
        this.playerId = playerId;
        this.changes = changes;
        this.placementSpace = space;
        this.lines = lines;
    }

    public Space getPlacementSpace() {
        return this.placementSpace;
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
    
    public Line[] getLines() {
        return this.lines;
    }

    public void invalidateLinesMove(Space space) {
        for (int i = this.lines.length - 1; i >= 0; i--) {
            if (this.lines[i] != null && !this.lines[i].contains(space)) {
                this.lines[i] = null;
            }
        }
    }

    public boolean equals(Move move) {
        return this.round == move.round && this.playerId == move.playerId && this.placementSpace.equals(move.placementSpace);
    }

    public String toString() {
        return String.format("Player %d placed on %s", this.playerId, this.placementSpace);
    }
}
