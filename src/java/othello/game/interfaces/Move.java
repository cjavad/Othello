package othello.game.interfaces;

import othello.game.Change;
import othello.game.Space;

public interface Move {
    public int getRound();
    public int getPlayerId();
    public Iterable<Change> getChanges();
    public void setChanges(Iterable<Change> changes);
    public Line[] getLines();
    public void invalidateLinesMove(Space space);
}
