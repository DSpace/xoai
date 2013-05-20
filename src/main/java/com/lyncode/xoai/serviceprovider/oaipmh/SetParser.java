package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.SetType;

public class SetParser extends ElementParser {
	public static final String NAME = "set";

	public SetParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}

	public SetType parse (boolean b) throws ParseException {
		SetType error = new SetType();
		super.checkStart(NAME, b);
		error.setSetSpec(super.getString("setSpec", true));
		error.setSetName(super.getString("setName", true));
		super.checkEnd(NAME, true);
		return error;
	}
}
