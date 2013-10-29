package com.lyncode.xoai.tests.dataprovider.acceptance.nonfunctional;

import com.lyncode.xoai.dataprovider.exceptions.ConfigurationException;
import com.lyncode.xoai.dataprovider.exceptions.InvalidContextException;
import com.lyncode.xoai.dataprovider.exceptions.OAIException;
import com.lyncode.xoai.dataprovider.exceptions.WritingXmlException;
import com.lyncode.xoai.tests.dataprovider.acceptance.AbstractDataProviderTest;
import org.dom4j.DocumentException;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.lyncode.xoai.tests.SyntacticSugar.and;
import static com.lyncode.xoai.tests.SyntacticSugar.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TimeZoneTest extends AbstractDataProviderTest {
    private static final String TEST_DATE = "2013-01-01T00:12:13Z";
    private static final String DATE_GMT = "2013-01-01T02:12:13Z";
    public static final String IN_TIMEZONE_GMT_LESS_2 = "GMT-2";

    private static Date date (String given, String timezone) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone(timezone));
        try {
            return format.parse(TEST_DATE);
        } catch (ParseException e) {
            return new Date();
        }
    }

    @Test
    public void shouldReturnGmtTimezoneAlwaysInResponseDate () throws WritingXmlException, OAIException, InvalidContextException, IOException, XMLStreamException, ConfigurationException, DocumentException {
        given(theSetRepository().doesntSupportSets());
        and(given(theDateIs(date(TEST_DATE, IN_TIMEZONE_GMT_LESS_2))));

        afterHandling(aRequest().withVerb("ListSets"));

        assertThat(theResult(), xPath("//o:responseDate", is(DATE_GMT)));
    }
}
