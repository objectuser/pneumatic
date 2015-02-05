package com.surgingsystems.etl.filter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestfulInputFilterDispatcher {

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, RestfulListenerFilter> pathToFilterMap = new HashMap<String, RestfulListenerFilter>();

    @RequestMapping(value = "/{path}", method = RequestMethod.POST)
    public String filter(@PathVariable("path") String path, @RequestBody String json) {

        RestfulListenerFilter filter = pathToFilterMap.get(path);
        if (filter != null) {
            filter.filter(path, json);
        }
        return "";
    }

    @PostConstruct
    public void createPathMap() {
        Map<String, RestfulListenerFilter> nameToFilterMap = applicationContext.getBeansOfType(RestfulListenerFilter.class);
        for (RestfulListenerFilter filter : nameToFilterMap.values()) {
            pathToFilterMap.put(filter.getPath(), filter);
        }
    }
}
