package org.concordion.internal.command;

import org.concordion.api.Element;

public class AssertEqualsFailureEvent {

    private final Element element;
    private final String expected;
    private final Object actual;

    public AssertEqualsFailureEvent(Element element, String expected, Object actual) {
        this.element = element;
        this.expected = expected;
        this.actual = actual;
    }

    public Element getElement() {
        return element;
    }

    public String getExpected() {
        return expected;
    }

    public Object getActual() {
        return actual;
    }
}
