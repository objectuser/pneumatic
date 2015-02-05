package com.surgingsystems.etl;

import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.GenericXmlApplicationContext;

@ImportResource(EtlContext.CONTEXT_PATH)
public class EtlContext {
    
    public static final String CONTEXT_PATH = "classpath:etl-context.xml";
    
    public static GenericXmlApplicationContext createEtlContext() {
        GenericXmlApplicationContext context = new GenericXmlApplicationContext();
        context.load(CONTEXT_PATH);
        return context;
    }

}
