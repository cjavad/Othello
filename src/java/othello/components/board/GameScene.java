package othello.components.board;

import javafx.event.ActionEvent;

// Manages game Pane and events

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import othello.components.board.advanced.BoardViewer3D;
import othello.components.board.basic.BoardScene2D;
import othello.components.ui.GoBackOnceButton;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.components.SceneManager;
import othello.components.SceneProvider;

public class GameScene extends SceneProvider {
	public boolean rtxOn = false;
	public BorderPane basicPane;
	public BorderPane advancedPane;
	public Board2D board;

	public othello.components.board.basic.BoardScene2D basicBoard;
	public BoardViewer3D advancedBoard;

	public SceneManager manager;

    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");

		this.board = board;

		GridPane grid = new GridPane();
		grid.add(new GoBackOnceButton(manager), 0, 0);
		Button rtxButton = new Button("RTX");
		rtxButton.setOnAction(this::toggleRTX);
		grid.add(rtxButton, 1, 0);

		MoveList moveList = new MoveList(manager, board);

        this.basicBoard = new BoardScene2D(manager, this.board);

		this.basicPane = new BorderPane(); 
		this.basicPane.setCenter(basicBoard.getRoot());
		this.basicPane.setRight(moveList.getRoot());
		this.basicPane.setTop(grid);
		this.basicBoard.getScene().setRoot(this.basicPane);

		// Listen for MoveEvents on the basic board
		this.basicBoard.getScene().addEventHandler(MoveEvent.UPDATE, event -> {
			moveList.update();
		});

		grid = new GridPane();
		grid.add(new GoBackOnceButton(manager), 0, 0);
		rtxButton = new Button("RTX");
		rtxButton.setOnAction(this::toggleRTX);
		grid.add(rtxButton, 1, 0);

		this.advancedPane = new BorderPane();
		this.advancedPane.setTop(grid);

		this.setScene(this.basicBoard.getScene());
	}

	public BoardViewer3D getAdvancedBoard() {
		if (this.advancedBoard == null) {
			this.advancedBoard = new BoardViewer3D(this.manager, this.board);
			this.advancedPane.setCenter(advancedBoard.getRoot());
			this.advancedBoard.getScene().setRoot(this.advancedPane);
		}
		return this.advancedBoard;
	}

	public void toggleRTX(ActionEvent event) {
		this.rtxOn = !this.rtxOn;

		if (this.rtxOn) {
			this.getSceneManager().setActiveScene(this.getAdvancedBoard().getScene());
		} else {
			this.getSceneManager().setActiveScene(this.basicBoard.getScene());
		}
	}
}
