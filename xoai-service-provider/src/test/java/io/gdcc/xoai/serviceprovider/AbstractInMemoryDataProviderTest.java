/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider;

import io.gdcc.xoai.dataprovider.DataProvider;
import io.gdcc.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import io.gdcc.xoai.dataprovider.exceptions.OAIException;
import io.gdcc.xoai.dataprovider.model.Context;
import io.gdcc.xoai.dataprovider.repository.InMemoryItemRepository;
import io.gdcc.xoai.dataprovider.repository.InMemorySetRepository;
import io.gdcc.xoai.dataprovider.repository.Repository;
import io.gdcc.xoai.dataprovider.repository.RepositoryConfiguration;
import io.gdcc.xoai.serviceprovider.client.OAIClient;
import io.gdcc.xoai.serviceprovider.exceptions.OAIRequestException;
import io.gdcc.xoai.serviceprovider.parameters.Parameters;
import io.gdcc.xoai.services.api.ResumptionTokenFormat;
import io.gdcc.xoai.services.impl.SimpleResumptionTokenFormat;
import io.gdcc.xoai.xml.XmlWriter;
import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static io.gdcc.xoai.dataprovider.model.MetadataFormat.identity;

public abstract class AbstractInMemoryDataProviderTest {
    protected static final String BASE_URL = "http://localhost";
    protected static final String FORMAT = "xoai";

    private final InMemoryItemRepository itemRepository = new InMemoryItemRepository();
    private final InMemorySetRepository setRepository = new InMemorySetRepository();
    private final ResumptionTokenFormat resumptionTokenFormat = new SimpleResumptionTokenFormat();
    private final RepositoryConfiguration repositoryConfiguration = new RepositoryConfiguration()
            .withDefaults()
            .withBaseUrl(BASE_URL);
    private final Context context = new Context().withMetadataFormat(FORMAT, identity());
    private final Repository repository = new Repository()
            .withConfiguration(repositoryConfiguration)
            .withSetRepository(setRepository)
            .withItemRepository(itemRepository)
            .withResumptionTokenFormatter(resumptionTokenFormat);
    private final DataProvider dataProvider = new DataProvider(theDataProviderContext(), theDataRepository());

    protected Context theDataProviderContext () {
        return context;
    }

    protected Repository theDataRepository () {
        return repository;
    }

    protected RepositoryConfiguration theDataRepositoryConfiguration () {
        return repositoryConfiguration;
    }

    protected InMemorySetRepository theDataSetRepository () {
        return setRepository;
    }

    protected InMemoryItemRepository theDataItemRepository () {
        return itemRepository;
    }

    protected OAIClient oaiClient () {
        return new OAIClient() {
            @Override
            public InputStream execute(Parameters parameters) throws OAIRequestException {
                OAIRequestParametersBuilder requestBuilder = new OAIRequestParametersBuilder();
                requestBuilder.withVerb(parameters.getVerb())
                        .withFrom(parameters.getFrom())
                        .withUntil(parameters.getUntil())
                        .withIdentifier(parameters.getIdentifier())
                        .withMetadataPrefix(parameters.getMetadataPrefix())
                        .withResumptionToken(parameters.getResumptionToken())
                        .withSet(parameters.getSet());
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    XmlWriter writer = new XmlWriter(outputStream);
                    dataProvider.handle(requestBuilder).write(writer);
                    writer.close();
//                    System.out.println(outputStream.toString());
                    return new ByteArrayInputStream(outputStream.toByteArray());
                } catch (OAIException | XMLStreamException | XmlWriteException e) {
                    throw new OAIRequestException(e);
                }
    
            }
        };
    }
}
