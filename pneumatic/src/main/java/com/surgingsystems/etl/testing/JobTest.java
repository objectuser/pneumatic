package com.surgingsystems.etl.testing;

import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("classpath:etl-context.xml")
public @interface JobTest {

    /**
     * Job configurations
     */
    String[] value() default {};
}
