package by.training.module4.model;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Ferry implements Runnable {
    private static Logger LOGGER = Logger.getLogger(Ferry.class);

    private static int MAX_CARRYING_WEIGHT;
    private static int MAX_PLATFORM_AREA;

    private Lock lock = new ReentrantLock();
    private AtomicInteger freeWeight;
    private AtomicInteger freeArea;
    private volatile boolean ferryIsSailing;
    private volatile boolean ferryIsLoaded;
    private boolean loadingInProcess;
    private int noCarsToLoad;
    private List<Car> cars;
    private CountDownLatch latch;

    public Ferry(int maxWeight, int maxArea) {
        MAX_CARRYING_WEIGHT = maxWeight;
        MAX_PLATFORM_AREA = maxArea;
        this.freeWeight = new AtomicInteger(MAX_CARRYING_WEIGHT);
        this.freeArea = new AtomicInteger(MAX_PLATFORM_AREA);
        this.cars = new ArrayList<>();
        this.latch = new CountDownLatch(1);
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public boolean isFerryIsSailing() {
        return ferryIsSailing;
    }

    public boolean tryLoadCar(Car arrivedCar) {
        int carWeight = arrivedCar.getCarWeight();
        int carArea = arrivedCar.getCarArea();

        if (arrivedCar.getType() == CarType.TRUCK) {
            Truck truck = (Truck) arrivedCar;
            if (truck.isFull()) {
                carWeight += truck.getCargo();
            }
        }

        try {
            lock.lock();

            if (this.freeArea.get() >= carArea && this.freeWeight.get() >= carWeight) {
                loadingInProcess = true;
                arrivedCar.setOnBoard(true);
                this.freeArea.addAndGet(-carArea);
                this.freeWeight.addAndGet(-carWeight);
                cars.add(arrivedCar);
                this.ferryIsLoaded = true;
                return true;
            }
            return false;
        } finally {
            loadingInProcess = false;
            lock.unlock();
        }
    }

    private void resetCapacityAndArea() {
        freeWeight.set(MAX_CARRYING_WEIGHT);
        freeArea.set(MAX_PLATFORM_AREA);
    }

    private void printCars() {
        LOGGER.info("Ferry delivered those cars: ");
        for (Car car : this.cars) {
            if (car.getType() == CarType.PASSENGER_CAR) {
                PassengerCar passengerCar = (PassengerCar) car;
                LOGGER.info(passengerCar);
            } else if (car.getType() == CarType.TRUCK) {
                Truck truck = (Truck) car;
                LOGGER.info(truck);
            }
        }
    }

    @Override
    public void run() {
        while (noCarsToLoad < 2) {

            LOGGER.info("Ferry is waiting cars loading...");
            try {
                TimeUnit.SECONDS.sleep(2);
                //the time of loading was over, if at least one car was loaded to ferry, ferry may try sailing
                //but if the next car started loading to ferry the ferry cannot start sailing and should wait while the last car finishes loading
                if (ferryIsLoaded) {
                    ferryIsSailing = true;
                    while (true) {
                        if (!loadingInProcess) {
                            LOGGER.info("Ferry ferries cars...");
                            //the ferry is sailing
                            TimeUnit.SECONDS.sleep(1);
                            //the ferry arrived to the opposite shore
                            printCars();
                            this.cars.clear();
                            resetCapacityAndArea();
                            //release all waiting cars
                            latch.countDown();
                            // the ferry is sailing back
                            TimeUnit.SECONDS.sleep(1);
                            latch = new CountDownLatch(1);
                            ferryIsSailing = false;
                            ferryIsLoaded = false;
                            break;
                        }
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                } else {
                    noCarsToLoad++;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LOGGER.info("There are no cars to deliver on the coast. I'm shutting down my engine!");
    }
}
