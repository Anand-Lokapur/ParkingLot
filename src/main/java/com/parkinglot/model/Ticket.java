package com.parkinglot.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Ticket {

    private final String        ticketId;
    private final Vehicle       vehicle;
    private final ParkingSpot   spot;
    private final LocalDateTime entryTime;

    private LocalDateTime exitTime;
    private double        fee;

    public Ticket(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime) {
        this.ticketId  = UUID.randomUUID().toString();
        this.vehicle   = vehicle;
        this.spot      = spot;
        this.entryTime = entryTime;
        this.fee       = 0.0;
    }

    public String        getTicketId()  { return ticketId;  }
    public Vehicle       getVehicle()   { return vehicle;   }
    public ParkingSpot   getSpot()      { return spot;      }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime()  { return exitTime;  }
    public double        getFee()       { return fee;       }

    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public void setFee(double fee)                  { this.fee = fee; }

    @Override
    public String toString() {
        return  "\n========== PARKING TICKET ==========\n" +
                "  Ticket ID : " + ticketId          + "\n" +
                "  Vehicle   : " + vehicle            + "\n" +
                "  Spot      : " + spot.getSpotId()  + " (Floor " + spot.getFloorId() + ")\n" +
                "  Entry     : " + entryTime          + "\n" +
                "  Exit      : " + (exitTime != null ? exitTime : "Still parked") + "\n" +
                "  Fee       : ₹" + String.format("%.2f", fee) + "\n" +
                "====================================\n";
    }
}
