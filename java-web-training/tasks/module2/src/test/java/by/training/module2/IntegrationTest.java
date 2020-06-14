package by.training.module2;

import by.trainig.module2.controller.ControllerValidationException;
import by.trainig.module2.controller.DataFileReader;
import by.trainig.module2.controller.DataFileReaderException;
import by.trainig.module2.controller.TextController;
import by.trainig.module2.model.ParagraphComposite;
import by.trainig.module2.model.SentenceComposite;
import by.trainig.module2.model.WordLeaf;
import by.trainig.module2.parser.*;
import by.trainig.module2.repository.ParagraphCompositeRepository;
import by.trainig.module2.repository.SentenceCompositeRepository;
import by.trainig.module2.repository.WholeTextCompositeRepository;
import by.trainig.module2.repository.WordLeafRepository;
import by.trainig.module2.service.*;
import by.trainig.module2.validator.FileValidator;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RunWith(JUnit4.class)
public class IntegrationTest {
    public static final String GOOD_DATA_FILE = "good_data.txt";
    public static final String NONEXISTENT_FILE = "nonexistent_file.txt";
    public static final String EMPTY_FILE = "empty_file.txt";
    public static final String FOR_SORTING_FILE = "file_for_sorting.txt";
    private static final Logger LOGGER = Logger.getLogger(IntegrationTest.class);
    private ClassLoader classLoader;
    private String path;

    private WholeTextCompositeService textService;
    private TextController controller;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
        FileValidator fileValidator = new FileValidator();
        DataFileReader fileReader = new DataFileReader();
        WordLeafRepository wordRepository = new WordLeafRepository();
        WordLeafService wordService = new WordLeafService(wordRepository);
        SentenceCompositeRepository sentRepository = new SentenceCompositeRepository();
        SentenceCompositeService sentService = new SentenceCompositeService(sentRepository);
        ParagraphCompositeRepository parRepository = new ParagraphCompositeRepository();
        ParagraphCompositeService parService = new ParagraphCompositeService(parRepository);
        WholeTextCompositeRepository textRepository = new WholeTextCompositeRepository();
        textService = new WholeTextCompositeService(wordService, sentService,
                parService, textRepository);
        WordParser wordParser = new WordParser(wordService);
        SentenceParser sentParser = new SentenceParser(sentService);
        ParagraphParser parParser = new ParagraphParser(parService);
        TextParser textParser = new TextParser(textService);

        ParserChain parserChain = new ParserChain(textParser, parParser, sentParser, wordParser);

        controller = new TextController(fileValidator, fileReader, parserChain);

    }

    @Test
    public void shouldFindFourParagraphsAndPrintText() throws ControllerValidationException, DataFileReaderException {
        path = new File(classLoader.getResource(GOOD_DATA_FILE).getFile()).getAbsolutePath();

        controller.handleFile(path);

        LOGGER.info("\n" + textService.getAllTexts().get(0).getText());
        Assert.assertEquals(4, textService.getAllTexts().get(0).getTextLeaves().size());
    }

    @Test(expected = ControllerValidationException.class)
    public void shouldThrowExceptionCauseOfEmptyFile() throws ControllerValidationException, DataFileReaderException {
        path = new File(classLoader.getResource(EMPTY_FILE).getFile()).getAbsolutePath();

        controller.handleFile(path);
    }

    @Test(expected = ControllerValidationException.class)
    public void shouldThrowExceptionCauseOfNonexistentFile() throws ControllerValidationException, DataFileReaderException {

        controller.handleFile(NONEXISTENT_FILE);
    }

    @Test
    public void shouldPrintSortedWords() throws ControllerValidationException, DataFileReaderException {
        path = new File(classLoader.getResource(FOR_SORTING_FILE).getFile()).getAbsolutePath();

        controller.handleFile(path);

        List<WordLeaf> sorted = textService.sortWordsInSentence(4, 4, new WordByLengthComparator(false));
        StringBuilder sb = new StringBuilder();
        for (WordLeaf w : sorted) {
            sb.append(w.getWord()).append(" ");
        }

        LOGGER.info("\n" + sb.toString());
        Assert.assertEquals(10, sorted.size());
    }

    @Test
    public void shouldPrintSortedSentences() throws ControllerValidationException, DataFileReaderException {
        path = new File(classLoader.getResource(FOR_SORTING_FILE).getFile()).getAbsolutePath();

        controller.handleFile(path);

        List<SentenceComposite> sorted = textService.sortSentences(4, new SentencesByWordsCountComparator(false));
        StringBuilder sb = new StringBuilder();
        for (SentenceComposite s : sorted) {
            sb.append(s.getText()).append("\n");
        }

        LOGGER.info("\n" + sb.toString() + "\n");
        Assert.assertEquals(4, sorted.size());
    }

    @Test
    public void shouldPrintSortedParagraphs() throws ControllerValidationException, DataFileReaderException {
        path = new File(classLoader.getResource(FOR_SORTING_FILE).getFile()).getAbsolutePath();

        controller.handleFile(path);

        List<ParagraphComposite> sorted = textService.sortParagraphs(new ParagraphsBySentencesCountComparator(false));
        StringBuilder sb = new StringBuilder();
        for (ParagraphComposite p : sorted) {
            sb.append(p.getText());
        }

        LOGGER.info("\n" + sb.toString());
        Assert.assertEquals(4, sorted.size());
    }

}
