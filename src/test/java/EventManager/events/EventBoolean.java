package EventManager.events;

import utils.EventManager.Interfaces.IEvent;

public class EventBoolean implements IEvent {

    public boolean bVal = false;
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public EventBoolean(boolean val) {
        bVal = val;
    }
}