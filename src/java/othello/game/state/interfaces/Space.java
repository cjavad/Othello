package othello.game.state.interfaces;

public interface Space extends Comparable<othello.game.state.Space> {
    int getRow();
    int getColumn();
    othello.game.state.Space getRelativeSpace(int stepsX, int stepsY, int maxX, int maxY);
}
