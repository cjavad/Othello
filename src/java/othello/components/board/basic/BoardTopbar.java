package othello.components.board.basic;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.ui.PauseMenu;

public class BoardTopbar extends GridPane {
    public BoardTopbar(SceneManager manager) {
        super();
        this.setAlignment(javafx.geometry.Pos.CENTER);
        Button menuButton = new Button("Menu");
        menuButton.setOnAction(event -> new PauseMenu(manager).setActive());
        this.add(menuButton, 0, 0);
    }
}
