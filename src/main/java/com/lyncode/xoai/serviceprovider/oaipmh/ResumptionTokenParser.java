package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.ResumptionTokenType;
import com.lyncode.xoai.serviceprovider.util.DateUtils;

public class ResumptionTokenParser extends ElementParser {
	public static final String NAME = "resumptionToken";

	public ResumptionTokenParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}

	public ResumptionTokenType parse(boolean b) throws ParseException {
		ResumptionTokenType res = new ResumptionTokenType();
		super.checkStart(NAME, b);
		Map<String, String> attributes = super.getAttributes();
		if (attributes.containsKey("cursor"))
			res.setCursor(Long.parseLong(attributes.get("cursor")));
		
		if (attributes.containsKey("completeListSize"))
			res.setCompleteListSize(Long.parseLong(attributes.get("completeListSize")));
		
		if (attributes.containsKey("expirationDate"))
			res.setExpirationDate(DateUtils.parse(attributes.get("expirationDate")));
		
		res.setValue(super.getString(true));
		
		super.checkEnd(NAME, true);
		return res;
	}
}
