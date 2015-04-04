package com.surgingsystems.etl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.surgingsystems.etl.context.EtlContextProvider;
import com.surgingsystems.etl.yamldsl.YamlParser;

public class YamlRunner {

    private static Logger logger = LogManager.getFormatterLogger(YamlRunner.class);

    public static void main(String[] args) throws Exception {
        try {
            try (GenericXmlApplicationContext applicationContext = EtlContext.createEtlContext()) {
                createPropertyMapFrom(applicationContext, args);
                JobConfigurer configurer = new XmlJobConfigurer(applicationContext);
                Job job = configurer.buildJob();
                job.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void createPropertyMapFrom(GenericXmlApplicationContext applicationContext, String[] args) {

        List<String> options = new ArrayList<String>();
        List<String> configs = new ArrayList<String>();

        for (String arg : args) {
            if (arg.startsWith("-D")) {
                options.add(arg.substring(2));
            } else if (arg.startsWith("-")) {

            } else {
                configs.add(arg);
            }
        }

        applicationContext.refresh();

        YamlParser yamlParser = applicationContext.getBean(YamlParser.class);

        for (String config : configs) {
            if (config.endsWith(".yml")) {
                logger.debug("Adding YAML configuration file: %s", config);
                yamlParser.parse(config);
            } else if (config.endsWith(".xml")) {
                logger.debug("Adding XML configuration file: %s", config);
                applicationContext.load(config);
            }
        }

        Map<String, String> result = new HashMap<String, String>();
        for (String option : options) {
            String[] keyValue = option.split("=");
            result.put(keyValue[0], keyValue[1]);
        }

        EtlContextProvider etlContextProvider = applicationContext.getBean(EtlContextProvider.class);
        etlContextProvider.setContextArguments(result);
    }
}
