package by.training.module1.controller;

import by.training.module1.parser.LineParser;
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

@RunWith(JUnit4.class)
public class RecordControllerTest {

    public static final String PERFECT_FILE = "perfectFile.txt";
    public static final String EMPTY_FILE = "emptyFile.txt";
    public static final String NONEXISTENT_FILE = "nonexistentFile.txt";
    public static final String MISTAKES_IN_THREE_STRINGS_FILE = "mistakesInThreeStrings.txt";
    public static final String MISTAKES_IN_ALL_STRINGS_FILE = "mistakesInAllStrings.txt";

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
    public void setUp(){
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

    }

    @Test
    public void withPerfectFile() throws IOException {
        path = new File(classLoader.getResource(PERFECT_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);

        Assert.assertEquals(9, repository.getAllRecords().size());
    }

    @Test(expected = IOException.class)
    public void withEmptyFile() throws IOException {
        path = new File(classLoader.getResource(EMPTY_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);

        Assert.assertEquals(0, repository.getAllRecords().size());
    }

    @Test(expected = IOException.class)
    public void withNonexistentFile() throws IOException {
        controller.processFile(NONEXISTENT_FILE);

        Assert.assertEquals(0, repository.getAllRecords().size());
    }

    @Test
    public void withFileWithMistakesInThreeStrings() throws IOException {
        path = new File(classLoader.getResource(MISTAKES_IN_THREE_STRINGS_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);

        Assert.assertEquals(6, repository.getAllRecords().size());
    }

    @Test
    public void withFileWithMistakesInAllStrings() throws IOException {
        path = new File(classLoader.getResource(MISTAKES_IN_ALL_STRINGS_FILE).getFile()).getAbsolutePath();
        controller.processFile(path);

        Assert.assertEquals(0, repository.getAllRecords().size());
    }

}
