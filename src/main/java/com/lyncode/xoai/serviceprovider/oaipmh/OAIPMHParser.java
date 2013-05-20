package com.lyncode.xoai.serviceprovider.oaipmh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHtype;

public class OAIPMHParser extends ElementParser {
	private static final String NAME = "OAI-PMH";
	private static final String RESPONSE_DATE = "responseDate";
	
	public static void main (String... args) throws FileNotFoundException, XMLStreamException, ParseException {
		BasicConfigurator.configure();
		Logger log = LogManager.getLogger(OAIPMHParser.class);
		OAIPMHParser p = OAIPMHParser.newInstance("test/test4.xml", log);
		p.parse();
	}

	public static OAIPMHParser newInstance (String filepath, Logger log) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(filepath));
		return new OAIPMHParser(log, stream);
	}
	public static OAIPMHParser newInstance (File f, Logger log) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(f));
		return new OAIPMHParser(log, stream);
	}
	public static OAIPMHParser newInstance (InputStream is, Logger log) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(is);
		return new OAIPMHParser(log, stream);
	}

	public static OAIPMHParser newInstance (String filepath, GenericParser parser) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(filepath));
		return new OAIPMHParser(parser.getLogger(), stream, parser);
	}
	public static OAIPMHParser newInstance (File f, GenericParser parser) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(f));
		return new OAIPMHParser(parser.getLogger(), stream, parser);
	}
	public static OAIPMHParser newInstance (InputStream is, GenericParser parser) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(is);
		return new OAIPMHParser(parser.getLogger(), stream, parser);
	}

	public static OAIPMHParser newInstance (String filepath, GenericParser parser, GenericParser description) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(filepath));
		return new OAIPMHParser(parser.getLogger(), stream, parser, description);
	}
	public static OAIPMHParser newInstance (File f, GenericParser parser, GenericParser description) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(f));
		return new OAIPMHParser(parser.getLogger(), stream, parser, description);
	}
	public static OAIPMHParser newInstance (InputStream is, GenericParser parser, GenericParser description) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(is);
		return new OAIPMHParser(parser.getLogger(), stream, parser, description);
	}

	public static OAIPMHParser newInstance (String filepath, GenericParser parser, GenericParser description, GenericParser about) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(filepath));
		return new OAIPMHParser(parser.getLogger(), stream, parser, description);
	}
	public static OAIPMHParser newInstance (File f, GenericParser parser, GenericParser description, GenericParser about) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(new FileInputStream(f));
		return new OAIPMHParser(parser.getLogger(), stream, parser, description, about);
	}
	public static OAIPMHParser newInstance (InputStream is, GenericParser parser, GenericParser description, GenericParser about) throws FileNotFoundException, XMLStreamException {
		XMLInputFactory factory = XMLInputFactory2.newInstance();
		XMLStreamReader stream = factory.createXMLStreamReader(is);
		return new OAIPMHParser(parser.getLogger(), stream, parser, description, about);
	}
	
	private RequestParser reqParser;
	private ListRecordsParser listRecordsParser;
	private GetRecordParser getRecordParser;
	private ListIdentifiersParser listIdentifiersParser;
	private ListSetsParser listSetsParser;
	private ListMetadataFormatsParser listMetadataFormatsParser;
	private IdentifyParser identifyParser;
	private ErrorParser errorParser;
	

	private OAIPMHParser(Logger log, XMLStreamReader reader, GenericParser metadata, GenericParser description, GenericParser about) {
		super(log, reader);
		reqParser = new RequestParser(log, reader);
		listRecordsParser = new ListRecordsParser(log, reader, metadata, about);
		getRecordParser = new GetRecordParser(log, reader, metadata, about);
		listIdentifiersParser = new ListIdentifiersParser(log, reader);
		listSetsParser = new ListSetsParser(log, reader);
		listMetadataFormatsParser = new ListMetadataFormatsParser(log, reader);
		identifyParser = new IdentifyParser(log, reader, description);
		errorParser = new ErrorParser(log, reader);
	}

	private OAIPMHParser(Logger log, XMLStreamReader reader, GenericParser metadata, GenericParser description) {
		super(log, reader);
		reqParser = new RequestParser(log, reader);
		listRecordsParser = new ListRecordsParser(log, reader, metadata);
		getRecordParser = new GetRecordParser(log, reader, metadata);
		listIdentifiersParser = new ListIdentifiersParser(log, reader);
		listSetsParser = new ListSetsParser(log, reader);
		listMetadataFormatsParser = new ListMetadataFormatsParser(log, reader);
		identifyParser = new IdentifyParser(log, reader, description);
		errorParser = new ErrorParser(log, reader);
	}
	
	private OAIPMHParser(Logger log, XMLStreamReader reader, GenericParser metadata) {
		super(log, reader);
		reqParser = new RequestParser(log, reader);
		listRecordsParser = new ListRecordsParser(log, reader, metadata);
		getRecordParser = new GetRecordParser(log, reader, metadata);
		listIdentifiersParser = new ListIdentifiersParser(log, reader);
		listSetsParser = new ListSetsParser(log, reader);
		listMetadataFormatsParser = new ListMetadataFormatsParser(log, reader);
		identifyParser = new IdentifyParser(log, reader);
		errorParser = new ErrorParser(log, reader);
	}
	
	private OAIPMHParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		reqParser = new RequestParser(log, reader);
		listRecordsParser = new ListRecordsParser(log, reader);
		getRecordParser = new GetRecordParser(log, reader);
		listIdentifiersParser = new ListIdentifiersParser(log, reader);
		listSetsParser = new ListSetsParser(log, reader);
		listMetadataFormatsParser = new ListMetadataFormatsParser(log, reader);
		identifyParser = new IdentifyParser(log, reader);
		errorParser = new ErrorParser(log, reader);
	}
	
	public OAIPMHtype parse () throws ParseException {
		super.checkStart(NAME, true);
		// Do something in between
		OAIPMHtype pmh = new OAIPMHtype();
		pmh.setResponseDate(super.getString(RESPONSE_DATE, true));
		pmh.setRequest(reqParser.parse(true));
		
		boolean isChecked = true;
		
		if (super.checkBooleanStart("ListRecords", true)) {
			pmh.setListRecords(listRecordsParser.parse(false));
		} else if (super.checkBooleanStart("GetRecord", false)) {
			pmh.setGetRecord(getRecordParser.parse(false));
		} else if (super.checkBooleanStart("ListIdentifiers", false)) {
			pmh.setListIdentifiers(listIdentifiersParser.parse(false));
		} else if (super.checkBooleanStart("ListSets", false)) {
			pmh.setListSets(listSetsParser.parse(false));
		} else if (super.checkBooleanStart("ListMetadataFormats", false)) {
			pmh.setListMetadataFormats(listMetadataFormatsParser.parse(false));
		} else if (super.checkBooleanStart("Identify", false)) {
			pmh.setIdentify(identifyParser.parse(false));
		} else if (super.checkBooleanStart("error", false)) {
			pmh.getError().add(errorParser.parse(false));
			while (super.checkBooleanStart("error", true))
				pmh.getError().add(errorParser.parse(false));
			isChecked = false;
		} else {
			throw new KnownParseException("Unexpected element: "+super.getReader().getLocalName()+" expecting some of [ListRecords, GetRecord, ListIdentifiers, ListSets, ListMetadataFormats, Identify, error]");
		}
		
		super.checkEnd(NAME, isChecked);
		return pmh;
	}
}
