package com.lyncode.xoai.serviceprovider.oaipmh.spec.schemas.oai_dc;

import java.util.ArrayList;
import java.util.List;

public class OAIDC {
	private List<Element> elements;
	
	public OAIDC() {
		elements = new ArrayList<Element>();
	}
	
	public List<Element> getElements () {
		return elements;
	}
	
	public void add (String name, String value) {
		this.elements.add(new Element(name, value));
	}

	public class Element {
		private String name;
		private String value;
		
		public Element (String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
		
		
	}
}
