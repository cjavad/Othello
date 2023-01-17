package othello.components;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class SceneManager {
    private int width;
    private int height;

    private Stage stage;
    private ArrayList<SceneProvider> scenes;
    private int activeSceneIndex;

    private HashMap<String, Boolean> options;
    public SceneManager(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.scenes = new ArrayList<>();
        this.options = new HashMap<>();

        // Update width and height on resize
        this.stage.widthProperty().addListener((observable, oldValue, newValue) -> {
            this.width = newValue.intValue();
        });

        this.stage.heightProperty().addListener((observable, oldValue, newValue) -> {
            this.height = newValue.intValue();
        });
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

    public void resetScene(String name, boolean onlyOnActive) {
        for (int i = 0; i < this.scenes.size(); i++) {
            if (this.scenes.get(i).getName().equals(name)) {
                if (i == this.activeSceneIndex || !onlyOnActive) {
                    this.setActive(this.scenes.get(i), true);
                }
            }
        }
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

    public void setOption(String name, boolean value) {
        this.options.put(name, value);
    }

    public boolean getOption(String name) {
        return this.options.get(name);
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
