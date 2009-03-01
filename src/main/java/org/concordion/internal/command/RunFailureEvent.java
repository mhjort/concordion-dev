package org.concordion.internal.command;

import org.concordion.api.Element;

public class RunFailureEvent {

	private final Element element;

	public RunFailureEvent(Element element) {
		this.element = element;
	}

	public Element getElement() {
		return element;
	}

}
