package tests.EventManager.events;

import utils.EventManager.Interfaces.IEvent;

public class EventString implements IEvent {

    public String sVal = "";
    private boolean cancelled = false;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }

    public EventString(String val) {
        sVal = val;
    }
}