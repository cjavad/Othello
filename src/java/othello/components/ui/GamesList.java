package othello.components.ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
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
        title.setStyle("-fx-font-size: 16px;");
        title.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        this.content = new VBox();
        this.content.setSpacing(5);
        this.content.setAlignment(Pos.CENTER_RIGHT);

        var scrollPane = new ScrollPane();
        scrollPane.setContent(this.content);
        // Remove border
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        scrollPane.setPadding(new javafx.geometry.Insets(20, 0, 0, 0));

        this.update();

        this.setTop(title);
        this.setCenter(scrollPane);
    }

    public void update() {
        this.content.getChildren().clear();
        for (String path : ResourceLoader.listOfSavedGames()) {
            var btn = new FancyButton(path, Color.BLANCHEDALMOND);

            btn.setOnAction(e -> {
                Board2D board = ResourceLoader.readGameObject(path);
                this.manager.resetScene(new GameScene(this.manager, board), false);
            });

            this.content.getChildren().add(btn);
        }
    }
}
