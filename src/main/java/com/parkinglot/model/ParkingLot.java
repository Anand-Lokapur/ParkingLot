package com.parkinglot.model;

import java.util.Collections;
import java.util.List;

public class ParkingLot {

    private static volatile ParkingLot instance;

    private final String name;
    private final List<ParkingFloor> floors;

    private ParkingLot(String name, List<ParkingFloor> floors) {
        this.name   = name;
        this.floors = Collections.unmodifiableList(floors);
    }

    public static void initialise(String name, List<ParkingFloor> floors) {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot(name, floors);
                }
            }
        }
    }

    public static ParkingLot getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                "ParkingLot has not been initialised. Call ParkingLot.initialise() first.");
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public String getName()              { return name;   }
    public List<ParkingFloor> getFloors(){ return floors; }

    @Override
    public String toString() {
        return "ParkingLot{name='" + name + "', floors=" + floors.size() + "}";
    }
}
