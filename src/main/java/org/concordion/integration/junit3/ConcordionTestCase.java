package org.concordion.integration.junit3;

import junit.framework.TestCase;

import org.concordion.api.ResultSummary;
import org.concordion.internal.ConcordionBuilder;

public abstract class ConcordionTestCase extends TestCase {

    public void testProcessSpecification() throws Throwable {
        ResultSummary resultSummary = new ConcordionBuilder().build().process(this);
        resultSummary.print(System.out);
        resultSummary.assertIsSatisfied();
    }
}
