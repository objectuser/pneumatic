package com.surgingsystems.etl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;

/**
 * Run a job as a Spring Boot application.
 */
@EnableAutoConfiguration
public class BootRunner implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        System.err.println("Running with args: " + args[0]);
        SpringApplication app = new SpringApplication(BootRunner.class, EtlContext.CONTEXT_PATH, "classpath:" + args[0]);
        app.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        JobConfigurer configurer = new XmlJobConfigurer(applicationContext);
        Job job = configurer.buildJob();
        job.start();
    }
}
