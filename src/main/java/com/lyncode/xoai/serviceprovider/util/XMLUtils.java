/**
 * Copyright 2012 Lyncode
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */

package com.lyncode.xoai.serviceprovider.util;

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

import com.lyncode.xoai.serviceprovider.data.Header;
import com.lyncode.xoai.serviceprovider.data.Identifier;
import com.lyncode.xoai.serviceprovider.data.Metadata;
import com.lyncode.xoai.serviceprovider.data.MetadataFormat;
import com.lyncode.xoai.serviceprovider.data.Record;
import com.lyncode.xoai.serviceprovider.data.Set;
import com.lyncode.xoai.serviceprovider.exceptions.BadArgumentException;
import com.lyncode.xoai.serviceprovider.exceptions.BadResumptionTokenException;
import com.lyncode.xoai.serviceprovider.exceptions.BadVerbException;
import com.lyncode.xoai.serviceprovider.exceptions.CannotDisseminateFormatException;
import com.lyncode.xoai.serviceprovider.exceptions.IdDoesNotExistException;
import com.lyncode.xoai.serviceprovider.exceptions.NoMetadataFormatsException;
import com.lyncode.xoai.serviceprovider.exceptions.NoRecordsMatchException;
import com.lyncode.xoai.serviceprovider.exceptions.NoSetHierarchyException;


/**
 * @author Development @ Lyncode <development@lyncode.com>
 * @version 2.2.9
 */
public class XMLUtils
{
    public static Document parseDocument (InputStream input) throws ParserConfigurationException, SAXException, IOException {
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
            	Header h = getHeader(list.item(i).getChildNodes());
            	if (list.item(i).hasAttributes()) {
            		Node n = list.item(i).getAttributes().getNamedItem("status");
            		if (n != null) {
            			if (n.getTextContent() != null)
            				h.setStatus(n.getTextContent().toLowerCase());
            		}
            	}
                record.setHeader(h);
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
                header.getSpecList().add(getText(item.item(i)));
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

	public static void check(Document doc) throws NoRecordsMatchException, BadArgumentException, BadResumptionTokenException, BadVerbException, CannotDisseminateFormatException, IdDoesNotExistException, NoMetadataFormatsException, NoSetHierarchyException {
		NodeList node = doc.getElementsByTagName("error");
		if (node.getLength() > 0) {
			Node n = node.item(0);
			String code = n.getAttributes().getNamedItem("code").getTextContent();
			String description = getText(n);
			
			if ("noRecordsMatch".equals(code))
				throw new NoRecordsMatchException(description);
			else if ("badArgument".equals(code))
				throw new BadArgumentException(description);
			else if ("badResumptionToken".equals(code))
				throw new BadResumptionTokenException(description);
			else if ("badVerb".equals(code))
				throw new BadVerbException(description);
			else if ("cannotDisseminateFormat".equals(code))
				throw new CannotDisseminateFormatException(description);
			else if ("idDoesNotExist".equals(code))
				throw new IdDoesNotExistException(description);
			else if ("noMetadataFormats".equals(code))
				throw new NoMetadataFormatsException(description);
			else if ("noSetHierarchy".equals(code))
				throw new NoSetHierarchyException(description);
		}
	}
	
	public static void checkListRecords(Document doc) throws NoRecordsMatchException, BadResumptionTokenException, CannotDisseminateFormatException, NoSetHierarchyException {
		NodeList node = doc.getElementsByTagName("error");
		if (node.getLength() > 0) {
			Node n = node.item(0);
			String code = n.getAttributes().getNamedItem("code").getTextContent();
			String description = getText(n);
			
			if ("noRecordsMatch".equals(code))
				throw new NoRecordsMatchException(description);
			else if ("badResumptionToken".equals(code))
				throw new BadResumptionTokenException(description);
			else if ("cannotDisseminateFormat".equals(code))
				throw new CannotDisseminateFormatException(description);
			else if ("noSetHierarchy".equals(code))
				throw new NoSetHierarchyException(description);
		}
	}

	public static void checkListSets(Document doc) throws NoRecordsMatchException, BadResumptionTokenException, NoSetHierarchyException {
		NodeList node = doc.getElementsByTagName("error");
		if (node.getLength() > 0) {
			Node n = node.item(0);
			String code = n.getAttributes().getNamedItem("code").getTextContent();
			String description = getText(n);
			
			if ("noRecordsMatch".equals(code))
				throw new NoRecordsMatchException(description);
			else if ("badResumptionToken".equals(code))
				throw new BadResumptionTokenException(description);
			else if ("noSetHierarchy".equals(code))
				throw new NoSetHierarchyException(description);
		}
	}

	public static void checkListMetadataFormats(Document doc) throws IdDoesNotExistException, NoMetadataFormatsException {
		NodeList node = doc.getElementsByTagName("error");
		if (node.getLength() > 0) {
			Node n = node.item(0);
			String code = n.getAttributes().getNamedItem("code").getTextContent();
			String description = getText(n);
			
			if ("idDoesNotExist".equals(code))
				throw new IdDoesNotExistException(description);
			else if ("noMetadataFormats".equals(code))
				throw new NoMetadataFormatsException(description);
		}
	}

	public static void checkListIdentifiers(Document doc) throws NoRecordsMatchException, BadResumptionTokenException, CannotDisseminateFormatException, NoSetHierarchyException {
		NodeList node = doc.getElementsByTagName("error");
		if (node.getLength() > 0) {
			Node n = node.item(0);
			String code = n.getAttributes().getNamedItem("code").getTextContent();
			String description = getText(n);
			
			if ("noRecordsMatch".equals(code))
				throw new NoRecordsMatchException(description);
			else if ("badResumptionToken".equals(code))
				throw new BadResumptionTokenException(description);
			else if ("cannotDisseminateFormat".equals(code))
				throw new CannotDisseminateFormatException(description);
			else if ("noSetHierarchy".equals(code))
				throw new NoSetHierarchyException(description);
		}
	}

	public static void checkGetRecord(Document doc) throws CannotDisseminateFormatException, IdDoesNotExistException {
		NodeList node = doc.getElementsByTagName("error");
		if (node.getLength() > 0) {
			Node n = node.item(0);
			String code = n.getAttributes().getNamedItem("code").getTextContent();
			String description = getText(n);
			
			if ("cannotDisseminateFormat".equals(code))
				throw new CannotDisseminateFormatException(description);
			else if ("idDoesNotExist".equals(code))
				throw new IdDoesNotExistException(description);
		}
	}
}
