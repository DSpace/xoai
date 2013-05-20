package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataFormatType;

public class MetadataFormatParser extends ElementParser {
	public static final String NAME = "metadataFormat";

	public MetadataFormatParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}

	public MetadataFormatType parse (boolean b) throws ParseException {
		MetadataFormatType error = new MetadataFormatType();
		super.checkStart(NAME, b);
		error.setMetadataPrefix(super.getString("metadataPrefix", true));
		error.setSchema(super.getString("schema", true));
		error.setMetadataNamespace(super.getString("metadataNamespace", true));
		super.checkEnd(NAME, true);
		return error;
	}
}
