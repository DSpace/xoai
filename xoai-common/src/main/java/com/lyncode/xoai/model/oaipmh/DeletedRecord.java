package com.lyncode.xoai.model.oaipmh;

public enum DeletedRecord {

	NO("no"),
	PERSISTENT("persistent"),
	TRANSIENT("transient");

    public static DeletedRecord fromValue(String value) {
        for (DeletedRecord c : DeletedRecord.values()) {
            if (c.value.equals(value)) {
                return c;
            }
        }
        throw new IllegalArgumentException(value);
    }

	private final String value;

	DeletedRecord(String value) {
		this.value = value;
	}

	public String value() {
		return value;
	}


    @Override
    public String toString() {
        return value();
    }
}
