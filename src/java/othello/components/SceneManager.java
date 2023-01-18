package othello.components;

import javafx.scene.Scene;
import javafx.stage.Stage;
import othello.game.Board2D;
import othello.utils.SettingsManager;

import java.util.ArrayList;

public class SceneManager {
    private int width;
    private int height;

    private Stage stage;

    private SettingsManager settings;
    private ArrayList<SceneProvider> scenes;
    private int activeSceneIndex;

    public SceneManager(Stage stage, int width, int height) {
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.scenes = new ArrayList<>();
        this.settings = new SettingsManager();

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

    public SceneProvider getScene(String name) {
        for (SceneProvider scene : this.scenes) {
            if (scene.getName().equals(name)) {
                this.setActiveScene(scene.getScene());
                return scene;
            }
        }

        return null;
    }

    public Board2D getNewBoard() {
        return this.settings.getBoardState();
    }

    public void registerScene(SceneProvider scene) {
        if (!this.scenes.contains(scene)) {
            this.scenes.add(scene);
        }
    }

    public <T> T getNode(String name) {
        for (SceneProvider scene : this.scenes) {
            if (scene.hasNode(name)) {
                return (T) scene.getNode(name);
            }
        }

        return null;
    }

    public boolean hasNode(String name) {
        for (SceneProvider scene : this.scenes) {
            if (scene.hasNode(name)) {
                return true;
            }
        }

        return false;
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

    public void resetScene(SceneProvider provider, boolean onlyOnActive) {
        for (int i = 0; i < this.scenes.size(); i++) {
            if (this.scenes.get(i).getName().equals(provider.getName())) {
                if (i == this.activeSceneIndex || !onlyOnActive) {
                    this.setActive(provider, true);
                }
            }
        }
    }

    public void goBack() {
        this.activeSceneIndex--;
        if (this.activeSceneIndex < 0) {
            this.activeSceneIndex = 0;
        }
        this.setActiveScene(this.getActiveScene());
    }

    public void goForward() {
        if (this.activeSceneIndex < this.scenes.size() - 1) {
            this.activeSceneIndex++;
            this.setActiveScene(this.getActiveScene());
        }
    }

    public void setOption(String name, boolean value) {
        this.settings.setOption(name, value);
    }

    public boolean getOption(String name) {
        return this.settings.getOption(name);
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

    public SceneProvider getActiveProvider() { return this.scenes.get(this.activeSceneIndex); }
}
