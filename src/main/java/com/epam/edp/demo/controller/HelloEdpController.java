package com.epam.edp.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller to expose application endpoints.
 */
@RestController
public class HelloEdpController {

    @Value("${application.properties.from.configmap:}")
    private String configMapContent;

    @Value("${application.secret.properties.from.secret:}")
    private String secretContent;

    @GetMapping(value = "/api/hello")
    public String hello() {
        return "Hello, EDP!";
    }

    @GetMapping(value = "/env")
    public Map<String, Object> getEnvironmentVariables() {
        Map<String, Object> response = new HashMap<>();
        response.put("environmentVariables", System.getenv());
        response.put("application.properties.from.configmap", configMapContent);
        response.put("application.secret.properties.from.secret", secretContent);
        return response;
    }
}
