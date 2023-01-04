package othello.ui;

// Basic class that exports a single Scene

import javafx.scene.Scene;

public class SceneProvider {
    private Scene scene;
    private SceneManager sceneManager;

    public SceneProvider(SceneManager manager) {
        this.sceneManager = manager;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public SceneManager getSceneManager() {
        return this.sceneManager;
    }
}
