package othello.components.board;

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

public class BoardMoves extends BorderPane {
    private Board2D board;
    private VBox content;

    private Text currentRoundText;

    private boolean inStaticMode;

    private int selectedMove;

    public BoardMoves(Board2D boardContext) {
        super();
        this.board = boardContext;

        var scrollPane = new ScrollPane();
        this.content = new VBox();

        this.content.setPadding(new javafx.geometry.Insets(10, 0, 10, 0));
        this.content.setSpacing(1);
        this.content.setStyle("-fx-background-color: #F9F9F9;");

        scrollPane.setContent(this.content);
        this.setCenter(scrollPane);

        var buttonBox = new HBox();

        buttonBox.setSpacing(10);
        buttonBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);

        Button forwardButton = new FancyButton(">", Color.BLACK);
        Button backButton = new FancyButton("<", Color.BLACK);

        forwardButton.setOnAction(e -> {
            // Find last move if any
            int newMove = this.selectedMove + 1;

            if (newMove >= this.board.getMoves().size()) {
                newMove = this.board.getMoves().size() - 1;
                newMove = Math.max(newMove, 0);
            }
            if (this.board.inSetup) newMove = -1;
            this.setSelectedMove(newMove, true);
        });

        backButton.setOnAction(e -> {
            // Find last move if any
            int newMove = this.selectedMove - 1;

            if (newMove < -1) {
                newMove = -1;
            }

            this.setSelectedMove(newMove, true);
        });

        // Square field with number text
        this.currentRoundText = new Text("In setup");

        currentRoundText.setStyle("-fx-font-size: 20px;");
        currentRoundText.setFill(Paint.valueOf("#000000"));
        currentRoundText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

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

        this.currentRoundText.setText(this.selectedMove > -1 ? "Round " + (this.selectedMove + 1) : "Starting setup");

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
    }

    public void setSelectedMove(int moveIndex, boolean doFireEvent) {
        if (moveIndex < -1) {
            this.inStaticMode = false;
            this.selectedMove = this.board.getMoves().size() - 1;
            this.selectedMove = this.selectedMove > -1 ? this.selectedMove : 0;
        } else {
            this.inStaticMode = moveIndex != this.board.getMoves().size() - 1;

            if (this.inStaticMode && this.selectedMove == moveIndex) {
                // Revert main board to moveIndex
                this.board.revert(moveIndex);
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
        this.inStaticMode = true;
        this.setSelectedMove(-1, true);
    }
}
