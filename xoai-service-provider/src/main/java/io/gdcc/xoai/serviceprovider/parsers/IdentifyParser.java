/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.serviceprovider.parsers;

import io.gdcc.xoai.model.oaipmh.DeletedRecord;
import io.gdcc.xoai.model.oaipmh.Description;
import io.gdcc.xoai.model.oaipmh.Granularity;
import io.gdcc.xoai.model.oaipmh.Identify;
import io.gdcc.xoai.serviceprovider.exceptions.InvalidOAIResponse;
import io.gdcc.xoai.xmlio.XmlReader;
import io.gdcc.xoai.xmlio.exceptions.XmlReaderException;

import java.io.InputStream;

import static io.gdcc.xoai.serviceprovider.xml.IslandParsers.dateParser;
import static io.gdcc.xoai.xmlio.matchers.QNameMatchers.localPart;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.aStartElement;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.elementName;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.text;
import static io.gdcc.xoai.xmlio.matchers.XmlEventMatchers.theEndOfDocument;
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
