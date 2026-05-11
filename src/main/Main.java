


package main;

import drones.StandardDrone;
import drones.ExpressDrone;
import drones.HeavyLiftDrone;
import drones.Drone;
import manager.DeliveryPackage;
import manager.DeliveryManager;
import components.Location;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // ==============================
        // MEMBER 1: Create Drones
        // All drones start at WAREHOUSE (0,0) by default
        // ==============================
        System.out.print("Enter ID for StandardDrone:  ");
        int id1 = sc.nextInt();
        System.out.print("Enter ID for ExpressDrone:   ");
        int id2 = sc.nextInt();
        System.out.print("Enter ID for HeavyLiftDrone: ");
        int id3 = sc.nextInt();

        StandardDrone  d1 = new StandardDrone(id1);
        ExpressDrone   d2 = new ExpressDrone(id2);
        HeavyLiftDrone d3 = new HeavyLiftDrone(id3);

        // ==============================
        // MEMBER 2: Setup Fleet
        // ==============================
        DeliveryManager manager = new DeliveryManager();
        manager.addDrone(d1);
        manager.addDrone(d2);
        manager.addDrone(d3);

        manager.showAllDrones();

        // ==============================
        // TEST 1: URGENT small package
        // ==============================
        System.out.println("\n========== TEST 1: URGENT PACKAGE ==========");
        System.out.print("Enter package ID:       ");  int pid1 = sc.nextInt();
        System.out.print("Enter weight (kg):      ");  double w1  = sc.nextDouble();
        System.out.print("Enter dest X:           ");  int x1    = sc.nextInt();
        System.out.print("Enter dest Y:           ");  int y1    = sc.nextInt();

        DeliveryPackage pkg1 = new DeliveryPackage(pid1, w1, "URGENT", new Location(x1, y1));
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
        // ==============================
        System.out.println("\n========== TEST 2: HEAVY NORMAL PACKAGE ==========");
        System.out.print("Enter package ID:       ");  int pid2 = sc.nextInt();
        System.out.print("Enter weight (kg):      ");  double w2  = sc.nextDouble();
        System.out.print("Enter dest X:           ");  int x2    = sc.nextInt();
        System.out.print("Enter dest Y:           ");  int y2    = sc.nextInt();

        DeliveryPackage pkg2 = new DeliveryPackage(pid2, w2, "NORMAL", new Location(x2, y2));
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
        // TEST 3: URGENT — simulate busy ExpressDrone
        // ==============================
        System.out.println("\n========== TEST 3: URGENT - EXPRESS BUSY ==========");
        System.out.print("Enter package ID:       ");  int pid3 = sc.nextInt();
        System.out.print("Enter weight (kg):      ");  double w3  = sc.nextDouble();
        System.out.print("Enter dest X:           ");  int x3    = sc.nextInt();
        System.out.print("Enter dest Y:           ");  int y3    = sc.nextInt();

        d2.setStatus("DELIVERING");   // mark express as busy
        DeliveryPackage pkg3 = new DeliveryPackage(pid3, w3, "URGENT", new Location(x3, y3));
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
        // TEST 4: No suitable drone (low battery)
        // ==============================
        System.out.println("\n========== TEST 4: NO SUITABLE DRONE ==========");
        System.out.print("Enter battery level to simulate low battery (e.g. 5): ");
        int lowBattery = sc.nextInt();

        d1.getBattery().setLevel(lowBattery);
        d2.getBattery().setLevel(lowBattery);
        d3.getBattery().setLevel(lowBattery);
        d2.setStatus("AVAILABLE");

        System.out.print("Enter package ID:       ");  int pid4 = sc.nextInt();
        System.out.print("Enter weight (kg):      ");  double w4  = sc.nextDouble();
        System.out.print("Enter dest X:           ");  int x4    = sc.nextInt();
        System.out.print("Enter dest Y:           ");  int y4    = sc.nextInt();

        DeliveryPackage pkg4 = new DeliveryPackage(pid4, w4, "NORMAL", new Location(x4, y4));
        System.out.println(pkg4);

        Drone assigned4 = manager.assignDrone(pkg4);
        if (assigned4 == null) {
            System.out.println("No drone assigned — battery is low, they are charging. Please wait.");
        }

        // ==============================
        // FINAL FLEET STATUS
        // ==============================
        System.out.println("\n========== FINAL FLEET STATUS ==========");
        System.out.print("Enter battery level to restore (e.g. 100): ");
        int fullBattery = sc.nextInt();



        d1.getBattery().setLevel(fullBattery);
        d2.getBattery().setLevel(fullBattery);
        d3.getBattery().setLevel(fullBattery);
        manager.showAllDrones();

        sc.close();
    }
}