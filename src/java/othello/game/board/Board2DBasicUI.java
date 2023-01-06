package othello.game.board;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import othello.game.state.Board2D;
import othello.game.state.Player;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class Board2DBasicUI extends SceneProvider {
    private Board2D board;
    private GridPane boardGrid;
    private Player[] players;

    public Board2DBasicUI(SceneManager manager, int playerCount) {
        super(manager, "Board2DBasicUI");
        this.players = new Player[playerCount];

        for (int i = 0; i < playerCount; i++) {
            this.players[i] = new Player(i);
        }

        this.board = new Board2D(this.players, true);
        this.boardGrid = this.createBoard();
        this.boardGrid.setAlignment(javafx.geometry.Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(this.boardGrid);

        // Text box to display current player
        Text currentPlayerText = new Text("Current player: " + this.board.getCurrentPlayerId());
        currentPlayerText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        Text currentPlayerScore = new Text("Current player score: " + this.board.getScore(this.board.getCurrentPlayerId()));
        currentPlayerScore.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Create button to switch player
        Button switchPlayerButton = new Button("Switch player");
        switchPlayerButton.setOnAction(event -> {
            this.board.nextPlayer();
            this.updateBoard();
            currentPlayerText.setText("Current player: " + this.board.getCurrentPlayerId());
            currentPlayerScore.setText("Current player score: " + this.board.getScore(this.board.getCurrentPlayerId()));
        });

        // Combine text and button
        GridPane bottomPane = new GridPane();
        bottomPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        bottomPane.add(currentPlayerText, 0, 0);
        bottomPane.add(currentPlayerScore, 1, 0);
        bottomPane.add(switchPlayerButton, 2, 0);
        // Add spacing
        bottomPane.setHgap(10);
        root.setBottom(bottomPane);

        Scene gameScene = new Scene(root, manager.getWidth(), manager.getHeight());
        this.setScene(gameScene);
    }

    public void updateBoard() {
        // Find all cells
        for (int row = 0; row < this.board.getRows(); row++) {
            for (int column = 0; column < this.board.getColumns(); column++) {
                Pane cell = (Pane) this.boardGrid.getChildren().get(row * this.board.getColumns() + column);
                this.updateCell(cell, row, column);
            }
        }
    }

    public void updateCell(Pane cell, int row, int column) {
        int newPlayerId = this.board.getCell(row, column);
        String color = "#FF0000";
        String borderColor = "#000000";
        if (newPlayerId != -1) {
            color = this.players[newPlayerId].getColor();
        }
        // If the cell is a valid move for the current player, highlight it
        if (this.board.isValidMove(row, column, this.board.getCurrentPlayerId()) == 0) {
            borderColor = "#00FF00";
        }
        cell.setStyle("-fx-border-width: 1; -fx-border-color: " + borderColor + "; -fx-background-color: " + color + ";");
    }

    public GridPane createBoard() {
        GridPane boardGrid = new GridPane();

        for (int row = 0; row < this.board.getRows(); row++) {
            for (int column = 0; column < this.board.getColumns(); column++) {
                Pane cell = new Pane();
                this.updateCell(cell, row, column);
                cell.setPrefSize(50, 50);
                int finalRow = row;
                int finalColumn = column;
                cell.setOnMouseClicked(event -> {
                    this.board.move(finalRow, finalColumn);
                    this.updateBoard();
                });
                // Add (row, column) text to cell
                Text cellText = new Text("(" + row + ", " + column + ")");
                // Give text white outline
                cellText.setStyle("-fx-stroke: white; -fx-stroke-width: 0.3;");
                StackPane cellStack = new StackPane();
                cellStack.setAlignment(Pos.CENTER);
                cellStack.getChildren().addAll(cellText);
                cell.getChildren().add(cellStack);
                boardGrid.add(cell, row, column);
                // Don't ask me why, but this is necessary to make the grid work
                if (column == 0) boardGrid.setHgap(-1);
            }
        }

        return boardGrid;
    }
}
