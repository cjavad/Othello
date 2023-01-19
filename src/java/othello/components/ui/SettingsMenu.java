package othello.components.ui;

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import othello.components.SceneManager;
import othello.components.SceneProvider;

import java.util.function.UnaryOperator;

public class SettingsMenu extends SceneProvider {
    public SettingsMenu(SceneManager manager) {
        super(manager, "SettingsMenu");

        // Settings Menu implements all fields from SettingsManager.java
        /**
         *         this.gameOptions.put("columns", SettingsManager.BOARD_WIDTH);
         *         this.gameOptions.put("rows", SettingsManager.BOARD_HEIGHT);
         *         this.gameOptions.put("maxPlacements", SettingsManager.MAX_PLACEMENTS);
         *         this.gameOptions.put("manual", 0);
         *         this.gameOptions.put("setup", 1);
         */

        // Create a scene and add the grid pane to it and center it
        GridPane pane = new GridPane();

        // Int validator
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };

        // Create options input fields
        // Create two integer inputs for rows and columns
        VBox vbox = new VBox();

        // Create fields with text as labels

        Label rowsLabel = new Label("Rows");
        Label columnsLabel = new Label("Columns");
        Label maxPlacementsLabel = new Label("Max Placements");
        Label manualLabel = new Label("Manual");
        Label setupLabel = new Label("Setup");

        Spinner rowsField = new Spinner(4, 64*4, manager.settings.getGameOption("rows"));
        Spinner columnsField = new Spinner(4, 64*4, manager.settings.getGameOption("columns"));
        Spinner maxPlacementsField = new Spinner(2, 64, manager.settings.getGameOption("maxPlacements"));

        // Create a checkbox for manual
        CheckBox manualField = new CheckBox();
        manualField.setSelected(manager.settings.getGameOption("manual") == 1);

        // Create a checkbox for setup
        CheckBox setupField = new CheckBox();
        setupField.setSelected(manager.settings.getGameOption("setup") == 1);

        // Create a button to save the settings
        Button saveButton = new FancyButton("Save", Color.BLACK);
        saveButton.setOnAction(this::handleSaveClick);

        this.createNode("rows", rowsField);
        this.createNode("columns", columnsField);
        this.createNode("maxPlacements", maxPlacementsField);
        this.createNode("manual", manualField);
        this.createNode("setup", setupField);

        // Add all fields to the vbox
        vbox.getChildren().addAll(rowsLabel, rowsField, columnsLabel, columnsField, maxPlacementsLabel, maxPlacementsField, manualLabel, manualField, setupLabel, setupField, saveButton);

        // Create input fields for each label
        // Dropdowns for booleans and text fields for integers (first 3)
        // Add all fields to the vbox
        // Add vbox to the root
        pane.getChildren().add(vbox);
        pane.setAlignment(javafx.geometry.Pos.CENTER);

        var root = new BorderPane();
        root.setCenter(pane);

        // Center pane in the BorderPane

        Scene scene = new Scene(root, manager.getWidth(), manager.getHeight());
        scene.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKey);

        this.setScene(scene);
    }

    private void handleKey(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE:
                this.getSceneManager().getScene("StartMenu").setActive();
                break;
            default:
                break;
        }
    }

    private void handleSaveClick(Event event) {
        Spinner rowsField = this.getNode("rows");
        Spinner columnsField = this.getNode("columns");
        Spinner maxPlacementsField = this.getNode("maxPlacements");
        CheckBox manualField = this.getNode("manual");
        CheckBox setupField = this.getNode("setup");

        this.getSceneManager().settings.setGameOption("rows", (int) rowsField.getValue());
        this.getSceneManager().settings.setGameOption("columns", (int) columnsField.getValue());
        this.getSceneManager().settings.setGameOption("maxPlacements", (int) maxPlacementsField.getValue());
        this.getSceneManager().settings.setGameOption("manual", manualField.isSelected() ? 1 : 0);
        this.getSceneManager().settings.setGameOption("setup", setupField.isSelected() ? 1 : 0);
        this.getSceneManager().settings.saveSettings();
    }
}
