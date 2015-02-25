package com.surgingsystems.etl.filter;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestfulInputFilterDispatcher {

    private static Logger logger = LogManager.getFormatterLogger(RestfulInputFilterDispatcher.class);

    @Autowired
    private ApplicationContext applicationContext;

    private Map<String, RestfulListenerFilter> pathToFilterMap = new HashMap<String, RestfulListenerFilter>();

    @RequestMapping(value = "/{path}", method = RequestMethod.POST)
    public ResponseEntity<String> filter(@PathVariable("path") String path, @RequestBody String json) {

        try {
            logger.trace("Received message on path (%s)", path);
            RestfulListenerFilter filter = pathToFilterMap.get(path);
            if (filter != null) {
                logger.trace("Dispatching to filter (%s)", filter.getName());
                filter.filter(path, json);
                return new ResponseEntity<String>(HttpStatus.ACCEPTED);
            } else {
                logger.warn("No filter found for path (%s), returning %s", path, HttpStatus.NOT_FOUND);
                return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Unable to handle reqest for path (%s), returning %s", path, HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostConstruct
    public void createPathMap() {
        Map<String, RestfulListenerFilter> nameToFilterMap = applicationContext
                .getBeansOfType(RestfulListenerFilter.class);
        for (RestfulListenerFilter filter : nameToFilterMap.values()) {
            pathToFilterMap.put(filter.getPath(), filter);
        }
    }
}
