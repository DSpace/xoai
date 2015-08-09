/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.parsers;

import com.lyncode.xml.XmlReader;
import com.lyncode.xml.exceptions.XmlReaderException;
import org.dspace.xoai.model.oaipmh.DeletedRecord;
import org.dspace.xoai.model.oaipmh.Description;
import org.dspace.xoai.model.oaipmh.Granularity;
import org.dspace.xoai.model.oaipmh.Identify;
import org.dspace.xoai.serviceprovider.exceptions.InvalidOAIResponse;

import java.io.InputStream;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.dspace.xoai.serviceprovider.xml.IslandParsers.dateParser;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class IdentifyParser {
    private final XmlReader reader;

    public IdentifyParser(InputStream stream) {
        try {
            this.reader = new XmlReader(stream);
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }

    @SuppressWarnings("unchecked")
	public Identify parse () {
        try {
            Identify identify = new Identify();
            reader.next(allOf(aStartElement(), elementName(localPart(equalTo("Identify")))));
            reader.next(elementName(localPart(equalTo("repositoryName"))));
            identify.withRepositoryName(reader.next(text()).getText());
            reader.next(elementName(localPart(equalTo("baseURL"))));
            identify.withBaseURL(reader.next(text()).getText());
            reader.next(elementName(localPart(equalTo("protocolVersion"))));
            identify.withProtocolVersion(reader.next(text()).getText());
            reader.next(elementName(localPart(equalTo("adminEmail")))).next(text());
            identify.withAdminEmail(reader.getText());
            while (reader.next(aStartElement()).current(elementName(localPart(equalTo("adminEmail")))))
                identify.withAdminEmail(reader.next(text()).getText());
            identify.withEarliestDatestamp(reader.next(text()).get(dateParser()));
            reader.next(elementName(localPart(equalTo("deletedRecord")))).next(text());
            identify.withDeletedRecord(DeletedRecord.fromValue(reader.getText()));
            reader.next(elementName(localPart(equalTo("granularity")))).next(text());
            identify.withGranularity(Granularity.fromRepresentation(reader.getText()));
            
            while (reader.next(aStartElement(), theEndOfDocument()).current(elementName(localPart(equalTo("compression")))))
                identify.withCompression(reader.next(text()).getText());
            if(reader.current(theEndOfDocument())) {
            	return identify;
            } else if (reader.current(elementName(localPart(equalTo("description"))))) {
            	identify.withDescription(reader.get(descriptionParser()));
			}
            
            while (reader.next(aStartElement(), theEndOfDocument()).current(elementName(localPart(equalTo("description")))))
            	identify.withDescription(reader.get(descriptionParser()));
            
            return identify;
        } catch (XmlReaderException e) {
            throw new InvalidOAIResponse(e);
        }
    }

    private XmlReader.IslandParser<Description> descriptionParser() {
        return new XmlReader.IslandParser<Description>() {
            @Override
            public Description parse(XmlReader reader) throws XmlReaderException {
                return new Description(reader.retrieveCurrentAsString());
            }
        };
    }
}
