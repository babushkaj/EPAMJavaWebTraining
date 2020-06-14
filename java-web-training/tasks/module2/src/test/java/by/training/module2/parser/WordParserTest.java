package by.training.module2.parser;

import by.trainig.module2.model.WordLeaf;
import by.trainig.module2.parser.WordParser;
import by.trainig.module2.repository.WordLeafRepository;
import by.trainig.module2.service.WordLeafService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class WordParserTest {
    private static final String TEXT =
            "\tIt has survived not only five centuries, but also the leap into electronic " +
                    "typesetting, remaining essentially unchanged.";

    private WordParser parser;

    @Before
    public void setUp() {
        WordLeafRepository repository = new WordLeafRepository();
        WordLeafService service = new WordLeafService(repository);
        parser = new WordParser(service);
    }

    @Test
    public void shouldFindSeventeenWords() {

        List<WordLeaf> sentences = parser.parse(TEXT, 1, 1);

        Assert.assertEquals(sentences.size(), 17);
    }
}
