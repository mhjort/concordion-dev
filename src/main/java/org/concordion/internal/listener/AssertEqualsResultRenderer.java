package org.concordion.internal.listener;

import org.concordion.api.Element;
import org.concordion.internal.command.AssertEqualsFailureEvent;
import org.concordion.internal.command.AssertEqualsListener;
import org.concordion.internal.command.AssertEqualsSuccessEvent;

public class AssertEqualsResultRenderer implements AssertEqualsListener {

    public void failureReported(AssertEqualsFailureEvent event) {
        Element element = event.getElement();
        element.addStyleClass("failure");
        
        Element spanExpected = new Element("del");
        spanExpected.addStyleClass("expected");
        element.moveChildrenTo(spanExpected);
        element.appendChild(spanExpected);
        spanExpected.appendNonBreakingSpaceIfBlank();
        
        Element spanActual = new Element("ins");
        spanActual.addStyleClass("actual");
        spanActual.appendText(convertToString(event.getActual()));
        spanActual.appendNonBreakingSpaceIfBlank();
        
        element.appendText("\n");
        element.appendChild(spanActual);        
    }

    public void successReported(AssertEqualsSuccessEvent event) {
        event.getElement()
            .addStyleClass("success")
            .appendNonBreakingSpaceIfBlank();
    }
    
    private String convertToString(Object object) {
        if (object == null) {
            return "(null)";
        }
        return "" + object;
    }
}
