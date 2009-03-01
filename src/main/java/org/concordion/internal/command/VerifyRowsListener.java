package org.concordion.internal.command;

import java.util.EventListener;

public interface VerifyRowsListener extends EventListener {

    void missingRow(MissingRowEvent event);
    
    void surplusRow(SurplusRowEvent event);
}
