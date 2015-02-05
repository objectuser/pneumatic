package com.surgingsystems.etl;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class JavaRunner {

    public static void main(String[] args) throws Exception {
        try {
            try (AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                    JavaJobDefaultConfiguration.class, Class.forName(args[0]))) {
                JobConfigurer configurer = applicationContext.getBean(JobConfigurer.class);
                Job job = configurer.buildJob();
                job.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
