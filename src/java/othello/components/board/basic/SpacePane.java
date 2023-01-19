package othello.components.board.basic;

import javafx.event.Event;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Move;
import othello.game.Player;
import othello.game.Space;

import java.util.concurrent.atomic.AtomicReference;

public class SpacePane extends Pane {
    private Board2D board;
    private final Space space;

    private Ellipse piece;

    public SpacePane(Board2D boardContext, Space space) {
        super();
        this.board = boardContext;
        this.space = space;

        // Set size and bind size of board space to piece
        this.piece = new Ellipse(this.getWidth(), this.getHeight());
        this.piece.centerXProperty().bind(this.widthProperty().divide(2));
        this.piece.centerYProperty().bind(this.heightProperty().divide(2));

        //Make piece fit cell
        this.piece.radiusXProperty().bind(this.widthProperty().divide(2).subtract(2));
        this.piece.radiusYProperty().bind(this.heightProperty().divide(2).subtract(2));

        // Initially make transparent
        this.piece.setFill(javafx.scene.paint.Color.TRANSPARENT);
        this.getChildren().add(this.piece);
        this.setStyle("-fx-border-width: 1; -fx-border-color: green; -fx-background-color: darkgreen;");
        this.setOnMouseClicked(this::handleClick);
        this.setOnMouseEntered(this::handleMouseEnter);
        this.setOnMouseExited(this::handleMouseExit);
    }

    public void setBoard(Board2D board) {
        this.board = board;
    }

    public Space getSpace() {
        return this.space;
    }

    public void update(boolean isBeingHovered, boolean hoveredHighlight) {
        int cellOccupant = this.board.getSpace(this.space);

        boolean isValidMove = this.board.isValidMove(space, this.board.getCurrentPlayerId()).getKey() == 0;
        boolean isValidFlip = cellOccupant != -1 && isValidMove;

        piece.setStrokeWidth(0);

        // If the move is valid but not a flip make a white glassish color to the piece
        if (isValidFlip || isBeingHovered) {
            Color pieceColor = (Color) piece.getFill();
            piece.setFill(pieceColor.deriveColor(0, 1, 1, 0.5));
        }

        if (isValidMove) {
            // Yellow outline
            this.piece.setFill(javafx.scene.paint.Color.rgb(255, 255, 255, 0.5));
            piece.setStroke((javafx.scene.paint.Color.YELLOW));
            piece.setStrokeWidth(1);
        }

        if (cellOccupant > -1) {
            Color playerColor = Color.valueOf(this.board.getPlayer(cellOccupant).getColor());

            if (hoveredHighlight) {
                // Lower opacity of piece
                playerColor = playerColor.deriveColor(0, 1, 1, 0.5);
            }

            this.piece.setFill(playerColor);
        } else {
            this.piece.setFill(javafx.scene.paint.Color.TRANSPARENT);
        }

        //Set piece on top of cell
        piece.toFront();
    }

    public void handleClick(Event e) {
        if (this.board.isStatic) return;
        this.fireEvent(new MoveEvent(MoveEvent.MOVE, this.space));
    }

    public void handleMouseEnter(Event e) {
        this.fireEvent(new MoveEvent(MoveEvent.HOVER, this.space));
    }

    public void handleMouseExit(Event e) {
        this.fireEvent(new MoveEvent(MoveEvent.HOVER, (Space) null));
    }
}
