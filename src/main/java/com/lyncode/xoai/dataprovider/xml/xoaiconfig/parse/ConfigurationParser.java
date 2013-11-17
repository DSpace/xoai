package com.lyncode.xoai.dataprovider.xml.xoaiconfig.parse;


import com.lyncode.xoai.dataprovider.xml.read.XmlReader;
import com.lyncode.xoai.dataprovider.xml.read.XmlReaderException;
import com.lyncode.xoai.dataprovider.xml.xoaiconfig.Configuration;

public class ConfigurationParser extends Parser<Configuration> {

    private final ContextConfigurationParser contextConfigurationParser = new ContextConfigurationParser();
    private final FormatConfigurationParser formatConfigurationParser = new FormatConfigurationParser();
    private final TransformerConfigurationParser transformerConfigurationParser = new TransformerConfigurationParser();
    private final FilterConfigurationParser filterConfigurationParser = new FilterConfigurationParser();
    private final ConditionConfigurationParser conditionConfigurationParser = new ConditionConfigurationParser();
    private final SetConfigurationParser setConfigurationParser = new SetConfigurationParser();

    @Override
    public Configuration parse(XmlReader reader) throws ParseException {
        try {
            reader.proceedToTheNextStartElement();
            Configuration configuration = new Configuration()
                    .withMaxListIdentifiersSize(Integer.valueOf(reader.getAttribute("maxListIdentifiersSize")))
                    .withMaxListRecordsSize(Integer.valueOf(reader.getAttribute("maxListRecordsSize")))
                    .withMaxListSetsSize(Integer.valueOf(reader.getAttribute("maxListSetsSize")))
                    .withIndented(Boolean.valueOf(reader.getAttribute("indented")))
                    .withStylesheet(reader.getAttribute("stylesheet"))
                    .withDescriptionFile(reader.getAttribute("descriptionFile"));

            reader.proceedToNextElement();
            while (reader.isStart()) {
                if (reader.elementNameIs("Contexts")) {
                    reader.proceedToNextElement();
                    while (reader.isStart() && reader.getName().equals("Context"))
                        configuration.withContextConfigurations(contextConfigurationParser.parse(reader));

                    reader.proceedToNextElement();
                } else if (reader.elementNameIs("Formats")) {
                    reader.proceedToNextElement();
                    while (reader.isStart())
                        configuration.withFormatConfigurations(formatConfigurationParser.parse(reader));

                    reader.proceedToNextElement();
                } else if (reader.elementNameIs("Transformers")) {
                    reader.proceedToNextElement();
                    while (reader.isStart())
                        configuration.withTransformerConfigurations(transformerConfigurationParser.parse(reader));

                    reader.proceedToNextElement();
                } else if (reader.elementNameIs("Filters")) {
                    reader.proceedToNextElement();
                    while (reader.isStart()) {
                        if (reader.elementNameIs("Filter"))
                            configuration.withFilters(filterConfigurationParser.parse(reader));
                        else if (reader.elementNameIs("CustomCondition"))
                            configuration.withConditions(conditionConfigurationParser.parse(reader));
                    }

                    reader.proceedToNextElement();
                } else if (reader.elementNameIs("Sets")) {
                    reader.proceedToNextElement();
                    while (reader.isStart())
                        configuration.withSets(setConfigurationParser.parse(reader));

                    reader.proceedToNextElement();
                } else throw new ParseException("Unexpected element " + reader.getName());
            }

            return configuration;

        } catch (XmlReaderException e) {
            throw new ParseException(e);
        }
    }
}
