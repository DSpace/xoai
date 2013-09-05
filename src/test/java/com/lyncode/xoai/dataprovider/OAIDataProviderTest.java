package com.lyncode.xoai.dataprovider;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.lyncode.xoai.dataprovider.configuration.ConfigurationManager;
import com.lyncode.xoai.dataprovider.core.DeleteMethod;
import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.core.ListItemIdentifiersResult;
import com.lyncode.xoai.dataprovider.core.ListItemsResults;
import com.lyncode.xoai.dataprovider.core.ListSetsResult;
import com.lyncode.xoai.dataprovider.core.ReferenceSet;
import com.lyncode.xoai.dataprovider.core.Set;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.data.AbstractIdentify;
import com.lyncode.xoai.dataprovider.data.AbstractItem;
import com.lyncode.xoai.dataprovider.data.AbstractItemIdentifier;
import com.lyncode.xoai.dataprovider.data.AbstractItemRepository;
import com.lyncode.xoai.dataprovider.data.AbstractSetRepository;
import com.lyncode.xoai.dataprovider.data.MetadataFormat;
import com.lyncode.xoai.dataprovider.exceptions.DuplicateDefinitionException;
import com.lyncode.xoai.dataprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.dataprovider.exceptions.IllegalVerbException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.UnknownParameterException;
import com.lyncode.xoai.dataprovider.exceptions.WrittingXmlException;
import com.lyncode.xoai.dataprovider.format.MetadataFormatManager;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMHerrorcodeType;
import com.lyncode.xoai.util.XSLTUtils;

@PrepareForTest({ XOAIManager.class, XSLTUtils.class })
@RunWith(PowerMockRunner.class)
public class OAIDataProviderTest {
    static final String REP_NAME = "Hello";
    static final String DESC = "<a>test</a>";
    
    OAIDataProvider dataProvider;
    AbstractItemRepository itemRepository;
    AbstractSetRepository setRepository;
    AbstractIdentify identify;

    @Before
    public void setUp() throws Exception {
        XOAIManager man = new XOAIManager("test", ConfigurationManager.readConfiguration(this.getClass().getResourceAsStream("xoai.xml")));
        
        MetadataFormat metadataFormat = mock(MetadataFormat.class);
        when(metadataFormat.isApplyable(any(AbstractItemIdentifier.class))).thenReturn(true);
        
        MetadataFormatManager formatManager = mock(MetadataFormatManager.class);
        when(formatManager.getFormatByPrefix("xoai")).thenReturn(metadataFormat);
        when(formatManager.formatExists("xoai")).thenReturn(true);
        when(formatManager.getFormat("xoai")).thenReturn(metadataFormat);
        
        XOAIManager manSpy = mock(XOAIManager.class);
        when(manSpy.getFormatManager()).thenReturn(formatManager);
        when(manSpy.getContextManager()).thenReturn(man.getContextManager());
        when(manSpy.getMaxListRecordsSize()).thenReturn(man.getMaxListRecordsSize());
        when(manSpy.getMaxListIdentifiersSize()).thenReturn(man.getMaxListIdentifiersSize());
        when(manSpy.getMaxListSetsSize()).thenReturn(man.getMaxListSetsSize());
        when(manSpy.getFilterManager()).thenReturn(man.getFilterManager());
        when(manSpy.getSetManager()).thenReturn(man.getSetManager());
        when(manSpy.getTransformerManager()).thenReturn(man.getTransformerManager());
        
        PowerMockito.mockStatic(XOAIManager.class);
        when(XOAIManager.getManager()).thenReturn(manSpy);
        
        PowerMockito.mockStatic(XSLTUtils.class);
        when(XSLTUtils.transform(any(File.class), any(AbstractItem.class))).thenReturn("<test></test>");
        
        identify = mock(AbstractIdentify.class);
        when(identify.getBaseUrl()).thenReturn("http://test.com/");
        when(identify.getDeleteMethod()).thenReturn(DeleteMethod.NO);
        when(identify.getGranularity()).thenReturn(Granularity.Day);
        when(identify.getRepositoryName()).thenReturn(REP_NAME);
        when(identify.getAdminEmails()).thenReturn(Arrays.asList(new String[]{ "test@gmail.com" }));
        when(identify.getEarliestDate()).thenReturn(new Date());
        
        
        setRepository = mock(AbstractSetRepository.class);
        itemRepository = mock(AbstractItemRepository.class);
        
        dataProvider = new OAIDataProvider("request", identify, setRepository, itemRepository);
    }





