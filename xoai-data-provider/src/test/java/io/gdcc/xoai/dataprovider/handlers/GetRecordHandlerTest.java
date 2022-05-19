/*
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree and available online at
 *
 * http://www.dspace.org/license/
 */

package io.gdcc.xoai.dataprovider.handlers;

import io.gdcc.xoai.dataprovider.exceptions.BadArgumentException;
import io.gdcc.xoai.dataprovider.exceptions.CannotDisseminateFormatException;
import io.gdcc.xoai.dataprovider.exceptions.IdDoesNotExistException;
import io.gdcc.xoai.dataprovider.model.InMemoryItem;
import io.gdcc.xoai.dataprovider.model.MetadataFormat;
import io.gdcc.xoai.model.oaipmh.GetRecord;
import io.gdcc.xoai.model.oaipmh.Metadata;
import io.gdcc.xoai.xml.EchoElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.xmlunit.matchers.HasXPathMatcher;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static io.gdcc.xoai.dataprovider.model.InMemoryItem.randomItem;
import static io.gdcc.xoai.model.oaipmh.Verb.Type.GetRecord;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GetRecordHandlerTest extends AbstractHandlerTest {
    private GetRecordHandler underTest;

    @BeforeEach
    public void setup () {
        underTest = new GetRecordHandler(aContext(), theRepository());
    }

    @Test
    void getRecordRequiresMetadataPrefixParameter() {
        assertThrows(BadArgumentException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withIdentifier("a"))));
    }

    @Test
    void getRecordRequiresIdentifierParameter() {
        assertThrows(BadArgumentException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("a"))));
    }

    @Test
    void idDoesNotExists() {
        assertThrows(IdDoesNotExistException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("xoai").withIdentifier("1"))));
    }

    @Test
    void cannotDisseminateFormat() {
        theItemRepository().withItem(randomItem().withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
    
        assertThrows(CannotDisseminateFormatException.class,
            () -> underTest.handle(a(request().withVerb(GetRecord).withMetadataPrefix("abcd").withIdentifier("1"))));
    }
    
    
    static Stream<Arguments> validItems() {
        return Stream.of(
            Arguments.of(randomItem().withDeleted(true)),
            Arguments.of(randomItem().withDeleted(false)),
            Arguments.of(randomItem()
                .withDeleted(false)
                .withMetadata(new Metadata(new EchoElement("<test>My Metadata</test>")))),
            Arguments.of(randomItem()
                .withDeleted(false)
                .withMetadata(Metadata.copyFromStream(new ByteArrayInputStream("<test>My Metadata</test>".getBytes(StandardCharsets.UTF_8)))))
        );
    }
    
    @ParameterizedTest
    @MethodSource("validItems")
    void validResponse(InMemoryItem item) throws Exception {
        // given
        theItemRepository().withItem(item.withIdentifier("1"));
        aContext().withMetadataFormat("xoai", MetadataFormat.identity());
        GetRecord handle = underTest.handle(a(request().withVerb(GetRecord)
                .withMetadataPrefix("xoai")
                .withIdentifier("1")));

        // when
        String result = write(handle);

        // then
        assertThat(result, xPath("//header/identifier", is(equalTo("1"))));
    }
    
    @Test
    void validResponseCopyElement() throws Exception {
        // given
        theItemRepository().withItem(
            randomItem()
                .withDeleted(false)
                .withIdentifier("copy")
                .withMetadata(Metadata.copyFromStream(new ByteArrayInputStream("<testdata>Test1234</testdata>".getBytes(StandardCharsets.UTF_8))))
        );
        aContext().withMetadataFormat("custom", MetadataFormat.identity());
        GetRecord handle = underTest.handle(a(request().withVerb(GetRecord)
            .withMetadataPrefix("custom")
            .withIdentifier("copy")));
    
        // when
        String result = write(handle);
    
        // then
        assertThat(result, xPath("//header/identifier", is(equalTo("copy"))));
        assertThat(result, HasXPathMatcher.hasXPath("//record/metadata"));
        assertThat(result, HasXPathMatcher.hasXPath("//record/metadata/testdata"));
    }
}
