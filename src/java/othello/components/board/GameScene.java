package othello.components.board;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.ui.PauseMenu;
import othello.events.MoveEvent;
import othello.events.SettingsEvent;
import othello.game.Board2D;
import othello.game.Move;
import othello.game.Player;

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
        this.moveList.addEventHandler(MoveEvent.UPDATE, this::handleUpdate);

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
        Pane root;
        if (this.advancedBoard == null) {
            this.advancedBoard = new BoardScene3D(this.getSceneManager(), this.board);
            root = this.advancedBoard.getNode("root");
            // Add event listeners for move and update + select
            root.addEventHandler(MoveEvent.MOVE, this::handleMove);
            root.addEventHandler(MoveEvent.UPDATE, this::handleUpdate);
            root.addEventHandler(MoveEvent.SELECT, this::handleSelect);
            // Settings update
            root.addEventHandler(SettingsEvent.UPDATE, this::handleSettingsUpdate);
        } else {
            root = this.advancedBoard.getNode("root");
            AnimationTimer timer = this.advancedBoard.getNode("timer");
            if (timer != null) timer.start();
        }

        this.root.setCenter(root);
    }

    public void handleBoardButton(Event event) {
        this.buttons.update();
        this.basicBoard.handleUpdate(null);
        this.moveList.update();
    }

    public Board2D createStatic(int moveIndex) {
        // -1 being the starting position
        if (moveIndex == -2) {
            return null;
        } else {
            // Show static board from move @
            Board2D b = this.board.copy(true, true);
            b.revert(moveIndex);
            return b;
        }
    }

    public void handleSettingsUpdate(Event event) {
        this.handleBoardButton(event);

        if (this.getSceneManager().getOption("3D")) {
            this.createAdvancedBoard();
        } else {
            // Stop rendering advanced board
            if (this.advancedBoard != null) {
                AnimationTimer t = this.advancedBoard.getNode("timer");
                if (t != null) {
                    t.stop();
                }
            }

            this.root.setCenter(this.basicBoard.getRoot());
        }
    }

    public void handleMove(MoveEvent event) {
        Board2D staticBoard = this.createStatic(-2);
        this.moveList.gotoCurrentMove();
        this.basicBoard.setStaticBoard(staticBoard);
        if (this.advancedBoard != null) this.advancedBoard.setStaticBoard(staticBoard);

        this.moveList.update();

        if (!this.board.inSetup && !this.board.isStatic && this.board.isGameOver()) {
            // Determine winner
            Player winner = this.board.getWinner();
            this.buttons.setWinner(winner.getPlayerId());
        } else {
            // Check if current player has any moves
            var iter = this.board.validMoves(this.board.getCurrentPlayerId());
            boolean hasMoves = false;

            while (iter.hasNext()) {
                if (iter.next() != null) {
                    hasMoves = true;
                    break;
                }
            }

            if (!hasMoves) {
                this.board.nextPlayer();
                this.handleBoardButton(null);
            }
        }
    }

    public void handleSelect(MoveEvent event) {
        Move latest = this.board.getLatestMove();
        Board2D staticBoard;

        if (latest == null || event.moveIndex != latest.getRound()) {
            // Create a static Board2D that displays the move
            staticBoard = this.createStatic(event.moveIndex);

        } else {
            // Create a static Board2D that displays the current board
            staticBoard = this.createStatic(-2);
        }

        this.basicBoard.setStaticBoard(staticBoard);
        if (this.advancedBoard != null) this.advancedBoard.setStaticBoard(staticBoard);
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

    public void handleUpdate(MoveEvent event) {
        Board2D staticBoard = this.createStatic(-2);
        this.basicBoard.setStaticBoard(staticBoard);
        if (this.advancedBoard != null) this.advancedBoard.setStaticBoard(staticBoard);
        this.buttons.update();
        this.basicBoard.handleUpdate(null);
        this.moveList.update();
    }
}
