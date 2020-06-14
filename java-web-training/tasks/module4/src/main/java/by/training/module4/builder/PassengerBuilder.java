package by.training.module4.builder;

import by.training.module4.model.CarType;
import by.training.module4.model.PassengerCar;

import java.util.Map;

public class PassengerBuilder implements CarBuilder {
    @Override
    public PassengerCar build(Map<String, String> carInfo) {

        String carName = carInfo.get("carName");
        int carWeight = Integer.parseInt(carInfo.get("carWeight"));
        int carArea = Integer.parseInt(carInfo.get("carArea"));
        CarType type = CarType.PASSENGER_CAR;
        int passengersCount = Integer.parseInt(carInfo.get("passCount"));

        return new PassengerCar(carName, carWeight, carArea, type, passengersCount);
    }
}
