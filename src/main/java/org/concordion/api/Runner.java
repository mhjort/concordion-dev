package org.concordion.api;

public interface Runner {
	
	boolean execute(Resource resource, String href) throws Exception;
}
