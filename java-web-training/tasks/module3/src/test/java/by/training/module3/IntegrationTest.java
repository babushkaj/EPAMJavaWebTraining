package by.training.module3;

import by.training.module3.annotation.ValidAnnotationHandler;
import by.training.module3.command.*;
import by.training.module3.controller.ControllerException;
import by.training.module3.controller.GemsController;
import by.training.module3.entity.Gem;
import by.training.module3.parser.DOMGemsParser;
import by.training.module3.parser.SAXGemsParser;
import by.training.module3.parser.StAXGemsParser;
import by.training.module3.repository.GemsRepositoryImpl;
import by.training.module3.service.GemsService;
import by.training.module3.validation.FileValidator;
import by.training.module3.validation.XMLbyXSDValidator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;

@RunWith(JUnit4.class)
public class IntegrationTest {
    private static final Logger LOGGER = LogManager.getLogger(IntegrationTest.class);

    private static final String VALID_XML_PATH = "gems.xml";
    private static final String INVALID_XML_PATH = "invalid_gems.xml";
    private static final String EMPTY_XML_PATH = "empty.xml";
    private static final String XSD_PATH = "gems.xsd";

    private ClassLoader classLoader;

    private GemsService service;
    private GemsController controller;

    @Before
    public void setUp() {
        classLoader = getClass().getClassLoader();
        String xsdPath = new File(classLoader.getResource(XSD_PATH).getFile()).getAbsolutePath();

        XMLbyXSDValidator xmlValidator = new XMLbyXSDValidator(xsdPath);

        ParserCommand<Gem> DOMParserCommand = new ParserCommand<>(xmlValidator, new DOMGemsParser());
        ParserCommand<Gem> SAXParserCommand = new ParserCommand<>(xmlValidator, new SAXGemsParser());
        ParserCommand<Gem> stAXParserCommand = new ParserCommand<>(xmlValidator, new StAXGemsParser());
        ParserCommandProvider provider = new ParserCommandProvider();
        provider.register(CommandType.DOM, DOMParserCommand);
        provider.register(CommandType.SAX, SAXParserCommand);
        provider.register(CommandType.STAX, stAXParserCommand);

        GemsRepositoryImpl repository = new GemsRepositoryImpl();
        service = new GemsService(repository);

        FileValidator fileValidator = new FileValidator();
        ValidAnnotationHandler handler = new ValidAnnotationHandler();

        controller = new GemsController(fileValidator, provider, service, handler);

    }

    @Test
    public void shouldReturnSevenEntitiesDOM() {
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.DOM);

        service.getAll().forEach(LOGGER::info);

        Assert.assertEquals(7, service.getAll().size());
    }

    @Test
    public void shouldReturnSevenEntitiesSAX() {
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.SAX);

        service.getAll().forEach(LOGGER::info);

        Assert.assertEquals(7, service.getAll().size());
    }

    @Test
    public void shouldReturnSevenEntitiesStAX() {
        String xmlPath = new File(classLoader.getResource(VALID_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.STAX);

        service.getAll().forEach(LOGGER::info);

        Assert.assertEquals(7, service.getAll().size());
    }

    @Test(expected = ControllerException.class)
    public void shouldThrowAnExceptionDOM() {

        String xmlPath = new File(classLoader.getResource(INVALID_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.DOM);

    }

    @Test(expected = ControllerException.class)
    public void shouldThrowAnExceptionSAX() {

        String xmlPath = new File(classLoader.getResource(INVALID_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.SAX);

    }

    @Test(expected = ControllerException.class)
    public void shouldThrowAnExceptionStAX() {

        String xmlPath = new File(classLoader.getResource(INVALID_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.STAX);

    }

    @Test
    public void couldPrintMessageEmptyFileDOM() {

        String xmlPath = new File(classLoader.getResource(EMPTY_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.DOM);

        Assert.assertEquals(0, service.getAll().size());

    }

    @Test
    public void couldPrintMessageEmptyFileSAX() {

        String xmlPath = new File(classLoader.getResource(EMPTY_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.SAX);

        Assert.assertEquals(0, service.getAll().size());

    }

    @Test
    public void couldPrintMessageEmptyFileStAX() {

        String xmlPath = new File(classLoader.getResource(EMPTY_XML_PATH).getFile()).getAbsolutePath();

        controller.processFile(xmlPath, CommandType.STAX);

        Assert.assertEquals(0, service.getAll().size());

    }
}
