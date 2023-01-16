package othello.game.state.interfaces;

import othello.game.state.Change;

public interface Move {
    public int getRound();
    public int getPlayerId();

    public Iterable<Change> getChanges();

    public void setChanges(Iterable<Change> changes);
}
