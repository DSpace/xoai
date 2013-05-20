package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListIdentifiersType;

public class ListIdentifiersParser extends ElementParser {
	public static final String NAME = "ListIdentifiers";

	private HeaderParser parser;
	private ResumptionTokenParser resParser;
	
	public ListIdentifiersParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		parser = new HeaderParser(log, reader);
		resParser = new ResumptionTokenParser(log, reader);
	}

	public ListIdentifiersType parse (boolean getNext) throws ParseException {
		ListIdentifiersType res = new ListIdentifiersType();
		super.checkStart(NAME, getNext);
		while (super.checkBooleanStart("header", true)) {
			res.getHeader().add(parser.parse(false));
		}
		if (super.checkBooleanStart("resumptionToken", false)) {
			res.setResumptionToken(resParser.parse(false));
			super.checkEnd(NAME, true);
		} else {
			super.checkEnd(NAME, false);
		}
		return res;
	}
}
