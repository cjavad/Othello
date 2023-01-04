package othello.game.state;

/**
 * @author
 *
 * Board interface for Othello world in 2D for JavaFX.
 *
 */

public interface Board2D {
    /**
     * Geometry of board
     */
    public int getRows();
    public int getColumns();

    public static void createCopyOfBoard(Board2D board) {

    }

    /**
     * These functions always refer to current player id
     * @return Player ID that occupies the cell, or -1 if empty
     */
    public int getCell(int row, int column);
    public void setCell(int row, int column, int playerId);

    /**
     *
     * @return Returns 0 is playable, 1 if something is wrong.
     */
    public int isValidMove(int row, int column, int playerId);
    // Return list of all valid moves
    public Iterable<int[]> getValidMoves(int playerId);

    /**
     * Handles moves
     * Checks if move is valid, and if so, updates the board and the neighbors of the cells.
     * Returns the move made, and the changes made to the board.
     */

    public Move move(int row, int column);

    /**
     * Player
     */

    public int getPlayerCount();
    public int getCurrentPlayerId();
    public Player getCurrentPlayer();

    /**
     * Board state
     */

    public int getRound();
    public Move getLatestMove();
    public Move getMove(int moveIndex);
    public Iterable<Move> getMoves(); // Chains moves

    public int getScore();
    public int getScore(int playerId);
}
