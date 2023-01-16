package othello.game.state;

public class TestState {
    public static void main(String[] args) {
        Line l = new Line(new Space(2, 4), new Space(3, 3), 8, 8);
        System.out.println(l.length());
    }
}
