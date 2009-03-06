package test.concordion.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import nu.xom.Document;
import nu.xom.Nodes;

import org.concordion.Concordion;
import org.concordion.api.Resource;
import org.concordion.api.ResultSummary;
import org.concordion.internal.ConcordionBuilder;
import org.concordion.internal.InvalidExpressionException;
import org.concordion.internal.XMLParser;
import org.concordion.internal.util.IOUtil;
import org.junit.Before;
import org.junit.Test;

import test.concordion.StubSource;
import test.concordion.StubTarget;

/**
 * Tests for the basic behaviour of the Concordion object:
 * <ul>
 * <li>the spec is satisfied (green)</li>
 * <li>the spec is not satisfied (red)</li>
 * <li>the spec breaks (exception)</li>
 * </ul>
 * Some edge cases are tested too, like when the html file does not exist, and others.
 *
 */
public class ConcordionTest {

	private static StubTarget target;
	private static StubSource source;

	private static final String NON_EXISTING_RESOURCE = "/does/not/exist";
	private static final String DUMMY_RESOURCE = "/mytest";
	private static final String EXCEPTION_MESSAGE = "Custom exception for testing purposes.";

	@Before
	public void setUp() {
		target = new StubTarget();
		source = new StubSource();
	}

	@Test
	public void testProcessFixtureIsNull() throws IOException {
		Concordion concordion = new ConcordionBuilder().build();
		Object fixture = null;
		try {
			concordion.process(fixture);
		} catch (RuntimeException e) {
			assertEquals("Fixture is null", e.getMessage());
		}
	}

	@Test
	public void testProcessResourceNotFound() {
		Concordion concordion = new ConcordionBuilder().build();
		Object fixture = null;
		Resource resource = new Resource(NON_EXISTING_RESOURCE);
		try {
			concordion.process(resource, fixture);
		} catch (IOException e) {
			assertEquals("Resource '" + NON_EXISTING_RESOURCE + "' not found",
					e.getMessage());
		}
	}

    @Test
	public void testProcessSuccess() throws IOException {
		String fileName = "HelloBob.html";
		addDocumentToSource(fileName, DUMMY_RESOURCE);
		Concordion concordion = createConcordion(source, target);
		
		HelloBobFixture fixture = new HelloBobFixture();
		Resource resource = new Resource(DUMMY_RESOURCE);
		ResultSummary summary = concordion.process(resource, fixture);
		
		assertSummaryNumbers(summary, 1, 0, 0);
		try {
			summary.assertIsSatisfied(fixture);
		} catch (AssertionError e) {
			String xml = target.getWrittenString(resource);
			System.err.println(xml);
			fail(e.getMessage());
		}
	}

	@Test
	public void testProcessFailure() throws IOException {
		String fileName = "HelloBob.html";
		addDocumentToSource(fileName, DUMMY_RESOURCE);
		Concordion concordion = createConcordion(source, target);
		
		Object fixture = new Object() {
			@SuppressWarnings("unused")
            public String getGreeting() {
				return "Hello Bo!";
			}
		};
		Resource resource = new Resource(DUMMY_RESOURCE);
		ResultSummary summary = concordion.process(resource, fixture);
		
		assertSummaryNumbers(summary, 0, 1, 0);
		try {
			summary.assertIsSatisfied(fixture);
		} catch (AssertionError e) {
			String xml = target.getWrittenString(resource);
			Document dom = XMLParser.parse(xml);
			Nodes failures = dom.query("/html/body//span[@class='failure']");
			assertEquals(1, failures.size());
			assertEquals("Hello Bob!", failures.get(0).query(
					"del[@class='expected']").get(0).getValue());
			assertEquals("Hello Bo!", failures.get(0).query(
					"ins[@class='actual']").get(0).getValue());
		}
	}

