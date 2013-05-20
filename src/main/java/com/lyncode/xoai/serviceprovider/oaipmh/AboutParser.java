package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.AboutType;

public class AboutParser extends ElementParser {
	public static final String NAME = "about";
	
	private GenericParser parser;

	public AboutParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		parser = null;
	}

	public AboutParser(Logger log, XMLStreamReader reader, GenericParser about) {
		super(log, reader);
		parser = about;
	}

	public AboutType parse(boolean b) throws ParseException {
		AboutType abt = new AboutType();
		super.checkStart(NAME, b);
		if (parser != null) {
			abt.setAny(parser.parse(getReader()));
			super.checkEnd(NAME, true);
		}
		else {
			StringParser np = new StringParser(getLogger(), getReader());
			String val = np.parse(true);
			abt.setAny(val);
			super.checkEnd(NAME, val != null);
		}
		return abt;
	}
}
