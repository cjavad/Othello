package othello;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import othello.components.SceneManager;
import othello.components.SceneProvider;

/**
 * Required preset class for assignment
 */

public class AdvancedReversi extends SceneProvider {
    public AdvancedReversi(SceneManager manager) {
        super(manager, "AdvancedReversi");

        // Create simple scene
        Scene scene = new Scene(new Pane(), manager.getWidth(), manager.getHeight());
        // Set scene
        this.setScene(scene);
    }
}
