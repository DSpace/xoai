/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import org.dspace.xoai.xml.XmlWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Identify implements Verb {
    protected String repositoryName;
    protected String baseURL;
    protected String protocolVersion = "1.0";
    protected List<String> adminEmails = new ArrayList<String>();
    protected Date earliestDatestamp;
    protected DeletedRecord deletedRecord = DeletedRecord.NO;
    protected Granularity granularity = Granularity.Second;
    protected List<String> compressions = new ArrayList<String>();
    protected List<Description> descriptions = new ArrayList<Description>();

    public String getRepositoryName() {
        return repositoryName;
    }

    public Identify withRepositoryName(String value) {
        this.repositoryName = value;
        return this;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public Identify withBaseURL(String value) {
        this.baseURL = value;
        return this;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public Identify withProtocolVersion(String value) {
        this.protocolVersion = value;
        return this;
    }

    public List<String> getAdminEmails() {
        return this.adminEmails;
    }

    public Date getEarliestDatestamp() {
        return earliestDatestamp;
    }

    public Identify withEarliestDatestamp(Date value) {
        this.earliestDatestamp = value;
        return this;
    }

    public DeletedRecord getDeletedRecord() {
        return deletedRecord;
    }

    public Identify withDeletedRecord(DeletedRecord value) {
        this.deletedRecord = value;
        return this;
    }

    public Granularity getGranularity() {
        return granularity;
    }

    public Identify withGranularity(Granularity value) {
        this.granularity = value;
        return this;
    }

    public List<String> getCompressions() {
        return this.compressions;
    }

    public List<Description> getDescriptions() {
        return this.descriptions;
    }

    public Identify withAdminEmail (String adminEmail) {
        this.adminEmails.add(adminEmail);
        return this;
    }
    public Identify withCompression (String compression) {
        this.compressions.add(compression);
        return this;
    }
    public Identify withDescription (Description description) {
        this.descriptions.add(description);
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        if (this.repositoryName == null) throw new XmlWriteException("Repository Name cannot be null");
        if (this.baseURL == null) throw new XmlWriteException("Base URL cannot be null");
        if (this.protocolVersion == null) throw new XmlWriteException("Protocol version cannot be null");
        if (this.earliestDatestamp == null) throw new XmlWriteException("Eerliest datestamp cannot be null");
        if (this.deletedRecord == null) throw new XmlWriteException("Deleted record persistency cannot be null");
        if (this.granularity == null) throw new XmlWriteException("Granularity cannot be null");
        if (this.adminEmails == null || this.adminEmails.isEmpty())
            throw new XmlWriteException("List of admin emails cannot be null or empty");

        writer.writeElement("repositoryName", repositoryName);
        writer.writeElement("baseURL", baseURL);
        writer.writeElement("protocolVersion", protocolVersion);

        for (String email : this.adminEmails)
            writer.writeElement("adminEmail", email);

        writer.writeElement("earliestDatestamp", earliestDatestamp, Granularity.Second);
        writer.writeElement("deletedRecord", deletedRecord.value());
        writer.writeElement("granularity", granularity.toString());

        if (!this.compressions.isEmpty())
            for (String compression : this.compressions)
                writer.writeElement("compression", compression);

        if (!this.descriptions.isEmpty())
            for (Description description : this.descriptions)
                writer.writeElement("description", description);
    }

    @Override
    public Type getType() {
        return Type.Identify;
    }
}
