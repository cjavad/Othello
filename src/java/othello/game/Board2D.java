package othello.game;

import javafx.beans.binding.BooleanExpression;
import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

public class Board2D implements othello.game.interfaces.Board2D {
    // Determines if moves are made automatically
    private boolean manual;

    private int columns;
    private int rows;

    private int[] board;
    private int[] startingPositions;
    private int round;
    private int currentPlayerId;
    private Player[] players;
    private ArrayList<Move> moves;

    // HashMap cache for operations run after move() in the same round
    public HashMap<Space, Integer> validMoveCache;

    public boolean inSetup;
    public boolean isStatic;

    public Board2D(Player[] players, HashMap<String, Integer> options) {
        this.players = players;

        this.columns = options.getOrDefault("columns", players.length * 4);
        this.rows = options.getOrDefault("rows", players.length * 4);

        this.board = new int[this.columns * this.rows];
        this.startingPositions = new int[this.columns * this.rows];

        Arrays.fill(this.board, -1);
        Arrays.fill(this.startingPositions, -1);

        this.round = 0;
        this.currentPlayerId = 0;
        this.manual = options.getOrDefault("manual", 0) == 1;
        this.moves = new ArrayList<Move>();
        this.validMoveCache = new HashMap<Space, Integer>();

        if (options.getOrDefault("setup", 0) == 1) {
            this.startSetup(options.getOrDefault("maxPlacements", players.length));
        } else {
            this.setStartingPositions();
        }
    }

    public Board2D(Player[] players, boolean manual) {
        this(players.length * 4, players.length * 4, players, manual);
    }

    public Board2D(Player[] players, int[] startingPositions, boolean manual) {
        this(players.length * 4, players.length * 4, players, manual);
        this.startingPositions = startingPositions;
        this.board = startingPositions.clone();
    }

    public Board2D(int columns, int rows, Player[] players, boolean manual) {
        this(players, new HashMap<>() {{
            put("columns", columns);
            put("rows", rows);
            put("manual", manual ? 1 : 0);
        }});
    }

    public Board2D(Player[] players) {
        this(players, false);
    }

    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

    public int getSpace(Space space) {
        return this.board[space.row * this.columns + space.column];
    }

    public void setSpace(Space space, int playerId) {
        if (this.inSetup) {
            this.startingPositions[space.row * this.columns + space.column] = playerId;
        }

        this.board[space.row * this.columns + space.column] = playerId;
    }

    public Line[] findLines(Space placementSpace, int playerId) {
        // Find all lines that are captured by this move
        Line[] lines = Line.getLinesFromOffset(placementSpace, this.columns, this.rows);

        for (int i = 0; i < lines.length; i++) {
            Line line = lines[i];

            if (line == null) continue;

            Space startSpace = null;
            Space endSpace = null;
            Space lastOwnSpace = null;

            for (Space space : line) {
                if (space == null) continue;
                int cell = this.getSpace(space);

                if (startSpace != null && !space.equals(placementSpace) && cell == -1) {
                    // Start space does not touch any other pieces
                    startSpace = null;
                    lastOwnSpace = null;
                    continue;
                }

                if (startSpace != null && endSpace != null && !startSpace.equals(endSpace) && !endSpace.equals(placementSpace)) {
                    startSpace = endSpace;
                    endSpace = null;
                }

                if (space.equals(placementSpace) || cell == playerId) {
                    if (startSpace == null) {
                        startSpace = space;
                    } else {
                        if (space.distanceTo(startSpace) < 2) {
                            startSpace = space;
                        } else {
                            endSpace = space;
                            // If the distance between the last own space and the current own space is greater than 1, then the line is finished.
                            if (endSpace.distanceTo(lastOwnSpace) > 1 && (startSpace.equals(placementSpace) || endSpace.equals(placementSpace))) {
                                break;
                            }
                        }
                    }

                    lastOwnSpace = space;
                }
            }

            if (startSpace == null || endSpace == null) {
                lines[i] = null;
                continue;
            }

            line.setStart(startSpace);
            line.setEnd(endSpace);

            if ((this.getSpace(endSpace) == -1 && !endSpace.equals(placementSpace)) ||
                    (this.getSpace(startSpace) == -1 && !startSpace.equals(placementSpace)) ||
                    !line.contains(placementSpace) || line.length() < 3) {

                lines[i] = null;
            }
        }

        // Find all valid lines
        return Arrays.stream(lines).filter(line -> {
            if (line == null) return false;
            // Line cannot contain any empty spaces
            for (int i = 0; i < line.length(); i++) {
                Space space = line.at(i);
                if (space == null) continue;
                if (this.getSpace(space) == -1 && i != 0 && i != line.length() - 1) return false;
            }
            return true;
        }).toArray(Line[]::new);
    }

