package othello.components.board.basic;

import javafx.event.Event;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Move;
import othello.game.Space;

import java.util.concurrent.atomic.AtomicReference;

public class SpacePane extends Pane {
    private final Board2D board;
    private final Space space;
    public SpacePane(Board2D boardContext, Space space) {
        super();
        this.board = boardContext;
        this.space = space;
        this.setOnMouseClicked(this::handleClick);
    }

    public int isOccupiedBy() {
        return this.board.getSpace(this.space);
    }

    public void update() {
        int cellOccupant = this.isOccupiedBy();
        AtomicReference<Ellipse> pieceRef = new AtomicReference<>();

        String borderColour = this.board.isValidMove(space, this.board.getCurrentPlayerId()) == 0 ? "gold" : "green";
        String cellStyle = "-fx-border-width: 1; -fx-border-color: " + borderColour + "; -fx-background-color: darkgreen;";
        this.setStyle(cellStyle);
        this.getChildren().forEach(child -> {
            if (child instanceof Ellipse ellipse) {
                pieceRef.set((ellipse));
            }
        });
        
        if (pieceRef.get() == null) {
            pieceRef.set(new Ellipse(this.getWidth(), this.getHeight()));
        }

        Ellipse piece = pieceRef.get();
        piece.setFill(cellOccupant > -1 ? Paint.valueOf(this.board.getPlayer(cellOccupant).getColor()) : javafx.scene.paint.Color.TRANSPARENT);

        if (cellOccupant > -1) {
            //Black outline
            piece.setStroke((javafx.scene.paint.Color.BLACK));
            piece.setStrokeWidth(1);

            //Center the piece
            piece.centerXProperty().bind(this.widthProperty().divide(2));
            piece.centerYProperty().bind(this.heightProperty().divide(2));

            //Make piece fit cell
            piece.radiusXProperty().bind(this.widthProperty().divide(2).subtract(2));
            piece.radiusYProperty().bind(this.heightProperty().divide(2).subtract(2));

            //Set piece on top of cell
            piece.toFront();
        }

        //Add piece to cell if it's not already there
        if (!this.getChildren().contains(piece)) {
            this.getChildren().add(piece);
        }
    }

    public void handleClick(Event e) {
        this.fireEvent(new MoveEvent(MoveEvent.MOVE, this.space));
    }
}
