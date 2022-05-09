/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.parsers;

import io.gdcc.xoai.model.oaipmh.Record;
import io.gdcc.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.serviceprovider.model.Context;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

import static io.gdcc.xoai.model.oaipmh.Error.Code.CANNOT_DISSEMINATE_FORMAT;
import static io.gdcc.xoai.model.oaipmh.Error.Code.ID_DOES_NOT_EXIST;
import static io.gdcc.xoai.xmlio.matchers.QNameMatchers.localPart;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.elementName;
import static org.hamcrest.CoreMatchers.equalTo;

public class GetRecordParser {

    private final XmlReader reader;
    private Context context;
    private String metadataPrefix;

    public GetRecordParser(InputStream stream, Context context, String metadataPrefix) {
        this.context = context;
        this.metadataPrefix = metadataPrefix;
        try {
            this.reader = new XmlReader(stream);
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }

    public Record parse () throws IdDoesNotExistException, CannotDisseminateFormatException {
        try {
            reader.next(errorElement(), recordElement());
            if (reader.current(errorElement())) {
                String code = reader.getAttributeValue(localPart(equalTo("code")));
                if (ID_DOES_NOT_EXIST.code().equals(code))
                    throw new IdDoesNotExistException();
                else if (CANNOT_DISSEMINATE_FORMAT.code().equals(code))
                    throw new CannotDisseminateFormatException();
                else
                    throw new InvalidOAIResponse("OAI responded with error code: "+code);
            } else {
                return new RecordParser(context, metadataPrefix).parse(reader);
            }
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }


    private Matcher<XMLEvent> errorElement() {
        return elementName(localPart(equalTo("error")));
    }

    private Matcher<XMLEvent> recordElement() {
        return elementName(localPart(equalTo("record")));
    }
}
