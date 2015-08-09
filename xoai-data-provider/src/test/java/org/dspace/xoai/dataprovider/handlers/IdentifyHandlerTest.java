/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package org.dspace.xoai.dataprovider.handlers;

import org.dspace.xoai.dataprovider.exceptions.InternalOAIException;
import org.dspace.xoai.model.oaipmh.Identify;
import org.junit.Test;

import static com.lyncode.test.matchers.xml.XPathMatchers.xPath;
import static org.dspace.xoai.model.oaipmh.Verb.Type.Identify;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class IdentifyHandlerTest extends AbstractHandlerTest {
    @Test
    public void validResponse() throws Exception {
        Identify handle = new IdentifyHandler(aContext(), theRepository()).handle(a(request().withVerb(Identify)));
        String result = write(handle);

        assertThat(result, xPath("//repositoryName", is(equalTo(theRepositoryConfiguration().getRepositoryName()))));
    }

    @Test(expected = InternalOAIException.class)
    public void internalExceptionForInvalidConfiguration () {
        theRepositoryConfiguration().withMaxListSets(0);
        new IdentifyHandler(aContext(), theRepository());
    }
}
