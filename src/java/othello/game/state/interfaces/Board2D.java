package othello.game.state.interfaces;

import othello.game.state.Space;

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

    public Iterable<Space> getSpaces();

    /**
     * These functions always refer to current player id
     * @return Player ID that occupies the cell, or -1 if empty
     */
    public int getCell(Space space);
    public void setCell(Space space, int playerId);

    public Space getRelativeSpace(Space currentSpace, int direction, int steps);
    public Space[] getAllNeighbors(Space space);

    /**
     *
     * @return Returns 0 is playable, 1 if something is wrong.
     */
    public int isValidMove(Space space, int playerId);
    // Return list of all valid moves
    public Iterable<Space> getValidMoves(int playerId);

    /**
     * Handles moves
     * Checks if move is valid, and if so, updates the board and the neighbors of the cells.
     * Returns the move made, and the changes made to the board.
     */

    public Move move(Space space);

    /**
     * Player
     */

    public int[] getStartingPositions(int playerId);
    public void nextPlayer();
    public int getPlayerCount();
    public int getCurrentPlayerId();
    public Player getCurrentPlayer();
    public Player getPlayer(int playerId);

    /**
     * Board state
     */

    public int getRound();
    public Move getLatestMove();
    public Move getMove(int moveIndex);
    public Iterable<othello.game.state.Move> getMoves(); // Chains moves

    public int getScore();
    public int getScore(int playerId);
}