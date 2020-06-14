package by.training.module4.builder;

import by.training.module4.model.CarType;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class CarBuilderFactory {

    private static final Logger LOGGER = LogManager.getLogger(CarBuilderFactory.class);

    public CarBuilder getCarBuilder(CarType type) {
        switch (type) {
            case PASSENGER_CAR: {
                return new PassengerBuilder();
            }
            case TRUCK: {
                return new TruckBuilder();
            }
            default: {
                LOGGER.error("Unknown format in CarBuilderFactory");
                throw new BuilderException("Unknown format in CarBuilderFactory");
            }
        }
    }

}
