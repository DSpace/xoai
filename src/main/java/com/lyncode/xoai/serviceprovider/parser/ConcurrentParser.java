package com.lyncode.xoai.serviceprovider.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;

import com.lyncode.xoai.serviceprovider.exceptions.ParseException;
import com.lyncode.xoai.serviceprovider.exceptions.UnknownParseException;



public class ConcurrentParser implements Runnable {
    private static XMLInputFactory factoryIn = XMLInputFactory2.newFactory();
    private static XMLOutputFactory factoryOut = XMLOutputFactory2.newFactory();
    
    @SuppressWarnings("unchecked")
    public static Object parse (XMLParser parser, XMLEventReader reader) throws ParseException  {
        int count = 0;
        
        try {
            PipedInputStream input = new PipedInputStream();
            PipedOutputStream output = new PipedOutputStream(input);
            
            ConcurrentParser p = new ConcurrentParser(parser, input);
            Thread thread = new Thread(p);
            thread.start();
            
            XMLStreamWriter writer = factoryOut.createXMLStreamWriter(output);;
            //writer.writeStartDocument();
            while (reader.peek() != null) {
                XMLEvent event = reader.peek();
                if (event.isStartElement()) {
                    count++;
                    StartElement start = event.asStartElement();
                    writer.writeStartElement(start.getName().getPrefix(), start.getName().getLocalPart(), start.getName().getNamespaceURI());
                    
                    Iterator<Namespace> it = start.getNamespaces();
                    while (it.hasNext()) {
                        Namespace n = it.next();
                        writer.writeNamespace(n.getPrefix(), n.getNamespaceURI());
                    }
                    
                    Iterator<Attribute> attrs = start.getAttributes();
                    while (attrs.hasNext()) {
                        Attribute attr = attrs.next();
                        writer.writeAttribute(attr.getName().getPrefix(), attr.getName().getNamespaceURI(), attr.getName().getLocalPart(), attr.getValue());
                    }
                    
                } else if (event.isEndElement()) {
                    writer.writeEndElement();
                    count--;
                    if (count == 0)
                        break;
                } else if (event.isCharacters()) {
                    writer.writeCharacters(event.asCharacters().getData());
                }
                
                if (reader.hasNext())
                    reader.nextEvent();
                else
                    break;
            }
            
            writer.flush();
            writer.close();
            output.close();
            thread.join();
            
            return p.getResult();
            
        } catch (IOException e) {
            throw new UnknownParseException(e);
        } catch (InterruptedException e) {
            throw new UnknownParseException(e);
        } catch (XMLStreamException e) {
            throw new UnknownParseException(e);
        }
    }
    
    private XMLParser parser;
    private InputStream input;
    
    
    private Object result;
    private Throwable exception;
    
    public ConcurrentParser (XMLParser parser, InputStream input) {
        this.parser = parser;
        this.input = input;
    }

    @Override
    public void run() {
        try {
            XMLEventReader reader = factoryIn.createXMLEventReader(input);
            if (reader.hasNext()) reader.nextEvent();
            this.result = parser.parse(reader);
        } catch (ParseException e) {
            this.exception = e;
        } catch (XMLStreamException e) {
            this.exception = e;
        }
    }

    public Object getResult () throws ParseException {
        if (this.exception != null) {
            if (this.exception instanceof ParseException)
                throw (ParseException) this.exception;
            else
                throw new ParseException(this.exception);
        } else return this.result;
    }
}
