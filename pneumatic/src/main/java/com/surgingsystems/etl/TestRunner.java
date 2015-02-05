package com.surgingsystems.etl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.surgingsystems.etl.testing.EtlTest;

public class TestRunner {

    private static Logger logger = LogManager.getFormatterLogger(TestRunner.class);

    public static void main(String[] args) {
        try {
            try (GenericXmlApplicationContext applicationContext = EtlContext.createEtlContext()) {
                for (String config : args) {
                    applicationContext.load(config);
                }
                applicationContext.refresh();

                Map<String, EtlTest> tests = applicationContext.getBeansOfType(EtlTest.class);
                for (Map.Entry<String, EtlTest> entry : tests.entrySet()) {
                    try {
                        logger.info("Running test: %s", entry.getKey());
                        EtlTest test = entry.getValue();
                        test.run();
                    } catch (Exception e) {
                        logger.error("Error in test: %s - %s", entry.getKey(), e.getMessage());
                        logger.catching(e);
                    }
                }
            }
        } catch (Exception e) {
            logger.catching(e);
        }
    }
}