    @Test
    public void shouldGiveBadVerb() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("SomeInvalidVerb");
        
        dataProvider.handle(params, out);
        
        // System.out.println(XMLUtils.format(out.toString()));
        
        assertTrue(out.toString().contains(OAIPMHerrorcodeType.BAD_VERB.value()));
    }
    
    @Test
    public void shouldGiveGoodIdentifyResponse() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("Identify");
        when(identify.getDescription()).thenReturn(Arrays.asList(new String[]{ DESC }));
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains(REP_NAME));
    }
    
    @Test
    public void shouldGiveGoodIdentifyResponse2() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("Identify");
        when(identify.getDescription()).thenReturn(null);
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains(REP_NAME));
    }

    
    @Test
    public void shouldGiveGoodIdentifyResponseDescription() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("Identify");
        when(identify.getDescription()).thenReturn(Arrays.asList(new String[]{ DESC }));
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains(DESC));
    }
    
    @Test
    public void listIdentifiersShouldGiveBadArgument() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListIdentifiers");
        
        dataProvider.handle(params, out);
        
        // System.out.println(out.toString());
        
        assertTrue(out.toString().contains(OAIPMHerrorcodeType.BAD_ARGUMENT.value()));
    }

    @Test
    public void listIdentifiersShouldGiveNoRecordsMatch() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListIdentifiers");
        when(params.getMetadataPrefix()).thenReturn("xoai");
        

        
        ListItemIdentifiersResult resultMock = mock(ListItemIdentifiersResult.class);
        when(resultMock.getResults()).thenReturn(new ArrayList<AbstractItemIdentifier>());
        when(resultMock.getTotal()).thenReturn(0);
        
        
        when(itemRepository.getItemIdentifiers(any(List.class), anyInt(), anyInt())).thenReturn(resultMock);
        
        dataProvider.handle(params, out);
        
        // System.out.println(out.toString());
        
        assertTrue(out.toString().contains(OAIPMHerrorcodeType.NO_RECORDS_MATCH.value()));
    }
    
    @Test
    public void listIdentifiersShouldGiveRecords() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListIdentifiers");
        when(params.getMetadataPrefix()).thenReturn("xoai");
        

        
        ListItemIdentifiersResult resultMock = mock(ListItemIdentifiersResult.class);
        AbstractItemIdentifier identifier = mock(AbstractItemIdentifier.class);
        when(identifier.getDatestamp()).thenReturn(new Date());
        when(identifier.getIdentifier()).thenReturn("Hello");
        when(identifier.getSets()).thenReturn(new ArrayList<ReferenceSet>());
        when(identifier.isDeleted()).thenReturn(false);
        
        when(resultMock.getResults()).thenReturn(Arrays.asList(new AbstractItemIdentifier[]{ identifier }));
        when(resultMock.getTotal()).thenReturn(1);
        
        
        when(itemRepository.getItemIdentifiers(any(List.class), anyInt(), anyInt())).thenReturn(resultMock);
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains("Hello"));
    }
    @Test
    public void listRecordsShouldGiveDeletedRecord() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListRecords");
        when(params.getMetadataPrefix()).thenReturn("xoai");
        
        ByteArrayOutputStream outputer = new ByteArrayOutputStream();
        

        
        ListItemsResults resultMock = mock(ListItemsResults.class);
        AbstractItem item = mock(AbstractItem.class);
        when(item.getDatestamp()).thenReturn(new Date());
        when(item.getIdentifier()).thenReturn("Hello");
        when(item.getSets()).thenReturn(new ArrayList<ReferenceSet>());
        when(item.isDeleted()).thenReturn(true);
        
        when(resultMock.getResults()).thenReturn(Arrays.asList(new AbstractItem[]{ item }));
        when(resultMock.getTotal()).thenReturn(1);
        
        
        when(itemRepository.getItems(any(List.class), anyInt(), anyInt())).thenReturn(resultMock);
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains("Hello"));
    }
    
    @Test
    public void listRecordsShouldGiveRecords() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListRecords");
        when(params.getMetadataPrefix()).thenReturn("xoai");
        
        ByteArrayOutputStream outputer = new ByteArrayOutputStream();
        

        
        ListItemsResults resultMock = mock(ListItemsResults.class);
        AbstractItem item = mock(AbstractItem.class);
        when(item.getDatestamp()).thenReturn(new Date());
        when(item.getIdentifier()).thenReturn("Hello");
        when(item.getSets()).thenReturn(new ArrayList<ReferenceSet>());
        when(item.isDeleted()).thenReturn(false);
        
        when(resultMock.getResults()).thenReturn(Arrays.asList(new AbstractItem[]{ item }));
        when(resultMock.getTotal()).thenReturn(1);
        
        
        when(itemRepository.getItems(any(List.class), anyInt(), anyInt())).thenReturn(resultMock);
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains("Hello"));
    }

    @Test
    public void listSetsShouldGiveRecords() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListSets");

        
        ListSetsResult resultMock = mock(ListSetsResult.class);
        Set set = new Set("setA", "b");
        
        when(resultMock.getResults()).thenReturn(Arrays.asList(new Set[]{ set }));
        when(resultMock.getTotalResults()).thenReturn(1);
        
        when(setRepository.supportSets()).thenReturn(true);
        when(setRepository.retrieveSets(anyInt(), anyInt())).thenReturn(resultMock);
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains("setA"));
    }

    @Test
    public void listMetadataFormatsShouldGiveOK() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("ListMetadataFormats");
        
        dataProvider.handle(params, out);
        
        //System.out.println(out.toString());
        
        assertTrue(out.toString().contains("xoai"));
    }

    @Test
    public void getRecordShouldGiveError() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException, IdDoesNotExistException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("GetRecord");
        when(params.getMetadataPrefix()).thenReturn("xoai");
        when(params.getIdentifier()).thenReturn("A");
        
        when(itemRepository.getItem(anyString())).thenThrow(IdDoesNotExistException.class);
        
        dataProvider.handle(params, out);
        
       //System.out.println(out.toString());
        
        assertTrue(out.toString().contains(OAIPMHerrorcodeType.ID_DOES_NOT_EXIST.value()));
    }
    @Test
    public void getRecordShouldGiveOk() throws OAIException, XMLStreamException, WrittingXmlException, IllegalVerbException, UnknownParameterException, DuplicateDefinitionException, IdDoesNotExistException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OAIRequestParameters params = mock(OAIRequestParameters.class);
        when(params.getVerb()).thenReturn("GetRecord");
        when(params.getMetadataPrefix()).thenReturn("xoai");
        when(params.getIdentifier()).thenReturn("A");
        

        AbstractItem item = mock(AbstractItem.class);
        when(item.getDatestamp()).thenReturn(new Date());
        when(item.getIdentifier()).thenReturn("Hello");
        when(item.getSets()).thenReturn(Arrays.asList(new ReferenceSet[]{ new ReferenceSet("test") }));
        when(item.isDeleted()).thenReturn(false);
        
        when(itemRepository.getItem(anyString())).thenReturn(item);
        
        dataProvider.handle(params, out);
        
        // System.out.println(out.toString());
        
        assertTrue(out.toString().contains("Hello"));
    }
}
