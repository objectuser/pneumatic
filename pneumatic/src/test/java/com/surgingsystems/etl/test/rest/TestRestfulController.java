package com.surgingsystems.etl.test.rest;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestfulController {

    private Map<String, String> symbolMap = new TreeMap<String, String>();

    @RequestMapping("/symbol/{name}")
    public String doSomething(@PathVariable String name) {
        return String.format("{\"symbol\": \"%s\"}", symbolMap.get(name));
    }
    
    @PostConstruct
    public void createMap() {
        symbolMap.put("Big Company", "BIG");
        symbolMap.put("Small Company", "SMAL");
        symbolMap.put("Medium Company", "MEDM");
    }
}
