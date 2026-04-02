package com.parkinglot.model;

import com.parkinglot.enums.SpotType;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ParkingFloor {

    private final int floorId;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorId, List<ParkingSpot> spots) {
        this.floorId = floorId;
        this.spots   = Collections.unmodifiableList(spots);
    }

    public int getFloorId() { return floorId; }

    public List<ParkingSpot> getSpots() { return spots; }

    public List<ParkingSpot> getAvailableSpotsByType(SpotType type) {
        return spots.stream()
                .filter(s -> s.getSpotType() == type && !s.isOccupied())
                .collect(Collectors.toList());
    }

    public long totalSpotsOfType(SpotType type) {
        return spots.stream().filter(s -> s.getSpotType() == type).count();
    }

    public long availableSpotsOfType(SpotType type) {
        return spots.stream().filter(s -> s.getSpotType() == type && !s.isOccupied()).count();
    }

    @Override
    public String toString() {
        return "ParkingFloor{floorId=" + floorId + ", totalSpots=" + spots.size() + "}";
    }
}
