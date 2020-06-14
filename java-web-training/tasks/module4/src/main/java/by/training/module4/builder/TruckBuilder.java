package by.training.module4.builder;

import by.training.module4.model.CarType;
import by.training.module4.model.Truck;

import java.util.Map;

public class TruckBuilder implements CarBuilder {
    @Override
    public Truck build(Map<String, String> carInfo) {

        String carName = carInfo.get("carName");
        int carWeight = Integer.parseInt(carInfo.get("carWeight"));
        int carArea = Integer.parseInt(carInfo.get("carArea"));
        CarType type = CarType.TRUCK;
        int cargo = Integer.parseInt(carInfo.get("cargo"));
        boolean isFull = Boolean.valueOf(carInfo.get("isFull"));

        return new Truck(carName, carWeight, carArea, type, cargo, isFull);
    }
}
