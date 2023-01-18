package othello.utils;

import othello.game.Board2D;
import othello.game.Player;

import java.util.HashMap;

public class SettingsManager {
    public static final String VERSION = "1.0.0";
    public static final String TITLE = "Othello";

    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    public static final int PLAYER_COUNT = 2;
    public static final int MAX_PLACEMENTS = 64;

    private HashMap<String, Boolean> options;
    private HashMap<String, Integer> gameOptions;
    private Player[] players;

    public SettingsManager() {
        this.options = new HashMap<>();

        this.gameOptions = new HashMap<String, Integer>();
        this.gameOptions.put("columns", SettingsManager.BOARD_WIDTH);
        this.gameOptions.put("rows", SettingsManager.BOARD_HEIGHT);
        this.gameOptions.put("maxPlacements", SettingsManager.MAX_PLACEMENTS);
        this.gameOptions.put("manual", 0);
        this.gameOptions.put("setup", 1);

        this.players = new Player[SettingsManager.PLAYER_COUNT];
        for (int i = 0; i < SettingsManager.PLAYER_COUNT; i++) {
            this.players[i] = new Player(i);
            this.players[i].maxPlacements = SettingsManager.MAX_PLACEMENTS;
        }
    }

    public Board2D getBoardState() {
        return new Board2D(this.players, this.gameOptions);
    }

    public void setOption(String key, boolean value) {
        this.options.put(key, value);
    }

    public boolean getOption(String key) {
        return this.options.getOrDefault(key, false);
    }

    public void setGameOption(String key, int value) {
        this.gameOptions.put(key, value);
    }

    public int getGameOption(String key) {
        return this.gameOptions.get(key);
    }
}