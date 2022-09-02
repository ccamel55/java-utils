package EventManager;

import utils.EventManager.EventManager;
import EventManager.classes.SomeClass1;
import EventManager.classes.SomeClass2;
import EventManager.events.EventBoolean;
import EventManager.events.EventString;

public class Main {

    public static void main(String[] args) {

        final var class1 = new SomeClass1();
        final var class2 = new SomeClass2();

        EventManager.register(class1);
        EventManager.register(class2);

        // only methods from class1 should call as we cancel in them
        EventManager.call(new EventBoolean(true));
        EventManager.call(new EventString("What the dog doing!"));

        System.out.println("");

        EventManager.unregister(class1);

        // only methods from class 2 should call as we unregistered class 1
        EventManager.call(new EventBoolean(false));
        EventManager.call(new EventString("Poo Poo Face"));

        System.out.println("");

        EventManager.unregister(class1);
        EventManager.unregister(class2);

        EventManager.register(class1, EventString.class);
        EventManager.register(class2);

        // class 1 should call string, class 2 should call bools
        EventManager.call(new EventBoolean(true));
        EventManager.call(new EventString("Ok we good!"));
    }
}
