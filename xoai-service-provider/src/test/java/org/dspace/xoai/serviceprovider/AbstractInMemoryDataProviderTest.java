/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.dataprovider.DataProvider;
import org.dspace.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import org.dspace.xoai.dataprovider.exceptions.OAIException;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.repository.InMemoryItemRepository;
import org.dspace.xoai.dataprovider.repository.InMemorySetRepository;
import org.dspace.xoai.dataprovider.repository.Repository;
import org.dspace.xoai.dataprovider.repository.RepositoryConfiguration;
import org.dspace.xoai.serviceprovider.client.OAIClient;
import org.dspace.xoai.serviceprovider.exceptions.OAIRequestException;
import org.dspace.xoai.serviceprovider.parameters.Parameters;
import org.dspace.xoai.services.api.ResumptionTokenFormat;
import org.dspace.xoai.services.impl.SimpleResumptionTokenFormat;
import org.dspace.xoai.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.dspace.xoai.dataprovider.model.MetadataFormat.identity;

public abstract class AbstractInMemoryDataProviderTest {
    protected static final String BASE_URL = "http://localhost";
    protected static final String FORMAT = "xoai";

    private InMemoryItemRepository itemRepository = new InMemoryItemRepository();
    private InMemorySetRepository setRepository = new InMemorySetRepository();
    private ResumptionTokenFormat resumptionTokenFormat = new SimpleResumptionTokenFormat();
    private RepositoryConfiguration repositoryConfiguration = new RepositoryConfiguration()
            .withDefaults()
            .withBaseUrl(BASE_URL);
    private Context context = new Context().withMetadataFormat(FORMAT, identity());
    private Repository repository = new Repository()
            .withConfiguration(repositoryConfiguration)
            .withSetRepository(setRepository)
            .withItemRepository(itemRepository)
            .withResumptionTokenFormatter(resumptionTokenFormat);
    private DataProvider dataProvider = new DataProvider(theDataProviderContext(), theDataRepository());

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
                } catch (OAIException e) {
                    throw new OAIRequestException(e);
                } catch (XmlWriteException e) {
                    throw new OAIRequestException(e);
                } catch (XMLStreamException e) {
                    throw new OAIRequestException(e);
                }

            }
        };
    }
}
