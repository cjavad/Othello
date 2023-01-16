package othello.ui;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import othello.AdvancedReversi;
import othello.game.state.Board2D;
import othello.game.state.Player;
import othello.game.state.interfaces.Board3D;

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
        Button NewGameButton = new Button("New Game");
        Button RTXButton = new Button("RTX Mode");


        resumeButton.setOnAction(event -> {
            Board2D board = new Board2D(
                    new Player[] { new Player(0), new Player(1) },
                    false
            );
            new BoardViewer2D(this.getSceneManager(), board).setActive();
        });
        homeButton.setOnAction(event -> {
            new StartMenu(this.getSceneManager()).setActive();
        });
        RTXButton.setOnAction(event ->{
        this.getSceneManager().setOption("RTX", !this.getSceneManager().getOption("RTX"));
                if (this.getSceneManager().getOption("RTX")==true) {
                    Board2D board = new Board2D(
                            new Player[] { new Player(0), new Player(1) },
                            false
                    );
                    new BoardViewer3D(this.getSceneManager(), board).setActive();
                } else {
                    Board2D board = new Board2D(
                            new Player[] { new Player(0), new Player(1) },
                            false
                    );
                    new BoardViewer2D(this.getSceneManager(), board).setActive();
                }
        });
        NewGameButton.setOnAction(event -> {
            this.getSceneManager().resetScene("BoardViewer2D", false);
        });
        pane.add(resumeButton, 0, 0);
        pane.add(homeButton, 0, 1);
        pane.add(RTXButton, 0, 2);
        pane.add(NewGameButton, 0, 3);
        return pane;
    }

}
