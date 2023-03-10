package othello.game;

public class Player implements othello.game.interfaces.Player {
    private int playerId;
    private String hexColor;

    public int maxPlacements;

    public Player(int playerId) {
        this.playerId = playerId;
        this.hexColor = playerId == 0 ? "#101010" : (playerId == 1 ? "#FFFFFF" : String.format("#%06X", (int) (Math.random() * 0xFFFFFF)));
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public String getColor() {
        return this.hexColor;
    }

    @Override
    public Player clone() {
        Player clone = new Player(this.playerId);
        clone.hexColor = this.hexColor;
        return clone;
    }
}
