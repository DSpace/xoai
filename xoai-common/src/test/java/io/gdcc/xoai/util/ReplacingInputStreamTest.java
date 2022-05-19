package io.gdcc.xoai.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Source copied from https://github.com/mwiesiolek/inbot-utils/blob/master/src/test/java/io/inbot/utils/ReplacingInputStreamTest.java
 * Originally licensed under MIT license
 * Slightly adapted to make use of JUnit5 and Hamcrest instead of TestNG etc
 *
 * @author jillesvangurp
 */
public class ReplacingInputStreamTest {
    
    public static Stream<Arguments> samples() {
        return Stream.of(
            Arguments.of("abcdefghijk", "def","fed", "abcfedghijk"),
            Arguments.of("abcdefghijkabcdefghijkabcdefghijk", "def","fed", "abcfedghijkabcfedghijkabcfedghijk"),
            Arguments.of("abcdefghijk", "def","def", "abcdefghijk"),
            Arguments.of("abcdefghijk", "def","", "abcghijk"),
            Arguments.of("abcdefghijk", "def",null, "abcghijk"),
            Arguments.of("abcdefghijk", "d","dd", "abcddefghijk"),
            Arguments.of("", "d","dd", "")
        );
    }
    
    public static Stream<Arguments> newlineSamples() {
        return Stream.of(
            Arguments.of("foo\n\rbar\r","foo\nbar\n"),
            Arguments.of("foo\rbar\r","foo\nbar\n")
        );
    }
    
    @ParameterizedTest
    @MethodSource("newlineSamples")
    public void shouldFixNewlines(String input, String expected) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        try(InputStream ris = ReplacingInputStream.newLineNormalizingInputStream(bis)) {
            String result = new String(ris.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(result, equalTo(expected));
        }
    }
    
    @ParameterizedTest
    @MethodSource("samples")
    public void shouldReplace(String input, String pattern, String replacement, String expected) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        byte[] replacementBytes=null;
        if(replacement!=null) {
            replacementBytes = replacement.getBytes(StandardCharsets.UTF_8);
        }
        try(ReplacingInputStream ris = new ReplacingInputStream(bis, pattern.getBytes(StandardCharsets.UTF_8), replacementBytes)) {
            String result = new String(ris.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(result, equalTo(expected));
        }
    }
    
    @ParameterizedTest
    @MethodSource("samples")
    public void shouldReplaceUsingStringConstructor(String input, String pattern, String replacement, String expected) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        try(ReplacingInputStream ris = new ReplacingInputStream(bis, pattern, replacement)) {
            assertThat(new String(ris.readAllBytes(), StandardCharsets.UTF_8), equalTo(expected));
        }
    }
}
