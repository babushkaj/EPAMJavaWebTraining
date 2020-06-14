package by.training.module1.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataFileReader {

    private static final Logger LOGGER = LogManager.getLogger(DataFileReader.class);

    public List<String> read(String path) throws IOException {

        List<String> strings = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        excludeEmptyStrings(strings);
        LOGGER.info("DataFileReader has successfully read file \n\'" + path + "\'.\n");

        return strings;
    }

    private void excludeEmptyStrings(List<String> strings) {
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).isEmpty()) {
                strings.remove(i);
            }
        }
    }
}
