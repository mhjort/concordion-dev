package spec.concordion.command.run;

import org.concordion.api.Resource;
import org.concordion.api.Result;
import org.concordion.api.Runner;
import org.concordion.api.RunnerResult;

public class RunTestRunner implements Runner {

	public static Result result = null;
	private String param = "";
	
	public void setTestParam(String param){
		this.param  = param;
	}
	
	public RunnerResult execute(Resource resource, String href) throws Exception {
		if(!param.equals(href)) {
			throw new RuntimeException("testParam not set");
		}
		return new RunnerResult(result);
	}
}
