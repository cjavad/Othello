package othello.components.ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.board.GameScene;
import othello.game.Board2D;
import othello.utils.ResourceLoader;

public class GamesList extends BorderPane {
    private VBox content;
    private SceneManager manager;
    public GamesList(SceneManager manager) {
        super();
        this.manager = manager;

        var title = new Text("Saved games");
        title.setStyle("-fx-font-size: 20px;");
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        this.content = new VBox();
        this.content.setSpacing(10);
        this.content.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        this.content.setAlignment(javafx.geometry.Pos.CENTER);

        this.update();

        this.setTop(title);
        this.setCenter(this.content);
    }

    public void update() {
        this.content.getChildren().clear();
        for (String path : ResourceLoader.listOfSavedGames()) {
            var btn = new Button(path);

            btn.setOnAction(e -> {
                Board2D board = ResourceLoader.readGameObject(path);
                this.manager.resetScene(new GameScene(this.manager, board), false);

            });

            this.content.getChildren().add(btn);
        }
    }
}
