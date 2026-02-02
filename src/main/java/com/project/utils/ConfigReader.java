package com.project.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigReader {

    private static final Logger logger = LogManager.getLogger(ConfigReader.class);
    private static Properties prop;

    // Load only once
    static {
        try {
            prop = new Properties();
            FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir") + FilePath.getConfigPropertyFilePath()
            );
            prop.load(fis);
            logger.info("Config properties loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load config properties", e);
        }
    }

    // Get property value
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
