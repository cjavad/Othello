package othello.game.interfaces;

import othello.game.Change;
import othello.game.Space;

import java.io.Serializable;

public interface Move extends Serializable {
    public int getRound();
    public int getPlayerId();
    public Iterable<Change> getChanges();
    public void setChanges(Iterable<Change> changes);
    public Line[] getLines();
    public void invalidateLinesMove(Space space);
}
