/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.repository;

import io.gdcc.xoai.model.oaipmh.DeletedRecord;
import io.gdcc.xoai.model.oaipmh.Granularity;
import io.gdcc.xoai.services.api.DateProvider;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class RepositoryConfiguration {
    private String repositoryName;
    private final List<String> adminEmails = new ArrayList<>();
    private String baseUrl;
    private Instant earliestDate;
    private int maxListIdentifiers;
    private int maxListSets;
    private int maxListRecords;
    private Granularity granularity;
    private DeletedRecord deleteMethod;
    private List<String> descriptions;
    private List<String> compressions;

    public String getRepositoryName() {
        return repositoryName;
    }

    public List<String> getAdminEmails() {
        return adminEmails;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public Instant getEarliestDate() {
        return earliestDate;
    }

    public int getMaxListIdentifiers() {
        return this.maxListIdentifiers;
    }

    public int getMaxListSets() {
        return this.maxListSets;
    }

    public int getMaxListRecords() {
        return this.maxListRecords;
    }

    public Granularity getGranularity() {
        return granularity;
    }

    public DeletedRecord getDeleteMethod() {
        return deleteMethod;
    }

    public List<String> getDescription() {
        return descriptions;
    }

    public List<String> getCompressions () {
        return compressions;
    }

    public RepositoryConfiguration withMaxListSets(int maxListSets) {
        this.maxListSets = maxListSets;
        return this;
    }

    public RepositoryConfiguration withGranularity(Granularity granularity) {
        this.granularity = granularity;
        return this;
    }

    public RepositoryConfiguration withRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
        return this;
    }

    public RepositoryConfiguration and () {
        return this;
    }

    public RepositoryConfiguration withAdminEmails(String... emails) {
        this.adminEmails.addAll(asList(emails));
        return this;
    }

    public RepositoryConfiguration withAdminEmail(String email) {
        this.adminEmails.add(email);
        return this;
    }
    public RepositoryConfiguration withDeleteMethod(DeletedRecord deleteMethod) {
        this.deleteMethod = deleteMethod;
        return this;
    }

    public RepositoryConfiguration withDescription(String description) {
        if (descriptions == null)
            descriptions = new ArrayList<>();
        descriptions.add(description);
        return this;
    }

    public RepositoryConfiguration withBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public RepositoryConfiguration withEarliestDate(Instant earliestDate) {
        this.earliestDate = earliestDate;
        return this;
    }

    public RepositoryConfiguration withCompression (String compression) {
        if (compressions == null)
            compressions = new ArrayList<>();
        compressions.add(compression);
        return this;
    }

    public RepositoryConfiguration withMaxListRecords(int maxListRecords) {
        this.maxListRecords = maxListRecords;
        return this;
    }

    public RepositoryConfiguration withDefaults () {
        this.repositoryName = "Repository";
        this.earliestDate = DateProvider.now();
        this.adminEmails.add("sample@test.com");
        this.baseUrl = "http://localhost";
        this.maxListIdentifiers = 100;
        this.maxListRecords = 100;
        this.maxListSets = 100;
        this.granularity = Granularity.Second;
        this.deleteMethod = DeletedRecord.NO;

        return this;
    }

    public boolean hasCompressions() {
        return compressions != null && !compressions.isEmpty();
    }

    public RepositoryConfiguration withMaxListIdentifiers(int maxListIdentifiers) {
        this.maxListIdentifiers = maxListIdentifiers;
        return this;
    }
}
