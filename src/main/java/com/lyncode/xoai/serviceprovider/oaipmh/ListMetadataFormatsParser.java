package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.ListMetadataFormatsType;

public class ListMetadataFormatsParser extends ElementParser {
	public static final String NAME = "ListMetadataFormats";

	private MetadataFormatParser mdfParser;
	
	public ListMetadataFormatsParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		mdfParser = new MetadataFormatParser(log, reader);
	}

	public ListMetadataFormatsType parse (boolean getNext) throws ParseException {
		ListMetadataFormatsType res = new ListMetadataFormatsType();
		super.checkStart(NAME, getNext);
		while (super.checkBooleanStart("metadataFormat", true)) {
			res.getMetadataFormat().add(mdfParser.parse(false));
		}
		super.checkEnd(NAME, false);
		return res;
	}
}
