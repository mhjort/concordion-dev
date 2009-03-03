package spec.concordion.command.run;

import org.concordion.api.Result;
import org.concordion.integration.junit3.ConcordionTestCase;

import test.concordion.TestRig;

public class RunTest extends ConcordionTestCase{
	public String successOrFailure(String fragment, String hardCodedTestResult, String evaluationResult) {
		
		System.setProperty("concordion.runner.concordion", RunTestRunner.class.getName());
		RunTestRunner.result = Result.valueOf(hardCodedTestResult);
        return new TestRig()
            .withStubbedEvaluationResult(evaluationResult)
            .processFragment(fragment)
            .successOrFailureInWords();
    }
}
