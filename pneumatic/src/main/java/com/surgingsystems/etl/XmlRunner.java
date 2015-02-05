package com.surgingsystems.etl;

import org.springframework.context.support.GenericXmlApplicationContext;

public class XmlRunner {

    public static void main(String[] args) throws Exception {
        try {
            try (GenericXmlApplicationContext applicationContext = EtlContext.createEtlContext()) {
                for (String config : args) {
                    applicationContext.load(config);
                }
                applicationContext.refresh();
                JobConfigurer configurer = new XmlJobConfigurer(applicationContext);
                Job job = configurer.buildJob();
                job.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
