package othello;

import othello.game.GameScene;
import othello.game.state.Board2D;
import othello.game.state.Player;
import othello.ui.SceneManager;
import othello.ui.SceneProvider;

/**
 * Required preset class for assignment
 */

public class BasicReversi extends SceneProvider {
    public BasicReversi(SceneManager manager) {
        super(manager, "BasicReversi");

        int playerCount = 2;
        Player[] players = new Player[playerCount];

        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player(i);
        }

        // Create simple scene
        Board2D board = new Board2D(players, true);
        GameScene gameUI = new GameScene(manager, board);
        // Set scene
        this.setScene(gameUI.getScene());
    }
}
