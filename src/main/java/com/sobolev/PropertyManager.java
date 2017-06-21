package com.sobolev;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class PropertyManager {
    private static PropertyManager propertyManager = null;

    public String getProp(String key) {
        String propertyValue = null;
        Properties prop = new Properties();

        InputStream propertiesInputStream = getClass().getClassLoader()
                .getResourceAsStream("config.properties");
        try {
            prop.load(propertiesInputStream);
            propertyValue = prop.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return propertyValue;
    }

    public static String getProperty(String key) {
        if(propertyManager == null) {
            propertyManager = new PropertyManager();
        }
        return propertyManager.getProp(key);
    }
}
