package othello.game.state.interfaces;

import othello.game.state.Space;

public interface Line {
    public Space at(int i);
    public Space getStart();
    public Space getEnd();
    public void setStart(Space start);
    public void setEnd(Space end);
    public boolean contains(Space space);

    public int length();
}
