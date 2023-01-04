package othello;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

/**
 * Required preset class for assignment
 */

public class AdvancedReversi extends SceneProvider {
    public AdvancedReversi(SceneManager sceneManager) {
        super(sceneManager);

        // Create simple scene
        Scene scene = new Scene(new Pane(), 800, 600);
        // Set scene
        this.setScene(scene);
    }
}
