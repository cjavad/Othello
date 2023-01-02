package othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Othello");
        Scene scene1 = new TestJavaFX().createScene();
        stage.setScene(scene1);
        stage.show();
    }
}
