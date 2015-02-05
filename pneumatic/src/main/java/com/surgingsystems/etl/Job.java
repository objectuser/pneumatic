package com.surgingsystems.etl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Job {

    private static Logger logger = LogManager.getFormatterLogger(Job.class);

    private Container container;

    public Job(Container container) {
	this.container = container;
    }

    public void start() {
	try {
	    logger.info("Starting container");
	    container.start();
	    logger.info("Container started");
	    container.waitForCompletion();
	} catch (Exception exception) {
	    logger.error("Error running job", exception);
	}
    }

}
