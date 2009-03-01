package org.concordion.internal;

import java.io.File;

import org.concordion.Concordion;
import org.concordion.api.Command;
import org.concordion.api.EvaluatorFactory;
import org.concordion.api.Source;
import org.concordion.api.SpecificationLocator;
import org.concordion.api.SpecificationReader;
import org.concordion.api.Target;
import org.concordion.internal.command.AssertEqualsCommand;
import org.concordion.internal.command.AssertEqualsListener;
import org.concordion.internal.command.AssertFalseCommand;
import org.concordion.internal.command.AssertTrueCommand;
import org.concordion.internal.command.EchoCommand;
import org.concordion.internal.command.ExecuteCommand;
import org.concordion.internal.command.LocalTextDecorator;
import org.concordion.internal.command.RunCommand;
import org.concordion.internal.command.SetCommand;
import org.concordion.internal.command.SpecificationCommand;
import org.concordion.internal.command.ThrowableCatchingDecorator;
import org.concordion.internal.command.ThrowableCaughtListener;
import org.concordion.internal.command.ThrowableCaughtPublisher;
import org.concordion.internal.command.VerifyRowsCommand;
import org.concordion.internal.listener.AssertEqualsResultRenderer;
import org.concordion.internal.listener.BreadcrumbRenderer;
import org.concordion.internal.listener.DocumentStructureImprover;
import org.concordion.internal.listener.PageFooterRenderer;
import org.concordion.internal.listener.RunResultRenderer;
import org.concordion.internal.listener.SpecificationExporter;
import org.concordion.internal.listener.StylesheetEmbedder;
import org.concordion.internal.listener.ThrowableRenderer;
import org.concordion.internal.listener.VerifyRowsResultRenderer;
import org.concordion.internal.util.Check;
import org.concordion.internal.util.IOUtil;

public class ConcordionBuilder {

    public static final String NAMESPACE_CONCORDION_2007 = "http://www.concordion.org/2007/concordion";
    private static final String PROPERTY_OUTPUT_DIR = "concordion.output.dir";
    private static final String EMBEDDED_STYLESHEET_RESOURCE = "/org/concordion/internal/resource/embedded.css";
    
    private SpecificationLocator specificationLocator = new ClassNameBasedSpecificationLocator();
    private Source source = new ClassPathSource();
    private Target target = null;
    private CommandRegistry commandRegistry = new CommandRegistry();
    private DocumentParser documentParser = new DocumentParser(commandRegistry);
    private SpecificationReader specificationReader;
    private EvaluatorFactory evaluatorFactory = new SimpleEvaluatorFactory();
    private SpecificationCommand specificationCommand = new SpecificationCommand();
    private AssertEqualsCommand assertEqualsCommand = new AssertEqualsCommand();
    private AssertTrueCommand assertTrueCommand = new AssertTrueCommand();
    private AssertFalseCommand assertFalseCommand = new AssertFalseCommand();
    private ExecuteCommand executeCommand = new ExecuteCommand();
    private RunCommand runCommand = new RunCommand();
    private VerifyRowsCommand verifyRowsCommand = new VerifyRowsCommand();
    private EchoCommand echoCommand = new EchoCommand();
    private File baseOutputDir;
    private ThrowableCaughtPublisher throwableListenerPublisher = new ThrowableCaughtPublisher();
    
    {
        throwableListenerPublisher.addThrowableListener(new ThrowableRenderer());
        
        commandRegistry.register("", "specification", specificationCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "run", runCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "execute", executeCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "set", new SetCommand());
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "assertEquals", assertEqualsCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "assertTrue", assertTrueCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "assertFalse", assertFalseCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "verifyRows", verifyRowsCommand);
        withApprovedCommand(NAMESPACE_CONCORDION_2007, "echo", echoCommand);
        
        assertEqualsCommand.addAssertEqualsListener(new AssertEqualsResultRenderer());
        assertTrueCommand.addAssertEqualsListener(new AssertEqualsResultRenderer());
        assertFalseCommand.addAssertEqualsListener(new AssertEqualsResultRenderer());
        verifyRowsCommand.addVerifyRowsListener(new VerifyRowsResultRenderer());
        runCommand.addRunListener(new RunResultRenderer());
        documentParser.addDocumentParsingListener(new DocumentStructureImprover());
        String stylesheetContent = IOUtil.readResourceAsString(EMBEDDED_STYLESHEET_RESOURCE);
        documentParser.addDocumentParsingListener(new StylesheetEmbedder(stylesheetContent));
    }
    
    public ConcordionBuilder withSource(Source source) {
        this.source = source;
        return this;
    }

    public ConcordionBuilder withTarget(Target target) {
        this.target = target;
        return this;
    }

    public ConcordionBuilder withEvaluatorFactory(EvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
        return this;
    }
    
    public ConcordionBuilder withThrowableListener(ThrowableCaughtListener throwableListener) {
        throwableListenerPublisher.addThrowableListener(throwableListener);
        return this;
    }

    public ConcordionBuilder withAssertEqualsListener(AssertEqualsListener listener) {
        assertEqualsCommand.addAssertEqualsListener(listener);
        return this;
    }
    
    private ConcordionBuilder withApprovedCommand(String namespaceURI, String commandName, Command command) {
        ThrowableCatchingDecorator throwableCatchingDecorator = new ThrowableCatchingDecorator(new LocalTextDecorator(command));
        throwableCatchingDecorator.addThrowableListener(throwableListenerPublisher);
        Command decoratedCommand = throwableCatchingDecorator;
        commandRegistry.register(namespaceURI, commandName, decoratedCommand);
        return this;
    }

    public ConcordionBuilder withCommand(String namespaceURI, String commandName, Command command) {
        Check.notEmpty(namespaceURI, "Namespace URI is mandatory");
        Check.notEmpty(commandName, "Command name is mandatory");
        Check.notNull(command, "Command is null");
        Check.isFalse(namespaceURI.contains("concordion.org"),
                "The namespace URI for user-contributed command '" + commandName + "' "
              + "must not contain 'concordion.org'. Use your own domain name instead.");
        return withApprovedCommand(namespaceURI, commandName, command);
    }
    
    public Concordion build() {
        if (target == null) {
            target = new FileTarget(getBaseOutputDir());
        }
        XMLParser xmlParser = new XMLParser();
        
        specificationCommand.addSpecificationListener(new BreadcrumbRenderer(source, xmlParser));
        specificationCommand.addSpecificationListener(new PageFooterRenderer(target));
        specificationCommand.addSpecificationListener(new SpecificationExporter(target));

        specificationReader = new XMLSpecificationReader(source, xmlParser, documentParser);        

        return new Concordion(specificationLocator, specificationReader, evaluatorFactory);
    }

    private File getBaseOutputDir() {
        if (baseOutputDir != null) {
            return baseOutputDir;
        }
        String outputPath = System.getProperty(PROPERTY_OUTPUT_DIR);
        if (outputPath == null) {
            return new File(System.getProperty("java.io.tmpdir"), "concordion");
        }
        return new File(outputPath);
    }


}
