# ParkingLot

**Smart Parking Lot System** — A Java-based application that models real-world parking lot operations including vehicle entry/exit, slot allocation, availability tracking, and fee calculation.

---

## Overview

ParkingLot is a command-line Java application that simulates a multi-level parking lot management system. It supports different vehicle types, automatic slot assignment, ticket generation, and fee computation — making it a complete end-to-end parking workflow.

---

## Features

- **Vehicle Entry & Exit** — Register a vehicle's arrival and departure with timestamps.
- **Slot Allocation** — Automatically assigns the nearest available parking slot to an incoming vehicle.
- **Multi-Vehicle Support** — Handles cars, motorcycles, and trucks with type-specific slot rules.
- **Ticket Generation** — Issues a unique parking ticket upon entry containing slot and time details.
- **Fee Calculation** — Computes parking charges based on duration and vehicle type when a vehicle exits.
- **Availability Tracking** — Real-time view of free and occupied slots across the lot.
- **Slot Release** — Frees the slot upon vehicle departure for reuse.

---

## Architecture / Components

```
ParkingLot/
├── src/
│   └── main/
│       └── java/
│           └── com/parkinglot/
│               ├── Main.java              # Entry point & CLI menu
│               ├── model/
│               │   ├── Vehicle.java       # Vehicle entity (type, plate)
│               │   ├── ParkingSlot.java   # Slot entity (id, type, status)
│               │   └── Ticket.java        # Parking ticket (slot, entry time)
│               ├── service/
│               │   ├── ParkingLotService.java  # Core business logic
│               │   └── FeeCalculator.java      # Duration-based fee rules
│               └── repository/
│                   └── SlotRepository.java     # In-memory slot store
├── pom.xml                                # Maven build config
└── README.md
```

**Key Design Decisions:**
- Object-oriented domain model (`Vehicle`, `ParkingSlot`, `Ticket`) for clarity.
- Service layer isolates business logic from I/O.
- In-memory repository keeps it self-contained (no DB dependency needed to run).

---

## Tech Stack

| Layer       | Technology         |
|-------------|--------------------|
| Language    | Java 17            |
| Build       | Maven              |
| Architecture| OOP / Layered (Model → Service → Repository) |
| Testing     | JUnit 5            |
| CLI         | Standard I/O       |

---

## Getting Started

### Prerequisites

- Java 17+ installed (`java -version`)
- Maven 3.8+ installed (`mvn -version`)

### Clone the Repository

```bash
git clone https://github.com/Anand-Lokapur/ParkingLot.git
cd ParkingLot
```

### Build

```bash
mvn clean compile
```

### Run

```bash
mvn exec:java -Dexec.mainClass="com.parkinglot.Main"
```

Or build a JAR and run:

```bash
mvn clean package
java -jar target/parkinglot-1.0.jar
```

### Run Tests

```bash
mvn test
```

---

## Sample Usage

```
===== Smart Parking Lot System =====
1. Park a vehicle
2. Exit a vehicle
3. View available slots
4. Exit

Choose option: 1
Enter vehicle type (CAR/BIKE/TRUCK): CAR
Enter license plate: KA-01-HH-1234
--> Ticket issued | Slot: A-3 | Entry: 10:05 AM

Choose option: 3
Available Slots: A-1 (BIKE), A-2 (CAR), A-4 (CAR), B-1 (TRUCK)
Occupied Slots : A-3 (CAR - KA-01-HH-1234)

Choose option: 2
Enter ticket number: TKT-001
--> Vehicle KA-01-HH-1234 exited | Duration: 2h 15m | Fee: ₹90.00
```

---

## Resume Bullets

Use these directly on your resume under a **Projects** section:

- Developed a **Smart Parking Lot System in Java** implementing slot allocation, vehicle entry/exit workflows, and automated fee calculation based on parking duration.
- Modeled core domain entities (`Vehicle`, `ParkingSlot`, `Ticket`) using **object-oriented design**, separating concerns across model, service, and repository layers.
- Built a **menu-driven CLI** supporting multiple vehicle types (Car, Bike, Truck) with type-specific slot assignment and real-time availability tracking.
- Applied **clean architecture principles** (layered design with a service layer for business logic) to keep the codebase maintainable and extensible.
- Wrote **JUnit 5 unit tests** to validate slot allocation logic, fee calculation rules, and edge cases (full lot, invalid tickets).

---

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

---

## License

[MIT](https://choosealicense.com/licenses/mit/)
