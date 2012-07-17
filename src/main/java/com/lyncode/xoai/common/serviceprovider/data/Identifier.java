
package com.lyncode.xoai.common.serviceprovider.data;

import java.io.Serializable;

public class Identifier implements Serializable {
    private static final long serialVersionUID = 3482025864486977541L;
    private Header header;
	
	public Header getHeader() {
		return header;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
}
