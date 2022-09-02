package EventManager.classes;

import EventManager.events.EventBoolean;
import EventManager.events.EventString;
import utils.EventManager.Interfaces.IListenableMethod;

public class SomeClass1 {

    @IListenableMethod
    public void func1(EventBoolean e) {
        System.out.println(this.getClass().getName() + " func1 " + e.bVal);
        e.setCancelled(true);
    }

    @IListenableMethod
    public void func2(EventString e) {
        System.out.println(this.getClass().getName() + " func2 " + e.sVal);
        e.setCancelled(true);
    }
}
