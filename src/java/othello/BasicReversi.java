package othello;

import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.GameScene;
import othello.game.Board2D;
import othello.game.Player;

import java.util.HashMap;

/**
 * Required preset class for assignment
 */

public class BasicReversi extends SceneProvider {

    public GameScene gameUI;
    public BasicReversi(SceneManager manager) {
        super(manager, "BasicReversi");
        // Create simple scene
        Board2D board = this.getSceneManager().getNewBoard();
        this.gameUI = new GameScene(manager, board);
        // Set scene
        this.setScene(this.gameUI.getScene());
    }
}
