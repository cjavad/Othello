package othello.components.ui;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
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
        BorderPane root = new BorderPane();
        StackPane pane = new StackPane();
        var title = new Text("Settings");
        title.setStyle("-fx-font-size: 40px;");
        title.setTextAlignment(TextAlignment.CENTER);
        pane.getChildren().add(title);
        pane.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        pane.setAlignment(Pos.CENTER);
        root.setTop(pane);

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
        vbox.setSpacing(10);

        // Create fields with text as labels
        Label volumeLabel = new Label("Volume");
        Label rowsLabel = new Label("Rows");
        Label columnsLabel = new Label("Columns");
        Label maxPlacementsLabel = new Label("Max Placements");
        Label playerCountLabel = new Label("Player Count");
        Label manualLabel = new Label("Manual");
        Label setupLabel = new Label("Setup");

        // Create slider for volume
        Slider volumeSlider = new Slider(0, 100, manager.settings.getGameOption("volume"));
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setShowTickMarks(true);
        volumeSlider.setMajorTickUnit(10);
        volumeSlider.setMinorTickCount(5);
        volumeSlider.setBlockIncrement(10);

        Spinner rowsField = new Spinner(4, 64 * 4, manager.settings.getGameOption("rows"));
        Spinner columnsField = new Spinner(4, 64 * 4, manager.settings.getGameOption("columns"));
        Spinner maxPlacementsField = new Spinner(2, 64, manager.settings.getGameOption("maxPlacements"));
        Spinner playerCountField = new Spinner(2, 4, manager.settings.getGameOption("playerCount"));

        // Create a checkbox for manual
        CheckBox manualField = new CheckBox();
        manualField.setSelected(manager.settings.getGameOption("manual") == 1);

        // Create a checkbox for setup
        CheckBox setupField = new CheckBox();
        setupField.setSelected(manager.settings.getGameOption("setup") == 1);

        // Create a button to save the settings
        Button saveButton = new FancyButton("Save", Color.BLACK);
        saveButton.setOnAction(this::handleSaveClick);

        this.createNode("volume", volumeSlider);
        this.createNode("rows", rowsField);
        this.createNode("columns", columnsField);
        this.createNode("maxPlacements", maxPlacementsField);
        this.createNode("playerCount", playerCountField);
        this.createNode("manual", manualField);
        this.createNode("setup", setupField);

        // Add all fields to the vbox
        vbox.getChildren().addAll(
                volumeLabel,
                volumeSlider,
                rowsLabel,
                rowsField,
                columnsLabel,
                columnsField,
                maxPlacementsLabel,
                maxPlacementsField,
                playerCountLabel,
                playerCountField,
                manualLabel,
                manualField,
                setupLabel,
                setupField,
                saveButton
        );

        GridPane grid = new GridPane();
        vbox.setAlignment(Pos.CENTER_LEFT);
        grid.add(vbox, 0, 0);
        grid.setAlignment(Pos.CENTER);
        root.setCenter(grid);

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
        Slider volumeSlider = (Slider) this.getNode("volume");
        Spinner rowsField = this.getNode("rows");
        Spinner columnsField = this.getNode("columns");
        Spinner maxPlacementsField = this.getNode("maxPlacements");
        Spinner playerCountField = this.getNode("playerCount");
        CheckBox manualField = this.getNode("manual");
        CheckBox setupField = this.getNode("setup");

        this.getSceneManager().settings.setGameOption("volume", (int) volumeSlider.getValue());
        this.getSceneManager().settings.setGameOption("rows", (int) rowsField.getValue());
        this.getSceneManager().settings.setGameOption("columns", (int) columnsField.getValue());
        this.getSceneManager().settings.setGameOption("maxPlacements", (int) maxPlacementsField.getValue());
        this.getSceneManager().settings.setGameOption("playerCount", (int) playerCountField.getValue());
        this.getSceneManager().settings.setGameOption("manual", manualField.isSelected() ? 1 : 0);
        this.getSceneManager().settings.setGameOption("setup", setupField.isSelected() ? 1 : 0);
        this.getSceneManager().settings.saveSettings();

        // Go back
        this.getSceneManager().goBack();
    }
}
