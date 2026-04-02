package com.parkinglot.strategy;

import com.parkinglot.enums.SpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingSpot;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class NearestAvailableSpotStrategy implements SpotAllocationStrategy {

    private static final Map<VehicleType, SpotType> VEHICLE_TO_SPOT = Map.of(
            VehicleType.MOTORCYCLE, SpotType.SMALL,
            VehicleType.CAR,        SpotType.MEDIUM,
            VehicleType.BUS,        SpotType.LARGE
    );

    @Override
    public Optional<ParkingSpot> findSpot(VehicleType vehicleType, List<ParkingFloor> floors) {
        SpotType required = VEHICLE_TO_SPOT.get(vehicleType);

        for (ParkingFloor floor : floors) {
            for (ParkingSpot spot : floor.getSpots()) {
                if (spot.getSpotType() == required && spot.occupy()) {
                    return Optional.of(spot);
                }
            }
        }
        return Optional.empty();
    }
}
