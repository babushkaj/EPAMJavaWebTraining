package by.training.module2.parser;

import by.trainig.module2.model.WholeTextComposite;
import by.trainig.module2.parser.*;
import by.trainig.module2.repository.ParagraphCompositeRepository;
import by.trainig.module2.repository.SentenceCompositeRepository;
import by.trainig.module2.repository.WholeTextCompositeRepository;
import by.trainig.module2.repository.WordLeafRepository;
import by.trainig.module2.service.ParagraphCompositeService;
import by.trainig.module2.service.SentenceCompositeService;
import by.trainig.module2.service.WholeTextCompositeService;
import by.trainig.module2.service.WordLeafService;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class ParserChainTest {

    private static final Logger LOGGER = Logger.getLogger(ParserChainTest.class);

    private static final String TEXT =
            "\tIt has survived not only five centuries, but also the leap into electronic " +
                    "typesetting, remaining essentially unchanged. It was popularised in the with the " +
                    "release of Letraset sheets containing Lorem Ipsum passages, and more recently with " +
                    "desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\n" +
                    "\tIt is a long established fact that a reader will be distracted by the readable " +
                    "content of a page when looking at its layout. The point of using Ipsum is that it has a " +
                    "more-or-less normal distribution of letters, as opposed to using 'Content here, content " +
                    "here', making it look like readable English.\n" +
                    "\tIt is a established fact that a reader will be of a page when looking at its layout.\n" +
                    "\tBye.";

    private ParserChain parserChain;

    @Before
    public void setUp() {
        WordLeafRepository wordRepository = new WordLeafRepository();
        WordLeafService wordService = new WordLeafService(wordRepository);
        SentenceCompositeRepository sentRepository = new SentenceCompositeRepository();
        SentenceCompositeService sentService = new SentenceCompositeService(sentRepository);
        ParagraphCompositeRepository parRepository = new ParagraphCompositeRepository();
        ParagraphCompositeService parService = new ParagraphCompositeService(parRepository);
        WholeTextCompositeRepository textRepository = new WholeTextCompositeRepository();
        WholeTextCompositeService textService = new WholeTextCompositeService(wordService, sentService,
                parService, textRepository);
        WordParser wordParser = new WordParser(wordService);
        SentenceParser sentParser = new SentenceParser(sentService);
        ParagraphParser parParser = new ParagraphParser(parService);
        TextParser textParser = new TextParser(textService);

        parserChain = new ParserChain(textParser, parParser, sentParser, wordParser);
    }

    @Test
    public void shouldFindFourParagraphs() {

        List<WholeTextComposite> texts = parserChain.doChainParsing(TEXT);

        LOGGER.info("\n" + texts.get(0).getText());
        Assert.assertEquals(texts.get(0).getTextLeaves().size(), 4);
    }

}
