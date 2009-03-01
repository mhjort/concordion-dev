package spec.concordion.command.run;

import org.concordion.api.Resource;
import org.concordion.api.Runner;

public class RunTestRunner implements Runner {

	public static boolean result = true;
	private String param = "";
	
	public void setTestParam(String param){
		this.param  = param;
	}
	
	public boolean execute(Resource resource, String href) throws Exception {
		if(!param.equals(href)) {
			throw new RuntimeException("testParam not set");
		}
		return result;
	}
}
