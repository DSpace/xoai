/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.xml;

import io.gdcc.xoai.xmlio.exceptions.XmlWriteException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create a {@link XmlWritable} element that reads from the given {@link InputStream} at the very last moment:
 * when the XML writer is asked to write the content of the element. The input is copied and an XML declaration
 * is removed if present.
 *
 * Obviously, the stream should send XML - although we write anything we receive. No input check is done for speed
 * reasons - it's the using applications responsibility to send valid XML that also withstands namespace checks!
 *
 * Note: you cannot write at the root level with this element, as the StAX writer needs at least one wrapping element.
 */
public class CopyElement implements XmlWritable {
    
    protected final InputStream xmlInputStream;
    
    /**
     * Create the element and associate with the InputStream
     * @param xmlInputStream The stream to write when this element gets written.
     */
    public CopyElement(final InputStream xmlInputStream) {
        Objects.requireNonNull(xmlInputStream);
        this.xmlInputStream = xmlInputStream;
    }
    
    @Override
    public void write(XmlWriter writer) throws XmlWriteException {
        try {
            // Make the XmlWriter think we want to write a value, so it prints ">" of the containing element to stream
            // This is somewhat hacky, but there is no other possibility to trick the StAX API into this.
            writer.writeCharacters("");
            // Flush the XmlWriter to make sure any preceding tags are written out
            writer.flush();
            
            // Now let's write the actual content
            writeXml(writer);
            
            // And flush stream & writer after the operation - again
            writer.flush();
        } catch (XMLStreamException | IOException e) {
            throw new XmlWriteException(e);
        }
    }
    
    /**
     * A matcher, created only once, reusable to match the XML declaration with any attributes. Non-greedy, so
     * we do not interfere with any XML processing instructions following.
     */
    private static final Matcher xmlDeclaration = Pattern.compile("<\\?xml .*?\\?>").matcher("");
    
    protected void writeXml(XmlWriter writer) throws IOException {
        
        /*
         * The - optional but recommended - xml declaration, if present, MUST be the first and only element of the input.
         * It may not be preceded by a comment or similar. No matter how long it is exactly, the limited number of
         * attributes inside <?xml version='1.0' encoding='UTF-8' standalone='yes' ?> make it VERY unlikely it is going
         * to exceed 1024 characters.
         *
         * If we don't find the declaration within this first section, but it's still present - that's invalid XML
         * and the application must take care of it.
         */
        
        try (
            xmlInputStream;
        ) {
            // fill the buffer once with the first 1024 chars and read as string
            byte[] buffer = xmlInputStream.readNBytes(1024);
            String firstChars = new String(buffer, StandardCharsets.UTF_8);
            
            // match the start with the compiled regex and replace with nothing when matching.
            firstChars = xmlDeclaration.reset(firstChars).replaceFirst("");
            
            // write the chars to the output stream
            writer.getOutputStream().write(firstChars.getBytes(StandardCharsets.UTF_8));
            
            // now send the rest of the stream
            xmlInputStream.transferTo(writer.getOutputStream());
        }
    }
}
