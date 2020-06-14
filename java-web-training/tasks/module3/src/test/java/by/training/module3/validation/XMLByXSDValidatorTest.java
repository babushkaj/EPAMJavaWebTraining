package by.training.module3.validation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

@RunWith(JUnit4.class)
public class XMLByXSDValidatorTest {

    private static final String VALID_XML_PATH = "gems.xml";
    private static final String INVALID_XML_PATH = "invalid_gems.xml";
    private static final String XSD_PATH = "gems.xsd";

    private ClassLoader classLoader;

    private XMLbyXSDValidator validator;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
        String xsdPath = new File(classLoader.getResource(XSD_PATH).getFile()).getAbsolutePath();

        validator = new XMLbyXSDValidator(xsdPath);
    }

    @Test
    public void shouldReturnTrue() {
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        boolean result = validator.validate(xmlPath);

        Assert.assertTrue(result);
    }

    @Test
    public void shouldReturnFalse() {
        String xmlPath = new File(classLoader.getResource(INVALID_XML_PATH).getFile()).getAbsolutePath();

        boolean result = validator.validate(xmlPath);

        Assert.assertFalse(result);
    }
}
