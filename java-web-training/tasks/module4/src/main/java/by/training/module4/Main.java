package by.training.module4;

import by.training.module4.builder.CarBuilderFactory;
import by.training.module4.builder.FerryBuilder;
import by.training.module4.controller.CarController;
import by.training.module4.controller.DataFileReader;
import by.training.module4.controller.FerryController;
import by.training.module4.controller.LineParser;
import by.training.module4.model.Car;
import by.training.module4.model.Ferry;
import by.training.module4.validator.FileValidator;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final String CARS_FILE = "cars.txt";
    private static final String FERRY_FILE = "ferry.txt";

//    cars and ferry with simple values
//    private static final String CARS_FILE = "simple_cars.txt";
//    private static final String FERRY_FILE = "ferry_for_simple_cars.txt";

    public static void main(String[] args) {

        FileValidator fileValidator = new FileValidator();
        DataFileReader reader = new DataFileReader();
        LineParser parser = new LineParser();
        CarBuilderFactory carBuilderFactory = new CarBuilderFactory();
        FerryBuilder ferryBuilder = new FerryBuilder();
        CarController carController = new CarController(fileValidator, reader, parser, carBuilderFactory);
        FerryController ferryController = new FerryController(fileValidator, reader, parser, ferryBuilder);
        ClassLoader classLoader = Main.class.getClassLoader();
        String carsPath = new File(classLoader.getResource(CARS_FILE).getFile()).getAbsolutePath();
        String ferryPath = new File(classLoader.getResource(FERRY_FILE).getFile()).getAbsolutePath();

        List<Car> cars = carController.readCars(carsPath);
        Ferry ferry = ferryController.readFerry(ferryPath);

        Thread ferryThread = new Thread(ferry);
        ferryThread.start();

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (Car car : cars) {
            car.setFerry(ferry);
            executorService.submit(car);
        }

        executorService.shutdown();

    }
}
