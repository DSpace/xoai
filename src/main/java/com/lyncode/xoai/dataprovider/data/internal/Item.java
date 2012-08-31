package com.lyncode.xoai.dataprovider.data.internal;

import com.lyncode.xoai.dataprovider.data.AbstractItem;

public class Item extends ItemIdentify {
	private AbstractItem item;

	public Item(AbstractItem item) {
		super(item);
		this.item = item;
	}

	public AbstractItem getItem() {
		return item;
	}
	
	
}
