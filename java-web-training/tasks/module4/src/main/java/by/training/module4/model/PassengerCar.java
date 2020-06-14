package by.training.module4.model;

import java.util.Objects;

public class PassengerCar extends Car {
    private int passengersCount;

    public PassengerCar(String carName, int carWeight, int carArea, CarType type, int passengersCount) {
        super(carName, carWeight, carArea, type);
        this.passengersCount = passengersCount;
    }

    public int getPassengersCount() {
        return passengersCount;
    }

    public void setPassengersCount(int passengersCount) {
        this.passengersCount = passengersCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassengerCar car = (PassengerCar) o;
        return this.getCarWeight() == car.getCarWeight() &&
                this.getCarArea() == car.getCarArea() &&
                Objects.equals(this.getCarName(), car.getCarName()) &&
                this.getType() == car.getType() &&
                this.passengersCount == car.getPassengersCount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCarName(), getCarWeight(), getCarArea(), getType(), this.passengersCount);
    }

    @Override
    public String toString() {
        return "Car{" +
                "carName='" + getCarName() + '\'' +
                ", carWeight=" + getCarWeight() +
                ", carArea=" + getCarArea() +
                ", type=" + getType() +
                ", passengerCount=" + this.passengersCount +
                '}';
    }

}
