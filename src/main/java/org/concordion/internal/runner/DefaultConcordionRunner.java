package org.concordion.internal.runner;

import org.concordion.api.Resource;
import org.concordion.api.Runner;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class DefaultConcordionRunner implements Runner {

	public boolean execute(Resource resource, String href) throws Exception {
		String name = resource.getName();
		Resource hrefResource = resource.getParent().getRelativeResource(href);
		name = hrefResource.getPath().replaceFirst("/", "").replace("/", ".").replaceAll("\\.html$", "Test");
		Class<?> concordionClass = Class.forName(name);
		Result result = JUnitCore.runClasses(concordionClass);
		return result.wasSuccessful();			
	}

}
