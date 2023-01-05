package othello.game.state.interfaces;

public interface Change {
    public int getRow();
    public int getColumn();
    public int getPrevPlayerId();
}
