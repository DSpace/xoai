
package com.lyncode.xoai.common.serviceprovider.data;

public class Record extends Identifier {

	/**
     * 
     */
    private static final long serialVersionUID = 3482025864486977541L;
	private Metadata metadata;

	public Metadata getMetadata() {
		return metadata;
	}
	public void setMetadata(Metadata metadata) {
		this.metadata = metadata;
	}
	
	public boolean hasMetadata () {
	    return (this.metadata != null);
	}
}
