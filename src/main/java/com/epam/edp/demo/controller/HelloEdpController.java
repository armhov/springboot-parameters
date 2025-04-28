package com.epam.edp.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@RestController
public class HelloEdpController {

    @Value("${application.properties.path:/config/application.properties}")
    private String configMapConfigPath;

    @Value("${application.secret.properties.path:/secret-config/application.secret.properties}")
    private String secretConfigPath;

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        Map<String, String> env = new HashMap<>();

        // Read ConfigMap and Secret files for envFrom variables
        Map<String, String> configMapData = readPropertiesFromFile(configMapConfigPath);
        Map<String, String> secretData = readPropertiesFromFile(secretConfigPath);

        // Read environment variables from System.getenv()
        System.getenv().forEach((key, value) -> {
            if (configMapData.containsKey(key) || secretData.containsKey(key)) {
                env.put(key, value);
            } else {
                env.put(key, value);
            }
        });

        // Add custom.config from ConfigMap
        addConfigFileToEnv(env, configMapConfigPath, "application.properties.from.configmap");

        // Add custom.config from Secret
        addConfigFileToEnv(env, secretConfigPath, "application.secret.properties.from.secret");

        return env;
    }

    private Map<String, String> readPropertiesFromFile(String filePath) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] lines = fileContent.split("\\r?\\n");
            Map<String, String> properties = new HashMap<>();
            for (String line : lines) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    properties.put(key, value);
                }
            }
            return properties;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void addConfigFileToEnv(Map<String, String> env, String filePath, String envKey) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            env.put(envKey, fileContent);
        } catch (IOException e) {
            env.put(envKey, "File not found or unreadable");
        }
    }
}

