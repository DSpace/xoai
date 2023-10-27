package com.lyncode.xoai.dataprovider.handlers;

import com.lyncode.xoai.dataprovider.core.OAIParameters;
import com.lyncode.xoai.dataprovider.exceptions.HandlerException;
import com.lyncode.xoai.dataprovider.exceptions.MarshallingException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.api.RepositoryConfiguration;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DeletedRecordType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.DescriptionType;
import com.lyncode.xoai.dataprovider.xml.oaipmh.IdentifyType;
import com.lyncode.xoai.dataprovider.xml.xoaidescription.XOAIDescription;
import com.lyncode.xoai.util.MarshallingUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;


public class IdentifyHandler extends VerbHandler<IdentifyType> {
    private static Logger log = LogManager.getLogger(IdentifyHandler.class);

    private static final String PROTOCOL_VERSION = "2.0";
    private static final String XOAI_DESC = "XOAI: OAI-PMH Java Toolkit";

    private RepositoryConfiguration identify;
    private List<String> compressions;

    public IdentifyHandler(DateProvider formatter, RepositoryConfiguration identify, List<String> compressions) {
        super(formatter);
        this.identify = identify;
        this.compressions = compressions;
    }


    @Override
    public IdentifyType handle(OAIParameters params) throws OAIException, HandlerException {
        IdentifyType ident = new IdentifyType();
        ident.setBaseURL(identify.getBaseUrl());
        ident.setRepositoryName(identify.getRepositoryName());
        for (String mail : identify.getAdminEmails())
            ident.getAdminEmail().add(mail);
        ident.setEarliestDatestamp(identify.getEarliestDate());
        ident.setDeletedRecord(DeletedRecordType.valueOf(identify.getDeleteMethod().name()));

        ident.setGranularity(identify.getGranularity().toGranularityType());
        ident.setProtocolVersion(PROTOCOL_VERSION);
        for (String com : this.compressions)
            ident.getCompression().add(com);


        List<String> descs = identify.getDescription();
        if (descs == null) {
            descs = new ArrayList<String>();
            XOAIDescription description = new XOAIDescription();
            description.setValue(XOAI_DESC);
            try {
                descs.add(MarshallingUtils.marshalWithoutXMLHeader(description));
            } catch (MarshallingException e) {
                log.warn("Description not added", e);
            }
        } else {
            for (String d : descs) {
                DescriptionType desc = new DescriptionType();
                desc.setAny(d);
                ident.getDescription().add(desc);
            }
        }

        return ident;
    }

}
