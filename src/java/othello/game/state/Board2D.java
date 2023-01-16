package othello.game.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Board2D implements othello.game.state.interfaces.Board2D {
    // Determines if moves are made automatically
    private boolean manual = true;
    private int rows;
    private int columns;
    private int[] board;
    private int round;
    private int currentPlayerId;
    private Player[] players;
    private ArrayList<Move> moves;

    private ArrayList<Space> spaces;

    public Board2D(Player[] players, boolean manuel) {
        this(players.length * 4, players.length * 4, players, manuel);
    }

    public Board2D(Player[] players, int[] startingBoard, boolean manuel) {
        this(players.length * 4, players.length * 4, players, manuel);
        this.board = startingBoard;
    }

    public Board2D(int rows, int columns, Player[] players, boolean manuel) {
        this.rows = rows;
        this.columns = columns;
        this.board = new int[rows * columns];
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
        this.manual = manuel;

        // Generate list of valid spaces initially once.
        this.spaces = new ArrayList<>();

        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                this.spaces.add(new Space(row, column));
            }
        }
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
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

    public Space getRelativeSpace(Space currentSpace, int direction, int steps) {
        // Directions are Top (0), TopRight (1), Right (2), BottomRight (3), Bottom (4), BottomLeft (5), Left (6), TopLeft (7)
        // Use direction to find stepX and stepY
        // Optimize and minify above switch statement to a ternary operator
        int stepsX = steps * ((direction == 0 || direction == 1 || direction == 2) ? 1 : (direction == 4 || direction == 5 || direction == 6) ? -1 : 0);
        int stepsY = steps * ((direction == 2 || direction == 3 || direction == 4) ? 1 : (direction == 6 || direction == 7 || direction == 0) ? -1 : 0);
        return currentSpace.getRelativeSpace(stepsX, stepsY, this.rows, this.columns);
    }

    public Space[] getAllNeighbors(Space space) {
        Space[] neighbors = new Space[8];
        for (int direction = 0; direction < neighbors.length; direction++)
            neighbors[direction] = getRelativeSpace(space, direction, 1);
        return neighbors;
    }

    public Line[] findLines(Space placementSpace, int playerId) {
        // Find all lines that are captured by this move
        Line[] lines = {
            new Line(placementSpace, "vertical", this.rows, this.columns),
            new Line(placementSpace, "horizontal", this.rows, this.columns),
            new Line(placementSpace, "diagonal", this.rows, this.columns),
            new Line(placementSpace, "antiDiagonal", this.rows, this.columns)
        };

        boolean debug = placementSpace.equals(new Space(5, 4));


        for (int i = 0; i < lines.length; i++) {
            boolean found = false;

            int len = lines[i].length();
            Space currentStart = lines[i].getStart();
            Space currentEnd = lines[i].getEnd();
            Space lastOwnOffset = null;

            for (int j = 0; j < len; j++) {
                Space space = lines[i].at(j);
                if (space == null) continue;
                int cell = this.getCell(space);
                if (found && !space.equals(placementSpace) && cell == -1) break;

                if (space.equals(placementSpace) || cell == playerId) {
                    // This space is considered owned by playerId
                    if (found) {
                        // This space is the end of the line
                        currentEnd = space;

                        if (currentEnd.distanceTo(lastOwnOffset) > 1) {
                            break;
                        } else {
                            lastOwnOffset = currentEnd;
                        }
                    } else {
                        // This space is the start of the line
                        currentStart = space;
                        lastOwnOffset = space;
                        found = true;
                    }
                }
            }


            if (lines[i] == null) {
                continue;
            }



            lines[i].setStart(currentStart);
            lines[i].setEnd(currentEnd);

            if (
                    (this.getCell(currentStart) == -1 && !currentStart.equals(placementSpace)) ||
                    (this.getCell(currentEnd) == -1 && !currentEnd.equals(placementSpace)) ||
                    !found ||
                    !lines[i].contains(placementSpace)
                    || lines[i].length() < 3
            ) {
                lines[i] = null;
            }
        }

        // Find all non-null lines
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
        boolean debug = space.equals(new Space(5, 4));
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
                if (line.at(1) != null && line.at(1).equals(space) && this.getCell(line.at(1)) == playerId) continue;
                if (line.at(line.length() - 2) != null && line.at(line.length() - 2).equals(space) && this.getCell(line.at(line.length() - 2)) == playerId) continue;
                // If line contains any spaces that are not occupied by the player and the move is a flip the move is valid
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

    // MAke above function an iterator that calls isValidMove every run
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

        if (move != null && move.getRound() == this.round && move.getPlayerId() == this.currentPlayerId) {
        } else {
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
        int topStartRow = (this.rows / 2) - (this.players.length / 2);
        int topStartColumn = (this.columns / 2) - (this.players.length / 2);

        for (int i = 0; i < this.players.length; i++) {
            int row = topStartRow + (playerId + i) % this.players.length;
            int column = topStartColumn + i;
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

    public String toString() {
        StringBuilder boardString = new StringBuilder("Cols:  ");
        int i = 0;
        int lc = 0;

        for (int j = 0; j < this.columns; j++) {
            boardString.append(String.format("%3s", j));
        }

        for (Space s : this.getSpaces()) {
            if (i % this.columns == 0) {
                boardString.append(String.format("\n%s\nRow %1s: ", "-".repeat(this.columns * 5), s.row));
            }


            // pad width of each cell to 3 characters
            boardString.append(String.format("%3s", this.getCell(s)));
            i++;
        }
        return boardString.toString();
    }
}
