package org.concordion.internal.command;

import org.concordion.api.Command;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.CommandCall;

public abstract class AbstractCommandDecorator implements Command {

    private final Command command;

    public AbstractCommandDecorator(Command command) {
        this.command = command;
    }

    public void setUp(final CommandCall commandCall, final Evaluator evaluator, final ResultRecorder resultRecorder) {
        process(commandCall, evaluator, resultRecorder, new Runnable() {
            public void run() {
                command.setUp(commandCall, evaluator, resultRecorder);
            }
        });
    }

    public void execute(final CommandCall commandCall, final Evaluator evaluator, final ResultRecorder resultRecorder) {
        process(commandCall, evaluator, resultRecorder, new Runnable() {
            public void run() {
                command.execute(commandCall, evaluator, resultRecorder);
            }
        });
    }
    
    public void verify(final CommandCall commandCall, final Evaluator evaluator, final ResultRecorder resultRecorder) {
        process(commandCall, evaluator, resultRecorder, new Runnable() {
            public void run() {
                command.verify(commandCall, evaluator, resultRecorder);
            }
        });
    }

    protected abstract void process(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder, Runnable runnable);
}
