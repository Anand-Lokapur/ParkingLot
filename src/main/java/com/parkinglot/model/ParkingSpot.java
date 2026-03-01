package com.parkinglot.model;

import com.parkinglot.enums.SpotType;
import java.util.concurrent.atomic.AtomicBoolean;

public class ParkingSpot {

    private final String spotId;
    private final int    floorId;
    private final SpotType spotType;
    private final AtomicBoolean occupied = new AtomicBoolean(false);

    public ParkingSpot(String spotId, int floorId, SpotType spotType) {
        this.spotId   = spotId;
        this.floorId  = floorId;
        this.spotType = spotType;
    }

    public boolean occupy() {
        return occupied.compareAndSet(false, true);
    }

    public void release() {
        occupied.set(false);
    }

    public boolean isOccupied() {
        return occupied.get();
    }

    public String getSpotId()    { return spotId;   }
    public int    getFloorId()   { return floorId;  }
    public SpotType getSpotType(){ return spotType; }

    @Override
    public String toString() {
        return "ParkingSpot{id='" + spotId + "', floor=" + floorId
                + ", type=" + spotType + ", occupied=" + occupied.get() + "}";
    }
}
