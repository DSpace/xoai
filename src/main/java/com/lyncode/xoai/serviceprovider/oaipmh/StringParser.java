package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class StringParser extends ElementParser {
	
	
	public StringParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}
	
	/**
	 * 
	 * @param getNext
	 * @return true if everything goes fine, false if it finds a close element at the first time.
	 * @throws ParseException
	 */
	public String parse (boolean getNext) throws ParseException {
		String result = "";
		try {
			if (getNext) {
				if (!super.getReader().hasNext()) throw new KnownParseException("Expecting an element, none appeared");
				super.getReader().nextTag();
			}
			int type = super.getReader().getEventType();
			if (type != XMLType.START_ELEMENT.getID())
				return null;
			
			String elementName = super.getReader().getLocalName();
			int count = 1;
			
			while (super.getReader().hasNext()) {
				type = super.getReader().next();
				switch (XMLType.fromID(type)) {
					case START_ELEMENT:
						result += "<"+super.getReader().getLocalName()+"";
						getLogger().debug("Start of element "+super.getReader().getLocalName());
						Map<String, String> attr = super.getRawAttributes();
						if (!attr.isEmpty()) {
							
							List<String> join = new ArrayList<String>();
							for (String k : attr.keySet())
								join.add(k+"=\""+attr.get(k)+"\"");
							result += " "+StringUtils.join(join, " ");
						}
						if (elementName.equals(super.getReader().getLocalName())) {
							count++;
						}
						result += ">";
						break;
					case END_ELEMENT:
						getLogger().debug("End of element "+super.getReader().getLocalName());
						//System.out.println(super.getReader().getLocalName());
						result += "</"+super.getReader().getLocalName()+">";
						if (elementName.equals(super.getReader().getLocalName())) {
							count--;
						}
						break;
					case CHARACTERS:
						result += super.getReader().getText();
						break;
					default:
						break;
				}
				if (count == 0) break;
			}
			
			if (count > 0) throw new KnownParseException("Expecting an element named "+elementName+", but it never appeared");
			
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		return result;
	}

}
