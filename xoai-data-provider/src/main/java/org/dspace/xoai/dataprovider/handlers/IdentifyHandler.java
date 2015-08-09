/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.dspace.xoai.dataprovider.exceptions.HandlerException;
import org.dspace.xoai.dataprovider.exceptions.InternalOAIException;
import org.dspace.xoai.dataprovider.exceptions.OAIException;
import org.dspace.xoai.dataprovider.model.Context;
import org.dspace.xoai.dataprovider.parameters.OAICompiledRequest;
import org.dspace.xoai.dataprovider.repository.Repository;
import org.dspace.xoai.dataprovider.repository.RepositoryConfiguration;
import org.dspace.xoai.model.oaipmh.DeletedRecord;
import org.dspace.xoai.model.oaipmh.Description;
import org.dspace.xoai.model.oaipmh.Identify;
import org.dspace.xoai.xml.XmlWritable;
import org.dspace.xoai.xml.XmlWriter;

import javax.xml.stream.XMLStreamException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class IdentifyHandler extends VerbHandler<Identify> {
    private static Logger log = LogManager.getLogger(IdentifyHandler.class);

    private static final String PROTOCOL_VERSION = "2.0";
    private static final String XOAI_DESC = "XOAI: OAI-PMH Java Toolkit";

    public IdentifyHandler(Context context, Repository repository) {
        super(context, repository);

        // Static validation
        RepositoryConfiguration configuration = getRepository().getConfiguration();
        if (configuration == null)
            throw new InternalOAIException("No repository configuration provided");
        if (configuration.getMaxListSets() <= 0)
            throw new InternalOAIException("The repository configuration must return maxListSets greater then 0");
        if (configuration.getMaxListIdentifiers() <= 0)
            throw new InternalOAIException("The repository configuration must return maxListIdentifiers greater then 0");
        if (configuration.getMaxListRecords() <= 0)
            throw new InternalOAIException("The repository configuration must return maxListRecords greater then 0");
        if (configuration.getAdminEmails() == null || configuration.getAdminEmails().isEmpty())
            throw new InternalOAIException("The repository configuration must return at least one admin email");
        try {
            if (configuration.getBaseUrl() == null)
                throw new InternalOAIException("The repository configuration must return a valid base url (absolute)");
            new URL(configuration.getBaseUrl());
        } catch (MalformedURLException e) {
            throw new InternalOAIException("The repository configuration must return a valid base url (absolute)", e);
        }
        if (configuration.getDeleteMethod() == null)
            throw new InternalOAIException("The repository configuration must return a valid delete method");
        if (configuration.getEarliestDate() == null)
            throw new InternalOAIException("The repository configuration must return a valid earliest date. That's the date of the first inserted item");
        if (configuration.getRepositoryName() == null)
            throw new InternalOAIException("The repository configuration must return a valid repository name");

    }

    @Override
    public Identify handle(OAICompiledRequest params) throws OAIException, HandlerException {
        Identify identify = new Identify();
        RepositoryConfiguration configuration = getRepository().getConfiguration();
        identify.withBaseURL(configuration.getBaseUrl());
        identify.withRepositoryName(configuration.getRepositoryName());
        for (String mail : configuration.getAdminEmails())
            identify.getAdminEmails().add(mail);
        identify.withEarliestDatestamp(configuration.getEarliestDate());
        identify.withDeletedRecord(DeletedRecord.valueOf(configuration.getDeleteMethod().name()));

        identify.withGranularity(configuration.getGranularity());
        identify.withProtocolVersion(PROTOCOL_VERSION);
        if (configuration.hasCompressions())
            for (String com : configuration.getCompressions())
                identify.getCompressions().add(com);


        List<String> descriptions = configuration.getDescription();
        if (descriptions == null) {
            try {
                identify.withDescription(new Description(XmlWriter.toString(new XOAIDescription().withValue(XOAI_DESC))));
            } catch (XmlWriteException e) {
                log.warn("Description not added", e);
            } catch (XMLStreamException e) {
                log.warn("Description not added", e);
            }
        } else {
            for (String description : descriptions) {
                identify.getDescriptions().add(new Description().withMetadata(description));
            }
        }

        return identify;
    }

    public class XOAIDescription implements XmlWritable {
        protected String value;
        protected String type;

        public String getValue() {
            return value;
        }

        public XOAIDescription withValue(String value) {
            this.value = value;
            return this;
        }

        public String getType() {
            return type;
        }

        public XOAIDescription withType(String value) {
            this.type = value;
            return this;
        }

        @Override
        public void write(XmlWriter writer) throws XmlWriteException {
            try {
                writer.writeStartElement("XOAIDescription");
                writer.writeAttribute("type", getType());
                writer.writeCharacters(getValue());
                writer.writeEndElement();
            } catch (XMLStreamException e) {
                throw new XmlWriteException(e);
            }
        }
    }
}
