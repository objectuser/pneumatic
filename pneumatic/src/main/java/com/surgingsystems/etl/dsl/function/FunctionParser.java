package com.surgingsystems.etl.dsl.function;

import java.util.Map;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import com.surgingsystems.etl.dsl.springbean.SpringBeanDefinitionParser;

public class FunctionParser {

    private static Logger logger = LogManager.getFormatterLogger(FunctionParser.class);

    private Map<String, BeanDefinitionParser> parserMap = new TreeMap<String, BeanDefinitionParser>();

    public FunctionParser() {
        parserMap.put("average", new AverageFunctionBeanDefinitionParser());
        parserMap.put("count", new CountFunctionBeanDefinitionParser());
        parserMap.put("sum", new SumFunctionBeanDefinitionParser());
        parserMap.put("bean", new SpringBeanDefinitionParser());
    }

    public BeanDefinition parse(Element functionDefinitionElement, ParserContext nestedParserContext) {
        BeanDefinitionParser parser = parserMap.get(functionDefinitionElement.getLocalName());
        if (parser == null) {
            logger.error("Unknown function (%s)", functionDefinitionElement.getLocalName());
            throw new RuntimeException(String.format("Unknown function (%s)", functionDefinitionElement.getLocalName()));
        }

        return parser.parse(functionDefinitionElement, nestedParserContext);
    }

}
