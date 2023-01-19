package othello.components.board.basic;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import othello.components.board.BoardButtons;
import othello.components.board.BoardTopbar;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Move;
import othello.game.Space;
import othello.components.SceneManager;
import othello.components.SceneProvider;

public class BoardScene2D extends SceneProvider {
    private Board2D board; // Board Model
    private BoardGrid boardGrid;

    public BoardScene2D(SceneManager manager, Board2D board) {
        super(manager, "BoardScene2D");
        this.board = board;
        this.boardGrid = new BoardGrid(this.board, (manager.getWidth() * 2/3) / this.board.getColumns());

        manager.getStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            int sizeIndicator = (int) Math.min(manager.getStage().getWidth(), manager.getStage().getHeight());
            this.boardGrid.setCellSize((sizeIndicator * 3/4) / this.board.getColumns());
        });

        manager.getStage().heightProperty().addListener((observable, oldValue, newValue) -> {
            int sizeIndicator = (int) Math.min(manager.getStage().getWidth(), manager.getStage().getHeight());
            this.boardGrid.setCellSize((sizeIndicator * 3/4) / this.board.getColumns());
        });

        this.setScene(new Scene(this.boardGrid, manager.getWidth(), manager.getHeight()));

        this.getRoot().addEventHandler(MoveEvent.UPDATE, event -> {
            this.handleUpdate(event.space);
        });
    }

    public void handleUpdate(Space space) {
        this.boardGrid.update();
    }

    public void setStatic(int moveIndex) {
        // -1 being the starting position
        if (moveIndex < -1) {
            this.boardGrid.setStaticBoard(null);
            return;
        }
        // Show static board from move @
        Board2D b = this.board.copy(true, true);
        b.revert(moveIndex);
        this.boardGrid.setStaticBoard(b);
    }
}
