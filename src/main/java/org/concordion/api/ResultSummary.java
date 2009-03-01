package org.concordion.api;

import java.io.PrintStream;

public interface ResultSummary {

    void assertIsSatisfied();

    boolean hasExceptions();

    long getSuccessCount();
    
    long getFailureCount();

    long getExceptionCount();

    void print(PrintStream out);
}
