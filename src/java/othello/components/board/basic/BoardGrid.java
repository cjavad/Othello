package othello.components.board.basic;

import javafx.scene.layout.GridPane;
import othello.events.MoveEvent;
import othello.events.SettingsEvent;
import othello.game.Board2D;
import othello.game.Move;
import othello.game.Space;

import java.util.ArrayList;
import java.util.Iterator;

public class BoardGrid extends GridPane {
    private Board2D board;
    private Space hoverSpace;

    public BoardGrid(Board2D boardContext, int cellSize) {
        super();
        this.board = boardContext;
        this.setAlignment(javafx.geometry.Pos.CENTER);

        for (Space s : this.board) {
            var spacePane = new SpacePane(this.board, s);
            spacePane.setPrefSize(cellSize, cellSize);
            spacePane.update(false, false);
            this.add(spacePane, s.x, s.y);
        }

        this.addEventHandler(MoveEvent.MOVE, event -> {
            Move move = this.board.move(event.space);

            this.update();
            this.fireEvent(new MoveEvent(MoveEvent.UPDATE, event.space));

            if (move == null) {
                this.fireEvent(new SettingsEvent(SettingsEvent.UPDATE));
            }
        });

        this.addEventHandler(MoveEvent.HOVER, event -> {
            this.hoverSpace = event.space;
            this.update();
        });
    }
    public void update() {
        final int cellOccupant;

        ArrayList<Space> hoveredSpaces = new ArrayList<>();

        if (this.hoverSpace != null) {
            cellOccupant = this.board.getSpace(this.hoverSpace);

            if (cellOccupant == -1) {
                // Inject move for hoverSpace
                Board2D copyOfBoard = this.board.copy(true);
                copyOfBoard.move(this.hoverSpace);

                for (Iterator<Space> it = copyOfBoard.validMoves(cellOccupant); it.hasNext(); ) {
                    Space s = it.next();
                    if (s == null) continue;
                    hoveredSpaces.add(s);
                }
            }
        } else {
            cellOccupant = -1;
        }

        this.getChildren().forEach(node -> {
            SpacePane spacePane = (SpacePane) node;

            // Check if spacePane space is in hoveredSpaces
            boolean flippable = false;
            Space ownSpace = spacePane.getSpace();

            for (Space s : hoveredSpaces)
                if (s.equals(ownSpace)) flippable = true;



            spacePane.update(ownSpace.equals(this.hoverSpace), flippable && this.board.getCurrentPlayerId() != this.board.getSpace(ownSpace));
        });
    }
}
