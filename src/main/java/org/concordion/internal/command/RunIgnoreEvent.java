package org.concordion.internal.command;

import org.concordion.api.Element;

public class RunIgnoreEvent {

	private final Element element;

	public RunIgnoreEvent(Element element) {
		this.element = element;
	}
	
	public Element getElement() {
		return element;
	}

}
