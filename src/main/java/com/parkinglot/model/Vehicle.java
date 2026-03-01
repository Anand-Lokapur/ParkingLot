package com.parkinglot.model;

import com.parkinglot.enums.VehicleType;

public class Vehicle {

    private final String licensePlate;
    private final VehicleType vehicleType;

    public Vehicle(String licensePlate, VehicleType vehicleType) {
        this.licensePlate = licensePlate;
        this.vehicleType  = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    @Override
    public String toString() {
        return "Vehicle{plate='" + licensePlate + "', type=" + vehicleType + "}";
    }
}
