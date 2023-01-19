package othello.components.board;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.ui.GoBackOnceButton;
import othello.components.ui.PauseMenu;
import othello.events.SettingsEvent;

public class BoardTopbar extends GridPane {
    public BoardTopbar(SceneManager manager) {
        super();
        this.setAlignment(Pos.TOP_LEFT);
        Button goBackButton = new GoBackOnceButton(manager);
        Button menuButton = new Button("Menu");
        Button rtxButton = new Button("RTX");

        rtxButton.setOnAction(event -> {
            manager.setOption("RTX", !manager.getOption("RTX"));
            this.fireEvent(new SettingsEvent(SettingsEvent.UPDATE));
        });

        menuButton.setOnAction(event -> new PauseMenu(manager).setActive());
        // this.add(goBackButton, 0, 0);
        this.add(menuButton, 0, 0);
        this.add(rtxButton, 1, 0);
    }
}
