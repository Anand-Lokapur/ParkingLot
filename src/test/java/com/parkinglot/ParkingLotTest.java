package com.parkinglot;

import com.parkinglot.enums.SpotType;
import com.parkinglot.enums.VehicleType;
import com.parkinglot.exception.InvalidTicketException;
import com.parkinglot.exception.ParkingLotFullException;
import com.parkinglot.model.*;
import com.parkinglot.repository.TicketRepository;
import com.parkinglot.service.FeeCalculatorService;
import com.parkinglot.service.ParkingService;
import com.parkinglot.strategy.NearestAvailableSpotStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ParkingLotTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        test_T1_motorcycleParkInSmallSpot();
        test_T2_carParkInMediumSpot();
        test_T3_busParkInLargeSpot();
        test_T4_spotFreedAfterCheckOut();
        test_T5_parkingLotFullException();
        test_T6_invalidTicketException();
        test_T7_feeCalculation();
        test_T8_concurrentCheckIn();

        System.out.println("\n==============================");
        System.out.println(" Tests passed : " + passed);
        System.out.println(" Tests failed : " + failed);
        System.out.println("==============================");
        if (failed > 0) System.exit(1);
    }

    static void test_T1_motorcycleParkInSmallSpot() {
        ParkingService svc = buildService(0, 1, 0);
        Ticket t = svc.checkIn(new Vehicle("MOTO-01", VehicleType.MOTORCYCLE));
        assertEq("T1", SpotType.SMALL, t.getSpot().getSpotType());
    }

    static void test_T2_carParkInMediumSpot() {
        ParkingService svc = buildService(1, 0, 0);
        Ticket t = svc.checkIn(new Vehicle("CAR-01", VehicleType.CAR));
        assertEq("T2", SpotType.MEDIUM, t.getSpot().getSpotType());
    }

    static void test_T3_busParkInLargeSpot() {
        ParkingService svc = buildService(0, 0, 1);
        Ticket t = svc.checkIn(new Vehicle("BUS-01", VehicleType.BUS));
        assertEq("T3", SpotType.LARGE, t.getSpot().getSpotType());
    }

    static void test_T4_spotFreedAfterCheckOut() {
        ParkingService svc = buildService(1, 0, 0);
        Ticket t1 = svc.checkIn(new Vehicle("CAR-01", VehicleType.CAR));
        svc.checkOut(t1);
        Ticket t2 = svc.checkIn(new Vehicle("CAR-02", VehicleType.CAR));
        assertEq("T4", t1.getSpot().getSpotId(), t2.getSpot().getSpotId());
    }

    static void test_T5_parkingLotFullException() {
        ParkingService svc = buildService(1, 0, 0);
        svc.checkIn(new Vehicle("CAR-01", VehicleType.CAR));
        try {
            svc.checkIn(new Vehicle("CAR-02", VehicleType.CAR));
            fail("T5", "Expected ParkingLotFullException");
        } catch (ParkingLotFullException ex) {
            pass("T5");
        }
    }

    static void test_T6_invalidTicketException() {
        ParkingService svc = buildService(1, 0, 0);
        try {
            svc.checkOut("non-existent-id");
            fail("T6", "Expected InvalidTicketException");
        } catch (InvalidTicketException ex) {
            pass("T6");
        }
    }

    static void test_T7_feeCalculation() {
        FeeCalculatorService calc = new FeeCalculatorService();
        ParkingSpot spot   = new ParkingSpot("M01", 1, SpotType.MEDIUM);
        Vehicle     car    = new Vehicle("CAR-X", VehicleType.CAR);
        Ticket      ticket = new Ticket(car, spot,
                java.time.LocalDateTime.of(2025, 1, 1, 10, 0));
        ticket.setExitTime(java.time.LocalDateTime.of(2025, 1, 1, 11, 30));
        double fee = calc.calculate(ticket);
        assertEq("T7", 80.0, fee);
    }

    static void test_T8_concurrentCheckIn() {
        int spotCount   = 10;
        ParkingService svc = buildService(spotCount, 0, 0);

        int threadCount = 50;
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount    = new AtomicInteger(0);

        ExecutorService pool  = Executors.newFixedThreadPool(threadCount);
        CountDownLatch  latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int id = i;
            pool.submit(() -> {
                try {
                    svc.checkIn(new Vehicle("CAR-" + id, VehicleType.CAR));
                    successCount.incrementAndGet();
                } catch (ParkingLotFullException e) {
                    failCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        try { latch.await(10, TimeUnit.SECONDS); } catch (InterruptedException ignored) {}
        pool.shutdown();

        if (successCount.get() == spotCount && failCount.get() == threadCount - spotCount) {
            pass("T8");
        } else {
            fail("T8", "Expected " + spotCount + " successes but got " + successCount.get());
        }
    }

    static ParkingService buildService(int medium, int small, int large) {
        ParkingLot.reset();
        List<ParkingSpot> spots = new ArrayList<>();
        for (int i = 1; i <= medium; i++)
            spots.add(new ParkingSpot("M" + String.format("%02d", i), 1, SpotType.MEDIUM));
        for (int i = 1; i <= small;  i++)
            spots.add(new ParkingSpot("S" + String.format("%02d", i), 1, SpotType.SMALL));
        for (int i = 1; i <= large;  i++)
            spots.add(new ParkingSpot("L" + String.format("%02d", i), 1, SpotType.LARGE));
        ParkingLot.initialise("Test Lot", List.of(new ParkingFloor(1, spots)));
        return new ParkingService(
                ParkingLot.getInstance(),
                new NearestAvailableSpotStrategy(),
                new FeeCalculatorService(),
                new TicketRepository());
    }

    static <T> void assertEq(String name, T expected, T actual) {
        if (expected.equals(actual)) {
            pass(name);
        } else {
            fail(name, "Expected <" + expected + "> but got <" + actual + ">");
        }
    }

    static void pass(String name) {
        passed++;
        System.out.println("  [PASS] " + name);
    }

    static void fail(String name, String reason) {
        failed++;
        System.out.println("  [FAIL] " + name + " — " + reason);
    }
}
