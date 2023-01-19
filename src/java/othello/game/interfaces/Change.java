package othello.game.interfaces;

import java.io.Serializable;

public interface Change extends Serializable {
    public int getRow();
    public int getColumn();
    public int getPrevPlayerId();
}
