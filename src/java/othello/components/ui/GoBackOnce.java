package othello.components.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import othello.components.SceneManager;
import othello.components.SceneProvider;

public class GoBackOnce extends SceneProvider {
    public GoBackOnce(SceneManager manager) {
        super(manager, "GoBackOnce");
        Button button = new Button("Go back");
        button.setOnAction(e -> {
            this.getSceneManager().goBack();
        });
        this.setScene(new Scene(button));
    }
}
