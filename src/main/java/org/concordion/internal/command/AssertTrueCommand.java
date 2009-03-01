package org.concordion.internal.command;

import org.concordion.api.Result;
import org.concordion.api.ResultRecorder;
import org.concordion.internal.CommandCall;

public class AssertTrueCommand extends BooleanCommand {

	@Override
	protected void processFalseResult(CommandCall commandCall, ResultRecorder resultRecorder) {
		resultRecorder.record(Result.FAILURE);
		announceFailure(commandCall.getElement(), commandCall.getExpression(), "== false");
	}

	@Override
	protected void processTrueResult(CommandCall commandCall, ResultRecorder resultRecorder) {
		resultRecorder.record(Result.SUCCESS);
		announceSuccess(commandCall.getElement());
	}
}
