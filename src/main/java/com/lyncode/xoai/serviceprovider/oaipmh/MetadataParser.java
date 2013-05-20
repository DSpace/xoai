package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.MetadataType;

public class MetadataParser extends ElementParser {
	private static final String NAME = "metadata";
	private GenericParser parser;

	public MetadataParser(Logger log, XMLStreamReader reader, GenericParser parser) {
		super(log, reader);
		this.parser = parser;
	}

	public MetadataParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		this.parser = null;
	}

	public MetadataType parse(boolean b) throws ParseException {
		MetadataType type = new MetadataType();
		super.checkStart(NAME, b);
		if (parser != null) {
			type.setAny(parser.parse(getReader()));
			super.checkEnd(NAME, true);
		} else {
			StringParser np = new StringParser(getLogger(), getReader());
			String val = np.parse(true);
			type.setAny(val);
			super.checkEnd(NAME, val != null);
		}
		return type;
	}
}
