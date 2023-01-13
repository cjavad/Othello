package othello.ui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

public class SceneManager {
    private int width;
    private int height;

    private Stage stage;
    private ArrayList<SceneProvider> scenes;
    private int activeSceneIndex;
    public SceneManager(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.scenes = new ArrayList<>();
    }

    public void setActiveScene(Scene scene) {
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void setActive(SceneProvider provider) {
       this.setActive(provider, false);
    }

    public void setActive(SceneProvider provider, boolean resetScene) {
        // There can only be one active scene of each type at a time, check if name already exists
        for (int i = 0; i < this.scenes.size(); i++) {
            if (this.scenes.get(i).getName().equals(provider.getName())) {
                if (resetScene) {
                    this.scenes.set(i, provider);
                } else {
                    provider = this.scenes.get(i);
                }
                this.activeSceneIndex = i;
                this.setActiveScene(provider.getScene());
                return;
            }
        }

        this.scenes.add(provider);
        this.activeSceneIndex = scenes.size() - 1;
        this.setActiveScene(provider.getScene());
    }

    public Scene getActiveScene() {
        return this.scenes.get(this.activeSceneIndex).getScene();
    }

    public Scene getPreviousScene() {
        return this.scenes.get(this.activeSceneIndex - 1).getScene();
    }

    public void goBack() {
        this.activeSceneIndex--;
        this.setActiveScene(this.getActiveScene());
    }

    public void goForward() {
        if (this.activeSceneIndex < this.scenes.size() - 1) {
            this.activeSceneIndex++;
            this.setActiveScene(this.getActiveScene());
        }
    }

    public Stage getStage() {
        return this.stage;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
