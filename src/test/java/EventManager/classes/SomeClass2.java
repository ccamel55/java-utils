package EventManager.classes;

import EventManager.events.EventBoolean;
import EventManager.events.EventString;
import utils.EventManager.Interfaces.IListenableMethod;

public class SomeClass2 {

    @IListenableMethod
    public void func1(EventBoolean e) {
        System.out.println(this.getClass().getName() + " func1 " + e.bVal);
    }

    @IListenableMethod
    public void func2(EventString e) {
        System.out.println(this.getClass().getName() + " func2 " + e.sVal);
    }

    @IListenableMethod
    public void func3(EventBoolean e) {
        System.out.println(this.getClass().getName() + " func3 " + e.bVal);
    }

    @IListenableMethod
    public void func4(EventString e) {
        System.out.println(this.getClass().getName() + " func4 " + e.sVal);
    }
}
