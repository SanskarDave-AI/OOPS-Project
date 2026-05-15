


package main;

import drones.StandardDrone;
import drones.ExpressDrone;
import drones.HeavyLiftDrone;
import drones.Drone;
import manager.DeliveryPackage;
import manager.DeliveryManager;
import components.Location;
import simulation.DeliverySimulator;
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

        DeliverySimulator simulator = new DeliverySimulator();

        manager.showAllDrones();

        System.out.println("\n========== DELIVERY INPUT LOOP ==========");
        while (true) {
            System.out.print("Enter package ID (-1 to exit): ");
            int packageId = sc.nextInt();
            if (packageId == -1) {
                break;
            }

            System.out.print("Enter weight (kg):      ");
            double weight = sc.nextDouble();
            System.out.print("Enter dest X:           ");
            int x = sc.nextInt();
            System.out.print("Enter dest Y:           ");
            int y = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter preferred drone type (STANDARD/EXPRESS/HEAVY/ANY): ");
            String preferredType = sc.nextLine().trim();
            if (preferredType.isBlank()) {
                preferredType = "ANY";
            }

            DeliveryPackage pkg = new DeliveryPackage(packageId, weight,
                    "NORMAL", preferredType, new Location(x, y));
            System.out.println(pkg);

            Drone assigned = manager.assignDrone(pkg);
            if (assigned != null) {
                simulator.executeDelivery(assigned, pkg);
                System.out.println("Package Delivered: " + pkg.isDelivered());
            } else {
                System.out.println("Package " + packageId + " was not assigned.");
            }
            System.out.println("\n------------------------------------------");
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