package othello.components.board;

import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import othello.components.SceneManager;
import othello.components.SceneProvider;
import othello.game.Board2D;
import othello.game.Change;
import othello.game.Move;

import java.util.ArrayList;

public class MoveList extends SceneProvider  {
    int selectedMove = 0;
    private Board2D board;

    private VBox movesBox;
    private AnchorPane anchorPane;
    public MoveList(SceneManager manager, Board2D boardContext) {
        super(manager, "MoveList");

        BorderPane borderPane = new BorderPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);

        this.movesBox = new VBox();
        // Set top padding to 10
        this.movesBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        this.movesBox.setSpacing(1);
        // Set min height to 50
        this.movesBox.setPrefWidth(manager.getWidth() / 4.0);
        this.movesBox.setPrefHeight(manager.getHeight());
        this.movesBox.setStyle("-fx-background-color: #F9F9F9;");
        this.board = boardContext;
        scrollPane.setContent(this.movesBox);
        // USE_COMPUTED_SIZE

        scrollPane.setFitToWidth(true);
        borderPane.setCenter(scrollPane);

        // Create back and forward buttons
        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);
        Button backButton = new Button("Back");

        // Square field with number text
        Text numberText = new Text("0");
        numberText.setStyle("-fx-font-size: 20px;");
        numberText.setFill(Paint.valueOf("#000000"));
        numberText.setWrappingWidth(50);
        numberText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        numberText.setTranslateX(25);
        numberText.setTranslateY(25);

        Button forwardButton = new Button("Forward");
        buttonBox.getChildren().addAll(backButton, numberText, forwardButton);
        borderPane.setBottom(buttonBox);

        this.setScene(new Scene(borderPane, 50, 200));
    }

    public void update() {
        this.movesBox.getChildren().clear();
        int i = 0;

        // Get index of latest move
        this.selectedMove = this.board.getLatestMove() == null ? this.selectedMove : this.board.getLatestMove().getRound();

        for (Move move : this.board.getMoves()) {
            if (move == null) continue;
            this.movesBox.getChildren().add(this.moveElement(i++, move));
        }

        for (javafx.scene.Node node : ((HBox) this.getScene().getRoot().getChildrenUnmodifiable().get(1)).getChildren()) {
            if (node instanceof Text) {
                ((Text) node).setText(String.valueOf(this.selectedMove));
                break;
            }
        }
    }

    public FlowPane moveElement(int index, Move move) {
        FlowPane pane = new FlowPane();
        pane.setPrefWidth(this.movesBox.getPrefWidth());
        pane.setMinHeight(50);
        pane.setStyle("-fx-background-color: #FFFFFF;");
        pane.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));
        pane.setHgap(10);
        pane.setVgap(10);
        pane.setAlignment(Pos.CENTER_LEFT);

        String background = "#FFFFFF";

        if (index % 2 == 0) {
            background = "#F9F9F9";
        } else {
            background = "#F9F9F9";
        }

        pane.setStyle("-fx-background-color: " + background + ";");

        Text moveText = new Text( "Move " + index + ". " + move.getPlacementSpace().toString());
        // Alternate background colors

        moveText.setWrappingWidth(this.movesBox.getPrefWidth() - 20);

        pane.getChildren().add(moveText);

        // Create pane on hover
        pane.setOnMouseEntered(event -> {
            pane.setStyle("-fx-background-color: #EEEEEE;");
        });

        // Remove pane on hover exit
        String finalBackground = background;
        pane.setOnMouseExited(event -> {
            pane.setStyle("-fx-background-color: " + finalBackground + ";");
        });

        pane.setOnMouseClicked(event -> {
            // check if anchor already exists
            if (this.movesBox.getChildren().contains(this.anchorPane)) {
                this.movesBox.getChildren().remove(this.anchorPane);

                if (this.anchorPane.getId().equals("anchor" + index)) {
                    return;
                }
            }

            anchorPane = changesPopup(move);
            anchorPane.setId("anchor" + index);
            anchorPane.setLayoutX(pane.getLayoutX() + pane.getWidth());
            anchorPane.setLayoutY(pane.getLayoutY());
            // Add right after the pane
            this.movesBox.getChildren().add(this.movesBox.getChildren().indexOf(pane) + 1, anchorPane);
        });

        return pane;
    }

    public void handleSelectMove(Event event) {
        System.out.println("Select move");
    }


    public AnchorPane changesPopup(Move move) {
        AnchorPane anchorPane = new AnchorPane();

        anchorPane.setPrefWidth(this.movesBox.getPrefWidth());
        anchorPane.setPrefHeight(50);
        anchorPane.setStyle("-fx-background-color: #FFFFFF;");
        anchorPane.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));

        VBox changesBox = new VBox();
        changesBox.setPrefWidth(this.movesBox.getPrefWidth());
        changesBox.setPrefHeight(50);

        for (Change change : move.getChanges()) {
            Text changeText = new Text("Flipped: " + change.getPrevPlayerId() + " to " + move.getPlayerId() + " at " + "(" + change.getColumn() + ", " + change.getRow() + ")");
            changeText.setWrappingWidth(this.movesBox.getPrefWidth() - 20);
            changesBox.getChildren().add(changeText);
        }

        anchorPane.getChildren().add(changesBox);
        return anchorPane;
    }

}
