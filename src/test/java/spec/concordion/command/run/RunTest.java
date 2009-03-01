package spec.concordion.command.run;

import org.concordion.integration.junit3.ConcordionTestCase;

import test.concordion.TestRig;

public class RunTest extends ConcordionTestCase{
	public String successOrFailure(String fragment, String expectedResult, String evaluationResult) {
		
		System.setProperty("concordion.runner.testrunner", RunTestRunner.class.getName());
		RunTestRunner.result = new Boolean(expectedResult);
        return new TestRig()
            .withStubbedEvaluationResult(evaluationResult)
            .processFragment(fragment)
            .successOrFailureInWords();
    }
}
