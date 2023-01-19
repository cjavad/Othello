package othello.components.board;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import othello.components.ui.FancyButton;
import othello.events.MoveEvent;
import othello.game.Board2D;
import othello.game.Space;

public class BoardButtons extends GridPane {
    private final Board2D board;
    private VBox playerScoreBox;

    private int winnerId = -1;

    private Button endSetupButton;

    public BoardButtons(Board2D boardContext) {
        super();
        this.board = boardContext;
        this.setAlignment(javafx.geometry.Pos.CENTER);
        this.setHgap(10);

        this.playerScoreBox = new VBox();
        this.playerScoreBox.setSpacing(10);
        this.playerScoreBox.setAlignment(javafx.geometry.Pos.CENTER);
        this.playerScoreBox.setPadding(new javafx.geometry.Insets(10, 0, 0, 0));


        // Create button to switch player
        Button switchPlayerButton = new FancyButton("Switch player", Color.BLANCHEDALMOND);

        switchPlayerButton.setOnAction(event -> {
            if (this.winnerId != -1) return;
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
        this.add(switchPlayerButton, 0, 2);
        this.add(endSetupButton, 0, 3);
        this.add(this.playerScoreBox, 0, 0);
        // Add spacing
        this.setVgap(10);

        this.update();
    }

    public void update() {
        if (this.winnerId != -1) this.setWinner(this.winnerId);

        this.playerScoreBox.getChildren().clear();

        for (int i = 0; i < this.board.getPlayerCount(); i++) {
            var player = this.board.getPlayer(i);
            var playerScore = new Text("Player " + player.getPlayerId() + ": " + this.board.getScore(player.getPlayerId()) + " points");
            var playerColor = Color.valueOf(player.getColor());

            // Create small circle to represent player with their color
            var playerCircle = new javafx.scene.shape.Circle(10, playerColor);
            playerCircle.setStroke(Color.BLACK);

            var playerBox = new HBox();
            playerBox.setSpacing(10);
            playerBox.setAlignment(javafx.geometry.Pos.CENTER);
            playerBox.getChildren().addAll(playerCircle, playerScore);
            // Set background color
            playerBox.setStyle("-fx-background-color: #F9F9F9;");

            // Highlight current player with Yellow rounded border
            if (this.board.getCurrentPlayerId() == player.getPlayerId()) {
                playerBox.setStyle("-fx-background-color: #F9F9F9; -fx-border-color: gold; -fx-border-width: 1; -fx-border-radius: 5; -fx-border-style: solid;");
            }

            this.playerScoreBox.getChildren().add(playerBox);
        }

        if (!this.board.inSetup) {
            this.getChildren().remove(this.endSetupButton);
        }
    }

    public void setWinner(int winnerId) {
        this.winnerId = winnerId;
        var winnerText = new Text("Player " + winnerId + " wins!");
        winnerText.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");
        this.playerScoreBox.getChildren().add(winnerText);
    }
}
