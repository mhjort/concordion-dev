package org.concordion.internal;

import java.util.EventListener;

import nu.xom.Document;

public interface DocumentParsingListener extends EventListener {

    void beforeParsing(Document document);
}
