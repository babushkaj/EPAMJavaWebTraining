package by.trainig.module2.parser;

import by.trainig.module2.model.SentenceComposite;
import by.trainig.module2.model.TextLeaf;
import by.trainig.module2.service.SentenceCompositeService;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceParser extends TextPartParser {
    public static final String SENTENCE_REGEX = "([A-Z])([^\\.\\?!]+)([\\.\\?!]+)";

    private SentenceCompositeService service;

    public SentenceParser(SentenceCompositeService service) {
        this.service = service;
    }

    @Override
    public List<SentenceComposite> parse(String text, long... par) {
        List<String> sentences = new LinkedList<>();
        Pattern paragraphPattern = Pattern.compile(SENTENCE_REGEX);
        Matcher matcher = paragraphPattern.matcher(text);
        while (matcher.find()) {
            sentences.add(text.substring(matcher.start(), matcher.end()));
        }
        List<SentenceComposite> sentenceModels = new LinkedList<>();
        if (next != null) {
            long sentNum = 1;
            for (String s : sentences) {
                SentenceComposite sc = new SentenceComposite(sentNum, par[0]);
                for (TextLeaf word : next.parse(s, sentNum++, par[0])) {
                    sc.addText(word);
                }
                service.create(sc);
                sentenceModels.add(sc);
            }
        }
        return sentenceModels;
    }

}
