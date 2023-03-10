package othello.components.board;

import javafx.beans.InvalidationListener;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import othello.components.ui.FancyButton;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Move;
import javafx.scene.paint.Color;

import java.util.function.Predicate;

public class BoardMoves extends BorderPane {
    private Board2D board;
    private VBox content;

    private Text currentRoundText;

    private FancyButton forwardButton;
    private FancyButton backButton;
    private ScrollPane scrollPane;

    private boolean inStaticMode;

    private int selectedMove;

    public BoardMoves(Board2D boardContext) {
        super();
        this.board = boardContext;

        this.scrollPane = new ScrollPane();
        // Scroll with mouse with new content
        this.scrollPane.setPannable(true);
        this.scrollPane.setFitToWidth(true);
        this.scrollPane.setFitToHeight(true);

        this.content = new VBox();

        this.content.setPadding(new javafx.geometry.Insets(10, 0, 10, 0));
        this.content.setSpacing(1);
        this.content.setStyle("-fx-background-color: #F9F9F9;");

        this.scrollPane.setContent(this.content);
        this.setCenter(this.scrollPane);

        var buttonBox = new HBox();

        buttonBox.setSpacing(10);
        buttonBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);

        this.forwardButton = new FancyButton(">", Color.BLACK);
        this.backButton = new FancyButton("<", Color.BLACK);

        this.forwardButton.setOnAction(e -> {
            // Find last move if any
            int newMove = this.selectedMove + 1;

            if (newMove >= this.board.getMoves().size()) {
                newMove = this.board.getMoves().size() - 1;
                newMove = Math.max(newMove, 0);
            }
            if (this.board.inSetup) newMove = -1;
            this.setSelectedMove(newMove, true);
        });


        this.backButton.setOnAction(e -> {
            // Find last move if any
            int newMove = this.selectedMove - 1;

            if (newMove < -1) {
                newMove = -1;
            }

            this.setSelectedMove(newMove, true);
        });

        // Square field with number text
        this.currentRoundText = new Text("In setup");

        this.currentRoundText.setStyle("-fx-font-size: 20px;");
        this.currentRoundText.setFill(Paint.valueOf("#000000"));
        this.currentRoundText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        buttonBox.getChildren().addAll(backButton, currentRoundText, forwardButton);
        // Set padding below buttons
        buttonBox.setPadding(new javafx.geometry.Insets(0, 0, 10, 0));
        this.setTop(buttonBox);

        // Set padding between top and center
        this.selectedMove = this.board.getMoves().size() - 1;
        this.update();
    }


    public void update() {
        this.content.getChildren().clear();

        if (!this.board.inSetup) {
            // Set starting moves
            var startingMoves = new FlowPane();
            startingMoves.setPrefWidth(75);

            startingMoves.setOnMouseClicked(e -> {
                this.gotoStartingPosition();
            });

            if (this.selectedMove == -1) {
                startingMoves.setStyle("-fx-background-color: #F9F9F9;");
            } else {
                startingMoves.setStyle("-fx-background-color: #FFFFFF;");
            }

            Text moveText = new Text("Starting setup");
            moveText.setStyle("-fx-font-size: 10px;");
            moveText.setFill(Paint.valueOf("#000000"));
            moveText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            startingMoves.getChildren().add(moveText);
            this.content.getChildren().add(startingMoves);
        }

        this.currentRoundText.setText(this.selectedMove > -1 ? "Round " + (this.selectedMove + 1) : "In setup");

        if (this.selectedMove < 0) {
            this.backButton.setVisible(false);
        } else {
            this.backButton.setVisible(true);
        }

        if (this.selectedMove == this.board.getMoves().size() - 1) {
            this.forwardButton.setVisible(false);
        } else {
            this.forwardButton.setVisible(true);
        }

        for (Move m : this.board.getMoves()) {
            var moveElement = new FlowPane();

            moveElement.setOnMouseClicked(e -> this.setSelectedMove(m.getRound(), true));

            if (m.getRound() == this.selectedMove) {
                moveElement.setStyle("-fx-background-color: #FF0000;");
            } else {
                moveElement.setStyle("-fx-background-color: #FFFFFF;");
            }

            Text moveText = new Text(m.toString());
            moveText.setStyle("-fx-font-size: 10px;");
            moveText.setFill(Paint.valueOf("#000000"));
            moveText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

            moveElement.getChildren().add(moveText);
            this.content.getChildren().add(moveElement);
        }

        // Scroll to bottom of scroll pane
        if (this.scrollPane != null) {
            this.scrollPane.setVvalue(1.0);
        }
    }

    public void setSelectedMove(int moveIndex, boolean doFireEvent) {
        if (moveIndex == -2) {
            this.inStaticMode = false;
            this.selectedMove = this.board.getMoves().size() - 1;
            this.selectedMove = this.selectedMove > -1 ? this.selectedMove : 0;
        } else {
            this.inStaticMode = moveIndex != this.board.getMoves().size() - 1;

            if (this.inStaticMode && this.selectedMove == moveIndex) {
                // Revert main board to moveIndex
                this.board.revert(moveIndex);
                this.board.isStatic = false;
                this.fireEvent(new MoveEvent(MoveEvent.UPDATE, null));
            }

            this.selectedMove = moveIndex;
        }

        if (doFireEvent) this.fireEvent(new MoveEvent(MoveEvent.SELECT, moveIndex));
    }

    public void gotoCurrentMove() {
        this.inStaticMode = false;
        this.setSelectedMove(this.board.getMoves().size() - 1, false);
    }

    public void gotoStartingPosition() {
        this.inStaticMode = false;
        this.setSelectedMove(-1, true);
    }
}
