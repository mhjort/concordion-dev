package org.concordion.api;

import java.io.PrintStream;

public interface ResultSummary {

    @Deprecated
    /* Use assertIsSatisfied(fixture) instead. */
    void assertIsSatisfied();
    
    void assertIsSatisfied(Object fixture);

    boolean hasExceptions();

    long getSuccessCount();
    
    long getFailureCount();

    long getExceptionCount();

    @Deprecated
    /* Use print(out, fixture) instead. */
    void print(PrintStream out);
    
    void print(PrintStream out, Object fixture);

}
