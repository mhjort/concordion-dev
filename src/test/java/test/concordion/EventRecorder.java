package test.concordion;

import java.util.ArrayList;
import java.util.List;

import org.concordion.internal.command.AssertEqualsFailureEvent;
import org.concordion.internal.command.AssertEqualsListener;
import org.concordion.internal.command.AssertEqualsSuccessEvent;
import org.concordion.internal.command.ThrowableCaughtEvent;
import org.concordion.internal.command.ThrowableCaughtListener;

public class EventRecorder implements AssertEqualsListener, ThrowableCaughtListener {

    private List<Object> events = new ArrayList<Object>();

    public void failureReported(AssertEqualsFailureEvent event) {
        events.add(event);
    }

    public void successReported(AssertEqualsSuccessEvent event) {
        events.add(event);
    }

    public void throwableCaught(ThrowableCaughtEvent event) {
        events.add(event);
    }

    public Object getLast(Class<?> eventClass) {
        Object lastMatch = null;
        for (Object object : events) {
            if (eventClass.isAssignableFrom(object.getClass())) {
                lastMatch = object;
            }
        }
        return lastMatch;
    }


}
