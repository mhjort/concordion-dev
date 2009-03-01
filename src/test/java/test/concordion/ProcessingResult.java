package test.concordion;

import nu.xom.Document;

import org.concordion.api.Element;
import org.concordion.api.ResultSummary;
import org.concordion.internal.XMLParser;
import org.concordion.internal.command.AssertEqualsFailureEvent;

public class ProcessingResult {

    private final ResultSummary resultSummary;
    private final EventRecorder eventRecorder;
    private final String documentXML;

    public ProcessingResult(ResultSummary resultSummary, EventRecorder eventRecorder, String documentXML) {
        this.resultSummary = resultSummary;
        this.eventRecorder = eventRecorder;
        this.documentXML = documentXML;
    }
    
    public long getSuccessCount() {
        return resultSummary.getSuccessCount();
    }

    public long getFailureCount() {
        return resultSummary.getFailureCount();
    }

    public long getExceptionCount() {
        return resultSummary.getExceptionCount();
    }
    
    public AssertEqualsFailureEvent getLastAssertEqualsFailureEvent() {
        return (AssertEqualsFailureEvent) eventRecorder.getLast(AssertEqualsFailureEvent.class);
    }

    public Document getXOMDocument() {
        try {
            return XMLParser.parse(documentXML);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse resultant XML document", e);
        }
    }
    
    public Element getRootElement() {
        return new Element(getXOMDocument().getRootElement());
    }
    
    public String toXML() {
        return getRootElement().toXML();
    }

    public String successOrFailureInWords() {
        return hasFailures()  ? "FAILURE" : "SUCCESS";
    }

    public boolean hasFailures() {
        return getFailureCount() + getExceptionCount() != 0;
    }

    public boolean isSuccess() {
        return !hasFailures();
    }

    private Element getOutputFragment() {
        return getRootElement().getFirstDescendantNamed("fragment");
    }

    public String getOutputFragmentXML() {
        return getOutputFragment().toXML().replaceAll("</?fragment>", "").replaceAll("\u00A0", "&#160;");
    }
}
