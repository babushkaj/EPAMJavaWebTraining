package by.trainig.module2.parser;

import by.trainig.module2.model.ParagraphComposite;
import by.trainig.module2.model.TextLeaf;
import by.trainig.module2.service.ParagraphCompositeService;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParagraphParser extends TextPartParser {
    public static final String PARAGRAPH_REGEX = "(\\t|(\\s{4})+)(.+)(\\n+)?";

    private ParagraphCompositeService service;

    public ParagraphParser(ParagraphCompositeService service) {
        this.service = service;
    }

    @Override
    public List<ParagraphComposite> parse(String text, long... par) {
        List<String> paragraphs = new LinkedList<>();
        Pattern paragraphPattern = Pattern.compile(PARAGRAPH_REGEX);
        Matcher matcher = paragraphPattern.matcher(text);

        while (matcher.find()) {
            paragraphs.add(text.substring(matcher.start(), matcher.end()));
        }

        List<ParagraphComposite> paragraphModels = new LinkedList<>();
        if (next != null) {
            long parNum = 1;
            for (String p : paragraphs) {
                ParagraphComposite pc = new ParagraphComposite(parNum);
                for (TextLeaf sent : next.parse(p, parNum++)) {
                    pc.addText(sent);
                }
                service.create(pc);
                paragraphModels.add(pc);
            }
        }
        return paragraphModels;
    }

}
