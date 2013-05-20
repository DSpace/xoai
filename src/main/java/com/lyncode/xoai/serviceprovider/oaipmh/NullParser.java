package com.lyncode.xoai.serviceprovider.oaipmh;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

public class NullParser extends ElementParser {
	
	
	public NullParser(Logger log, XMLStreamReader reader) {
		super(log, reader);
	}
	
	/**
	 * 
	 * @param getNext
	 * @return true if everything goes fine, false if it finds a close element at the first time.
	 * @throws ParseException
	 */
	public boolean parse (boolean getNext) throws ParseException {
		try {
			if (getNext) {
				if (!super.getReader().hasNext()) throw new KnownParseException("Expecting an element, none appeared");
				super.getReader().nextTag();
			}
			int type = super.getReader().getEventType();
			if (type != XMLType.START_ELEMENT.getID())
				return false;
			
			String elementName = super.getReader().getLocalName();
			int count = 1;
			
			while (super.getReader().hasNext()) {
				type = super.getReader().next();
				switch (XMLType.fromID(type)) {
					case START_ELEMENT:
						getLogger().debug("Start of element "+super.getReader().getLocalName());
						if (elementName.equals(super.getReader().getLocalName())) {
							count++;
						}
						break;
					case END_ELEMENT:
						getLogger().debug("End of element "+super.getReader().getLocalName());
						//System.out.println(super.getReader().getLocalName());
						if (elementName.equals(super.getReader().getLocalName())) {
							count--;
						}
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
		return true;
	}

}
