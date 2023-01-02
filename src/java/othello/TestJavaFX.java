package othello;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;

public class TestJavaFX {
    private int counter = 0;
    private Button button;

    public TestJavaFX() {
        this.button = new Button("Im a counter! Click ME!!!");
        this.button.setOnAction(this::handleClick);
    }

    public Scene createScene() {
        StackPane root = new StackPane();
        root.getChildren().add(this.button);
        Scene scene = new Scene(root, 300, 250);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        return scene;
    }

    private void handleClick(ActionEvent event) {
        this.counter++;
        this.button.setMinWidth(this.button.getWidth());
        this.button.setText("" + this.counter);
    }

    private void handleKey(KeyEvent event) {
        this.counter += KeyCode.UP == event.getCode() ? 1 : KeyCode.DOWN == event.getCode() ? -1 : 0;
        this.button.setText("" + this.counter);
    }
}
