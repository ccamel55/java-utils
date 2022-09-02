package utils.EventManager.Core;

import java.lang.reflect.Method;

// every method that can be invoked by the listener will be converted into this class
public class EventListenerMethod {

    public Object oSuperType;
    public Method mMethod;
    public byte bPriority;

    public EventListenerMethod(Object superType, Method method, byte priority) {
        oSuperType = superType;
        mMethod = method;
        bPriority = priority;
    }
}
