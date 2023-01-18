package othello.components.board;

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.basic.BoardScene2D;
import othello.components.board.advanced.BoardScene3D;
import othello.events.SettingsEvent;
import othello.game.Board2D;

public class GameScene extends SceneProvider {
    private BoardScene2D basicBoard;
    private BoardScene3D advancedBoard;

    private Board2D board;

    BorderPane root;
    private BoardTopbar topbar;
    private BoardButtons buttons;

    private BoardMoves moveList;


    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");
        this.board = board;

        this.root = new BorderPane();

        this.topbar = new BoardTopbar(manager);
        this.buttons = new BoardButtons(this.board);
        this.moveList = new BoardMoves(manager, this.board);

        this.topbar.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);
        this.root.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);

        this.root.setTop(this.topbar);
        this.root.setRight(this.moveList.getRoot());
        this.root.setBottom(this.buttons);

        this.basicBoard = new BoardScene2D(manager, this.board);

        if (manager.getOption("RTX")) {
            this.advancedBoard = new BoardScene3D(manager, this.board);
            root.setCenter(this.advancedBoard.getRoot());
        } else {
            root.setCenter(this.basicBoard.getRoot());
        }

        this.setScene(new Scene(root, manager.getWidth(), manager.getHeight()));
    }

    public void createAdvancedBoard() {
        this.advancedBoard = new BoardScene3D(this.getSceneManager(), this.board);
        this.root.setCenter(this.advancedBoard.getRoot());
    }

    public void handleSettingsUpdate(Event event) {
        this.buttons.update();
        this.moveList.update();
        this.basicBoard.handleUpdate(null);

        if (this.getSceneManager().getOption("RTX")) {
            this.createAdvancedBoard();
        } else {
            this.root.setCenter(this.basicBoard.getRoot());
        }
    }
}
