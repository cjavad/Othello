package othello;

import javafx.application.Application;
import javafx.stage.Stage;
import othello.faskinen.Window;
import othello.ui.SceneManager;
import othello.ui.StartMenu;

public class App extends Application {

    public static boolean faskine = false;
    public static void main(String[] args) {
        launch(args);
        if (faskine) {
            Window.create("Window", 1280, 720);
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Othello");
        SceneManager sceneManager = new SceneManager(stage);
        StartMenu startMenu = new StartMenu(sceneManager);
        sceneManager.setActiveScene(startMenu.getScene());
    }
}