    /**
     *
     * @param space The space of the attempted move
     * @param playerId The id of the player attempting the move
     * @return Returns a pair containing the state of the attempted move, and the possible valid lines it forms.
     */

    public Pair<Integer, Line[]> isValidMove(Space space, int playerId) {
        if (this.inSetup) {
            this.validMoveCache.put(space, -1);
            return new Pair<>(1, null);
        }

        if (this.validMoveCache.containsKey(space)) {
            int prevValue = this.validMoveCache.getOrDefault(space, -1);
            Move lastMove = getLatestMove();

            if (lastMove != null && lastMove.getRound() == this.round && lastMove.getPlayerId() == playerId && prevValue != -1) {
                return new Pair<>(prevValue, null);
            }
        }

        // Get state of space
        int playerOccupied = this.getSpace(space);

        // If the player already occupies the space, then the move is invalid (1)
        if (playerOccupied == playerId) {
            this.validMoveCache.put(space, -1);
            return new Pair<>(1, null);
        }

        // Find last move
        Move lastMove = this.getLatestMove();

        // If last move was made in this round, then the following moves can only be flips
        if (lastMove != null && lastMove.getRound() == this.round) {
            // If the space is empty, then the move is invalid, as it is not a flip (1)
            if (playerOccupied == -1) {
                this.validMoveCache.put(space, -1);
                return new Pair<>(1, null);
            }

            // Flips can only be made on existing lines
            // Fetch precalculated lines
            Line[] lines = lastMove.getLines();

            // For each line if the space is on the line, then the move is valid (0)
            for (Line line : lines) {
                if (line == null) continue;
                if (line.contains(space)) {
                    this.validMoveCache.put(space, playerId);
                    return new Pair<>(0, lines);
                }
            }
        } else if (playerOccupied == -1) {
            Line[] lines = this.findLines(space, playerId);

            for (Line line : lines) {
                if (line == null) continue;
                if (line.length() < 3) continue;

                // Cannot be neighbour to own piece
                if (line.at(1) != null && line.at(1).equals(space) && this.getSpace(line.at(1)) == playerId)
                    continue;

                if (line.at(line.length() - 2) != null && line.at(line.length() - 2).equals(space) && this.getSpace(line.at(line.length() - 2)) == playerId)
                    continue;

                for (Space lineSpace : line) {
                    if (lineSpace == null) continue;

                    if (!space.equals(lineSpace) && this.getSpace(lineSpace) != playerId && this.getSpace(lineSpace) != -1) {
                        this.validMoveCache.put(space, playerId);
                        return new Pair<>(0, lines);
                    }
                }
            }
        }

        // Otherwise the move is invalid
        this.validMoveCache.put(space, -1);
        return new Pair<>(1, null);
    }

