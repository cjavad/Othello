package othello;

import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.GameScene;
import othello.game.Board2D;
import othello.game.Player;

/**
 * Required preset class for assignment
 */

public class BasicReversi extends SceneProvider {

    public GameScene gameUI;
    public BasicReversi(SceneManager manager) {
        super(manager, "BasicReversi");

        int[] emptyStartingBoard8x8 = {
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1, 1, -1, -1, -1, -1, -1, -1,
                -1,-1,  0, -1, -1, -1, -1, -1,
                -1,-1, -1,  1, -1, -1, -1, -1,
                -1,-1, -1, -1,  0, -1, -1, -1,
                -1,-1, -1, -1, -1,  1, -1, -1,
                -1,-1, -1, -1, -1, -1,  0, -1,
                -1,-1, -1, -1, -1, -1, -1,  -1,
        };

        // 1 0 1 0 1 0 1 Diag
        //  Diagonal bugs

        Player[] players = {
                new Player(0),
                new Player(1),
        };

        // Create simple scene
        Board2D board = new Board2D(players, false);
        this.gameUI = new GameScene(manager, board);
        // Set scene
        this.setScene(this.gameUI.getScene());
    }
}
