package othello;

import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.GameScene;
import othello.game.Board2D;

/**
 * Required preset class for assignment
 */

public class AdvancedReversi extends SceneProvider {

    public GameScene gameUI;
    public AdvancedReversi(SceneManager manager) {
        super(manager, "AdvancedReversi");
        // Create simple scene
        Board2D board = this.getSceneManager().getNewBoard();
        this.gameUI = new GameScene(manager, board);
        // Set scene
        this.setScene(this.gameUI.getScene());
    }

    @Override
    public void onActive() {
        this.gameUI.onActive();
    }
}
