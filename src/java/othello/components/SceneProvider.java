package othello.components;

// Basic class that exports a single Scene

import javafx.scene.Parent;
import javafx.scene.Scene;

public class SceneProvider {

    private String name;
    private Scene scene;
    private SceneManager sceneManager;

    public SceneProvider(SceneManager manager, String name) {
        this.name = name;
        this.sceneManager = manager;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setActive(SceneProvider provider) {
        this.sceneManager.setActive(provider);
    }

    public void setActive() {
        this.sceneManager.setActive(this);
    }

    public SceneManager getSceneManager() {
        return this.sceneManager;
    }

    public Parent getRoot() {
        return this.scene.getRoot();
    }

    public String getName() {
        return this.name;
    }
}
