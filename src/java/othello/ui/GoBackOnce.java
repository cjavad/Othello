package othello.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;

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
