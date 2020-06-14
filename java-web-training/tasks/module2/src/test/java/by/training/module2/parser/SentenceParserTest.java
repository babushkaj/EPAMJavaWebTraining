package by.training.module2.parser;

import by.trainig.module2.model.SentenceComposite;
import by.trainig.module2.parser.SentenceParser;
import by.trainig.module2.parser.WordParser;
import by.trainig.module2.repository.SentenceCompositeRepository;
import by.trainig.module2.repository.WordLeafRepository;
import by.trainig.module2.service.SentenceCompositeService;
import by.trainig.module2.service.WordLeafService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class SentenceParserTest {


    private static final String TEXT =
            "\tIt has survived not only five centuries, but also the leap into electronic\n " +
                    "typesetting, remaining essentially unchanged. It was popularised in the with the\n " +
                    "release of Letraset sheets containing Lorem Ipsum passages, and more recently with\n " +
                    "desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n";

    private SentenceParser sentParser;

    @Before
    public void setUp() {
        WordLeafRepository wordRepository = new WordLeafRepository();
        WordLeafService wordService = new WordLeafService(wordRepository);
        SentenceCompositeRepository sentRepository = new SentenceCompositeRepository();
        SentenceCompositeService sentService = new SentenceCompositeService(sentRepository);
        WordParser wordParser = new WordParser(wordService);
        sentParser = new SentenceParser(sentService);
        sentParser.setNext(wordParser);

    }

    @Test
    public void shouldFindTwoSentences() {

        List<SentenceComposite> sentences = sentParser.parse(TEXT, 1);

        Assert.assertEquals(sentences.size(), 2);
    }

}
