package othello.game;

// Manages game Pane and events

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import othello.game.board.advanced.BoardScene;
import othello.game.state.Board2D;
import othello.ui.GoBackOnce;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class GameScene extends SceneProvider {
    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");

        Scene boardScene = new BoardScene(manager, board).getScene();
		BorderPane root = new BorderPane();
        GoBackOnce goBackOnce = new GoBackOnce(manager);
        root.setTop(goBackOnce.getRoot());
		root.setCenter(boardScene.getRoot());
		boardScene.setRoot(root);
		this.setScene(boardScene);
	}
}
