package othello.components.board;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.ui.FancyButton;
import othello.components.ui.GoBackOnceButton;
import othello.components.ui.PauseMenu;
import othello.events.SettingsEvent;
import othello.game.Board2D;
import othello.utils.ResourceLoader;

public class BoardTopbar extends GridPane {
    public BoardTopbar(SceneManager manager, Board2D boardContext) {
        super();
        this.setAlignment(Pos.TOP_LEFT);
        Button menuButton = new FancyButton("Menu", Color.BLACK);
        Button rtxButton = new FancyButton("RTX", Color.BLACK);
        Button saveGameButton = new FancyButton("Save game", Color.BLACK);
        TextField saveGameName = new TextField();
        saveGameName.setPromptText("Game name");
        saveGameName.setPrefWidth(100);
        saveGameName.setPrefHeight(20);
        saveGameName.setAlignment(Pos.CENTER);
        saveGameName.setVisible(false);

        saveGameButton.setOnAction(e -> {
            saveGameName.setVisible(true);
        });

        saveGameName.setOnAction(e -> {
            ResourceLoader.saveGameObject(boardContext, saveGameName.getText());
            saveGameName.setVisible(false);
        });


        rtxButton.setOnAction(event -> {
            manager.setOption("RTX", !manager.getOption("RTX"));
            this.fireEvent(new SettingsEvent(SettingsEvent.UPDATE));
        });

        menuButton.setOnAction(event -> new PauseMenu(manager).setActive());
        // this.add(goBackButton, 0, 0);
        this.add(menuButton, 0, 0);
        this.add(rtxButton, 1, 0);
        this.add(saveGameButton, 2, 0);
        this.add(saveGameName, 3, 0);
    }

    public void createSaveTextInputPopup() {

    }
}
