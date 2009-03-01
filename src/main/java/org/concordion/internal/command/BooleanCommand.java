package org.concordion.internal.command;

import org.concordion.api.Element;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.CommandCall;
import org.concordion.internal.CommandCallList;
import org.concordion.internal.InvalidExpressionException;
import org.concordion.internal.util.Announcer;

public abstract class BooleanCommand extends AbstractCommand {

    private Announcer<AssertEqualsListener> listeners = Announcer.to(AssertEqualsListener.class);
    
    public void addAssertEqualsListener(AssertEqualsListener listener) {
        listeners.addListener(listener);
    }

    public void removeAssertEqualsListener(AssertEqualsListener listener) {
        listeners.removeListener(listener);
    }
    
    @Override
    public void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder) {
//        Check.isFalse(commandCall.hasChildCommands(), "Nesting commands inside an 'assertTrue' is not supported");
        CommandCallList childCommands = commandCall.getChildren();
        childCommands.setUp(evaluator, resultRecorder);
        childCommands.execute(evaluator, resultRecorder);
        childCommands.verify(evaluator, resultRecorder);
        
        String expression = commandCall.getExpression();
        Object result = evaluator.evaluate(expression);
        if (result != null && result instanceof Boolean) {
            if ((Boolean) result) {
            	processTrueResult(commandCall, resultRecorder);
            } else {
            	processFalseResult(commandCall, resultRecorder);
            }
        } else {
            throw new InvalidExpressionException("Expression '" + expression + "' did not produce a boolean result (needed for assertTrue).");
        }
    }
    
    protected void announceSuccess(Element element) {
        listeners.announce().successReported(new AssertEqualsSuccessEvent(element));
    }

    protected void announceFailure(Element element, String expected, Object actual) {
        listeners.announce().failureReported(new AssertEqualsFailureEvent(element, expected, actual));
    }
    
    protected abstract void processTrueResult(CommandCall commandCall,ResultRecorder resultRecorder);
    protected abstract void processFalseResult(CommandCall commandCall, ResultRecorder resultsRecorder);
}
