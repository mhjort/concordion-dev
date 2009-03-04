package org.concordion.internal.runner;

import org.concordion.api.ExpectedToFail;
import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.Runner;
import org.concordion.api.RunnerResult;
import org.junit.runner.JUnitCore;

public class DefaultConcordionRunner implements Runner {

	public RunnerResult execute(Resource resource, String href) throws Exception {
		String name = resource.getName();
		Resource hrefResource = resource.getParent().getRelativeResource(href);
		name = hrefResource.getPath().replaceFirst("/", "").replace("/", ".").replaceAll("\\.html$", "");
		Class<?> concordionClass;
		try {
		    concordionClass = Class.forName(name);
		} catch (ClassNotFoundException e) {
		    concordionClass = Class.forName(name + "Test");
		}
		org.junit.runner.Result jUnitResult = JUnitCore.runClasses(concordionClass);
		
		Result result = Result.FAILURE;
		if (jUnitResult.wasSuccessful()) {
		    result = Result.SUCCESS;
		    if (isOnlySuccessfulBecauseItWasExpectedToFail(concordionClass)) {
		        result = Result.IGNORED;
		    }
		}
		return new RunnerResult(result);			
	}

    private boolean isOnlySuccessfulBecauseItWasExpectedToFail(Class<?> concordionClass) {
        return concordionClass.getAnnotation(ExpectedToFail.class) != null;
    }
}
