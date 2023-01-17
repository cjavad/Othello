package othello.components.ui;

import javafx.scene.control.Button;
import othello.components.SceneManager;

public class GoBackOnceButton extends Button {
    public GoBackOnceButton(SceneManager manager) {
        super("Go back");

        this.setOnAction(e -> {
            manager.goBack();
        });
    }
}