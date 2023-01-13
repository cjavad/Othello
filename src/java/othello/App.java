package othello;

import javafx.application.Application;
import javafx.stage.Stage;
import othello.faskinen.Window;
import othello.ui.SceneManager;
import othello.ui.StartMenu;

public class App extends Application {
    public static void main(String[] args) {
        launch(args); 
    }

    @Override
    public void start(Stage stage) throws Exception {
		Window.initialize();

        stage.setTitle("Othello");
        SceneManager sceneManager = new SceneManager(stage, 800, 600);
        StartMenu startMenu = new StartMenu(sceneManager);
        sceneManager.setActive(startMenu);
    }
}
