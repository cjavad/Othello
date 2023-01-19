package othello.components.board;

import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.basic.BoardScene2D;
import othello.components.board.advanced.BoardScene3D;
import othello.events.MoveEvent;
import othello.events.SettingsEvent;
import othello.game.Board2D;

public class GameScene extends SceneProvider {
    private BoardScene2D basicBoard;
    private BoardScene3D advancedBoard;

    private Board2D board;

    BorderPane root;
    private BoardTopbar topbar;
    private BoardButtons buttons;

    private BoardMovesOld moveList;


    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");
        this.board = board;

        this.root = new BorderPane();

        this.topbar = new BoardTopbar(manager);
        this.buttons = new BoardButtons(this.board);
        this.moveList = new BoardMovesOld(manager, this.board);

        this.topbar.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);
        this.root.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);
        this.buttons.addEventHandler(MoveEvent.UPDATE, this::handleBoardButton);

        this.root.setTop(this.topbar);
        this.root.setRight(this.moveList.getRoot());
        this.root.setBottom(this.buttons);

        this.basicBoard = new BoardScene2D(manager, this.board);
        this.root.setCenter(this.basicBoard.getRoot());

        this.setScene(new Scene(this.root, manager.getWidth(), manager.getHeight()));
    }

    @Override
    public void onActive() {
        this.handleSettingsUpdate(null);
    }

    public void createAdvancedBoard() {
        this.advancedBoard = new BoardScene3D(this.getSceneManager(), this.board);
        Pane imageRoot = this.advancedBoard.getNode("root");
        this.root.setCenter(imageRoot);
    }

    public void handleBoardButton(Event event) {
        this.buttons.update();
        this.moveList.update();
        this.basicBoard.handleUpdate(null);
    }

    public void handleSettingsUpdate(Event event) {
        this.handleBoardButton(event);

        if (this.getSceneManager().getOption("RTX")) {
            this.createAdvancedBoard();
        } else {
            this.root.setCenter(this.basicBoard.getRoot());
        }
    }
}
