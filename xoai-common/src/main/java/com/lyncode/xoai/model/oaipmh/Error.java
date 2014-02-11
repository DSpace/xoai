package com.lyncode.xoai.model.oaipmh;

import com.lyncode.xml.exceptions.XmlWriteException;
import com.lyncode.xoai.xml.XmlWriter;
import com.lyncode.xoai.xml.XmlWritable;

import javax.xml.stream.XMLStreamException;

public class Error implements XmlWritable {
    private final String value;
    private Code code;

    public Error (String message) {
        this.value = message;
    }

    public String getMessage() {
        return value;
    }

    public Code getCode() {
        return code;
    }

    public Error withCode(Code value) {
        this.code = value;
        return this;
    }

    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        try {
            if (this.code != null)
                writer.writeAttribute("code", this.code.toString());

            writer.writeCharacters(value);
        } catch (XMLStreamException e) {
            throw new XmlWriteException(e);
        }
    }

    public static enum Code {

        CANNOT_DISSEMINATE_FORMAT("cannotDisseminateFormat"),
        ID_DOES_NOT_EXIST("idDoesNotExist"),
        BAD_ARGUMENT("badArgument"),
        BAD_VERB("badVerb"),
        NO_METADATA_FORMATS("noMetadataFormats"),
        NO_RECORDS_MATCH("noRecordsMatch"),
        BAD_RESUMPTION_TOKEN("badResumptionToken"),
        NO_SET_HIERARCHY("noSetHierarchy");

        private final String code;

        Code(String code) {
            this.code = code;
        }

        public String code() {
            return code;
        }



        public static Code fromCode(String code) {
            for (Code c : Code.values()) {
                if (c.code.equals(code)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(code);
        }

        @Override
        public String toString() {
            return code;
        }
    }
}
