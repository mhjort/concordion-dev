package org.concordion.internal.command;

import java.util.EventListener;

public interface AssertEqualsListener extends EventListener {

    void successReported(AssertEqualsSuccessEvent event);
    
    void failureReported(AssertEqualsFailureEvent event);
}
