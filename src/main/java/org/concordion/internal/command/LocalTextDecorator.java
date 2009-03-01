package org.concordion.internal.command;

import org.concordion.api.Command;
import org.concordion.api.Evaluator;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.CommandCall;

public class LocalTextDecorator extends AbstractCommandDecorator {

    private static final String TEXT_VARIABLE = "#TEXT";

    public LocalTextDecorator(Command command) {
        super(command);
    }

    @Override
    protected void process(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder, Runnable runnable) {
        Object savedValue = evaluator.getVariable(TEXT_VARIABLE);
        try {
            evaluator.setVariable(TEXT_VARIABLE, commandCall.getElement().getText());
            runnable.run();
        } finally {
            evaluator.setVariable(TEXT_VARIABLE, savedValue);
        }        
    }
}
