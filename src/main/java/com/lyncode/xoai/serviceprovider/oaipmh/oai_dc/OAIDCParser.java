package com.lyncode.xoai.serviceprovider.oaipmh.oai_dc;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.GenericParser;
import com.lyncode.xoai.serviceprovider.oaipmh.KnownParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.ParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.UnknownParseException;
import com.lyncode.xoai.serviceprovider.oaipmh.XMLType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.schemas.oai_dc.OAIDC;

public class OAIDCParser extends GenericParser {

	public OAIDCParser(Logger log) {
		super(log);
	}

	@Override
	public Object parse(XMLStreamReader reader) throws ParseException {
		OAIDC oaidc = new OAIDC();
		super.checkStart(reader, "dc", true);
		while (!this.stop(reader)) {
			oaidc.add(reader.getLocalName(), super.getString(reader, reader.getLocalName(), false));
		}
		super.checkEnd(reader, "dc", false);
		return oaidc;
	}

	private boolean stop(XMLStreamReader reader) throws ParseException {
		try {
			if (!reader.hasNext()) 
				throw new KnownParseException("Expecting an element (start or end) but none appeared");
			int type = reader.nextTag();
			if (type == XMLType.START_ELEMENT.getID()) return false;
			else if (type == XMLType.END_ELEMENT.getID()) return true;
			else throw new KnownParseException("Expecting an element (start or end) but another one appeared ("+XMLType.fromID(type).name()+")");
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
	}
}
