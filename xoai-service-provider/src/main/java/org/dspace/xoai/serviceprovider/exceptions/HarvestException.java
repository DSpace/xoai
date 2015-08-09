/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.serviceprovider.exceptions;


/**
 * @author Development @ Lyncode
 * @version 3.1.0
 */
public class HarvestException extends Exception {
    private static final long serialVersionUID = -1824340625967423555L;
    private String url;


    public HarvestException() {
        url = "";
    }

    public HarvestException(String arg0) {
        super(arg0);
    }

    public HarvestException(Throwable arg0) {
        super(arg0);
    }

    public HarvestException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }


    public void setURL(String url) {
        this.url = url;
    }

    public String getURL() {
        return this.url;
    }
}
