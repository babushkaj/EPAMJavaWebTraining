package by.trainig.module2.parser;

import by.trainig.module2.model.WordLeaf;
import by.trainig.module2.service.WordLeafService;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordParser extends TextPartParser {

    public static final String WORD_REGEX = "(['\\(]+)?([\\w\\-\\']+)([\\.\\?!,'\\)]+)?";
    private WordLeafService service;

    public WordParser(WordLeafService service) {
        this.service = service;
    }

    @Override
    public List<WordLeaf> parse(String text, long... par) {
        List<WordLeaf> wordModels = new LinkedList<>();
        Pattern paragraphPattern = Pattern.compile(WORD_REGEX);
        Matcher matcher = paragraphPattern.matcher(text);
        int num = 1;
        while (matcher.find()) {
            WordLeaf wl = new WordLeaf(num++, par[0], par[1], matcher.group(1), matcher.group(2), matcher.group(3));
            service.create(wl);
            wordModels.add(wl);
        }
        return wordModels;
    }
}
