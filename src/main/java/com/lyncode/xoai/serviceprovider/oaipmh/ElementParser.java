package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

public abstract class ElementParser {
	private Logger log;
	private XMLStreamReader reader;
	
	public ElementParser (Logger log, XMLStreamReader reader) {
		this.log = log;
		this.reader = reader;
	}
	
	public Logger getLogger () {
		return this.log;
	}
	
	public XMLStreamReader getReader () {
		return this.reader;
	}
	
	public boolean checkBooleanStart (String name, boolean getNext) {
		try {
			if (getNext) {
				if (!reader.hasNext()) return false;
				reader.nextTag();
			}
			int type = reader.getEventType();
			if (type != XMLType.START_ELEMENT.getID())
				return false;
			if (reader.getLocalName() == null)
				return false;
			if (!name.toLowerCase().equals(reader.getLocalName().toLowerCase()))
				return false;
		} catch (XMLStreamException e) {
			return false;
		}
		return true;
	}
	
	public void checkStart (String name, boolean getNext) throws ParseException {
		try {
			if (getNext) {
				if (!reader.hasNext()) throw new KnownParseException("Expecting the "+name+" start element, but none appeared");
				reader.nextTag();
			}
			int type = reader.getEventType();
			if (type != XMLType.START_ELEMENT.getID())
				throw new KnownParseException("Expecting the "+name+" start element, but another one appeared - "+XMLType.fromID(type).name());
			if (reader.getLocalName() == null)
				throw new KnownParseException("Expecting the "+name+" start element, but a null named element appeared");
			if (!name.toLowerCase().equals(reader.getLocalName().toLowerCase()))
				throw new KnownParseException("Expecting the "+name+" start element, but another one appeared - "+reader.getLocalName()+" ("+XMLType.fromID(type).name()+")");
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		this.getLogger().debug("Checked start of element "+name);
	}
	
	public boolean checkBooleanEnd (String name, boolean getNext) {
		try {
			if (getNext) {
				if (!reader.hasNext()) return false;
				reader.nextTag();
			}
			int type = reader.getEventType();
			if (type != XMLType.END_ELEMENT.getID())
				return false;
			if (reader.getLocalName() == null)
				return false;
			if (!name.toLowerCase().equals(reader.getLocalName().toLowerCase()))
				return false;
		} catch (XMLStreamException e) {
			return false;
		}
		return true;
	}
	
	public void checkEnd (String name, boolean getNext) throws ParseException {
		try {
			if (getNext) {
				if (!reader.hasNext()) throw new KnownParseException("Expecting the "+name+" end element, but none appeared");
				reader.nextTag();
			}
			int type = reader.getEventType();
			if (type != XMLType.END_ELEMENT.getID())
				throw new KnownParseException("Expecting the "+name+" end element, but another one appeared - "+XMLType.fromID(type).name());
			if (reader.getLocalName() == null)
				throw new KnownParseException("Expecting the "+name+" end element, but a null named element appeared");
			if (!name.toLowerCase().equals(reader.getLocalName().toLowerCase()))
				throw new KnownParseException("Expecting the "+name+" end element, but another one appeared - "+reader.getLocalName()+" ("+XMLType.fromID(type).name()+")");
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		this.getLogger().debug("Checked end of element "+name);
	}

	public String getString (String name, boolean getNext) throws ParseException {
		String text = "";
		this.checkStart(name, getNext);
		try {
			if (!this.reader.hasNext())
				throw new KnownParseException("Expecting some text within element "+name+", but none appeared.");
			int type = this.reader.next();
			if (type != XMLType.CHARACTERS.getID())
				throw new KnownParseException("Expecting some text within element "+name+", but another one appeared ("+XMLType.fromID(type).name()+")");
			text = this.reader.getText();
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		this.checkEnd(name, true);
		return text;
	}
	
	public String getString (boolean getNext) throws ParseException {
		String text = "";
		try {
			if (getNext) {
				if (!this.reader.hasNext())
					throw new KnownParseException("Expecting some text, but none appeared.");
				this.reader.next();
			}
			int type = this.reader.getEventType();
			if (type != XMLType.CHARACTERS.getID())
				throw new KnownParseException("Expecting some text, but another one thing appeared ("+XMLType.fromID(type).name()+")");
			text = this.reader.getText();
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		return text;
	}
	
	public Map<String, String> getAttributes () {
		Map<String, String> attributes = new LowerMap();
		for (int i=0;i<this.reader.getAttributeCount();i++) {
			attributes.put(this.reader.getAttributeLocalName(i), this.reader.getAttributeValue(i));
		}
		return attributes;
	}

	public Map<String, String> getRawAttributes () {
		Map<String, String> attributes = new HashMap<String, String>();
		for (int i=0;i<this.reader.getAttributeCount();i++) {
			attributes.put(this.reader.getAttributeLocalName(i), this.reader.getAttributeValue(i));
		}
		return attributes;
	}
	
	public class LowerMap implements Map<String, String> {
		private Map<String, String> map = new HashMap<String, String>();
		
		@Override
		public int size() {
			return map.size();
		}

		@Override
		public boolean isEmpty() {
			return map.isEmpty();
		}

		@Override
		public boolean containsKey(Object key) {
			if (key != null) {
				if (key instanceof String) {
					return map.containsKey(((String) key).toLowerCase());
				}
			}
			return map.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			return map.containsValue(value);
		}

		@Override
		public String get(Object key) {
			if (key != null) {
				if (key instanceof String) {
					return map.get(((String) key).toLowerCase());
				}
			}
			return map.get(key);
		}

		@Override
		public String put(String key, String value) {
			if (key != null) key = key.toLowerCase();
			return map.put(key, value);
		}

		@Override
		public String remove(Object key) {
			if (key != null) {
				if (key instanceof String) {
					return map.remove(((String) key).toLowerCase());
				}
			}
			return map.remove(key);
		}

		@Override
		public void putAll(Map<? extends String, ? extends String> m) {
			for (String s : m.keySet())
				map.put(s, m.get(s));
		}

		@Override
		public void clear() {
			map.clear();
		}

		@Override
		public Set<String> keySet() {
			return map.keySet();
		}

		@Override
		public Collection<String> values() {
			return map.values();
		}

		@Override
		public Set<java.util.Map.Entry<String, String>> entrySet() {
			return map.entrySet();
		}
		
	}
}
