package othello.faskinen;

public interface Move {
    public int getRound();
    public int getPlayerId();

    public Iterable<Change> getChanges();
}
