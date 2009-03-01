package org.concordion.internal.command;

import org.concordion.api.Command;
import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.CommandCall;
import org.concordion.internal.util.Announcer;

public class ThrowableCatchingDecorator extends AbstractCommandDecorator {

    private Announcer<ThrowableCaughtListener> listeners = Announcer.to(ThrowableCaughtListener.class);
    
    public void addThrowableListener(ThrowableCaughtListener listener) {
        listeners.addListener(listener);
    }

    public void removeThrowableListener(ThrowableCaughtListener listener) {
        listeners.removeListener(listener);
    }
    
    public ThrowableCatchingDecorator(Command command) {
        super(command);
    }

    private void announceThrowableCaught(Element element, Throwable t, String expression) {
        listeners.announce().throwableCaught(new ThrowableCaughtEvent(t, element, expression));
    }

    @Override
    protected void process(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable t) {
            resultRecorder.record(Result.EXCEPTION);
            announceThrowableCaught(commandCall.getElement(), t, commandCall.getExpression());
        }        
    }
}
