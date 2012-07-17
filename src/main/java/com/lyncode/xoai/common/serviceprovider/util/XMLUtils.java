package com.lyncode.xoai.common.serviceprovider.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lyncode.xoai.common.serviceprovider.data.Header;
import com.lyncode.xoai.common.serviceprovider.data.Identifier;
import com.lyncode.xoai.common.serviceprovider.data.Metadata;
import com.lyncode.xoai.common.serviceprovider.data.MetadataFormat;
import com.lyncode.xoai.common.serviceprovider.data.Record;
import com.lyncode.xoai.common.serviceprovider.data.Set;

public class XMLUtils
{
    public static Document parseRecords (InputStream input) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(input);
        return doc;
    }
    
    public static String getText (Node elem) {
        if (elem.getNodeType() == Element.TEXT_NODE)
            return elem.getTextContent();
        else if (elem.hasChildNodes())
            return elem.getFirstChild().getTextContent();
        return "";
    }
    public static String getText (NodeList elem) {
        if (elem.getLength() > 0 && elem.item(0).getFirstChild() != null)
            return elem.item(0).getFirstChild().getTextContent();
        return "";
    }

    public static Record getRecord(Node node)
    {
        Record record = new Record();
        NodeList list = node.getChildNodes();
        for (int i=0;i<list.getLength();i++) {
            if (list.item(i).getNodeName().toLowerCase().equals("header")) {
                record.setHeader(getHeader(list.item(i).getChildNodes()));
            } else if (list.item(i).getNodeName().toLowerCase().equals("metadata")) {
                record.setMetadata(getMetadata(list.item(i).getChildNodes()));
            }
        }
        return record;
    }
    public static Identifier getIdentifier(Node node)
    {
        Identifier record = new Identifier();
        record.setHeader(getHeader(node.getChildNodes()));
        return record;
    }

    public static Metadata getMetadata(NodeList childNodes)
    {
        try {
            StringBuilder builder = new StringBuilder();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            for (int i=0;i<childNodes.getLength();i++) {
                StringWriter sw = new StringWriter();
                t.transform(new DOMSource(childNodes.item(i)), new StreamResult(sw));
                builder.append(sw.toString());
            }
            return new Metadata(builder.toString());
        } catch (Exception ex) {
            return null;
        }
    }
    
    public static String getXML (NodeList childNodes) {
        try {
            StringBuilder builder = new StringBuilder();
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            for (int i=0;i<childNodes.getLength();i++) {
                StringWriter sw = new StringWriter();
                t.transform(new DOMSource(childNodes.item(i)), new StreamResult(sw));
                builder.append(sw.toString());
            }
            return builder.toString();
        } catch (Exception ex) {
            return "";
        }
    }

    public static Header getHeader(NodeList item)
    {
        Header header = new Header();
        for (int i = 0; i<item.getLength(); i++) {
            if (item.item(i).getNodeName().toLowerCase().equals("identifier"))
                header.setIdentifier(getText(item.item(i)));
            else if (item.item(i).getNodeName().toLowerCase().equals("datestamp"))
                header.setDatestamp(getText(item.item(i)));
            else if (item.item(i).getNodeName().toLowerCase().equals("setSpec"))
                header.setStatus(getText(item.item(i)));
        }
        return header;
    }

    public static Set getSet(Node item)
    {
        Set set = new Set();
        NodeList childs = item.getChildNodes();
        for (int i=0;i<childs.getLength();i++) {
            if (childs.item(i).getNodeName().toLowerCase().equals("setspec"))
                set.setSetSpec(getText(childs.item(i)));
            else if (childs.item(i).getNodeName().toLowerCase().equals("setname"))
                set.setSetName(getText(childs.item(i)));
        }
        return set;
    }

    public static MetadataFormat getMetadataFormat(Node item)
    {
        MetadataFormat format = new MetadataFormat();
        NodeList childs = item.getChildNodes();
        for (int i = 0;i<childs.getLength();i++) {
            if (childs.item(i).getNodeName().toLowerCase().equals("metadataprefix"))
                format.setMetadataPrefix(getText(childs.item(i)));
            else if (childs.item(i).getNodeName().toLowerCase().equals("schema"))
                format.setSchema(getText(childs.item(i)));
            else if (childs.item(i).getNodeName().toLowerCase().equals("metadatanamespace"))
                format.setMetadataNamespace(getText(childs.item(i)));
        }
        return format;
    }
}
