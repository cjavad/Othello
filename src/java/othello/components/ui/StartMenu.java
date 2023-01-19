package othello.components.ui;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import othello.AdvancedReversi;
import othello.BasicReversi;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.basic.BoardScene2D;
import othello.game.Board2D;
import othello.game.Player;
import othello.utils.SettingsManager;

// Start Menu UI

public class StartMenu extends SceneProvider {
    private GamesList gamesList;
    public StartMenu(SceneManager manager) {
        super(manager, "StartMenu");
        // Create two buttons, one for basic and one for advanced
        // When clicked, create a new game with the appropriate game type
        Text title = new Text(SettingsManager.TITLE);
        title.setStyle("-fx-font-size: 40px;");
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button basicButton = new FancyButton("Play Game", Color.CRIMSON);
        basicButton.setOnAction(this::handleGameClick);

        Button advancedButton = new FancyButton("Pre-load Advanced Game", Color.CRIMSON);
        advancedButton.setOnAction(this::handleAdvancedClick);

        Button settingsButton = new FancyButton("Settings", Color.CRIMSON);
        settingsButton.setOnAction(this::handleSettingsClick);

        this.gamesList = new GamesList(this.getSceneManager());

        // Create a grid pane to hold the buttons
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(title, 0, 0);
        grid.add(basicButton, 0, 1);
        grid.add(advancedButton, 0, 2);
        grid.add(settingsButton, 0, 3);
        grid.setAlignment(javafx.geometry.Pos.CENTER);
        grid.setHgap(10);

        // Create a scene and add the grid pane to it and center it
        GridPane root = new GridPane();
        root.add(this.gamesList, 1, 0);
        root.setHgap(50);
        root.add(grid, 0, 0);
        root.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(root, manager.getWidth(), manager.getHeight());
        //scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);
        this.setScene(scene);
    }

    private void handleGameClick(ActionEvent event) {
        // Create a new game with the basic game
        new BasicReversi(this.getSceneManager()).setActive();
    }

    private void handleAdvancedClick(ActionEvent event) {
        // Create a new game with the advanced game type
        new AdvancedReversi(this.getSceneManager()).setActive();
    }

    private void handleSettingsClick(ActionEvent event) {
        new SettingsMenu(this.getSceneManager()).setActive();
    }

    private void handleKey(KeyEvent event) {
        // Handle key presses

    }

    @Override
    public void onActive() {
        super.onActive();
        this.gamesList.update();
    }
}
