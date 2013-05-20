package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.GetRecordType;

public class GetRecordParser extends ElementParser {
	public static final String NAME = "GetRecord";
	
	private RecordParser recordParser;
	
	public GetRecordParser(Logger log, XMLStreamReader reader, GenericParser parser) {
		super(log, reader);
		recordParser = new RecordParser(log, reader, parser);
	}

	public GetRecordParser(Logger log, XMLStreamReader reader,
			GenericParser metadata, GenericParser about) {
		super(log, reader);
		recordParser = new RecordParser(log, reader, metadata, about);
	}

	public GetRecordParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		recordParser = new RecordParser(log, reader);
	}

	public GetRecordType parse (boolean getNext) throws ParseException {
		GetRecordType res = new GetRecordType();
		super.checkStart(NAME, getNext);
		res.setRecord(recordParser.parse(true));
		super.checkEnd(NAME, true);
		return res;
	}
}
