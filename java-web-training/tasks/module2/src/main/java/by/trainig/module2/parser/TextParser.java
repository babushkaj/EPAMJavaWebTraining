package by.trainig.module2.parser;

import by.trainig.module2.model.TextLeaf;
import by.trainig.module2.model.WholeTextComposite;
import by.trainig.module2.service.WholeTextCompositeService;

import java.util.LinkedList;
import java.util.List;

public class TextParser extends TextPartParser {

    private WholeTextCompositeService service;

    public TextParser(WholeTextCompositeService service) {
        this.service = service;
    }

    @Override
    List<WholeTextComposite> parse(String text, long... par) {
        List<WholeTextComposite> textComposites = new LinkedList<>();
        if (next != null) {
            WholeTextComposite newText = new WholeTextComposite();
            for (TextLeaf paragraph : next.parse(text)) {
                newText.addText(paragraph);
            }
            service.create(newText);
            textComposites.add(newText);
        }
        return textComposites;
    }
}
