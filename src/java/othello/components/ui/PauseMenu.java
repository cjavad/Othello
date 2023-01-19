package othello.components.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.GameScene;
import othello.events.SettingsEvent;
import othello.game.Board2D;

public class PauseMenu extends SceneProvider {
    public GridPane centerPane;

    public PauseMenu(SceneManager manager) {
        super(manager, "PauseMenu");
        this.centerPane = this.createCenterPane();

        BorderPane pane = new BorderPane();
        var title = new Text("Pause Menu");
        title.setFill(Color.BLACK);
        title.setStyle("-fx-font-size: 30px;");

        var gridTitle = new GridPane();
        gridTitle.setPadding(new Insets(10, 10, 10, 10));
        gridTitle.setAlignment(Pos.CENTER);
        gridTitle.add(title, 0, 0);

        pane.setTop(gridTitle);
        pane.setCenter(this.centerPane);
        this.centerPane.setAlignment(Pos.CENTER);
        this.setScene(new Scene(pane, manager.getWidth(), manager.getHeight()));
        this.getScene().addEventHandler(KeyEvent.KEY_PRESSED, this::handleKey);
        this.getSceneManager().setOption("RTX", false);
    }
    public GridPane createCenterPane() {
        GridPane pane = new GridPane();
        Button resumeButton = new FancyButton("Resume", Color.BLACK);
        Button homeButton = new FancyButton("Home", Color.BLACK);
        Button newGameButton = new FancyButton("New Game", Color.BLACK);
        Button RTXButton = new FancyButton("RTX Mode", Color.BLACK);


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

        pane.setAlignment(Pos.CENTER);
        pane.setVgap(10);
        
        pane.add(resumeButton, 0, 0);
        pane.add(homeButton, 0, 1);
        pane.add(RTXButton, 0, 2);
        pane.add(newGameButton, 0, 3);

        return pane;
    }


    public void handleKey(KeyEvent event) {
        switch (event.getCode()) {
            case ESCAPE:
                this.getSceneManager().getScene("GameScene");
                break;
            default:
                break;
        }
    }

}
