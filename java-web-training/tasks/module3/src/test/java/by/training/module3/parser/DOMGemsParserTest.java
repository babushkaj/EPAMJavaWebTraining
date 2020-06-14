package by.training.module3.parser;

import by.training.module3.entity.Gem;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.List;

@RunWith(JUnit4.class)
public class DOMGemsParserTest {

    private static final String VALID_XML_PATH = "gems.xml";

    private ClassLoader classLoader;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void shouldReturnSixteenEntities() throws DOMParserException {
        classLoader = getClass().getClassLoader();
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        DOMGemsParser parser = new DOMGemsParser();

        List<Gem> gems = parser.parse(xmlPath);

        Assert.assertEquals(16, gems.size());
    }
}
