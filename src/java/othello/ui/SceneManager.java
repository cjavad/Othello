package othello.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private Stage stage;
    private Scene activeScene;
    public SceneManager(Stage stage) {
        this.stage = stage;
    }

    public void setActiveScene(Scene scene) {
        this.activeScene = scene;
        stage.setScene(this.activeScene);
        this.stage.show();
    }

    public void setActive(SceneProvider provider) {
        setActiveScene(provider.getScene());
    }

    public Scene getActiveScene() {
        return this.activeScene;
    }

    public Stage getStage() {
        return this.stage;
    }
}
