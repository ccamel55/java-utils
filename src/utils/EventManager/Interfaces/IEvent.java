package utils.EventManager.Interfaces;

// every event that can be listened for will inherit this, by default all events will be cancelable
public interface IEvent {
    boolean isCancelled();
    void setCancelled(boolean b);
}
