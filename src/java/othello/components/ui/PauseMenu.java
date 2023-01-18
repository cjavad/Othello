package othello.components.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.GameScene;
import othello.components.board.advanced.BoardScene3D;
import othello.components.board.basic.BoardScene2D;
import othello.events.SettingsEvent;
import othello.game.Board2D;

public class PauseMenu extends SceneProvider {
    public GridPane centerPane;

    public PauseMenu(SceneManager manager) {
        super(manager, "PauseMenu");
        this.centerPane = this.createCenterPane();

        BorderPane pane = new BorderPane();

        pane.setCenter(this.centerPane);
        this.centerPane.setAlignment(Pos.CENTER);
        this.setScene(new Scene(pane, manager.getWidth(), manager.getHeight()));

        this.getSceneManager().setOption("RTX", false);

    }
    public GridPane createCenterPane() {
        GridPane pane = new GridPane();
        Button resumeButton = new Button("Resume");
        Button homeButton = new Button("Home");
        Button newGameButton = new Button("New Game");
        Button RTXButton = new Button("RTX Mode");


        resumeButton.setOnAction(event -> {
            this.getSceneManager().getScene("GameScene");
        });

        homeButton.setOnAction(event -> {
            new StartMenu(this.getSceneManager()).setActive();
        });

        RTXButton.setOnAction(event ->{
            this.getSceneManager().setOption("RTX", !this.getSceneManager().getOption("RTX"));
            this.getSceneManager().getScene("GameScene").getRoot().fireEvent(new SettingsEvent(SettingsEvent.UPDATE));
        });

        newGameButton.setOnAction(event -> {
            // Create simple scene
            Board2D board = this.getSceneManager().getNewBoard();
            this.getSceneManager().resetScene(new GameScene(this.getSceneManager(), board), false);
        });
        
        pane.add(resumeButton, 0, 0);
        pane.add(homeButton, 0, 1);
        pane.add(RTXButton, 0, 2);
        pane.add(newGameButton, 0, 3);
        return pane;
    }

}
