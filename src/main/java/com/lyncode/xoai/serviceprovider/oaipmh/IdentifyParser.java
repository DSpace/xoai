package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

import com.lyncode.xoai.serviceprovider.oaipmh.spec.DeletedRecordType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.GranularityType;
import com.lyncode.xoai.serviceprovider.oaipmh.spec.IdentifyType;
import com.lyncode.xoai.serviceprovider.util.DateUtils;

public class IdentifyParser extends ElementParser {
	public static final String NAME = "Identify";
	private DescriptionParser parser;

	public IdentifyParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
		parser = new DescriptionParser(log, reader);
	}
	public IdentifyParser(Logger log, XMLStreamReader reader, GenericParser parse) {
		super(log, reader);
		parser = new DescriptionParser(log, reader, parse);
	}

	public IdentifyType parse (boolean getNext) throws ParseException {
		IdentifyType type = new IdentifyType();
		super.checkStart(NAME, getNext);
		type.setRepositoryName(super.getString("repositoryName", true));
		type.setBaseURL(super.getString("baseURL", true));
		type.setProtocolVersion(super.getString("protocolVersion", true));
		while (super.checkBooleanStart("adminEmail", true))
			type.getAdminEmail().add(super.getString("adminEmail", false));
		type.setEarliestDatestamp(DateUtils.parse(super.getString("earliestDatestamp", false)));
		type.setDeletedRecord(DeletedRecordType.fromValue(super.getString("deletedRecord", true)));
		type.setGranularity(GranularityType.fromValue(super.getString("granularity", true)));
		while (super.checkBooleanStart("compression", true))
			type.getAdminEmail().add(super.getString("compression", false));
		if (super.checkBooleanStart("description", false)) {
			type.getDescription().add(parser.parse(false));
			while (super.checkBooleanStart("description", true)) {
				type.getDescription().add(parser.parse(false));
			}
		}
		super.checkEnd(NAME, false);
		return type;
	}
}
