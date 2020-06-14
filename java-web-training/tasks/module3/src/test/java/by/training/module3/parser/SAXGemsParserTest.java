package by.training.module3.parser;

import by.training.module3.entity.Gem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class SAXGemsParserTest {
    private static final String VALID_XML_PATH = "gems.xml";

    private ClassLoader classLoader;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void shouldReturnSixteenEntities() throws SAXParserException {
        classLoader = getClass().getClassLoader();
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        SAXGemsParser parser = new SAXGemsParser();

        List<Gem> gems = parser.parse(xmlPath);

        Assert.assertEquals(16, gems.size());
    }
}
