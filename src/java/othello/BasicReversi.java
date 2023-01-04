package othello;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import othello.game.GameUIWrapper;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

/**
 * Required preset class for assignment
 */

public class BasicReversi extends SceneProvider {
    public BasicReversi(SceneManager manager) {
        super(manager, "BasicReversi");
        // Create simple scene
        GameUIWrapper gameUI = new GameUIWrapper(manager);
        // Set scene
        this.setScene(gameUI.getScene());
    }
}
