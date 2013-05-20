package com.lyncode.xoai.serviceprovider.oaipmh;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;

public abstract class GenericParser {
	private Logger log;
	
	public GenericParser(Logger log) {
		this.log = log;
	}

	public Logger getLogger () {
		return this.log;
	}

	public abstract Object parse (XMLStreamReader reader) throws ParseException;
	

	public boolean checkBooleanStart (XMLStreamReader reader, String name, boolean getNext) {
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
	
	public void checkStart (XMLStreamReader reader, String name, boolean getNext) throws ParseException {
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
	
	public boolean checkBooleanEnd (XMLStreamReader reader, String name, boolean getNext) {
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
	
	public void checkEnd (XMLStreamReader reader, String name, boolean getNext) throws ParseException {
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

	public String getString (XMLStreamReader reader, String name, boolean getNext) throws ParseException {
		String text = "";
		this.checkStart(reader, name, getNext);
		try {
			if (!reader.hasNext())
				throw new KnownParseException("Expecting some text within element "+name+", but none appeared.");
			int type = reader.next();
			if (type == XMLType.END_ELEMENT.getID()) {
				this.checkEnd(reader, name, false);
				return text;
			}
			if (type != XMLType.CHARACTERS.getID())
				throw new KnownParseException("Expecting some text within element "+name+", but another one appeared ("+XMLType.fromID(type).name()+")");
			text = reader.getText();
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		this.checkEnd(reader, name, true);
		return text;
	}
	
	public String getString (XMLStreamReader reader, boolean getNext) throws ParseException {
		String text = "";
		try {
			if (getNext) {
				if (!reader.hasNext())
					throw new KnownParseException("Expecting some text, but none appeared.");
				reader.next();
			}
			int type = reader.getEventType();
			if (type != XMLType.CHARACTERS.getID())
				throw new KnownParseException("Expecting some text, but another one thing appeared ("+XMLType.fromID(type).name()+")");
			text = reader.getText();
		} catch (XMLStreamException e) {
			throw new UnknownParseException(e);
		}
		return text;
	}
	
	public Map<String, String> getAttributes (XMLStreamReader reader) {
		Map<String, String> attributes = new LowerMap();
		for (int i=0;i<reader.getAttributeCount();i++) {
			attributes.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
		}
		return attributes;
	}

	public Map<String, String> getRawAttributes (XMLStreamReader reader) {
		Map<String, String> attributes = new HashMap<String, String>();
		for (int i=0;i<reader.getAttributeCount();i++) {
			attributes.put(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
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
