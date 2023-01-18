package othello.events;

import javafx.event.Event;
import javafx.event.EventType;

public class SettingsEvent extends Event {
    public static final EventType<SettingsEvent> UPDATE = new EventType<>(Event.ANY, "UPDATE_EVENT");

    public SettingsEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
