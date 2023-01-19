package othello.utils;

import othello.game.Board2D;
import othello.game.Player;

import java.util.HashMap;

public class SettingsManager {
    public static final String VERSION = "1.0.0";
    public static final String TITLE = "Othello                                 ";

    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;
    public static final int PLAYER_COUNT = 2;
    public static final int MAX_PLACEMENTS = 2;

    private HashMap<String, Boolean> options;
    private HashMap<String, Integer> gameOptions;
    private Player[] players;

    public SettingsManager() {
        this.options = new HashMap<>();

        this.gameOptions = new HashMap<>();
        this.setGameOption("columns", SettingsManager.BOARD_WIDTH);
        this.setGameOption("rows", SettingsManager.BOARD_HEIGHT);
        this.setGameOption("maxPlacements", SettingsManager.MAX_PLACEMENTS);
        this.setGameOption("playerCount", SettingsManager.PLAYER_COUNT);
        this.setGameOption("manual", 0);
        this.setGameOption("setup", 1);
        this.setGameOption("volume", 50);

        // Try to read saved config
        HashMap<String, Integer> savedConfig = ResourceLoader.readSavedConfig();

        if (savedConfig != null) {
            this.gameOptions = savedConfig;
        } else {
            // If no saved config, create a new one
            ResourceLoader.saveConfig(this.gameOptions);
        }

        this.players = new Player[this.getGameOption("playerCount")];

        for (int i = 0; i < this.getGameOption("playerCount"); i++) {
            this.players[i] = new Player(i);
            this.players[i].maxPlacements = this.getGameOption("maxPlacements");
        }
    }

    public void saveSettings() {
        ResourceLoader.saveConfig(this.gameOptions);
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