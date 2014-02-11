/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     client://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.xoai.serviceprovider;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.dataprovider.DataProvider;
import com.lyncode.xoai.dataprovider.builder.OAIRequestParametersBuilder;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.model.Context;
import com.lyncode.xoai.dataprovider.repository.InMemoryItemRepository;
import com.lyncode.xoai.dataprovider.repository.InMemorySetRepository;
import com.lyncode.xoai.dataprovider.repository.Repository;
import com.lyncode.xoai.dataprovider.repository.RepositoryConfiguration;
import com.lyncode.xoai.serviceprovider.client.OAIClient;
import com.lyncode.xoai.serviceprovider.exceptions.OAIRequestException;
import com.lyncode.xoai.serviceprovider.parameters.Parameters;
import com.lyncode.xoai.services.api.ResumptionTokenFormat;
import com.lyncode.xoai.services.impl.SimpleResumptionTokenFormat;
import com.lyncode.xoai.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static com.lyncode.xoai.dataprovider.model.MetadataFormat.identity;

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
