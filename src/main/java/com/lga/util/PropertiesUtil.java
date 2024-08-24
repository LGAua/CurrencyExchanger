package com.lga.util;

import com.lga.exceptions.FailedToInitializePropertyFile;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Properties;

@UtilityClass
public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        initializePropertyFile();
    }

    public static String getDataBasePath() {
        String prefix = PROPERTIES.getProperty("db.driver.prefix");
        String resourcePath = PROPERTIES.getProperty("db.resource.path");

        if (prefix == null || resourcePath == null) {
            throw new FailedToInitializePropertyFile("Missing required properties for database path configuration.");
        }
        String absolutePath;
        try {
            absolutePath = Path.of(PropertiesUtil.class.getClassLoader().getResource(resourcePath).toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to load database absolute path");
        }
        return prefix + absolutePath;
    }

    public static String getDriverClassName(){
        return PROPERTIES.getProperty("db.driver.name");
    }

    private static void initializePropertyFile() {
        try (InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new FailedToInitializePropertyFile("Failed to initialize property file");
        }
    }
}
