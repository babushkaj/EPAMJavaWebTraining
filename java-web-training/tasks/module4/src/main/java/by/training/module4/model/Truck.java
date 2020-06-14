package by.training.module4.model;

import java.util.Objects;

public class Truck extends Car {
    private int cargo;
    private boolean isFull;

    public Truck(String carName, int carWeight, int carArea, CarType type, int cargo, boolean isFull) {
        super(carName, carWeight, carArea, type);
        this.cargo = cargo;
        this.isFull = isFull;
    }

    public double getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Truck car = (Truck) o;
        return this.getCarWeight() == car.getCarWeight() &&
                this.getCarArea() == car.getCarArea() &&
                Objects.equals(this.getCarName(), car.getCarName()) &&
                this.getType() == car.getType() &&
                this.cargo == car.getCargo() &&
                this.isFull == car.isFull();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCarName(), getCarWeight(), getCarArea(), getType(), this.cargo, this.isFull);
    }

    @Override
    public String toString() {
        return "Car{" +
                "carName='" + getCarName() + '\'' +
                ", carWeight=" + getCarWeight() +
                ", carArea=" + getCarArea() +
                ", type=" + getType() +
                ", cargo=" + this.cargo +
                ", isFull=" + this.isFull +
                '}';
    }
}
