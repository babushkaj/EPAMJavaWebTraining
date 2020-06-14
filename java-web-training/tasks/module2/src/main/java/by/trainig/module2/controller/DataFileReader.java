package by.trainig.module2.controller;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataFileReader {
    private static final Logger LOGGER = LogManager.getLogger(DataFileReader.class);

    public String read(String path) throws DataFileReaderException {

        List<String> stringsList;
        try {
            stringsList = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.error("That file hasn't been read.", e);
            throw new DataFileReaderException("That file hasn't been read.", e);
        }
        LOGGER.info("DataFileReader has successfully read file \n\'" + path + "\'.\n");

        return String.join("\n", stringsList);

    }

}
