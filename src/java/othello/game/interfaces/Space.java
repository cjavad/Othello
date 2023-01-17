package othello.game.interfaces;

public interface Space extends Comparable<othello.game.Space> {
    int compareToColumn(othello.game.Space space);
    int compareToRow(othello.game.Space space);
    boolean equals(othello.game.Space space);
    int distanceTo(othello.game.Space space);
}
