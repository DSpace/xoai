package com.lyncode.test.matchers.xml;

import com.lyncode.builder.MapBuilder;
import org.dom4j.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.util.List;
import java.util.regex.Pattern;

public class XPathMatchers {
    private static final String DEFAULT_PREFIX = "defprefix";

    private static String replaceXpath(String xpath) {
        int offset = 0;
        String newXpath = "";
        Pattern pattern = Pattern.compile("/[^/]+");
        java.util.regex.Matcher matcher = pattern.matcher(xpath);
        while (matcher.find()) {
            if (matcher.start() > offset) newXpath += xpath.substring(offset, matcher.start());
            if (!matcher.group().contains(":") && !matcher.group().startsWith("/@"))
                newXpath += "/" + DEFAULT_PREFIX + ":" + matcher.group().substring(1);
            else
                newXpath += matcher.group();
            offset = matcher.end() + 1;
        }

        return newXpath;
    }

    public static <T> XPathValueMatcher xPath(String expression, Matcher<T> value) {
        return new XPathValueMatcher(expression, value);
    }

    public static <T> XPathValueMatcher xPath(String expression, Matcher<T> value, String defaultNamespace) {
        return xPath(replaceXpath(expression), value, new MapBuilder<String, String>().withPair(DEFAULT_PREFIX, defaultNamespace));
    }

    public static <T> XPathValueMatcher xPath(String expression, Matcher<T> value, MapBuilder<String, String> namespaces) {
        return new XPathValueMatcher(expression, value, namespaces);
    }

    public static XPathMatcher hasXPath(String expression) {
        return new XPathMatcher(expression);
    }

    public static XPathMatcher hasXPath(String expression, String defaultNamespace) {
        return new XPathMatcher(replaceXpath(expression), new MapBuilder<String, String>().withPair(DEFAULT_PREFIX, defaultNamespace));
    }

    public static XPathMatcher hasXPath(String expression, MapBuilder<String, String> namespaces) {
        return new XPathMatcher(expression, namespaces);
    }

    private static class XPathMatcher extends BaseMatcher<String> {
        private String expression;
        private MapBuilder<String, String> contexts;

        public XPathMatcher(String expression) {
            this.expression = expression;
        }

        public XPathMatcher(String expression, MapBuilder<String, String> namespaces) {
            this.expression = expression;
            this.contexts = namespaces;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String input = (String) o;
                try {
                    Document document = DocumentHelper.parseText(input);
                    XPath xPath = document.createXPath(this.expression);
                    if (this.contexts != null) {
                        xPath.setNamespaceURIs(this.contexts.build());
                    }
                    List<Node> list = xPath.selectNodes(document);
                    boolean contains = !list.isEmpty();
                    return contains;
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("XML with XPath ").appendValue(this.expression);
        }
    }

    private static class XPathValueMatcher<T> extends BaseMatcher<String> {
        private MapBuilder<String, String> namespaces;
        private Matcher<T> value;
        private String expression;

        public XPathValueMatcher(String expression, Matcher<T> value) {
            this.value = value;
            this.expression = expression;
        }

        public XPathValueMatcher(String expression, Matcher<T> value, MapBuilder<String, String> namespaces) {
            this.value = value;
            this.expression = expression;
            this.namespaces = namespaces;
        }

        @Override
        public boolean matches(Object o) {
            if (o instanceof String) {
                String input = (String) o;
                try {
                    Document document = DocumentHelper.parseText(input);
                    XPath xPath = document.createXPath(this.expression);
                    if (this.namespaces != null)
                        xPath.setNamespaceURIs(this.namespaces.build());
                    String evaluatedValue = xPath.valueOf(document);
                    return !xPath.selectNodes(document).isEmpty() && value.matches(evaluatedValue);
                } catch (DocumentException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            System.err.println("Incorrect input");
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("XPath ").appendValue(this.expression).appendText(" must resolve to ");
            value.describeTo(description);
        }
    }
}