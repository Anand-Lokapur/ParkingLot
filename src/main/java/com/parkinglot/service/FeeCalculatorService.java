package com.parkinglot.service;

import com.parkinglot.enums.VehicleType;
import com.parkinglot.model.Ticket;

import java.time.Duration;
import java.util.Map;

public class FeeCalculatorService {

    private final Map<VehicleType, Double> ratesPerHour;

    public FeeCalculatorService() {
        this(Map.of(
                VehicleType.MOTORCYCLE, 20.0,
                VehicleType.CAR,        40.0,
                VehicleType.BUS,        100.0
        ));
    }

    public FeeCalculatorService(Map<VehicleType, Double> ratesPerHour) {
        this.ratesPerHour = Map.copyOf(ratesPerHour);
    }

    public double calculate(Ticket ticket) {
        if (ticket.getExitTime() == null) {
            throw new IllegalStateException("Cannot calculate fee: exitTime is not set.");
        }

        Duration duration    = Duration.between(ticket.getEntryTime(), ticket.getExitTime());
        long    totalMinutes = duration.toMinutes();
        long    billedHours  = (long) Math.ceil(totalMinutes / 60.0);
        billedHours = Math.max(billedHours, 1);

        double rate = ratesPerHour.getOrDefault(
                ticket.getVehicle().getVehicleType(), 0.0);

        return billedHours * rate;
    }

    public double getRatePerHour(VehicleType vehicleType) {
        return ratesPerHour.getOrDefault(vehicleType, 0.0);
    }
}
