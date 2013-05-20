package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.RecordType;

public class RecordParser extends ElementParser {
	public static final String NAME = "record";

	private HeaderParser headerParser;
	private MetadataParser metadataParser;
	private AboutParser aboutParser;
	
	public RecordParser(Logger log, XMLStreamReader reader, GenericParser parser) {
		super(log, reader);
		headerParser = new HeaderParser(log, reader);
		metadataParser = new MetadataParser(log, reader, parser);
		aboutParser = new AboutParser(log, reader);
	}

	public RecordParser(Logger log, XMLStreamReader reader,
			GenericParser metadata, GenericParser about) {
		super(log, reader);
		headerParser = new HeaderParser(log, reader);
		metadataParser = new MetadataParser(log, reader, metadata);
		aboutParser = new AboutParser(log, reader, about);
	}

	public RecordParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		headerParser = new HeaderParser(log, reader);
		metadataParser = new MetadataParser(log, reader);
		aboutParser = new AboutParser(log, reader);
	}

	public RecordType parse(boolean b) throws ParseException {
		RecordType rec = new RecordType();
		super.checkStart(NAME, b);
		while (this.parseContents(rec));
		super.checkEnd(NAME, false);
		return rec;
	}

	private boolean parseContents(RecordType rec) throws ParseException {
		if (super.checkBooleanStart("header", true)) {
			// Header
			rec.setHeader(headerParser.parse(false));
			return true;
		} else if (super.checkBooleanStart("metadata", false)) {
			// Metadata
			rec.setMetadata(metadataParser.parse(false));
			return true;
		} else if (super.checkBooleanStart("about", false)) {
			// About
			rec.getAbout().add(aboutParser.parse(false));
			return true;
		}
		return false;
	}
}
