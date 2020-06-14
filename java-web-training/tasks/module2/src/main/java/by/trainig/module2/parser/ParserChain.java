package by.trainig.module2.parser;

import by.trainig.module2.model.WholeTextComposite;

import java.util.List;

public class ParserChain {

    private TextParser textParser;
    private ParagraphParser paragraphParser;
    private SentenceParser sentenceParser;
    private WordParser wordParser;

    public ParserChain(TextParser textParser, ParagraphParser paragraphParser,
                       SentenceParser sentenceParser, WordParser wordParser) {
        this.textParser = textParser;
        this.paragraphParser = paragraphParser;
        this.sentenceParser = sentenceParser;
        this.wordParser = wordParser;
        this.textParser.setNext(this.paragraphParser);
        this.paragraphParser.setNext(this.sentenceParser);
        this.sentenceParser.setNext(this.wordParser);
    }

    public List<WholeTextComposite> doChainParsing(String text) {
        return textParser.parse(text);
    }
}
