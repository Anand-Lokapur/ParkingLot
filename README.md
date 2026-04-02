# Smart Parking Lot System

A backend system for managing a multi-floor parking lot, written in **plain Java** (no frameworks).

## Features

| Feature | Implementation |
|---|---|
| Multi-floor, multi-spot lot | `ParkingLot` → `ParkingFloor` → `ParkingSpot` |
| Vehicle size allocation | `NearestAvailableSpotStrategy` + `SpotAllocationStrategy` interface |
| Check-in & check-out | `ParkingService` |
| Fee calculation (ceiling-hour billing) | `FeeCalculatorService` |
| Real-time availability | `AvailabilityService` |
| Thread-safe concurrent access | `AtomicBoolean` CAS + `ConcurrentHashMap` |

## Project Structure

```
src/
└── main/java/com/parkinglot/
│   ├── enums/          VehicleType, SpotType
│   ├── model/          Vehicle, ParkingSpot, ParkingFloor, ParkingLot, Ticket
│   ├── exception/      ParkingLotFullException, InvalidTicketException
│   ├── repository/     TicketRepository
│   ├── strategy/       SpotAllocationStrategy (interface), NearestAvailableSpotStrategy
│   ├── service/        FeeCalculatorService, AvailabilityService, ParkingService
│   └── Main.java
└── test/java/com/parkinglot/
    └── ParkingLotTest.java   (8 tests, no JUnit required)
```

## How to Run

**Compile:**
```
javac -d out/main $(find src/main -name "*.java")
```

**Run demo:**
```
java -cp out/main com.parkinglot.Main
```

**Run tests:**
```
javac -cp out/main -d out/test $(find src/test -name "*.java")
java -cp "out/main:out/test" com.parkinglot.ParkingLotTest
```

> On Windows, replace `:` with `;` in the classpath and use PowerShell `Get-ChildItem` instead of `find`.

## Documentation

| Document | Purpose |
|---|---|
| [`GUIDE.md`](GUIDE.md) | Complete teaching guide — every class, every design decision, every concept explained in detail |
| [`plan-smartParkingLot.prompt.md`](plan-smartParkingLot.prompt.md) | Original architecture plan |

## Design Patterns

- **Singleton** — `ParkingLot` (one physical lot, one instance)
- **Strategy** — `SpotAllocationStrategy` (swap allocation algorithm without changing `ParkingService`)
- **Repository** — `TicketRepository` (decouple storage from business logic)
- **Dependency Injection** — all services receive their dependencies via constructor
