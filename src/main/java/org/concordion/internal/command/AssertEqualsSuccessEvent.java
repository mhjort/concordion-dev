package org.concordion.internal.command;

import org.concordion.api.Element;

public class AssertEqualsSuccessEvent {

    private final Element element;

    public AssertEqualsSuccessEvent(Element element) {
        this.element = element;
    }

    public Element getElement() {
        return element;
    }
}
