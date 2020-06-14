package by.training.module1;

import by.training.module1.controller.DataFileReader;
import by.training.module1.controller.RecordBuilderFactory;
import by.training.module1.controller.RecordController;
import by.training.module1.model.*;
import by.training.module1.parser.LineParser;
import by.training.module1.repository.ByDurationFromRecordSpecification;
import by.training.module1.repository.ByDurationToRecordSpecification;
import by.training.module1.repository.ByStyleComparator;
import by.training.module1.repository.RecordRepository;
import by.training.module1.service.RecordService;
import by.training.module1.validator.FileValidator;
import by.training.module1.validator.FormatValidator;
import by.training.module1.validator.LineValidatorFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class IntegrationTest {

    public static final String PERFECT_FILE = "perfectFile.txt";
    public static final String EMPTY_FILE = "emptyFile.txt";
    public static final String NONEXISTENT_FILE = "nonexistentFile.txt";
    public static final String MISTAKES_IN_THREE_STRINGS_FILE = "mistakesInThreeStrings.txt";
    public static final String MISTAKES_IN_ALL_STRINGS_FILE = "mistakesInAllStrings.txt";

    private static final int DURATION_FROM = 240;
    private static final int DURATION_TO = 260;

    public static Record expectedFirstRecord;

    private ClassLoader classLoader;
    private String path;

    private RecordRepository repository;
    private RecordService recordService;

    private FormatValidator formatValidator;
    private FileValidator fileValidator;
    private LineValidatorFactory lineValidatorFactory;
    private DataFileReader dataFileReader;
    private LineParser lineParser;
    private RecordBuilderFactory factory;

    private RecordController controller;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();

        repository = new RecordRepository();
        recordService = new RecordService(repository);

        formatValidator = new FormatValidator();
        fileValidator = new FileValidator();
        lineValidatorFactory = new LineValidatorFactory();
        dataFileReader = new DataFileReader();
        lineParser = new LineParser();
        factory = new RecordBuilderFactory();

        controller = new RecordController(recordService, formatValidator, fileValidator, lineValidatorFactory,
                                          dataFileReader, lineParser, factory);

        Map<String, String> spearsID3Tags = new HashMap<>();
        spearsID3Tags.put("year", "2000");
        expectedFirstRecord = new MP3Record("Britney Spears", "Oops!...I Did It Again",
                251, Style.POP, Compression.HIGH, spearsID3Tags);

    }

    @Test
    public void withPerfectFileService() throws IOException {
        path = new File(classLoader.getResource(PERFECT_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);
        int duration = recordService.getDuration();
        List<Record> sortedRecordList = recordService.sortBy(new ByStyleComparator());
        List<Record> byDurationRecordsList = recordService.find(new ByDurationFromRecordSpecification(DURATION_FROM)
                .and(new ByDurationToRecordSpecification(DURATION_TO)));

        Assert.assertEquals(duration, 2497);
        Assert.assertEquals(sortedRecordList.get(0), expectedFirstRecord);
        Assert.assertEquals(byDurationRecordsList.size(), 5);
    }

    @Test(expected = IOException.class)
    public void withEmptyFileAllService() throws IOException {
        path = new File(classLoader.getResource(EMPTY_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);
        int duration = recordService.getDuration();
        List<Record> sortedRecordList = recordService.sortBy(new ByStyleComparator());
        List<Record> byDurationRecordsList = recordService.find(new ByDurationFromRecordSpecification(DURATION_FROM)
                .and(new ByDurationToRecordSpecification(DURATION_TO)));

        Assert.assertEquals(duration, 0);
        Assert.assertTrue(sortedRecordList.isEmpty());
        Assert.assertTrue(byDurationRecordsList.isEmpty());
    }

    @Test(expected = IOException.class)
    public void withNonexistentFileAllService() throws IOException {
        controller.processFile(NONEXISTENT_FILE);
        int duration = recordService.getDuration();
        List<Record> sortedRecordList = recordService.sortBy(new ByStyleComparator());
        List<Record> byDurationRecordsList = recordService.find(new ByDurationFromRecordSpecification(DURATION_FROM)
                .and(new ByDurationToRecordSpecification(DURATION_TO)));

        Assert.assertEquals(duration, 0);
        Assert.assertTrue(sortedRecordList.isEmpty());
        Assert.assertTrue(byDurationRecordsList.isEmpty());
    }

    @Test
    public void withMistakesInThreeStringsFileService() throws IOException {
        path = new File(classLoader.getResource(MISTAKES_IN_THREE_STRINGS_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);
        int duration = recordService.getDuration();
        List<Record> sortedRecordList = recordService.sortBy(new ByStyleComparator());
        List<Record> byDurationRecordsList = recordService.find(new ByDurationFromRecordSpecification(DURATION_FROM)
                .and(new ByDurationToRecordSpecification(DURATION_TO)));

        Assert.assertEquals(duration, 1680);
        Assert.assertEquals(sortedRecordList.get(0),
                new CDDARecord("Tech N9ne", "Uralya", 248, Style.RAP, Compression.LOW));
        Assert.assertEquals(byDurationRecordsList.size(), 3);
    }

    @Test
    public void withMistakesInAllStringsFileService() throws IOException {
        path = new File(classLoader.getResource(MISTAKES_IN_ALL_STRINGS_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);
        int duration = recordService.getDuration();
        List<Record> sortedRecordList = recordService.sortBy(new ByStyleComparator());
        List<Record> byDurationRecordsList = recordService.find(new ByDurationFromRecordSpecification(DURATION_FROM)
                .and(new ByDurationToRecordSpecification(DURATION_TO)));

        Assert.assertEquals(duration, 0);
        Assert.assertTrue(sortedRecordList.isEmpty());
        Assert.assertTrue(byDurationRecordsList.isEmpty());
    }
}
