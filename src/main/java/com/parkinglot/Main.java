package com.parkinglot;

import com.parkinglot.enums.SpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.ParkingLotFullException;
import com.parkinglot.model.*;
import com.parkinglot.repository.TicketRepository;
import com.parkinglot.service.AvailabilityService;
import com.parkinglot.service.FeeCalculatorService;
import com.parkinglot.service.ParkingService;
import com.parkinglot.strategy.NearestAvailableSpotStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        ParkingLot.initialise("City Centre Parking", buildFloors(3));
        ParkingLot lot = ParkingLot.getInstance();
        System.out.println("Initialized: " + lot);

        TicketRepository      ticketRepo     = new TicketRepository();
        FeeCalculatorService  feeCalc        = new FeeCalculatorService();
        ParkingService        parkingService = new ParkingService(
                lot, new NearestAvailableSpotStrategy(), feeCalc, ticketRepo);
        AvailabilityService   availability   = new AvailabilityService(lot);

        availability.printAvailabilityReport();

        List<Vehicle> vehicles = List.of(
                new Vehicle("KA-01-AA-0001", VehicleType.CAR),
                new Vehicle("KA-01-AA-0002", VehicleType.MOTORCYCLE),
                new Vehicle("KA-01-AA-0003", VehicleType.BUS),
                new Vehicle("KA-01-AA-0004", VehicleType.CAR),
                new Vehicle("KA-01-AA-0005", VehicleType.MOTORCYCLE),
                new Vehicle("KA-01-AA-0006", VehicleType.CAR)
        );

        List<Ticket> tickets = new java.util.concurrent.CopyOnWriteArrayList<>();

        ExecutorService checkInPool = Executors.newFixedThreadPool(6);
        for (Vehicle vehicle : vehicles) {
            checkInPool.submit(() -> {
                try {
                    Ticket t = parkingService.checkIn(vehicle);
                    tickets.add(t);
                } catch (ParkingLotFullException ex) {
                    System.out.println("[DENIED] " + vehicle.getLicensePlate()
                            + " — " + ex.getMessage());
                }
            });
        }
        checkInPool.shutdown();
        checkInPool.awaitTermination(10, TimeUnit.SECONDS);

        availability.printAvailabilityReport();

        System.out.println("--- Attempting to fill all BUS spots ---");
        List<Ticket> busTickets = new ArrayList<>();
        for (int i = 2; i <= 7; i++) {
            try {
                Ticket t = parkingService.checkIn(
                        new Vehicle("BUS-EXTRA-" + i, VehicleType.BUS));
                busTickets.add(t);
            } catch (ParkingLotFullException ex) {
                System.out.println("[LOT FULL] " + ex.getMessage());
            }
        }

        System.out.println("\n--- Checking out all vehicles ---");
        for (Ticket t : tickets) {
            parkingService.checkOut(t);
            System.out.println(t);
        }
        for (Ticket t : busTickets) {
            parkingService.checkOut(t);
        }

        availability.printAvailabilityReport();
    }

    private static List<ParkingFloor> buildFloors(int numFloors) {
        List<ParkingFloor> floors = new ArrayList<>();
        for (int f = 1; f <= numFloors; f++) {
            List<ParkingSpot> spots = new ArrayList<>();
            for (int s = 1; s <= 3; s++) {
                spots.add(new ParkingSpot(
                        "F" + f + "-S" + String.format("%02d", s), f, SpotType.SMALL));
            }
            for (int m = 1; m <= 3; m++) {
                spots.add(new ParkingSpot(
                        "F" + f + "-M" + String.format("%02d", m), f, SpotType.MEDIUM));
            }
            for (int l = 1; l <= 2; l++) {
                spots.add(new ParkingSpot(
                        "F" + f + "-L" + String.format("%02d", l), f, SpotType.LARGE));
            }
            floors.add(new ParkingFloor(f, spots));
        }
        return floors;
    }
}
