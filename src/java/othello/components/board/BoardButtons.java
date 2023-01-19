package othello.components.board;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import othello.components.ui.FancyButton;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Space;

public class BoardButtons extends GridPane {
    private final Board2D board;

    private Text currentPlayerText;
    private Text currentPlayerScore;

    private Button endSetupButton;

    public BoardButtons(Board2D boardContext) {
        super();
        this.board = boardContext;
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.currentPlayerText = new Text();
        this.currentPlayerText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        this.currentPlayerScore = new Text();
        this.currentPlayerScore.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        this.update();

        // Create button to switch player
        Button switchPlayerButton = new FancyButton("Switch player", Color.BLANCHEDALMOND);

        switchPlayerButton.setOnAction(event -> {
            this.board.nextPlayer();
            this.update();
            this.fireEvent(new MoveEvent(MoveEvent.UPDATE, (Space) null));
        });

        this.endSetupButton = new FancyButton("End setup", Color.BLANCHEDALMOND);

        this.endSetupButton.setOnAction(event -> {
            this.board.endSetup();
            this.update();
            // Remove this button
            this.getChildren().remove(this.endSetupButton);
            this.fireEvent(new MoveEvent(MoveEvent.UPDATE, (Space) null));
        });

        this.setAlignment(javafx.geometry.Pos.TOP_CENTER);
        this.add(this.currentPlayerText, 0, 0);
        this.add(this.currentPlayerScore, 0, 1);
        this.add(switchPlayerButton, 0, 2);
        this.add(endSetupButton, 0, 3);
        // Add spacing
        this.setVgap(10);
    }

    public void update() {
        this.currentPlayerText.setText("Current player: " + this.board.getCurrentPlayerId());
        this.currentPlayerScore.setText("Current player score: " + this.board.getScore(this.board.getCurrentPlayerId()));

        if (!this.board.inSetup) {
            this.getChildren().remove(this.endSetupButton);
        }
    }
}
