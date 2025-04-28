package com.epam.edp.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Pavlo_Yemelianov
 */
@RestController
public class HelloEdpController {

    // Значения из файла application.properties
    @Value("${server.port}")
    private String serverPort;

    @Value("${logging.level.root}")
    private String loggingLevelRoot;

    // Значения из ConfigMap и Secret
    @Value("${application.properties.from.configmap:}")
    private String applicationPropertiesFromConfigMap;

    @Value("${application.secret.properties.from.secret:}")
    private String applicationSecretPropertiesFromSecret;

    @GetMapping(value = "/api/hello")
    public String hello() {
        return "Hello, EDP!";
    }
    
    @GetMapping(value = "/env")
    @ResponseBody
    public Map<String, Object> env() {
        List<String> requiredKeys = List.of(
                "ENV_VAR1",
                "ENV_VAR2",
                "ENV_VAR3",
                "ENV_VAR4",
                "ENV_VAR5",
                "SPRING_PROFILES_ACTIVE"
        );

        Map<String, Object> filteredEnv = System.getenv().entrySet().stream()
                .filter(entry -> requiredKeys.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (v1, v2) -> v1, HashMap::new));

        filteredEnv.put("server.port", serverPort);
        filteredEnv.put("logging.level.root", loggingLevelRoot);

        filteredEnv.put("application.properties.from.configmap",
                applicationPropertiesFromConfigMap.isEmpty() ? "Not configured" : applicationPropertiesFromConfigMap);
        filteredEnv.put("application.secret.properties.from.secret",
                applicationSecretPropertiesFromSecret.isEmpty() ? "Not configured" : applicationSecretPropertiesFromSecret);

        return filteredEnv;
    }
}
