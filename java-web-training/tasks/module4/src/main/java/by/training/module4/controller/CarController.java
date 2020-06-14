package by.training.module4.controller;

import by.training.module4.builder.CarBuilder;
import by.training.module4.builder.CarBuilderFactory;
import by.training.module4.model.Car;
import by.training.module4.model.CarType;
import by.training.module4.validator.FileValidator;
import by.training.module4.validator.ValidationResult;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CarController {

    private static Logger LOGGER = Logger.getLogger(CarController.class);

    private FileValidator fileValidator;
    private DataFileReader reader;
    private LineParser parser;
    private CarBuilderFactory carBuilderFactory;

    public CarController(FileValidator fileValidator, DataFileReader reader, LineParser parser, CarBuilderFactory carBuilderFactory) {
        this.fileValidator = fileValidator;
        this.reader = reader;
        this.parser = parser;
        this.carBuilderFactory = carBuilderFactory;
    }

    public List<Car> readCars(String path) {
        ValidationResult vr = fileValidator.validate(path);

        if (!vr.isValid()) {
            LOGGER.error(vr.getErrorsAsString());
            throw new ControllerException(vr.getErrorsAsString());
        }

        List<String> carsStringList;
        try {
            carsStringList = reader.read(path);
        } catch (DataFileReaderException e) {
            throw new ControllerException("Reading file was unsuccessful.", e);
        }

        List<Car> cars = new ArrayList<>();

        for (String carString : carsStringList) {
            Map<String, String> carMap = parser.parseString(carString);
            CarType type = CarType.valueOf(carMap.get("type"));
            CarBuilder builder = carBuilderFactory.getCarBuilder(type);
            cars.add(builder.build(carMap));
        }

        return cars;

    }

}
