package othello;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.events.SettingsEvent;

/**
 * Required preset class for assignment
 */

public class AdvancedReversi extends SceneProvider {
    public AdvancedReversi(SceneManager manager) {
        super(manager, "AdvancedReversi");
        manager.setOption("RTX", true);
        this.setScene(new BasicReversi(manager).getScene());
    }
}
