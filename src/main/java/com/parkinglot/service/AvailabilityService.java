package com.parkinglot.service;

import com.parkinglot.enums.VehicleType;
import com.parkinglot.enums.SpotType;
import com.parkinglot.model.ParkingFloor;
import com.parkinglot.model.ParkingLot;

import java.util.LinkedHashMap;
import java.util.Map;

public class AvailabilityService {

    private final ParkingLot parkingLot;

    public AvailabilityService(ParkingLot parkingLot) {
        this.parkingLot = parkingLot;
    }

    public Map<SpotType, Long> getAvailabilityBySpotType() {
        Map<SpotType, Long> result = new LinkedHashMap<>();
        for (SpotType type : SpotType.values()) {
            long count = parkingLot.getFloors().stream()
                    .mapToLong(f -> f.availableSpotsOfType(type))
                    .sum();
            result.put(type, count);
        }
        return result;
    }

    public Map<SpotType, Long> getAvailabilityByFloor(int floorId) {
        ParkingFloor floor = parkingLot.getFloors().stream()
                .filter(f -> f.getFloorId() == floorId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Floor not found: " + floorId));

        Map<SpotType, Long> result = new LinkedHashMap<>();
        for (SpotType type : SpotType.values()) {
            result.put(type, floor.availableSpotsOfType(type));
        }
        return result;
    }

    public long getAvailableCountForVehicle(VehicleType vehicleType) {
        SpotType required = switch (vehicleType) {
            case MOTORCYCLE -> SpotType.SMALL;
            case CAR        -> SpotType.MEDIUM;
            case BUS        -> SpotType.LARGE;
        };
        return getAvailabilityBySpotType().getOrDefault(required, 0L);
    }

    public void printAvailabilityReport() {
        System.out.println("\n===== Parking Lot Availability =====");
        System.out.println("Lot : " + parkingLot.getName());
        for (ParkingFloor floor : parkingLot.getFloors()) {
            System.out.printf("  Floor %d:%n", floor.getFloorId());
            for (SpotType type : SpotType.values()) {
                long avail = floor.availableSpotsOfType(type);
                long total = floor.totalSpotsOfType(type);
                System.out.printf("    %-7s : %d / %d free%n", type, avail, total);
            }
        }
        System.out.println("====================================\n");
    }
}
