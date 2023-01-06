package othello.game.state.interfaces;

public interface Space {
    int getRow();
    int getColumn();
    Space getRelativeSpace(int stepsX, int stepsY, int maxX, int maxY);
}
