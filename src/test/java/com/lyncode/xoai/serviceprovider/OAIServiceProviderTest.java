package com.lyncode.xoai.serviceprovider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.lyncode.xoai.serviceprovider.core.Harvester;
import com.lyncode.xoai.serviceprovider.core.Parameters;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.InternalHarvestException;
import com.lyncode.xoai.serviceprovider.oaipmh.oai_dc.OAIDCParser;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.IdentifyType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListMetadataFormatsType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.RecordType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.schemas.oai_dc.OAIDC;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;


@SuppressWarnings("unchecked")
public class OAIServiceProviderTest {

    //@Test
    public void testIdentify() throws BadArgumentException, InternalHarvestException {
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = mock(OAIServiceConfiguration.class);
        when(config.getLogger()).thenReturn(Logger.getLogger("root"));
        
        OAIServiceProvider provider = new OAIServiceProvider(config);
        Harvester harvester = provider.build("http://demo.dspace.org/oai/request");
        IdentifyType id = harvester.identify();
        
        assertEquals("http://demo.dspace.org/oai/request", id.getBaseURL());
    }
    
    //@Test
    public void testListMetadataFormats() throws BadArgumentException, InternalHarvestException {
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = mock(OAIServiceConfiguration.class);
        when(config.getLogger()).thenReturn(Logger.getLogger("root"));
        
        OAIServiceProvider provider = new OAIServiceProvider(config);
        Harvester harvester = provider.build("http://demo.dspace.org/oai/request");
        ListMetadataFormatsType id = harvester.listMetadataFormats(new Parameters());
        
        assertTrue(id.getMetadataFormat().size() > 0);
    }

    
    //@Test(expected=BadArgumentException.class)
    public void testGetRecordException() throws BadArgumentException, InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException {
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = mock(OAIServiceConfiguration.class);
        when(config.getLogger()).thenReturn(Logger.getLogger("root"));
        
        OAIServiceProvider provider = new OAIServiceProvider(config);
        Harvester harvester = provider.build("http://demo.dspace.org/oai/request");
        harvester.getRecord(new Parameters().identifier("oai:demo.dspace.org:10673/4"));
    }

    //@Test
    public void testGetRecord() throws BadArgumentException, InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException {
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = mock(OAIServiceConfiguration.class);
        when(config.getLogger()).thenReturn(Logger.getLogger("root"));
        
        OAIServiceProvider provider = new OAIServiceProvider(config);
        Harvester harvester = provider.build("http://demo.dspace.org/oai/request");
        RecordType record = harvester.getRecord(new Parameters().identifier("oai:demo.dspace.org:10673/4").metadataPrefix("qdc")).getRecord();
        System.out.println(record.getMetadata().getAny().toString());
        assertEquals("oai:demo.dspace.org:10673/4", record.getHeader().getIdentifier());
    }
    


    //@Test
    public void testGetRecordOAI() throws BadArgumentException, InternalHarvestException, CannotDisseminateFormatException, IdDoesNotExistException {
        OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> config = mock(OAIServiceConfiguration.class);
        when(config.getLogger()).thenReturn(Logger.getLogger("root"));
        when(config.getMetadataParser()).thenReturn(new OAIDCParser());
        
        OAIServiceProvider provider = new OAIServiceProvider(config);
        Harvester harvester = provider.build("http://demo.dspace.org/oai/request");
        RecordType record = harvester.getRecord(new Parameters().identifier("oai:demo.dspace.org:10673/4").metadataPrefix("oai_dc")).getRecord();
        //System.out.println(record.getMetadata().getAny().toString());
        assertTrue(record.getMetadata().getAny() instanceof OAIDC);
    }
}
