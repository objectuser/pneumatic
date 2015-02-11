package com.surgingsystems.etl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.ParseException;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.surgingsystems.etl.context.EtlContextProvider;

public class XmlRunner {

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

    private static void createPropertyMapFrom(GenericXmlApplicationContext applicationContext, String[] args)
            throws ParseException {
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

        for (String config : configs) {
            applicationContext.load(config);
        }

        applicationContext.refresh();

        Map<String, String> result = new HashMap<String, String>();
        for (String option : options) {
            String[] keyValue = option.split("=");
            result.put(keyValue[0], keyValue[1]);
        }

        EtlContextProvider etlContextProvider = applicationContext.getBean(EtlContextProvider.class);
        etlContextProvider.setContextArguments(result);
    }
}