	@Test
	public void testProcessException() throws IOException {
		String fileName = "HelloBob.html";
		addDocumentToSource(fileName, DUMMY_RESOURCE);
		Concordion concordion = createConcordion(source, target);
		
		Object fixture = new Object() {
			@SuppressWarnings("unused")
            public String getGreeting() {
				throw new RuntimeException(EXCEPTION_MESSAGE);
			}
		};
		Resource resource = new Resource(DUMMY_RESOURCE);
		ResultSummary summary = concordion.process(resource, fixture);
		
		assertSummaryNumbers(summary, 0, 0, 1);
		try {
			summary.assertIsSatisfied(fixture);
		} catch (AssertionError e) {
			String xml = target.getWrittenString(resource);
			Document dom = XMLParser.parse(xml);
			Nodes failures = dom.query("/html/body//span[@class='failure']");
			assertEquals(1, failures.size());
			assertEquals("Hello Bob!", failures.get(0).query(
					"del[@class='expected']").get(0).getValue());
			assertEquals(EXCEPTION_MESSAGE, dom.query(
					"/html/body//span[@class='exceptionMessage']").get(0).getValue());
			assertEquals("View Stack",dom.query(
					"/html/body//input[@class='stackTraceButton']/@value").get(0).getValue());
			assertEquals("greeting",dom.query(
					"/html/body//span[@class='stackTrace']/p/code").get(0).getValue());
			assertEquals(InvalidExpressionException.class.getName()+": "+EXCEPTION_MESSAGE, dom.query(
			"/html/body//span[@class='stackTrace']/span[@class='stackTraceExceptionMessage']").get(0).getValue());
		}
	}

	@Test
	public void testResourceFromFixtureClassName() throws IOException {
		String fileName = "empty.html";
		String resourceName = "/test/concordion/internal/ConcordionTest$EmptyFixture.html";
		addDocumentToSource(fileName, resourceName);
		
		Concordion concordion = createConcordion(source, target);
		EmptyFixture fixture = new EmptyFixture();
		ResultSummary summary = concordion.process(fixture);
		
		assertSummaryNumbers(summary, 0, 0, 0);
		try {
			summary.assertIsSatisfied(fixture);
		} catch (AssertionError e) {
			Resource resource = new Resource(resourceName);
			String xml = target.getWrittenString(resource);
			System.err.println(xml);
			fail(e.getMessage());
		}
	}

	@Test
	public void testResourcesWithLinks() throws IOException {
		String fileName = "EmptyWithLinks.html";
		String resourceName = "/test/concordion/internal/ConcordionTest$EmptyFixture.html";
		addDocumentToSource(fileName, resourceName);
		
		String fileName_S1 = "EmptyWithLinkToNowhere.html";
		String resourceName_S1 = "/test/concordion/internal/ConcordionTest$EmptyFixture_S1.html";
		addDocumentToSource(fileName_S1, resourceName_S1);

		Concordion concordion = createConcordion(source, target);
		EmptyFixture fixture = new EmptyFixture();
		ResultSummary summary = concordion.process(fixture);
		
		assertSummaryNumbers(summary, 0, 0, 0);

		EmptyFixture_S1 fixture_S1 = new EmptyFixture_S1();
		ResultSummary summary_S1 = concordion.process(fixture_S1);
		
		assertSummaryNumbers(summary_S1, 0, 1, 0);
		 
	}

	class EmptyFixture {

	}

	class EmptyFixture_S1 {
		public String getGreeting() {
			return "Hello Bo!";
		}
	}

	class HelloBobFixture {
		public String getGreeting() {
			return "Hello Bob!";
		}
	}
	
	private void assertSummaryNumbers(ResultSummary summary, int successCount,
			int failureCount, int exceptionCount) {
		assertEquals(successCount, summary.getSuccessCount());
		assertEquals(failureCount, summary.getFailureCount());
		assertEquals(exceptionCount, summary.getExceptionCount());
	}

	private Concordion createConcordion(StubSource source, StubTarget target) {
		return new ConcordionBuilder().withTarget(target).withSource(source)
				.build();
	}

	private void addDocumentToSource(String fileName, String resourceName) throws IOException {
		String document = IOUtil.readResourceAsString("/test/concordion/internal/" + fileName);
		source.addResource(resourceName, document);
	}
}
