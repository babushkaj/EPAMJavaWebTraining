package by.training.module4.model;


import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

public abstract class Car implements Runnable {
    private static Logger LOGGER = Logger.getLogger(Car.class);
    private String carName;
    private int carWeight;
    private int carArea;
    private CarType type;
    private Ferry ferry;
    private boolean onBoard;

    public Car(String carName, int carWeight, int carArea, CarType type) {
        this.carName = carName;
        this.carWeight = carWeight;
        this.carArea = carArea;
        this.type = type;
    }

    private void tryLoadToFerry() {
        while (!onBoard) {
            try {
                if (ferry.isFerryIsSailing()) {
                    TimeUnit.SECONDS.sleep(1);
                } else {
                    if (ferry.tryLoadCar(this)) {
                        LOGGER.info("Car " + this.carName + " is on ferry.");
                        //the car is waiting while the ferry reaches the opposite shore
                        ferry.getLatch().await();
                    } else {
                        TimeUnit.SECONDS.sleep(1);
                    }
                }
            } catch (InterruptedException e) {
                throw new IllegalMonitorStateException();
            }
        }
    }

    @Override
    public void run() {
        LOGGER.info("Car " + this.carName + " arrived to the ferry service.");
        tryLoadToFerry();
        LOGGER.info("Car " + this.carName + " left the ferry.");
    }

    public String getCarName() {
        return carName;
    }

    public int getCarWeight() {
        return carWeight;
    }

    public int getCarArea() {
        return carArea;
    }

    public CarType getType() {
        return type;
    }

    public void setFerry(Ferry ferry) {
        this.ferry = ferry;
    }

    public void setOnBoard(boolean onBoard) {
        this.onBoard = onBoard;
    }
}
