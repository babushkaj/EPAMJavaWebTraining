package by.training.module3.parser;

import by.training.module3.entity.Gem;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

public class SAXGemsParser implements Parser<Gem> {
    public List<Gem> parse(String xmlPath) throws SAXParserException {
        SAXParserFactory parserFactor = SAXParserFactory.newInstance();
        SAXParser parser;

        try {
            parser = parserFactor.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new SAXParserException(e);
        }

        SAXHandler handler = new SAXHandler();
        try {
            parser.parse(xmlPath, handler);
        } catch (SAXException | IOException e) {
            throw new SAXParserException(e);
        }

        return handler.getGems();
    }
}
