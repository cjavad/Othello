package othello.events;

import javafx.event.Event;
import javafx.event.EventType;
import othello.game.Move;
import othello.game.Space;

public class MoveEvent extends Event {
    public static final EventType<MoveEvent> MOVE = new EventType<>(Event.ANY, "MOVE");
    public static final EventType<MoveEvent> UPDATE = new EventType<>(Event.ANY, "UPDATE");
    public static final EventType<MoveEvent> HOVER = new EventType<>(Event.ANY, "HOVER");

    public static final EventType<MoveEvent> SELECT = new EventType<>(Event.ANY, "SELECT");
    
    public Space space;
    public int moveIndex;
    public MoveEvent(EventType<MoveEvent> eventType, Space space) {
        super(eventType);
        this.space = space;
    }

    public MoveEvent(EventType<MoveEvent> eventType, int moveIndex) {
        super(eventType);
        this.moveIndex = moveIndex;
    }
}
