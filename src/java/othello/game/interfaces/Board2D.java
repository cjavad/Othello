package othello.game.interfaces;

import othello.game.Space;

import java.util.Iterator;

/**
 * @author
 *
 * Board interface for Othello world in 2D for JavaFX.
 *
 */

public interface Board2D extends Iterable<Space> {
    /**
     * Geometry of board
     */
    int getColumns();
    int getRows();

    /**
     * These functions always refer to current player id
     * @return Player ID that occupies the cell, or -1 if empty
     */
    int getSpace(othello.game.Space space);
    void setSpace(othello.game.Space space, int playerId);

    Line[] findLines(othello.game.Space space, int playerId);
    /**
     *
     * @return Returns 0 is playable, 1 if something is wrong.
     */
    int isValidMove(othello.game.Space space, int playerId);
    // Return list of all valid moves
    Iterator<Space> validMoves(int playerId);

    /**
     * Handles moves
     * Checks if move is valid, and if so, updates the board and the neighbors of the cells.
     * Returns the move made, and the changes made to the board.
     */

    Move move(Space space);

    /**
     * Player
     */

    int[] getStartingPositions(int playerId);
    void nextPlayer();
    int getPlayerCount();
    int getCurrentPlayerId();
    Player getCurrentPlayer();
    Player getPlayer(int playerId);

    /**
     * Board state
     */

    int getRound();
    othello.game.Move getLatestMove();
    othello.game.Move getMove(int moveIndex);
    Iterable<othello.game.Move> getMoves(); // Chains moves

    int getScore();
    int getScore(int playerId);
}