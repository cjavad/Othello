package othello.components.board.basic;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Space;
import othello.components.SceneManager;
import othello.components.SceneProvider;

public class BoardScene2D extends SceneProvider {
    private Board2D board; // Board Model
    BoardGrid boardGrid;
    private BoardButtons bP;

    public BoardScene2D(SceneManager manager, Board2D board) {
        super(manager, "BoardScene2D");
        this.board = board;

        // Create required nodes
        BorderPane root = new BorderPane();
        BoardTopbar boardTopbar = new BoardTopbar(manager);

        this.boardGrid = new BoardGrid(this.board, (manager.getWidth() * 2/3) / this.board.getColumns());
        this.bP = new BoardButtons(this.board);
        this.bP.addEventHandler(MoveEvent.UPDATE, event -> {
            this.handleUpdate(event.space);
        });

        // Add nodes to root
        root.setTop(boardTopbar);
        root.setCenter(boardGrid);
        root.setBottom(bP);

        this.setScene(new Scene(root, manager.getWidth(), manager.getHeight()));

        this.getScene().addEventHandler(MoveEvent.UPDATE, event -> {
            this.handleUpdate(event.space);
        });
    }

    public void handleUpdate(Space space) {
        this.bP.update();
        this.boardGrid.update();
    }
}
