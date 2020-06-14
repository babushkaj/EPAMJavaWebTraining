package by.training.module3.command;

import by.training.module3.entity.Gem;
import by.training.module3.validation.XMLbyXSDValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.List;

@RunWith(JUnit4.class)
public class StAXParserCommandTest {
    private static final Logger LOGGER = LogManager.getLogger(StAXParserCommandTest.class);

    private static final String VALID_XML_PATH = "gems.xml";
    private static final String INVALID_XML_PATH = "invalid_gems.xml";
    private static final String XSD_PATH = "gems.xsd";

    private ClassLoader classLoader;

    private StAXParserCommand command;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
        String xsdPath = new File(classLoader.getResource(XSD_PATH).getFile()).getAbsolutePath();

        XMLbyXSDValidator validator = new XMLbyXSDValidator(xsdPath);

        command = new StAXParserCommand(validator);
    }

    @Test
    public void shouldReturnSixteenEntities() throws CommandException {
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        List<Gem> gems = command.build(xmlPath);

        gems.forEach(LOGGER::info);

        Assert.assertEquals(16, gems.size());
    }

    @Test(expected = CommandException.class)
    public void shouldThrowAnException() throws CommandException {
        String xmlPath = new File(classLoader.getResource(INVALID_XML_PATH).getFile()).getAbsolutePath();

        command.build(xmlPath);
    }


}
