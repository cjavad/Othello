package othello.game.board.advanced;

import javafx.scene.input.MouseEvent;
import othello.game.state.Board2D;
import othello.game.state.Space;
import othello.ui.BoardViewer3D;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

public class BoardScene extends SceneProvider {
	Board2D board;
	public BoardViewer3D viewer;

	public BoardScene(SceneManager manager, Board2D board) {
		super(manager, "BoardScene");

		this.board = board;
		this.viewer = new BoardViewer3D(manager, board);
		this.viewer.getScene().setOnMouseClicked(this::handleMousePressed);
		this.setScene(this.viewer.getScene());
	}

	public void handleMousePressed(MouseEvent event) {
		int id = this.viewer.getPixelId(event);
		if (id == -1) return;

		int x = id % this.board.getColumns();
		int y = id / this.board.getColumns();

		Space space = new Space(x, y);
		this.board.move(space);
	}
}
