package org.concordion.internal;

import java.io.IOException;

import org.concordion.api.FullOGNL;
import org.concordion.api.ResultSummary;

public class FixtureRunner {
    
    public void run(final Object fixture) throws IOException {
        ConcordionBuilder concordionBuilder = new ConcordionBuilder();
        if (fixture.getClass().isAnnotationPresent(FullOGNL.class)) {
            concordionBuilder.withEvaluatorFactory(new OgnlEvaluatorFactory());
        }
        ResultSummary resultSummary = concordionBuilder.build().process(fixture);
        resultSummary.print(System.out, fixture);
        resultSummary.assertIsSatisfied(fixture);
    }
}