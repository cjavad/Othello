package othello.game.state;

public class TestState {
    public static void main(String[] args) {
        int[] startingBoard8x8 = {
                -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1,  0, -1, -1, -1,
                -1, -1, -1, -1,  0, -1, -1, -1,
                -1, -1, -1, -1,  0, -1, -1, -1,
                -1, -1, -1, -1,  0, -1, -1, -1,
                -1, -1, -1, -1,  0, -1, -1, -1,
                -1, -1, -1, -1,  0, -1, -1, -1,
        };

        Player[] players = {
            new Player(0),
            new Player(1),
        };

        Board2D board = new Board2D(players, startingBoard8x8, true);

        System.out.println(board);

        Line[] lines = board.findLines(new Space(4, 1), 1);

        for (Line line : lines) {
            System.out.println(line);
        }

        System.out.println();
    }
}
