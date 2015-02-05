package com.surgingsystems.etl;

import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.surgingsystems.etl.filter.Filter;

public class XmlJobConfigurer implements JobConfigurer {

    private ApplicationContext applicationContext;

    public XmlJobConfigurer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Job buildJob() {
        Container container = new Container();

        Map<String, Filter> configuredFilters = applicationContext.getBeansOfType(Filter.class);
        for (Filter filter : configuredFilters.values()) {
            container.addFilter(filter);
        }

        return new Job(container);
    }

}
