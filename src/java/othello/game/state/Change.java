package othello.game.state;

public interface Change {
    public int getRow();
    public int getColumn();
    public int getPrevPlayerId();
}
