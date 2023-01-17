package othello.game.interfaces;

public interface Space extends Comparable<othello.game.Space> {
    int getRow();
    int getColumn();
}
