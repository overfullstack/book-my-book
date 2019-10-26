package com.gakshintala.bookmybook.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/* gakshintala created on 10/27/19 */
@UtilityClass
public class Utils {
    private static final String RESOURCE_PATH = Paths.get(".").toAbsolutePath() + "/./src/main/resources/";
    
    public static <T> T getObjectFromFile(String fileName, Class<T> tClass) {
        var filePath = RESOURCE_PATH + fileName;
        var mapper = new ObjectMapper();
        try {
            var fileContent = fileToString(filePath);
            return mapper.readValue(fileContent, tClass);
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String fileToString(String filePath) {
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException ignored) {
        }
        return "";
    }
}
