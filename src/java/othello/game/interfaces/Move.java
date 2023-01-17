package othello.game.interfaces;

import othello.game.Change;

public interface Move {
    public int getRound();
    public int getPlayerId();

    public Iterable<Change> getChanges();

    public void setChanges(Iterable<Change> changes);
}
