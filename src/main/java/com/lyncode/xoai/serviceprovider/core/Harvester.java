package com.lyncode.xoai.serviceprovider.core;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.*;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.serviceprovider.verbs.*;
import com.lyncode.xoai.util.ProcessingQueue;


public class Harvester {
    private OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config;
    private Parameters parameters;

    public Harvester(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config, String baseURL) {
        this.config = config;
        this.parameters = new Parameters(this.config.getFormatter(), baseURL);
    }


    public ProcessingQueue<RecordType> listRecords(Parameters parameters) {
        return new ListRecords(this.parameters.merge(parameters).verb(OAIVerb.ListRecords), config).harvest();
    }

    public ProcessingQueue<HeaderType> listIdentifiers(Parameters parameters) {
        return new ListIdentifiers(this.parameters.merge(parameters).verb(OAIVerb.ListIdentifiers), config).harvest();
    }

    public ListMetadataFormatsType listMetadataFormats(Parameters parameters) throws InternalHarvestException {
        return new ListMetadataFormats(this.parameters.merge(parameters).verb(OAIVerb.ListMetadataFormats), config).harvest();
    }

    public ProcessingQueue<SetType> listSets(Parameters parameters) {
        return new ListSets(this.parameters.merge(parameters).verb(OAIVerb.ListSets), config).harvest();
    }

    public GetRecordType getRecord(Parameters parameters) throws InternalHarvestException, BadArgumentException, CannotDisseminateFormatException, IdDoesNotExistException {
        return new GetRecord(this.parameters.merge(parameters).verb(OAIVerb.GetRecord), config).harvest();
    }

    public IdentifyType identify() throws InternalHarvestException, BadArgumentException {
        return new Identify(this.parameters.merge(parameters).verb(OAIVerb.Identify), config).harvest();
    }
}