    // Make above function an iterator that calls isValidMove every run
    public Iterator<Space> validMoves(int playerId) {
        final var iter = iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public Space next() {
                Space space = iter.next();
                if (isValidMove(space, playerId).getKey() == 0) {
                    return space;
                }
                return null;
            }
        };
    }

    public Move move(Space space) {
        if (this.inSetup) {
            if (this.getCurrentPlayer().maxPlacements < 1) {
                // Forcefully switch to the next player with placements left
                Arrays.stream(this.players).filter(player -> player.maxPlacements > 0).findFirst().ifPresentOrElse(player -> this.currentPlayerId = player.getPlayerId(), this::endSetup);

                if (!this.inSetup) {
                    return null;
                }
            }
            if (this.isStatic) return null;
            // When in setup simply place the piece if the space is empty
            if (this.getSpace(space) != -1) return null;
            this.setSpace(space, this.currentPlayerId);
            this.players[this.currentPlayerId].maxPlacements--;
            return null;
        }

        var validity = this.isValidMove(space, this.currentPlayerId);

        if (validity.getKey() > 0) return null;
        int prevValue = this.getSpace(space);

        Move move = this.getLatestMove();

        if (move == null || move.getRound() != this.round || move.getPlayerId() != this.currentPlayerId) {
            // We are making a new placement
            // Create a new move
            this.setSpace(space, this.currentPlayerId);
            move = new Move(this.round, this.currentPlayerId, space, validity.getValue(), new ArrayList<>());
            this.moves.add(move);
        }

        ArrayList<Change> changes = (ArrayList<Change>) move.getChanges();

        if (this.manual) {
            // Don't propagate changes if manual
            changes.add(new Change(space, this.currentPlayerId, prevValue));
            // move.invalidateLinesMove(space);
            this.setSpace(space, this.currentPlayerId);
        } else {
            // Automatically propagate changes by executing all valid moves
            Iterator<Space> validMovesIterator = this.validMoves(this.currentPlayerId);

            while (validMovesIterator.hasNext()) {
                Space validMove = validMovesIterator.next();
                if (validMove == null) continue;
                changes.add(new Change(validMove, this.currentPlayerId, this.getSpace(validMove)));
                // Update valid lines
                // move.invalidateLinesMove(space);
                this.setSpace(validMove, this.currentPlayerId);
            }

            // Automatically switch player
            this.nextPlayer();
        }

        move.setChanges(changes);

        return move;
    }

    public void revert(int moveIndex) {
        // Remove all moves after the specified index
        if (moveIndex < -1 || moveIndex >= this.moves.size()) return;
        this.moves.subList(moveIndex + 1, this.moves.size()).clear();
        this.board = othello.game.interfaces.Board2D.createCopyFromMoves(this.startingPositions, this.moves, this.columns, this.rows);
        this.round = this.moves.size() > 0 ? this.getLatestMove().getRound() : 0;
        this.currentPlayerId = this.moves.size() > 0 ? this.getLatestMove().getPlayerId() : 0;
        if (!this.inSetup) this.nextPlayer();
    }

    public void setStartingPositions() {
        for (Player player : players) {
            int playerId = player.getPlayerId();
            // Find starting positions
            // Create player count x player count grid in the middle of the board with diagonals of players
            int[] startingPositions = new int[this.players.length];
            int topStartColumn = (this.columns / 2) - (this.players.length / 2);
            int topStartRow = (this.rows / 2) - (this.players.length / 2);

            for (int i = 0; i < this.players.length; i++) {
                int column = topStartColumn + i;
                int row = topStartRow + (playerId + i) % this.players.length;
                startingPositions[i] = row * this.columns + column;
            }

            // Set starting positions
            for (int position : startingPositions) {
                this.board[position] = playerId;
                this.startingPositions[position] = position;
            }
        }
    }

    public void nextPlayer() {
        if (!this.inSetup) this.round++;
        this.currentPlayerId = (this.currentPlayerId + 1) % this.players.length;
    }

    public int getPlayerCount() {
        return this.players.length;
    }

    public Player getPlayer(int playerId) {
        return this.players[playerId];
    }

    @Override
    public boolean isGameOver() {
        for (Player player : this.players) {
            var iter = this.validMoves(player.getPlayerId());
            while (iter.hasNext()) {
                if (iter.next() != null) return false;
            }
        }

        return true;
    }

    @Override
    public void startSetup(int maxPlacements) {
        this.inSetup = true;
        for (Player p : this.players) {
            p.maxPlacements = maxPlacements;
        }
    }

    @Override
    public void endSetup() {
        this.inSetup = false;
        this.currentPlayerId = 0;
    }

    public int getCurrentPlayerId() {
        return this.currentPlayerId;
    }

    public Player getCurrentPlayer() {
        return this.players[this.currentPlayerId];
    }

    public int getRound() {
        return this.round;
    }

    public Move getLatestMove() {
        return this.moves.stream().reduce((first, second) -> second).orElse(null);
    }

    public Move getMove(int moveIndex) {
        return this.moves.stream().skip(moveIndex).findFirst().orElse(null);
    }

    public ArrayList<Move> getMoves() {
        return this.moves;
    }

    public int getScore() {
        // Count amount of currentPlayerId in board
        return this.getScore(this.currentPlayerId);
    }

    public int getScore(int playerId) {
        // Count amount of playerId in board
        return (int) Arrays.stream(this.board).filter(i -> i == playerId).count();
    }

    public Player getWinner() {
        int maxScore = 0;
        Player winner = null;
        for (Player player : this.players) {
            int score = this.getScore(player.getPlayerId());
            if (score > maxScore) {
                maxScore = score;
                winner = player;
            }
        }

        return winner;
    }

    @Override
    public Iterator<Space> iterator() {
        return new Iterator<>() {
            int index = 0;
            @Override
            public boolean hasNext() {
                return index < rows * columns;
            }

            @Override
            public Space next() {
                int column = index % columns;
                int row = index / columns;
                index++;
                return new Space(column, row);
            }
        };
    }

    @Override
    public Board2D clone() {
        return this.copy(this.manual, this.isStatic);
    }

    public Board2D copy(boolean manual, boolean isStatic) {
        Board2D copy = new Board2D(this.rows, this.columns, this.players.clone(), manual);
        copy.board = this.board.clone();
        copy.startingPositions = this.startingPositions.clone();
        copy.currentPlayerId = this.currentPlayerId;
        copy.round = this.round;
        copy.inSetup = this.inSetup;
        copy.isStatic = isStatic;
        copy.moves = new ArrayList<>(this.moves);
        return copy;
    }
}
