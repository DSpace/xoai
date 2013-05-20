package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHerrorType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.OAIPMHerrorcodeType;

public class ErrorParser extends ElementParser {
	public static final String NAME = "error";

	public ErrorParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}

	public OAIPMHerrorType parse (boolean b) throws ParseException {
		OAIPMHerrorType error = new OAIPMHerrorType();
		super.checkStart(NAME, b);
		Map<String, String> attr = super.getAttributes();
		if (attr.containsKey("code"))
			error.setCode(OAIPMHerrorcodeType.fromValue(attr.get("code")));
		error.setValue(super.getString(true));
		super.checkEnd(NAME, true);
		return error;
	}
}
