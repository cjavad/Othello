package othello.game;

// Manages game Pane and events

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import othello.game.board.Board2DBasicUI;
import othello.ui.GoBackOnce;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class GameUIWrapper extends SceneProvider {
    public GameUIWrapper(SceneManager manager) {
        super(manager, "GameUIWrapper");
        Board2DBasicUI gameInterface = new Board2DBasicUI(manager, 2);

        BorderPane root = new BorderPane();
        root.setCenter(gameInterface.getRoot());
        GoBackOnce goBackOnce = new GoBackOnce(manager);
        root.setLeft(goBackOnce.getRoot());
        this.setScene(new Scene(root, manager.getWidth(), manager.getHeight()));
    }
}
