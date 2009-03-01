package org.concordion.internal.command;

import java.util.EventListener;

public interface ThrowableCaughtListener extends EventListener {

    void throwableCaught(ThrowableCaughtEvent event);
}
