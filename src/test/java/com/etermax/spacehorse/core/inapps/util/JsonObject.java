package com.etermax.spacehorse.core.inapps.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonObject {

    public static Object newFromResource(String resourceName, Class objectClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File jsonFile = new File(objectClass.getClassLoader().getResource(resourceName).getFile());
            return mapper.readValue(jsonFile, objectClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
