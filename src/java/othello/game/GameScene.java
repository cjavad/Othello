package othello.game;

// Manages game Pane and events

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import othello.game.board.basic.BoardScene;
import othello.game.state.Board2D;
import othello.ui.GoBackOnce;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class GameScene extends SceneProvider {
    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");
        BoardScene boardScene = new BoardScene(manager, board);
        BorderPane root = new BorderPane();
        root.setCenter(boardScene.getRoot());
        GoBackOnce goBackOnce = new GoBackOnce(manager);
        root.setLeft(goBackOnce.getRoot());
        this.setScene(new Scene(root, manager.getWidth(), manager.getHeight()));
    }
}
