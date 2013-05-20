package com.lyncode.xoai.serviceprovider.oaipmh;

public enum XMLType {
	UNKNOWN(0),
	START_ELEMENT(1),
	END_ELEMENT(2),
	PROCESSING_INSTRUCTION(3),
	CHARACTERS(4),
	COMMENT(5),
	SPACE(6),
	START_DOCUMENT(7),
	END_DOCUMENT(8),
	ENTITY_REFERENCE(9),
	ATTRIBUTE(10),
	DTD(11),
	CDATA(12),
	NAMESPACE(13),
	NOTATION_DECLARATION(14),
	ENTITY_DECLARATION(15)
	;
	
	private int id;
	
	XMLType (int n) {
		this.id = n;
	}
	
	public int getID () {
		return this.id;
	}
	
	public static XMLType fromID (int id) {
		for (XMLType t : XMLType.values()) {
			if (t.id == id) return t;
		}
		return UNKNOWN;
	}
}