package othello.components.board;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.basic.BoardScene2D;
import othello.components.board.advanced.BoardScene3D;
import othello.components.ui.PauseMenu;
import othello.events.MoveEvent;
import othello.events.SettingsEvent;
import othello.game.Board2D;
import othello.game.Move;

import static javafx.scene.input.KeyCode.ESCAPE;

public class GameScene extends SceneProvider {
    private BoardScene2D basicBoard;
    private BoardScene3D advancedBoard;

    private Board2D board;

    private BorderPane root;
    private VBox sidebar;
    private BoardTopbar topbar;
    private BoardButtons buttons;

    private BoardMoves moveList;


    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");
        this.board = board;

        this.root = new BorderPane();
        this.sidebar = new VBox();

        this.topbar = new BoardTopbar(manager, this.board);
        this.buttons = new BoardButtons(this.board);
        this.moveList = new BoardMoves(this.board);

        this.moveList.setPrefWidth(this.getSceneManager().getWidth() / 4);
        this.moveList.setPrefHeight(this.getSceneManager().getHeight() / 2);

        this.topbar.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);
        this.root.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);
        this.buttons.addEventHandler(MoveEvent.UPDATE, this::handleBoardButton);
        this.moveList.addEventHandler(MoveEvent.SELECT, this::handleSelect);

        this.sidebar.getChildren().addAll(this.moveList, this.buttons);

        this.root.setTop(this.topbar);
        this.root.setRight(this.sidebar);

        this.basicBoard = new BoardScene2D(manager, this.board);
        this.basicBoard.getRoot().addEventHandler(MoveEvent.MOVE, this::handleMove);

        this.root.setCenter(this.basicBoard.getRoot());

        Scene s = new Scene(this.root, manager.getWidth(), manager.getHeight());
        s.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKey);
        this.setScene(s);
    }

    @Override
    public void onActive() {
        this.handleSettingsUpdate(null);
    }

    public void createAdvancedBoard() {
        if (this.advancedBoard == null) this.advancedBoard = new BoardScene3D(this.getSceneManager(), this.board);
        this.root.setCenter(this.advancedBoard.getNode("root"));
    }

    public void handleBoardButton(Event event) {
        this.buttons.update();
        this.basicBoard.handleUpdate(null);
        this.moveList.update();
    }

    public void handleSettingsUpdate(Event event) {
        this.handleBoardButton(event);

        if (this.getSceneManager().getOption("RTX")) {
            this.createAdvancedBoard();
        } else {
            this.root.setCenter(this.basicBoard.getRoot());
        }
    }

    public void handleMove(MoveEvent event) {
        this.moveList.gotoCurrentMove();
        this.basicBoard.setStatic(-2);
        this.basicBoard.handleUpdate(null);
        this.moveList.update();
    }

    public void handleSelect(MoveEvent event) {
        Move latest = this.board.getLatestMove();

        if (latest == null || event.moveIndex != latest.getRound()) {
            // Create a static Board2D that displays the move
            this.basicBoard.setStatic(event.moveIndex);
        } else {
            // Create a static Board2D that displays the current board
            this.basicBoard.setStatic(-2);
        }

        this.basicBoard.handleUpdate(null);
        this.moveList.update();
    }

    public void handleKey(KeyEvent event) {
        // Open pause menu on escape
        switch (event.getCode()) {
            case ESCAPE:
                PauseMenu m = this.getSceneManager().getScene("PauseMenu");
                if (m == null) m = new PauseMenu(this.getSceneManager());
                m.setActive();
                break;
            default:
                break;
        }
    }
}
