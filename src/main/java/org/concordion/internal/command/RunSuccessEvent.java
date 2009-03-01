package org.concordion.internal.command;

import org.concordion.api.Element;

public class RunSuccessEvent {

	private final Element element;

	public RunSuccessEvent(Element element) {
		this.element = element;
	}
	
	public Element getElement() {
		return element;
	}

}
