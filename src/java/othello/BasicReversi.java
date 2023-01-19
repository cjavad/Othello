package othello;

import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.components.board.BoardScene2D;
import othello.components.board.BoardScene3D;
import othello.events.MoveEvent;

/**
 * Required preset class for assignment
 */

public class BasicReversi extends SceneProvider {
    public BasicReversi(SceneManager manager) {
        super(manager, "BasicReversi");
        var sceneProvider = new BoardScene2D(manager, this.getSceneManager().getNewBoard());

        sceneProvider.getRoot().addEventHandler(MoveEvent.UPDATE, event -> {
            sceneProvider.handleUpdate(event.space);
        });

        this.setScene(sceneProvider.getScene());
    }
}
