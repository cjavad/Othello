package othello;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

/**
 * Required preset class for assignment
 */

public class BasicReversi extends SceneProvider {
    public BasicReversi(SceneManager manager) {
        super(manager);

        // Create simple scene
        Scene scene = new Scene(new Pane(), 800, 600);
        // Set scene
        this.setScene(scene);
    }
}
