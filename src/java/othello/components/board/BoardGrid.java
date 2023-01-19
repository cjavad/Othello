package othello.components.board;

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
    private Board2D staticBoard;
    private Space hoverSpace;

    public BoardGrid(Board2D boardContext, int cellSize) {
        super();
        this.board = boardContext;
        this.setAlignment(javafx.geometry.Pos.CENTER);

        for (Space s : this.board) {
            var spacePane = new SpacePane(this.board, s);
            spacePane.setPrefSize(cellSize, cellSize);
            spacePane.update(false, false);
            this.add(spacePane, s.column, s.row);
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

    public void setCellSize(int cellSize) {
        this.getChildren().forEach(node -> {
            var spacePane = (SpacePane) node;
            spacePane.setPrefSize(cellSize, cellSize);
        });
    }

    public void setStaticBoard(Board2D staticBoard) {
        this.staticBoard = staticBoard;

        this.getChildren().forEach(node -> {
            var spacePane = (SpacePane) node;
            if (this.staticBoard != null) {
                spacePane.setBoard(this.staticBoard);
            } else {
                spacePane.setBoard(this.board);
            }
        });
    }

    public void update() {
        final int cellOccupant;

        ArrayList<Space> hoveredSpaces = new ArrayList<>();

        if (this.hoverSpace != null) {
            cellOccupant = this.board.getSpace(this.hoverSpace);

            if (cellOccupant == -1) {
                // Inject move for hoverSpace
                Board2D copyOfBoard = this.board.copy(true, true);
                copyOfBoard.move(this.hoverSpace);

                for (Iterator<Space> it = copyOfBoard.validMoves(cellOccupant); it.hasNext(); ) {
                    Space s = it.next();
                    if (s == null) continue;
                    hoveredSpaces.add(s);
                }
            }
        }

        this.getChildren().forEach(node -> {
            SpacePane spacePane = (SpacePane) node;
            // Check if spacePane space is in hoveredSpaces
            Space ownSpace = spacePane.getSpace();
            spacePane.update(ownSpace.equals(this.hoverSpace), hoveredSpaces.contains(ownSpace) && this.board.getCurrentPlayerId() != this.board.getSpace(ownSpace));
        });
    }
}
