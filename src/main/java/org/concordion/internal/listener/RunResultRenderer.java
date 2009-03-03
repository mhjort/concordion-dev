package org.concordion.internal.listener;

import org.concordion.internal.RunListener;
import org.concordion.internal.command.RunFailureEvent;
import org.concordion.internal.command.RunIgnoreEvent;
import org.concordion.internal.command.RunSuccessEvent;

public class RunResultRenderer extends ThrowableRenderer implements RunListener {

    public void successReported(RunSuccessEvent event) {
        event.getElement().addStyleClass("success").appendNonBreakingSpaceIfBlank();
    }

    public void failureReported(RunFailureEvent event) {
        event.getElement().addStyleClass("failure").appendNonBreakingSpaceIfBlank();
    }

    public void ignoredReported(RunIgnoreEvent event) {
        event.getElement().addStyleClass("ignored").appendNonBreakingSpaceIfBlank();
    }

}
