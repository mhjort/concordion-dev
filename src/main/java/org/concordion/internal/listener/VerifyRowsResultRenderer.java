package org.concordion.internal.listener;

import org.concordion.api.Element;
import org.concordion.internal.command.MissingRowEvent;
import org.concordion.internal.command.SurplusRowEvent;
import org.concordion.internal.command.VerifyRowsListener;

public class VerifyRowsResultRenderer implements VerifyRowsListener {

    public void missingRow(MissingRowEvent event) {
        Element element = event.getRowElement();
        element.addStyleClass("missing");
    }

    public void surplusRow(SurplusRowEvent event) {
        Element element = event.getRowElement();
        element.addStyleClass("surplus");
    }
}
