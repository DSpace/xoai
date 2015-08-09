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
import org.dspace.xoai.model.oaipmh.About;
import org.dspace.xoai.model.oaipmh.Metadata;
import org.dspace.xoai.model.oaipmh.Record;
import org.dspace.xoai.serviceprovider.exceptions.InternalHarvestException;
import org.dspace.xoai.serviceprovider.model.Context;
import org.dspace.xoai.xml.XSLPipeline;
import org.hamcrest.Matcher;

import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.TransformerException;
import java.io.ByteArrayInputStream;

import static com.lyncode.xml.matchers.QNameMatchers.localPart;
import static com.lyncode.xml.matchers.XmlEventMatchers.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;

public class RecordParser {
    private final Context context;
    private String metadataPrefix;

    public RecordParser(Context context, String metadataPrefix) {
        this.context = context;
        this.metadataPrefix = metadataPrefix;
    }

    public Record parse (XmlReader reader) throws XmlReaderException {
        HeaderParser headerParser = new HeaderParser();

        reader.next(elementName(localPart(equalTo("header"))));
        Record record = new Record()
                .withHeader(headerParser.parse(reader));


        if (!record.getHeader().isDeleted()) {
            reader.next(elementName(localPart(equalTo("metadata")))).next(aStartElement());
            String content = reader.retrieveCurrentAsString();
            ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
            XSLPipeline pipeline = new XSLPipeline(inputStream, true)
                    .apply(context.getMetadataTransformer(metadataPrefix));
            if (context.hasTransformer())
                pipeline.apply(context.getTransformer());
            try {
                record.withMetadata(new Metadata(new MetadataParser().parse(pipeline.process())));
            } catch (TransformerException e) {
                throw new InternalHarvestException("Unable to process transformer");
            }
        }

        if (reader.next(aboutElement(), endOfRecord()).current(aboutElement())) {
            reader.next(aStartElement());
            record.withAbout(new About(reader.retrieveCurrentAsString()));
        }

        return record;
    }

    private Matcher<XMLEvent> endOfRecord() {
        return allOf(anEndElement(), elementName(localPart(equalTo("record"))));
    }

    private Matcher<XMLEvent> aboutElement() {
        return elementName(localPart(equalTo("about")));
    }
}
