package othello.game;

import javafx.event.ActionEvent;

// Manages game Pane and events

import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import othello.game.state.Board2D;
import othello.ui.GoBackOnce;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class GameScene extends SceneProvider {
	public boolean rtxOn = false;
	public BorderPane basicPane;
	public BorderPane advancedPane;
	public Board2D board;

	public othello.game.board.basic.BoardScene basicBoard;
	public othello.game.board.advanced.BoardScene advancedBoard;

    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");

		this.board = board;

		GridPane grid = new GridPane();
		grid.add(new GoBackOnce(manager).getRoot(), 0, 0);
		Button rtxButton = new Button("RTX");
		rtxButton.setOnAction(this::toggleRTX);
		grid.add(rtxButton, 1, 0);

        this.basicBoard = new othello.game.board.basic.BoardScene(manager, this.board);	

		this.basicPane = new BorderPane(); 
		this.basicPane.setCenter(basicBoard.getRoot());
		this.basicPane.setTop(grid);
		this.basicBoard.getScene().setRoot(this.basicPane);

		grid = new GridPane();
		grid.add(new GoBackOnce(manager).getRoot(), 0, 0);
		rtxButton = new Button("RTX");
		rtxButton.setOnAction(this::toggleRTX);
		grid.add(rtxButton, 1, 0);

		this.advancedBoard = new othello.game.board.advanced.BoardScene(manager, this.board);

		this.advancedPane = new BorderPane();
		this.advancedPane.setCenter(advancedBoard.getRoot());
		this.advancedPane.setTop(grid);
		this.advancedBoard.getScene().setRoot(this.advancedPane);

		this.setScene(this.basicBoard.getScene());
	}

	public void toggleRTX(ActionEvent event) {
		this.rtxOn = !this.rtxOn;

		if (this.rtxOn) {
			this.getSceneManager().setActiveScene(this.advancedBoard.getScene());
		} else {
			this.getSceneManager().setActiveScene(this.basicBoard.getScene());
		}
	}
}
