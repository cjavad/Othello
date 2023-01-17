package othello.events;

import javafx.event.Event;
import javafx.event.EventType;
import othello.game.Space;

public class MoveEvent extends Event {
    public static final EventType<MoveEvent> MOVE = new EventType<>(Event.ANY, "MOVE");
    public static final EventType<MoveEvent> UPDATE = new EventType<>(Event.ANY, "UPDATE");
    
    public Space space;
    public MoveEvent(EventType<? extends Event> eventType, Space space) {
        super(eventType);
        this.space = space;
    }
}
