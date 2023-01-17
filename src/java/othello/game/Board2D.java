package othello.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board2D implements othello.game.interfaces.Board2D {
    // Determines if moves are made automatically
    private boolean manual;

    private int columns;
    private int rows;

    private int[] board;
    private int round;
    private int currentPlayerId;
    private Player[] players;
    private ArrayList<Move> moves;

    private ArrayList<Space> spaces;

    public Board2D(Player[] players, boolean manual) {
        this(players.length * 4, players.length * 4, players, manual);
    }

    public Board2D(Player[] players, int[] startingBoard, boolean manual) {
        this(players.length * 4, players.length * 4, players, manual);
        this.board = startingBoard;
    }

    public Board2D(int columns, int rows, Player[] players, boolean manual) {
        this.columns = columns;
        this.rows = rows;
        this.board = new int[columns * rows];
        this.players = players;

        // Fill board with empty spaces
        Arrays.fill(this.board, -1);

        // Set starting positions
        for (Player player : players) {
            int playerId = player.getPlayerId();
            // Find starting positions
            int[] startingPositions = this.getStartingPositions(playerId);
            // Set starting positions
            for (int position : startingPositions) {
                this.board[position] = playerId;
            }
        }

        this.round = 0;
        this.currentPlayerId = 0;
        this.moves = new ArrayList<>();
        this.manual = manual;

        // Generate list of valid spaces initially once.
        this.spaces = new ArrayList<>();
        for (int column = 0; column < this.columns; column++) {
            for (int row = 0; row < this.rows; row++) {
                this.spaces.add(new Space(column, row));
            }
        }
    }

    public int getColumns() {
        return this.columns;
    }

    public int getRows() {
        return this.rows;
    }

    public Iterable<Space> getSpaces() {
        return this.spaces;
    }

    public int getCell(Space space) {
        return this.board[space.row * this.columns + space.column];
    }

    public void setCell(Space space, int playerId) {
        this.board[space.row * this.columns + space.column] = playerId;
    }

    public Line[] findLines(Space placementSpace, int playerId) {
        // Find all lines that are captured by this move
        Line[] lines = {
            new Line(placementSpace, "vertical", this.columns, this.rows),
            new Line(placementSpace, "horizontal", this.columns, this.rows),
            new Line(placementSpace, "diagonal", this.columns, this.rows),
            new Line(placementSpace, "antiDiagonal", this.columns, this.rows)
        };

        for (int i = 0; i < lines.length; i++) {
            Line line = lines[i];
            if (line == null) continue;

            Space startSpace = null;
            Space endSpace = null;
            Space lastOwnSpace = null;

            for (int j = 0; j < line.length(); j++) {
                Space space = line.at(j);

                if (space == null) continue;
                int cell = this.getCell(space);

                if (startSpace != null && !space.equals(placementSpace) && cell == -1) {
                    // Start space does not touch any other pieces
                    startSpace = null;
                    lastOwnSpace = null;
                    continue;
                }

                if (space.equals(placementSpace) || cell == playerId) {
                    if (startSpace == null) startSpace = space;
                    else if (space.distanceTo(startSpace) < 2) {
                        // If the current own space is too close to the start then we can't place here.
                        startSpace = space;
                    } else {
                           endSpace = space;
                        // If the distance between the last own space and the current own space is greater than 1, then the line is finished.
                        if (endSpace.distanceTo(lastOwnSpace) > 1 &&
                            (startSpace.equals(placementSpace) ||
                                endSpace.equals(placementSpace)))
                            break;
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

            if (
                    (this.getCell(endSpace) == -1 && !endSpace.equals(placementSpace)) ||
                    (this.getCell(startSpace) == -1 && !startSpace.equals(placementSpace)) ||
                    !line.contains(placementSpace) || line.length() < 3
            ) {
                lines[i] = null;
            }
        }

        // Find all valid lines
        return Arrays.stream(lines).filter(line -> this.isValidLine(line, placementSpace, playerId)).toArray(Line[]::new);
    }

    public boolean isValidLine(Line line, Space placementSpace, int playerId) {
        if (line == null) return false;
        // Line cannot contain any empty spaces
        for (int i = 0; i < line.length(); i++) {
            Space space = line.at(i);
            if (space == null) continue;
            if (this.getCell(space) == -1 && i != 0 && i != line.length() - 1) return false;
        }

        return true;
    }

    public int isValidMove(Space space, int playerId) {
        // Get state of space
        int playerOccupied = this.getCell(space);

        // If the player already occupies the space, then the move is invalid (1)
        if (playerOccupied == playerId) return 1;
        // Find last move
        Move lastMove = this.getLatestMove();

        // If last move was made in this round, then the following moves can only be flips
        if (lastMove != null && lastMove.getRound() == this.round) {
            // If the space is empty, then the move is invalid, as it is not a flip (1)
            if (playerOccupied == -1) return 1;

            // Flips can only be made on existing lines
            // Fetch precalculated lines
            Line[] lines = lastMove.getLines();

            // For each line if the space is on the line, then the move is valid (0)
            for (Line line : lines) {
                if (line == null) continue;
                if (line.contains(space)) {
                    return 0;
                }
            }
        } else if (playerOccupied == -1) {
            Line[] lines = this.findLines(space, playerId);

            for (Line line : lines) {
                if (line == null) continue;
                if (line.length() < 3) continue;
                // Cannot be neighbour to own piece
                if (line.at(1) != null && line.at(1).equals(space) && this.getCell(line.at(1)) == playerId) continue;
                if (line.at(line.length() - 2) != null && line.at(line.length() - 2).equals(space) && this.getCell(line.at(line.length() - 2)) == playerId) continue;

                for (int i = 0; i < line.length(); i++) {
                    Space lineSpace = line.at(i);
                    if (lineSpace == null) continue;

                    if (!space.equals(lineSpace) && this.getCell(lineSpace) != playerId && this.getCell(lineSpace) != -1) {
                        return 0;
                    }
                }
            }
        }

        // Otherwise the move is invalid
        return 1;
    }

    public Iterable<Space> getValidMoves(int playerId) {
        // Loop over entire board and determine valid moves
        ArrayList<Space> validMoves = new ArrayList<>();

        for (Space space : this.getSpaces()) {
            if (this.isValidMove(space, playerId) == 0) {
                validMoves.add(space);
            }
        }

        return validMoves;
    }

    // Make above function an iterator that calls isValidMove every run
    public Iterator<Space> getValidMovesIterator(int playerId) {
        final int[] spaceIndex = {0};
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return spaceIndex[0] < spaces.size();
            }

            @Override
            public Space next() {
                Space space = spaces.get(spaceIndex[0]);
                if (isValidMove(space, playerId) == 0) {
                    return space;
                }
                spaceIndex[0]++;
                return null;
            }
        };
    }


    public Move move(Space space) {
        if (this.isValidMove(space, this.currentPlayerId) > 0) return null;
        int prevValue = this.getCell(space);

        Move move = this.getLatestMove();

        if (move == null || move.getRound() != this.round || move.getPlayerId() != this.currentPlayerId) {
            // We are making a new placement
            // Create a new move
            this.setCell(space, this.currentPlayerId);
            move = new Move(this.round, this.currentPlayerId, space, this.findLines(space, this.currentPlayerId), new ArrayList<>());
            this.moves.add(move);
        }

        ArrayList<Change> changes = (ArrayList<Change>) move.getChanges();

        if (this.manual) {
            // Don't propagate changes if manual
            changes.add(new Change(space, prevValue));
            // move.invalidateLinesMove(space);
            this.setCell(space, this.currentPlayerId);
        } else {
            // Automatically propagate changes by executing all valid moves
            Iterator<Space> validMovesIterator = this.getValidMovesIterator(this.currentPlayerId);

            while (validMovesIterator.hasNext()) {
                Space validMove = validMovesIterator.next();
                if (validMove == null) continue;
                changes.add(new Change(validMove, this.getCell(validMove)));
                move.setChanges(changes);
                // Update valid lines
                // move.invalidateLinesMove(space);
                this.setCell(validMove, this.currentPlayerId);
            }

            // Automatically switch player
            this.nextPlayer();
        }

        move.setChanges(changes);

        return move;
    }

    public int[] getStartingPositions(int playerId) {
        // Create player count x player count grid in the middle of the board with diagonals of players
        int[] startingPositions = new int[this.players.length];
        int topStartColumn = (this.columns / 2) - (this.players.length / 2);
        int topStartRow = (this.rows / 2) - (this.players.length / 2);

        for (int i = 0; i < this.players.length; i++) {
            int column = topStartColumn + i;
            int row = topStartRow + (playerId + i) % this.players.length;
            startingPositions[i] = row * this.columns + column;
        }

        return startingPositions;
    }

    public void nextPlayer() {
        this.round += 1;
        this.currentPlayerId = (this.currentPlayerId + 1) % this.players.length;
    }

    public int getPlayerCount() {
        return this.players.length;
    }

    public Player getPlayer(int playerId) {
        return this.players[playerId];
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

    @Override
    public Iterable<Move> getMoves() {
        return this.moves;
    }

    public int getScore() {
        // Count amount of currentPlayerId in board
        return this.getScore(this.currentPlayerId);
    }

    public int getScore(int playerId) {
        // Count amount of playerId in board
        int score = 0;

        for (Space space : this.getSpaces()) {
            if (this.getCell(space) == playerId) score++;
        }

        return score;
    }
}
