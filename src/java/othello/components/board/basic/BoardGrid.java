package othello.components.board.basic;

import javafx.scene.layout.GridPane;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Space;

public class BoardGrid extends GridPane {
    private Board2D board;
    public BoardGrid(Board2D boardContext, int cellSize) {
        super();
        this.board = boardContext;
        this.setAlignment(javafx.geometry.Pos.CENTER);

        for (Space s : this.board) {
            var spacePane = new SpacePane(this.board, s);
            spacePane.setPrefSize(cellSize, cellSize);
            spacePane.update();
            this.add(spacePane, s.x, s.y);
        }

        this.addEventHandler(MoveEvent.MOVE, event -> {
            this.board.move(event.space);
            this.update();
            this.fireEvent(new MoveEvent(MoveEvent.UPDATE, event.space));
        });
    }
    public void update() {
        this.getChildren().forEach(node -> {
            ((SpacePane) node).update();
        });
    }
}
