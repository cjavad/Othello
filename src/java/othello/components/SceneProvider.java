package othello.components;

// Basic class that exports a single Scene

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;

import java.util.HashMap;

public class SceneProvider {
    private String name;
    private Scene scene;
    private SceneManager sceneManager;
    private HashMap<String, Object> nodes;
    boolean isRegistered = false;

	// music
	static AudioClip music = new AudioClip("file:src/resources/audio/Orchard.wav");

    public SceneProvider(SceneManager manager, String name) {
		if (!music.isPlaying()) {
			music.play();
			music.setCycleCount(AudioClip.INDEFINITE);
		}

        this.name = name;
        this.sceneManager = manager;
        this.nodes = new HashMap<>();
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        this.register();
    }

    public void register() {
        if (!this.isRegistered) {
            this.sceneManager.registerScene(this);
            this.isRegistered = true;
        }
    }

    public <T> T createNode(String name, T node) {
        // Store node and type
        this.nodes.put(name, node);
        return (T) this.getNode(name);
    }

    public <T> T getNode(String name) {
        // Get node and cast to type
        return (T) this.nodes.getOrDefault(name, null);
    }

    public boolean hasNode(String name) {
        return this.nodes.containsKey(name);
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setActive() {
        this.sceneManager.setActive(this);
        this.isRegistered = true;
    }

    public void onActive() {
        // Override this method to run code when the scene is activated
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
