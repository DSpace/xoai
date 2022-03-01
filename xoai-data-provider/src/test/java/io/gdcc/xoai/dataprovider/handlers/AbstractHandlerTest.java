/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import com.lyncode.xml.exceptions.XmlWriteException;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.dataprovider.parameters.OAICompiledRequest;
import io.gdcc.xoai.dataprovider.repository.InMemoryItemRepository;
import io.gdcc.xoai.dataprovider.repository.InMemorySetRepository;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.dataprovider.repository.RepositoryConfiguration;
import io.gdcc.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import io.gdcc.xoai.dataprovider.exceptions.IllegalVerbException;
import io.gdcc.xoai.dataprovider.exceptions.UnknownParameterException;
import io.gdcc.xoai.dataprovider.filter.Filter;
import io.gdcc.xoai.dataprovider.filter.FilterResolver;
import io.gdcc.xoai.dataprovider.model.ItemIdentifier;
import io.gdcc.xoai.dataprovider.model.conditions.Condition;
import org.dspace.xoai.exceptions.InvalidResumptionTokenException;
import org.dspace.xoai.model.oaipmh.ResumptionToken;
import org.dspace.xoai.services.impl.SimpleResumptionTokenFormat;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import javax.xml.stream.XMLStreamException;

public abstract class AbstractHandlerTest {

    protected static final String EXISTING_METADATA_FORMAT = "xoai";
    private Context context = new Context().withMetadataFormat(EXISTING_METADATA_FORMAT, MetadataFormat.identity());
    private InMemorySetRepository setRepository = new InMemorySetRepository();
    private InMemoryItemRepository itemRepository = new InMemoryItemRepository();
    private RepositoryConfiguration repositoryConfiguration = new RepositoryConfiguration().withDefaults();
    private Repository repository = new Repository()
            .withSetRepository(setRepository)
            .withItemRepository(itemRepository)
            .withResumptionTokenFormatter(new SimpleResumptionTokenFormat())
            .withConfiguration(repositoryConfiguration);

    protected String write(final XmlWritable handle) throws XMLStreamException, XmlWriteException {
        return XmlWriter.toString(new XmlWritable() {
            @Override
            public void write(XmlWriter writer) throws XmlWriteException {
                try {
                    writer.writeStartElement("root");
                    writer.writeNamespace("xsi", "something");
                    writer.write(handle);
                    writer.writeEndElement();
                } catch (XMLStreamException e) {
                    throw new XmlWriteException(e);
                }
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
        return new TypeSafeMatcher<String>() {
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
        return new Condition() {
            @Override
            public Filter getFilter(FilterResolver filterResolver) {
                return new Filter() {
                    @Override
                    public boolean isItemShown(ItemIdentifier item) {
                        return false;
                    }
                };
            }
        };
    }

    protected String valueOf(ResumptionToken.Value resumptionToken) {
        return theRepository().getResumptionTokenFormatter()
                .format(resumptionToken);
    }
}