package othello.components.board;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.ui.PauseMenu;
import othello.game.Board2D;
import othello.game.Space;

import java.util.concurrent.atomic.AtomicReference;

public class BoardViewer2D extends SceneProvider {
    public Board2D board;
    public GridPane grid;
    public GridPane bottomPane;
    public GridPane topPane;

    public BoardViewer2D(SceneManager manager, Board2D board) {
        super(manager, "BoardViewer2D");
        this.board = board;
        this.grid = this.createGrid();
        this.bottomPane = this.createBottomPane();
        this.bottomPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        BorderPane pane = new BorderPane();

        this.topPane = this.createTopPane();
        //this.topPane.setAlignment(javafx.geometry.Pos.TOP_CENTER);

        pane.setTop(this.topPane);
        pane.setCenter(this.grid);
        pane.setBottom(this.bottomPane);

        this.setScene(new Scene(pane, manager.getWidth(), manager.getHeight()));
    }

    public GridPane createTopPane() {
        GridPane pane = new GridPane();
        Text menuText = new Text("Menu");
        //pane.add(menuText, 0, 0);

        //creates menu button
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> new PauseMenu(this.getSceneManager()).setActive());
        pane.add(menuButton, 0, 0);
        return pane;
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
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        for (Space space : this.board) {
            Pane cell = new Pane();
            this.updateCell(cell, space);
            int cellSize = (this.getSceneManager().getWidth() * 2 / 3) / this.board.getColumns();
            cell.setPrefSize(cellSize, cellSize);
            cell.setOnMouseClicked(event -> this.handleCellClick(space));

            grid.add(cell, space.column, space.row);
        }

        return grid;
    }

    public void updateGrid() {
        for (Space space : this.board) {
            Pane cell = (Pane) this.grid.getChildren().get(space.row * this.board.getColumns() + space.column);
            this.updateCell(cell, space);
        }
    }

    public void updateCell(Pane cell, Space space) {
        int cellOccupant = this.board.getSpace(space);
        AtomicReference<Ellipse> pieceRef = new AtomicReference<>();

        String borderColour = this.board.isValidMove(space, this.board.getCurrentPlayerId()) == 0 ? "black" : "green";
        String cellStyle = "-fx-border-width: 1; -fx-border-color: " + borderColour + "; -fx-background-color: darkgreen;";
        cell.setStyle(cellStyle);
        cell.getChildren().forEach(child -> {
            if (child instanceof Ellipse ellipse) {
                pieceRef.set((ellipse));
            }
        });

        if (pieceRef.get() == null) {
            pieceRef.set(new Ellipse(cell.getWidth(), cell.getHeight()));
        }

        Ellipse piece = pieceRef.get();
        piece.setFill(cellOccupant > -1 ? Paint.valueOf(this.board.getPlayer(cellOccupant).getColor()) : javafx.scene.paint.Color.TRANSPARENT);

        if (cellOccupant > -1) {
            //Black outline
            piece.setStroke((javafx.scene.paint.Color.BLACK));
            piece.setStrokeWidth(1);

            //Center the piece
            piece.centerXProperty().bind(cell.widthProperty().divide(2));
            piece.centerYProperty().bind(cell.heightProperty().divide(2));

            //Make piece fit cell
            piece.radiusXProperty().bind(cell.widthProperty().divide(2).subtract(2));
            piece.radiusYProperty().bind(cell.heightProperty().divide(2).subtract(2));

            //Set piece on top of cell
            piece.toFront();
        }

        //Add piece to cell if it's not already there
        if (!cell.getChildren().contains(piece)) {
            cell.getChildren().add(piece);
        }
    }

    public void handleCellClick(Space space) {
        this.board.move(space);
        this.updateGrid();
    }
}

