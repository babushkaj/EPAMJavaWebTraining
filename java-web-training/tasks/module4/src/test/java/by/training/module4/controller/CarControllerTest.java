package by.training.module4.controller;

import by.training.module4.builder.CarBuilderFactory;
import by.training.module4.model.Car;
import by.training.module4.validator.FileValidator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.util.List;

@RunWith(JUnit4.class)
public class CarControllerTest {

    private static String CARS_FILE = "cars.txt";

    private CarController controller;
    private String path;

    @Before
    public void setUp() {
        FileValidator fileValidator = new FileValidator();
        DataFileReader reader = new DataFileReader();
        LineParser parser = new LineParser();
        CarBuilderFactory carBuilderFactory = new CarBuilderFactory();
        controller = new CarController(fileValidator, reader, parser, carBuilderFactory);
        ClassLoader classLoader = getClass().getClassLoader();
        path = new File(classLoader.getResource(CARS_FILE).getFile()).getAbsolutePath();
    }

    @Test
    public void shouldFindEightCars() {
        List<Car> cars = controller.readCars(path);
        Assert.assertEquals(cars.size(), 8);
    }

}
