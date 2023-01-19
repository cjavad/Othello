package othello.game.interfaces;

import java.io.Serializable;

public interface Player extends Serializable {
    public int getPlayerId();
    public String getColor();
}
