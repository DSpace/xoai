package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListRecordsType;

public class ListRecordsParser extends ElementParser {
	public static final String NAME = "ListRecords";
	private ResumptionTokenParser resumptionTokenParser;
	private RecordParser recordParser;
	
	public ListRecordsParser(Logger log, XMLStreamReader reader, GenericParser parser) {
		super(log, reader);
		resumptionTokenParser = new ResumptionTokenParser(log, reader);
		recordParser = new RecordParser(log, reader, parser);
	}

	public ListRecordsParser(Logger log, XMLStreamReader reader,
			GenericParser metadata, GenericParser about) {
		super(log, reader);
		resumptionTokenParser = new ResumptionTokenParser(log, reader);
		recordParser = new RecordParser(log, reader, metadata, about);
	}

	public ListRecordsParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		resumptionTokenParser = new ResumptionTokenParser(log, reader);
		recordParser = new RecordParser(log, reader);
	}

	public ListRecordsType parse (boolean getNext) throws ParseException {
		ListRecordsType type = new ListRecordsType();
		
		super.checkStart(NAME, getNext);
		while (super.checkBooleanStart("record", true)) {
			type.getRecord().add(recordParser.parse(false));
		}
		if (super.checkBooleanStart("resumptionToken", false)) {
			type.setResumptionToken(resumptionTokenParser.parse(false));
			super.checkEnd(NAME, true);
		} else 
			super.checkEnd(NAME, false);
		return type;
	}
}
