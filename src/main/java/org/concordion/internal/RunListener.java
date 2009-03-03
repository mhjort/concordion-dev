package org.concordion.internal;

import org.concordion.internal.command.RunFailureEvent;
import org.concordion.internal.command.RunIgnoreEvent;
import org.concordion.internal.command.RunSuccessEvent;
import org.concordion.internal.command.ThrowableCaughtListener;

public interface RunListener extends ThrowableCaughtListener{

	void successReported(RunSuccessEvent runSuccessEvent);

	void failureReported(RunFailureEvent runFailureEvent);

    void ignoredReported(RunIgnoreEvent runIgnoreEvent);

}
