package othello.game.state;

public class TestState {
    public static void main(String[] args) {
        int[] startingBoard8x8 = {
                0, 1, 1, 1, 1, 1, 1, 1,
                0, 0, 1, 1, 0, 0, 1,-1,
                0, 0, 0, 1, 0, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 0, 1,
                -1,1, 1, 1, 0, 1, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 0,
                0, 1, 0, 1, 0, 1, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1,
        };

        int[] emptyStartingBoard8x8 = {
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1,1, 1, 1, 1, 0, -1, -1,
                -1,-1, -1, -1, -1, -1, -1, -1,
                -1,-1, -1, -1, -1, -1, -1, -1,
        };

        Player[] players = {
            new Player(0),
            new Player(1),
        };


        Board2D board = new Board2D(players, emptyStartingBoard8x8, true);
        System.out.println(board);


        Space space = new Space(5, 4);

        Line[] lines = board.findLines(space, 0);
        // System.out.println(board.isValidMove(space, 0));

        for (Line line : lines) {
            System.out.println(line);
            System.out.println(board.isValidLine(line, space, 0));
        }

        System.out.println(board.isValidMove(space, 0));


        System.out.println();
    }
}
