/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import io.gdcc.xoai.dataprovider.exceptions.IllegalVerbException;
import io.gdcc.xoai.dataprovider.exceptions.UnknownParameterException;
import io.gdcc.xoai.dataprovider.filter.Filter;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.model.conditions.Condition;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.repository.InMemoryItemRepository;
import io.gdcc.xoai.dataprovider.repository.InMemorySetRepository;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.dataprovider.repository.RepositoryConfiguration;
import io.gdcc.xoai.exceptions.InvalidResumptionTokenException;
import io.gdcc.xoai.model.oaipmh.ResumptionToken;
import io.gdcc.xoai.services.impl.SimpleResumptionTokenFormat;
import io.gdcc.xoai.xml.XmlWritable;
import io.gdcc.xoai.xml.XmlWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.xmlunit.matchers.EvaluateXPathMatcher;

import javax.xml.stream.XMLStreamException;

public abstract class AbstractHandlerTest {

    protected static final String EXISTING_METADATA_FORMAT = "xoai";
    private final Context context = new Context().withMetadataFormat(EXISTING_METADATA_FORMAT, MetadataFormat.identity());
    private final InMemorySetRepository setRepository = new InMemorySetRepository();
    private final InMemoryItemRepository itemRepository = new InMemoryItemRepository();
    private final RepositoryConfiguration repositoryConfiguration = new RepositoryConfiguration().withDefaults();
    private final Repository repository = new Repository()
            .withSetRepository(setRepository)
            .withItemRepository(itemRepository)
            .withResumptionTokenFormatter(new SimpleResumptionTokenFormat())
            .withConfiguration(repositoryConfiguration);
    
    protected static Matcher<? super String> xPath(String xpath, Matcher<String> stringMatcher) {
        return EvaluateXPathMatcher.hasXPath(xpath, stringMatcher);
    }
    protected String write(final XmlWritable handle) throws XMLStreamException, XmlWriteException {
        return XmlWriter.toString(writer -> {
            try {
                writer.writeStartElement("root");
                writer.writeNamespace("xsi", "something");
                writer.write(handle);
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                throw new XmlWriteException(e);
            }
        });
    }

    protected OAICompiledRequest a (OAIRequestParametersBuilder builder) throws BadArgumentException, InvalidResumptionTokenException, UnknownParameterException, IllegalVerbException, DuplicateDefinitionException {
        return OAICompiledRequest.compile(builder);
    }

    protected OAIRequestParametersBuilder request() {
        return new OAIRequestParametersBuilder();
    }

    protected Context aContext () {
        return context;
    }
    protected Context theContext () {
        return context;
    }

    protected InMemorySetRepository theSetRepository() {
        return setRepository;
    }

    protected InMemoryItemRepository theItemRepository () {
        return itemRepository;
    }

    protected RepositoryConfiguration theRepositoryConfiguration() {
        return repositoryConfiguration;
    }

    protected Repository theRepository() {
        return repository;
    }

    protected Matcher<String> asInteger(final Matcher<Integer> matcher) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(String item) {
                return matcher.matches(Integer.valueOf(item));
            }
    
            @Override
            public void describeTo(Description description) {
                description.appendDescriptionOf(matcher);
            }
        };
    }

    protected Condition alwaysFalseCondition() {
        return filterResolver -> (Filter) item -> false;
    }

    protected String valueOf(ResumptionToken.Value resumptionToken) {
        return theRepository().getResumptionTokenFormatter()
                .format(resumptionToken);
    }
}
