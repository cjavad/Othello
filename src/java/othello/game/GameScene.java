package othello.game;

import javafx.event.ActionEvent;

// Manages game Pane and events

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import othello.game.state.Board2D;
import othello.ui.GoBackOnce;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class GameScene extends SceneProvider {
	public boolean rtxOn = false;
	public BorderPane root;
	public Board2D board;

    public GameScene(SceneManager manager, Board2D board) {
        super(manager, "GameScene");

		this.board = board;

		GridPane grid = new GridPane();
		grid.add(new GoBackOnce(manager).getRoot(), 0, 0);
		Button rtxButton = new Button("RTX");
		rtxButton.setOnAction(this::toggleRTX);
		grid.add(rtxButton, 1, 0);

        Scene boardScene = new othello.game.board.basic.BoardScene(manager, this.board).getScene();
		this.root = new BorderPane(); 
		this.root.setCenter(boardScene.getRoot());
		this.root.setTop(grid);
		boardScene.setRoot(this.root);
		this.setScene(boardScene);
	}

	public void toggleRTX(ActionEvent event) {
		this.rtxOn = !this.rtxOn;

		if (this.rtxOn) {
			Scene boardScene = new othello.game.board.advanced.BoardScene(
				this.getSceneManager(),
				this.board
			).getScene();
			
			this.root.setCenter(boardScene.getRoot());
		} else {
			Scene boardScene = new othello.game.board.basic.BoardScene(
				this.getSceneManager(),
				this.board
			).getScene();
			
			this.root.setCenter(boardScene.getRoot());
		}
	}
}
