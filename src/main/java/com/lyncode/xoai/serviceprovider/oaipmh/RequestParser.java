package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.RequestType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.VerbType;
import com.lyncode.xoai.util.DateUtils;

public class RequestParser extends ElementParser {
	public static final String NAME = "request";

	public RequestParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}

	public RequestType parse (boolean getNext) throws ParseException {
		RequestType reqType = new RequestType();
		super.checkStart(NAME, getNext);
		Map<String, String> str = super.getAttributes();
		if (str.containsKey("from"))
			reqType.setFrom(DateUtils.parse(str.get("from")));
		reqType.setIdentifier(str.get("identifier"));
		reqType.setMetadataPrefix(str.get("metadataPrefix"));
		reqType.setResumptionToken(str.get("resumptionToken"));
		reqType.setSet(str.get("set"));
		if (str.containsKey("until"))
			reqType.setUntil(DateUtils.parse(str.get("until")));
		reqType.setVerb(VerbType.fromValue(str.get("verb")));
		reqType.setValue(super.getString(true));
		super.checkEnd(NAME, true);
		return reqType;
	}
}
