package com.lyncode.xoai.serviceprovider.oaipmh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.codehaus.stax2.XMLInputFactory2;

import com.lyncode.xoai.serviceprovider.OAIServiceConfiguration;
import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;
import com.lyncode.xoai.serviceprovider.parser.AboutItemParser;
import com.lyncode.xoai.serviceprovider.parser.AboutSetParser;
import com.lyncode.xoai.serviceprovider.parser.DescriptionParser;
import com.lyncode.xoai.serviceprovider.parser.MetadataParser;
import com.lyncode.xoai.util.DateUtils;

public class OAIPMHParser extends ElementParser<OAIPMHtype> {
	private static final String NAME = "OAI-PMH";
	private static final String RESPONSE_DATE = "responseDate";
	private static XMLInputFactory factory = XMLInputFactory2.newInstance();

	private static void untilFirstStartElement (XMLEventReader reader) throws XMLStreamException {
	    while (reader.peek() != null && !reader.peek().isStartElement() && !reader.peek().isEndDocument())
	        reader.nextEvent();
	}

	public static OAIPMHtype parse (InputStream instream, OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) throws ParseException {
        XMLEventReader stream;
        try {
            stream = factory.createXMLEventReader(instream);
            untilFirstStartElement(stream);
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
        OAIPMHParser parser = new OAIPMHParser(oaiServiceConfiguration);
        return parser.parse(stream);
    }
    public static OAIPMHtype parse (File f, OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) throws FileNotFoundException, ParseException {
        try {
            XMLEventReader stream = factory.createXMLEventReader(new FileInputStream(f));
            untilFirstStartElement(stream);
            OAIPMHParser parser =  new OAIPMHParser(oaiServiceConfiguration);
            return parser.parse(stream);
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
    }

    public static OAIPMHtype parse (String filepath, OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) throws FileNotFoundException, XMLStreamException, ParseException {
        try {
            XMLEventReader stream = factory.createXMLEventReader(new FileInputStream(filepath));
            untilFirstStartElement(stream);
            OAIPMHParser parser =  new OAIPMHParser(oaiServiceConfiguration);
            return parser.parse(stream);
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
    }
	
	private RequestParser reqParser;
	private ListRecordsParser listRecordsParser;
	private GetRecordParser getRecordParser;
	private ListIdentifiersParser listIdentifiersParser;
	private ListSetsParser listSetsParser;
	private ListMetadataFormatsParser listMetadataFormatsParser;
	private IdentifyParser identifyParser;
	private ErrorParser errorParser;
	
	
	public OAIPMHParser(OAIServiceConfiguration<MetadataParser, AboutItemParser, DescriptionParser, AboutSetParser> oaiServiceConfiguration) {
		super(oaiServiceConfiguration);
		reqParser = new RequestParser(oaiServiceConfiguration);
		listRecordsParser = new ListRecordsParser(oaiServiceConfiguration);
		getRecordParser = new GetRecordParser(oaiServiceConfiguration);
		listIdentifiersParser = new ListIdentifiersParser(oaiServiceConfiguration);
		listSetsParser = new ListSetsParser(oaiServiceConfiguration);
		listMetadataFormatsParser = new ListMetadataFormatsParser(oaiServiceConfiguration);
		identifyParser = new IdentifyParser(oaiServiceConfiguration);
		errorParser = new ErrorParser(oaiServiceConfiguration);
	}

    @Override
    protected OAIPMHtype parseElement(XMLEventReader reader) throws ParseException {
        OAIPMHtype pmh = new OAIPMHtype();
        
        try {
            if (!reader.peek().asStartElement().getName().getLocalPart().equals(NAME))
                throw new ParseException("Expecting "+NAME+" element");
            
            reader.nextEvent();
            this.nextElement(reader);
            
            pmh.setResponseDate(DateUtils.parse(this.getElement(reader, RESPONSE_DATE)));
            pmh.setRequest(reqParser.parse(reader));
            
            String name = reader.peek().asStartElement().getName().getLocalPart();
            
            if (name.equals("ListRecords"))
                pmh.setListRecords(listRecordsParser.parse(reader));
            else if (name.equals("GetRecord"))
                pmh.setGetRecord(getRecordParser.parse(reader));
            else if (name.equals("ListIdentifiers"))
                pmh.setListIdentifiers(listIdentifiersParser.parse(reader));
            else if (name.equals("ListSets"))
                pmh.setListSets(listSetsParser.parse(reader));
            else if (name.equals("ListMetadataFormats"))
                pmh.setListMetadataFormats(listMetadataFormatsParser.parse(reader));
            else if (name.equals("Identify"))
                pmh.setIdentify(identifyParser.parse(reader));
            else {
                while (reader.peek().asStartElement().getName().getLocalPart().equals("error")) {
                    pmh.getError().add(errorParser.parse(reader));
                }
            }
            
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        } catch (java.text.ParseException e) {
            throw new ParseException(e);
        }
        
        return pmh;
    }
}
