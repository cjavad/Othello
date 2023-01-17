package othello.game.interfaces;

import othello.game.Space;

import java.util.Iterator;

public interface Line extends Iterable<Space> {
    public Space at(int i);
    public Iterator<Space> iterator();
    public Space getStart();
    public Space getEnd();
    public void setStart(Space start);
    public void setEnd(Space end);
    public boolean contains(Space space);
    public int length();
}
