package othello.game.board.basic;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import othello.game.state.Board2D;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class BoardScene extends SceneProvider {
    Board2D board; // Board Model
    BorderPane root; // Board View
    GridPane grid; // Board View
    GridPane bottomPane; // Board View

    public BoardScene(SceneManager manager, Board2D board) {
        super(manager, "Board2DScene");
        this.board = board;
        this.root = new BorderPane();
        this.grid = this.createGrid();
        this.grid.setAlignment(javafx.geometry.Pos.CENTER);
        this.root.setCenter(this.grid);
        this.bottomPane = this.createBottomPane();
        this.bottomPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        this.root.setBottom(this.bottomPane);
        this.setScene(new Scene(this.root, manager.getWidth(), manager.getHeight()));
    }

    public GridPane createBottomPane() {
        // Text box to display current player
        Text currentPlayerText = new Text("Current player: " + this.board.getCurrentPlayerId());
        currentPlayerText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        Text currentPlayerScore = new Text("Current player score: " + this.board.getScore(this.board.getCurrentPlayerId()));
        currentPlayerScore.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        // Create button to switch player
        Button switchPlayerButton = new Button("Switch player");
        switchPlayerButton.setOnAction(event -> {
            this.board.nextPlayer();
            this.updateGrid();
            currentPlayerText.setText("Current player: " + this.board.getCurrentPlayerId());
            currentPlayerScore.setText("Current player score: " + this.board.getScore(this.board.getCurrentPlayerId()));
        });

        GridPane bottomPane = new GridPane();
        bottomPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        bottomPane.add(currentPlayerText, 0, 0);
        bottomPane.add(currentPlayerScore, 1, 0);
        bottomPane.add(switchPlayerButton, 2, 0);
        // Add spacing
        bottomPane.setHgap(10);

        return bottomPane;
    }

    public GridPane createGrid() {
        GridPane boardGrid = new GridPane();

        for (int row = 0; row < this.board.getRows(); row++) {
            for (int column = 0; column < this.board.getColumns(); column++) {
                Pane cell = new Pane();
                this.updateCell(cell, row, column);

                // Calculate cell size based on width of screen
                // Only based on width (square)
                int cellSize = (this.getSceneManager().getWidth() * 2/3) / this.board.getColumns();
                cell.setPrefSize(cellSize, cellSize);

                int finalRow = row, finalColumn = column;
                cell.setOnMouseClicked(event -> { this.handleCellClick(finalRow, finalColumn); });

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

    public void updateGrid() {
        for (int row = 0; row < this.board.getRows(); row++) {
            for (int column = 0; column < this.board.getColumns(); column++) {
                Pane cell = this.getCell(row, column);
                this.updateCell(cell, row, column);
            }
        }
    }

    public Pane getCell(int row, int column) {
        return (Pane) this.grid.getChildren().get(row * this.board.getColumns() + column);
    }

    public void updateCell(Pane cell, int row, int column) {
        int cellOccupant = this.board.getCell(row, column);
        AtomicReference<Ellipse> pieceRef = new AtomicReference<>();

        String borderColor = this.board.isValidMove(row, column, this.board.getCurrentPlayerId()) == 0 ? "black" : "green";
        String cellStyle = "-fx-border-width: 1; -fx-border-color: "+ borderColor + "; -fx-background-color: darkgreen;";
        cell.setStyle(cellStyle);
        cell.getChildren().forEach(child -> {
            if (child instanceof Ellipse ellipse) {
                pieceRef.set(ellipse);
            }
        });

        if (pieceRef.get() == null) {
            pieceRef.set(new Ellipse(cell.getWidth(), cell.getHeight()));
        }

        Ellipse piece = pieceRef.get();
        piece.setFill(cellOccupant > -1 ? Paint.valueOf(this.board.getPlayer(cellOccupant).getColor()) : javafx.scene.paint.Color.TRANSPARENT);

        if (cellOccupant > -1) {
            // Black outline
            piece.setStroke(javafx.scene.paint.Color.BLACK);
            piece.setStrokeWidth(1);

            // Center piece
            piece.centerXProperty().bind(cell.widthProperty().divide(2));
            piece.centerYProperty().bind(cell.heightProperty().divide(2));
            // Make piece fit in cell
            piece.radiusXProperty().bind(cell.widthProperty().divide(2).subtract(2));
            piece.radiusYProperty().bind(cell.heightProperty().divide(2).subtract(2));

            // Set piece to be on top of cell
            piece.toFront();
        }

        // Add piece to cell if it's not already there
        if (!cell.getChildren().contains(piece)) {
            cell.getChildren().add(piece);
        }
    }

    public void handleCellClick(int row, int column) {
        this.board.move(row, column);
        this.updateGrid();
    }
}
