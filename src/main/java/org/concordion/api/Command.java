package org.concordion.api;

import org.concordion.internal.CommandCall;

public interface Command {

    void setUp(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder);

    void execute(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder);

    void verify(CommandCall commandCall, Evaluator evaluator, ResultRecorder resultRecorder);
}
