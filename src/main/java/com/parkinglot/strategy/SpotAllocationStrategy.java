package com.parkinglot.strategy;

import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.enums.VehicleType;

import java.util.List;
import java.util.Optional;

public interface SpotAllocationStrategy {

    Optional<ParkingSpot> findSpot(VehicleType vehicleType, List<ParkingFloor> floors);
}
