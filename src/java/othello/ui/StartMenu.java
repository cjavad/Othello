package othello.ui;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import othello.AdvancedReversi;
import othello.App;
import othello.BasicReversi;
import othello.faskinen.Faskinen;
import othello.faskinen.Window;

// Start Menu UI

public class StartMenu extends SceneProvider {
    public StartMenu(SceneManager manager) {
        super(manager, "StartMenu");
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
        grid.add(advancedButton, 0, 1);
        grid.add(faskineButton, 0, 2);
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);

        // Create a scene and add the grid pane to it and center it
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        Scene scene = new Scene(root, manager.getWidth(), manager.getHeight());
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        this.setScene(scene);
    }

    private void handleBasicClick(ActionEvent event) {
        // Create a new game with the basic game
        new BasicReversi(this.getSceneManager()).setActive();
    }

    private void handleAdvancedClick(ActionEvent event) {
        // Create a new game with the advanced game type
        new AdvancedReversi(this.getSceneManager()).setActive();
    }

    private void handleFaskineClick(ActionEvent event) {
        Faskinen faskinen = new Faskinen();	
    }

    private void handleKey(KeyEvent event) {
        // Handle key presses

    }
}
