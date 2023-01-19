package othello.game.interfaces;

import javafx.util.Pair;
import othello.game.Space;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author
 *
 * Board interface for Othello world in 2D for JavaFX.
 *
 */

public interface Board2D extends Iterable<Space>, Serializable, Cloneable {
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
     * @return Returns 0 is playable, 1 if something is wrong.
     */
    Pair<Integer, othello.game.Line[]> isValidMove(Space space, int playerId);
    // Return list of all valid moves
    Iterator<Space> validMoves(int playerId);

    /**
     * Handles moves
     * Checks if move is valid, and if so, updates the board and the neighbors of the cells.
     * Returns the move made, and the changes made to the board.
     */

    Move move(Space space);

    void revert(int moveIndex);

    /**
     * Player
     */

    void setStartingPositions();
    void nextPlayer();
    int getPlayerCount();
    int getCurrentPlayerId();
    Player getCurrentPlayer();
    Player getPlayer(int playerId);

    /**
     * Board state
     */

    boolean isGameOver();
    void startSetup(int maxPlacements);
    void endSetup();

    int getRound();
    othello.game.Move getLatestMove();
    othello.game.Move getMove(int moveIndex);
    ArrayList<othello.game.Move> getMoves(); // Chains moves

    static int[] createCopyFromMoves(int[] state, Iterable<othello.game.Move> moves, int columns, int rows) {
        int[] copy = state.clone();

        for (othello.game.Move move : moves) {
            Space placementSpace = move.getPlacementSpace();
            copy[placementSpace.row * columns + placementSpace.column] = move.getPlayerId();
            for (othello.game.Change change : move.getChanges()) {
                copy[change.getRow() * columns + change.getColumn()] = change.getPlayerId();
            }
        }
        return copy;
    }

    static int[] reverseStateFromMoves(int[] state, ArrayList<othello.game.Move> moves) {
        // Make copy of state
        int[] copy = new int[state.length];
        System.arraycopy(state, 0, copy, 0, state.length);

        Collections.reverse(moves);

        for (othello.game.Move move : moves) {
            Space placementSpace = move.getPlacementSpace();
            copy[placementSpace.row * copy.length + placementSpace.column] = -1;
            for (othello.game.Change change : move.getChanges()) {
                copy[change.getRow() * copy.length + change.getColumn()] = change.getPrevPlayerId();
            }
        }

        return copy;
    }

    int getScore();
    int getScore(int playerId);
}