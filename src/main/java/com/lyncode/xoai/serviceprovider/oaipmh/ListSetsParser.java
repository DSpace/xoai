package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListSetsType;

public class ListSetsParser extends ElementParser {
	public static final String NAME = "ListSets";

	private SetParser setParser;
	private ResumptionTokenParser resParser;
	
	public ListSetsParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		
		setParser = new SetParser(log, reader);
		resParser = new ResumptionTokenParser(log, reader);
	}

	public ListSetsType parse (boolean getNext) throws ParseException {
		ListSetsType sets = new ListSetsType();
		super.checkStart(NAME, getNext);
		while (super.checkBooleanStart("set", true)) {
			sets.getSet().add(setParser.parse(false));
		}
		if (super.checkBooleanStart("resumptionToken", false)) {
			sets.setResumptionToken(resParser.parse(false));
			super.checkEnd(NAME, true);
		} else {
			super.checkEnd(NAME, false);
		}
		return sets;
	}
}
