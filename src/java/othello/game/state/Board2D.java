package othello.game.state;

import java.util.ArrayList;
import java.util.Arrays;

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

    public Board2D(Player[] players, boolean manuel) {
        this(players.length*4, players.length*4, players, manuel);
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
        this.moves = new ArrayList<Move>();
        this.manual = manuel;
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public int getCell(int row, int column) {
        return this.board[row * this.columns + column];
    }

    public void setCell(int row, int column, int playerId) {
        this.board[row * this.columns + column] = playerId;
    }

    public int[] findNeighbor(int row, int columns, int direction, int steps) {
        // Format: [ [row, column], ... ]
        int[] neighbor = new int[2];
        // Directions are Top (0), TopRight (1), Right (2), BottomRight (3), Bottom (4), BottomLeft (5), Left (6), TopLeft (7)
        // Use direction to find stepX and stepY
        // Optimize and minify above switch statement to a ternary operator
        neighbor[0] = row + steps * ((direction == 0 || direction == 1 || direction == 2) ? 1 : (direction == 4 || direction == 5 || direction == 6) ? -1 : 0);
        neighbor[1] = columns + steps * ((direction == 2 || direction == 3 || direction == 4) ? 1 : (direction == 6 || direction == 7 || direction == 0) ? -1 : 0);
        return neighbor[0] < 0 || neighbor[0] >= this.rows || neighbor[1] < 0 || neighbor[1] >= this.columns ? null : neighbor;
    }

    public int[][] getAllNeighbors(int row, int column) {
        // Format: [ [row, column], ... ]
        int[][] neighbors = new int[8][2];
        for (int direction = 0; direction < 8; direction++)
            neighbors[direction] = findNeighbor(row, column, direction, 1);
        return neighbors;
    }

    public int isValidMove(int row, int column, int playerId) {
        // Determine if move can be played by player
        int playerOccupied = this.getCell(row, column);
        if (playerOccupied == playerId) return 1;
        // If last move was made in this round and the target cell is empty it is not a valid move
        if (this.getLatestMove() != null && this.getLatestMove().getRound() == this.round && playerOccupied == -1) return 1;

        int[][] neighbors = this.getAllNeighbors(row, column);
        boolean[] validDirections = new boolean[8];

        for (int direction = 0; direction < 8; direction++) {
            if (neighbors[direction] == null) continue;
            int neighbor = this.getCell(neighbors[direction][0], neighbors[direction][1]);
            if (neighbor == -1) continue;
            if (neighbor == playerId) {
                validDirections[direction] = true;
                continue;
            }

            // Otherwise loop for own piece in direction (to ensure that there is a line between the two pieces)
            int steps = 1;
            int[] nextNeighbor;
            int nextNeighborId;

            do {
                nextNeighbor = findNeighbor(neighbors[direction][0], neighbors[direction][1], direction, steps);
                nextNeighborId = nextNeighbor == null ? -1 : this.getCell(nextNeighbor[0], nextNeighbor[1]);
                // If we find our own piece we can flip or place the piece
                if (nextNeighborId == playerId) return 0;
                steps++;
            } while (nextNeighborId != -1);
        }

        // If there are two opposing valid directions we can flip or place the piece
        if (validDirections[0] && validDirections[4]) return 0;
        if (validDirections[1] && validDirections[5]) return 0;
        if (validDirections[2] && validDirections[6]) return 0;
        if (validDirections[3] && validDirections[7]) return 0;

        return 1;
    }

    public Iterable<int[]> getValidMoves(int playerId) {
        // Loop over entire board and determine valid moves
        ArrayList<int[]> validMoves = new ArrayList<int[]>();

        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                if (this.isValidMove(row, column, playerId) == 0) {
                    validMoves.add(new int[]{row, column});
                }
            }
        }

        return validMoves;
    }

    public Move move(int row, int column) {
        if (this.isValidMove(row, column, this.currentPlayerId) > 0) return null;
        int prevValue = this.getCell(row, column);

        ArrayList<Change> changes = new ArrayList<>();

        if (this.manual) {
            // Don't propagate changes if manual
            changes.add(new Change(row, column, prevValue));
            this.setCell(row, column, this.currentPlayerId);
        } else {
            // Automatically propagate changes
            // TODO
        }

        // Create move
        Move move = new Move(this.round, this.currentPlayerId, changes);
        // Add to list of moves
        this.moves.add(move);
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
        for (int row = 0; row < this.rows; row++) {
            for (int column = 0; column < this.columns; column++) {
                if (this.getCell(row, column) == playerId) score++;
            }
        }
        return score;
    }
}
