package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.HeaderType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.StatusType;
import com.lyncode.xoai.serviceprovider.util.DateUtils;

public class HeaderParser extends ElementParser {
	public static final String NAME = "header";

	public HeaderParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}

	public HeaderType parse(boolean b) throws ParseException {
		HeaderType type = new HeaderType();
		super.checkStart(NAME, b);
		Map<String, String> attr = super.getAttributes();
		if (attr.containsKey("status"))
			type.setStatus(StatusType.fromValue(attr.get("status")));
		while (this.parseContents(type));
		super.checkEnd(NAME, false);
		return type;
	}

	private boolean parseContents(HeaderType type) throws ParseException {
		if (super.checkBooleanStart("identifier", true)) {
			type.setIdentifier(super.getString("identifier", false));
			return true;
		} else if (super.checkBooleanStart("datestamp", false)) {
			type.setDatestamp(DateUtils.parse(super.getString("datestamp", false)));
			return true;
		} else if (super.checkBooleanStart("setSpec", false)) {
			type.getSetSpec().add(super.getString("setSpec", false));
			return true;
		}
		return false;
	}
}
