package com.parkinglot.service;

import com.parkinglot.exception.InvalidTicketException;
import com.parkinglot.exception.ParkingLotFullException;
import com.parkinglot.model.ParkingLot;
import com.parkinglot.model.ParkingSpot;
import com.parkinglot.model.Ticket;
import com.parkinglot.model.Vehicle;
import com.parkinglot.repository.TicketRepository;
import com.parkinglot.strategy.SpotAllocationStrategy;

import java.time.LocalDateTime;
import java.util.Optional;

public class ParkingService {

    private final ParkingLot             parkingLot;
    private final SpotAllocationStrategy allocationStrategy;
    private final FeeCalculatorService   feeCalculator;
    private final TicketRepository       ticketRepository;

    public ParkingService(ParkingLot parkingLot,
                          SpotAllocationStrategy allocationStrategy,
                          FeeCalculatorService feeCalculator,
                          TicketRepository ticketRepository) {
        this.parkingLot         = parkingLot;
        this.allocationStrategy = allocationStrategy;
        this.feeCalculator      = feeCalculator;
        this.ticketRepository   = ticketRepository;
    }

    public Ticket checkIn(Vehicle vehicle) {
        Optional<ParkingSpot> spotOpt = allocationStrategy.findSpot(
                vehicle.getVehicleType(), parkingLot.getFloors());

        if (spotOpt.isEmpty()) {
            throw new ParkingLotFullException(
                    "No available spot for vehicle type: " + vehicle.getVehicleType());
        }

        Ticket ticket = new Ticket(vehicle, spotOpt.get(), LocalDateTime.now());
        ticketRepository.saveActiveTicket(ticket);

        System.out.println("[CHECK-IN]  " + vehicle.getLicensePlate()
                + " → Spot " + spotOpt.get().getSpotId()
                + " (Floor " + spotOpt.get().getFloorId() + ")"
                + " | Ticket: " + ticket.getTicketId());

        return ticket;
    }

    public double checkOut(String ticketId) {
        Ticket ticket = ticketRepository.findActiveTicket(ticketId);
        if (ticket == null) {
            throw new InvalidTicketException("Invalid or already-closed ticket: " + ticketId);
        }

        ticket.setExitTime(LocalDateTime.now());
        double fee = feeCalculator.calculate(ticket);
        ticket.setFee(fee);

        ticket.getSpot().release();
        ticketRepository.closeTicket(ticket);

        System.out.println("[CHECK-OUT] " + ticket.getVehicle().getLicensePlate()
                + " ← Spot " + ticket.getSpot().getSpotId()
                + " | Duration: " + java.time.Duration.between(
                        ticket.getEntryTime(), ticket.getExitTime()).toMinutes() + " min"
                + " | Fee: ₹" + String.format("%.2f", fee));

        return fee;
    }

    public double checkOut(Ticket ticket) {
        return checkOut(ticket.getTicketId());
    }
}
