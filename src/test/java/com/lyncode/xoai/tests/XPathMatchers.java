package com.lyncode.xoai.tests;

import com.lyncode.xoai.builders.MapBuilder;
import org.dom4j.*;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.jaxen.SimpleNamespaceContext;

import java.util.List;
import java.util.Map;

public class XPathMatchers {
    public static XPathValueMatcher hasXPath(String expression, String value) {
        return new XPathValueMatcher(expression, value);
    }

    public static XPathValueMatcher hasXPath(String expression, String value, MapBuilder<String, String> namespaces) {
        return new XPathValueMatcher(expression, value, namespaces);
    }

    public static XPathMatcher hasXPath(String expression) {
        return new XPathMatcher(expression);
    }
    public static XPathMatcher hasXPath(String expression, MapBuilder<String, String> namespaces) {
        return new XPathMatcher(expression, namespaces);
    }

    private static class XPathMatcher extends BaseMatcher<String> {
        private String expression;
        private MapBuilder<String, String> contexts;

        public XPathMatcher (String expression) {
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

    private static class XPathValueMatcher extends BaseMatcher<String> {
        private MapBuilder<String, String> namespaces;
        private String value;
        private String expression;

        public XPathValueMatcher (String expression, String value) {
            this.value = value;
            this.expression = expression;
        }

        public XPathValueMatcher(String expression, String value, MapBuilder<String, String> namespaces) {
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
                    return this.value.equals(evaluatedValue);
                } catch (DocumentException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
            System.err.println("Incorrect input");
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("XPath ").appendValue(this.expression).appendText(" must resolve to ").appendValue(this.value);
        }
    }
}
