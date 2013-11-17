package com.lyncode.xoai.tests.dataprovider.acceptance;

import com.lyncode.xoai.builders.ListBuilder;
import com.lyncode.xoai.builders.MapBuilder;
import com.lyncode.xoai.builders.dataprovider.OAIRequestParametersBuilder;
import com.lyncode.xoai.dataprovider.OAIDataProvider;
import com.lyncode.xoai.dataprovider.core.Granularity;
import com.lyncode.xoai.dataprovider.core.XOAIManager;
import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.dataprovider.services.api.DateProvider;
import com.lyncode.xoai.dataprovider.services.api.FilterResolver;
import com.lyncode.xoai.dataprovider.services.api.ResourceResolver;
import com.lyncode.xoai.dataprovider.services.impl.BaseDateProvider;
import com.lyncode.xoai.dataprovider.xml.XmlOutputContext;
import com.lyncode.xoai.dataprovider.xml.oaipmh.OAIPMH;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.ContextConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FilterConfiguration;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.FormatConfiguration;
import com.lyncode.xoai.tests.helpers.AbstractIdentifyBuilder;
import com.lyncode.xoai.tests.helpers.stubs.StubbedItemRepository;
import com.lyncode.xoai.tests.helpers.stubs.StubbedSetRepository;
import com.lyncode.xoai.util.matchers.XPathMatchers;
import org.codehaus.stax2.XMLOutputFactory2;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;
import org.hamcrest.Matcher;
import org.junit.Before;

import javax.xml.stream.XMLStreamException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractDataProviderTest {
    private static final String CONTEXT = "request";
    private static final String XOAI_FORMAT = "xoai";
    private static final String XOAI_NAMESPACE = "http://www.lyncode.com/xoai";
    private static final String XOAI_PREFIX = "xoai";
    private static final String XOAI_SCHEMA_LOCATION = "schemaLocation";
    private static final String XOAI_XSLT_LOCATION = "xoai-schema-location";

    private static TransformerFactory tFactory = TransformerFactory.newInstance();


    private OAIDataProvider dataProvider;
    private XOAIManager manager;
    private FilterResolver filterResolver = mock(FilterResolver.class);
    private AbstractIdentifyBuilder identify = new AbstractIdentifyBuilder();
    private StubbedSetRepository setRepository = new StubbedSetRepository();
    private StubbedItemRepository itemRepository = new StubbedItemRepository();
    private ResourceResolver resourceResolver = mock(ResourceResolver.class);
    private Configuration configuration;
    private DateProvider formatter = new BaseDateProvider();

    private OAIPMH result;

    @Before
    public void setUp() throws IOException, TransformerConfigurationException, ParseException {
        when(resourceResolver.getTransformer(XOAI_XSLT_LOCATION)).thenReturn(tFactory.newTransformer());

        configuration = new Configuration().withIndented(true);

        configuration.withFormatConfigurations(new FormatConfiguration(XOAI_FORMAT)
                .withNamespace(XOAI_NAMESPACE)
                .withPrefix(XOAI_PREFIX)
                .withSchemaLocation(XOAI_SCHEMA_LOCATION)
                .withXslt(XOAI_XSLT_LOCATION));

        configuration.withContextConfigurations(new ContextConfiguration(CONTEXT)
                .withFormats(XOAI_FORMAT));
    }

    protected void afterHandling(OAIRequestParametersBuilder params) throws InvalidContextException, ConfigurationException, OAIException, XMLStreamException, WritingXmlException, IOException {
        if (dataProvider == null)
            dataProvider = new OAIDataProvider(theManager(), CONTEXT, identify.build(), setRepository, itemRepository);
        result = dataProvider.handle(params.build());
    }

    protected String theResult() throws XMLStreamException, WritingXmlException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        XmlOutputContext context = new XmlOutputContext(this.formatter, XMLOutputFactory2.newFactory().createXMLStreamWriter(output), identify.getGranularity());
        result.write(context);
        context.getWriter().close();
        return output.toString();
    }

    protected Configuration theConfiguration() {
        return this.configuration;
    }

    protected Date theDateIs(Date date) {
        BaseDateProvider resource = new BaseDateProvider();
        formatter = mock(DateProvider.class);
        when(formatter.now()).thenReturn(date);
        when(formatter.format(any(Date.class), any(Granularity.class))).thenReturn(resource.format(date));
        OAIDataProvider.setDateFormatter(formatter);
        return date;
    }

    protected FilterConfiguration aFilter(String id) {
        return new FilterConfiguration(id);
    }

    protected XOAIManager theManager() throws ConfigurationException {
        if (manager == null)
            manager = new XOAIManager(filterResolver, resourceResolver, configuration);
        return manager;
    }

    protected String theFormatPrefix() {
        return XOAI_PREFIX;
    }

    protected AbstractIdentifyBuilder theRepositoryIsConfiguredto() {
        return identify;
    }

    protected StubbedSetRepository theSetRepository() {
        return setRepository;
    }

    protected StubbedItemRepository theItemRepository() {
        return itemRepository;
    }

    protected Matcher<? super String> hasXPath(String s) {
        return XPathMatchers.hasXPath(s, new MapBuilder<String, String>()
                .withPair("o", "http://www.openarchives.org/OAI/2.0/")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    protected <T> Matcher<? super String> xPath(String s, Matcher<T> val) {
        return XPathMatchers.xPath(s, val, new MapBuilder<String, String>()
                .withPair("o", "http://www.openarchives.org/OAI/2.0/")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance"));
    }

    protected List<String> singletonList(String single) {
        return new ListBuilder<String>().add(single).build();
    }

    protected OAIRequestParametersBuilder aRequest() {
        return new OAIRequestParametersBuilder();
    }

    protected String missing() {
        return null;
    }

    protected String getXPath(String s) throws WritingXmlException, XMLStreamException, IOException, DocumentException {
        Document document = DocumentHelper.parseText(theResult());
        XPath xPath = document.createXPath(s);
        xPath.setNamespaceURIs(new MapBuilder<String, String>()
                .withPair("o", "http://www.openarchives.org/OAI/2.0/")
                .withPair("xsi", "http://www.w3.org/2001/XMLSchema-instance").build());
        return xPath.valueOf(document);
    }
}
