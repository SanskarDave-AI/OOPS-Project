

// ==========================================
// File: main/Main.java  (Member 1 + Member 2)
// ==========================================
package main;

import drones.StandardDrone;
import drones.ExpressDrone;
import drones.HeavyLiftDrone;
import drones.Drone;
import manager.DeliveryPackage;
import manager.DeliveryManager;
import components.Location;

public class Main {
    public static void main(String[] args) {

        // ==============================
        // MEMBER 1: Create Drones
        // All drones start at WAREHOUSE (0,0) by default
        // ==============================
        StandardDrone  d1 = new StandardDrone(101);
        ExpressDrone   d2 = new ExpressDrone(102);
        HeavyLiftDrone d3 = new HeavyLiftDrone(103);

        // ==============================
        // MEMBER 2: Setup Fleet
        // ==============================
        DeliveryManager manager = new DeliveryManager();
        manager.addDrone(d1);
        manager.addDrone(d2);
        manager.addDrone(d3);

        // Show initial fleet
        manager.showAllDrones();

        // ==============================
        // TEST 1: URGENT small package
        // Expected: ExpressDrone (d2) assigned
        // Flow: (0,0) → (12,15) → back to (0,0)
        // ==============================
        System.out.println("\n========== TEST 1: URGENT PACKAGE ==========");
        DeliveryPackage pkg1 = new DeliveryPackage(501, 2.0, "URGENT", new Location(12, 15));
        System.out.println(pkg1);

        Drone assigned1 = manager.assignDrone(pkg1);
        if (assigned1 != null) {
            assigned1.flyTo(pkg1.getDestination());
            assigned1.deliverPackage();
            pkg1.setDelivered(true);
            assigned1.returnToBase();
            System.out.println("Package 1 Delivered: " + pkg1.isDelivered());
        }

        // ==============================
        // TEST 2: NORMAL heavy package
        // Expected: HeavyLiftDrone (d3) assigned
        // Flow: (0,0) → (25,30) → back to (0,0)
        // ==============================
        System.out.println("\n========== TEST 2: HEAVY NORMAL PACKAGE ==========");
        DeliveryPackage pkg2 = new DeliveryPackage(502, 15.0, "NORMAL", new Location(25, 30));
        System.out.println(pkg2);

        Drone assigned2 = manager.assignDrone(pkg2);
        if (assigned2 != null) {
            assigned2.flyTo(pkg2.getDestination());
            assigned2.deliverPackage();
            pkg2.setDelivered(true);
            assigned2.returnToBase();
            System.out.println("Package 2 Delivered: " + pkg2.isDelivered());
        }

        // ==============================
        // TEST 3: URGENT but ExpressDrone busy
        // Expected: fallback to StandardDrone
        // ==============================
        System.out.println("\n========== TEST 3: URGENT - EXPRESS BUSY ==========");
        d2.setStatus("DELIVERING");
        DeliveryPackage pkg3 = new DeliveryPackage(503, 2.0, "URGENT", new Location(6, 8));
        System.out.println(pkg3);

        Drone assigned3 = manager.assignDrone(pkg3);
        if (assigned3 != null) {
            assigned3.flyTo(pkg3.getDestination());
            assigned3.deliverPackage();
            pkg3.setDelivered(true);
            assigned3.returnToBase();
            System.out.println("Package 3 Delivered: " + pkg3.isDelivered());
        }

        // ==============================
        // TEST 4: No suitable drone
        // Expected: "No suitable drone found"
        // ==============================
        System.out.println("\n========== TEST 4: NO SUITABLE DRONE ==========");
        d1.getBattery().setLevel(5);
        d2.getBattery().setLevel(5);
        d3.getBattery().setLevel(5);
        d2.setStatus("AVAILABLE");
        DeliveryPackage pkg4 = new DeliveryPackage(504, 1.0, "NORMAL", new Location(3, 4));
        System.out.println(pkg4);

        Drone assigned4 = manager.assignDrone(pkg4);
        if (assigned4 == null) {
            System.out.println("No drone assigned — as their battery is low and they are charging  , wait sometime.");
        }

        // ==============================
        // FINAL FLEET STATUS
        // ==============================
        System.out.println("\n========== FINAL FLEET STATUS ==========");
        d1.getBattery().setLevel(100);
        d2.getBattery().setLevel(100);
        d3.getBattery().setLevel(100);
        manager.showAllDrones();// 
    }
}
