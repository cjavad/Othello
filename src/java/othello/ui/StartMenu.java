package othello.ui;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import othello.AdvancedReversi;
import othello.BasicReversi;

// Start Menu UI

public class StartMenu extends SceneProvider {
    public StartMenu(SceneManager manager) {
        super(manager);
        // Create two buttons, one for basic and one for advanced
        // When clicked, create a new game with the appropriate game type

        Button basicButton = new Button("Basic");
        basicButton.setOnAction(this::handleBasicClick);

        Button advancedButton = new Button("Advanced");
        advancedButton.setOnAction(this::handleAdvancedClick);

        Button faskineButton = new Button("Faskinen");
        faskineButton.setOnAction(this::handleFaskineClick);

        // Create a grid pane to hold the buttons
        GridPane grid = new GridPane();
        grid.add(basicButton, 0, 0);
        grid.add(advancedButton, 1, 1);
        grid.add(faskineButton, 2, 2);

        // Create a scene and add the grid pane to it and center it
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        Scene scene = new Scene(root, 300, 250);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        this.setScene(scene);
    }

    private void handleBasicClick(ActionEvent event) {
        // Create a new game with the basic game type
        this.getSceneManager().setActive(new BasicReversi(this.getSceneManager()));
    }

    private void handleAdvancedClick(ActionEvent event) {
        // Create a new game with the advanced game type
        this.getSceneManager().setActive(new AdvancedReversi(this.getSceneManager()));
    }

    private void handleFaskineClick(ActionEvent event) {
        // Create a new game with the advanced game type

    }

    private void handleKey(KeyEvent event) {
        // Handle key presses

    }
}
