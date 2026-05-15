
// ==========================================
// File: manager/DeliveryManager.java
// ==========================================
package manager;

import drones.Drone;
import drones.ExpressDrone;
import drones.HeavyLiftDrone;
import drones.StandardDrone;
import components.Location;
import java.util.ArrayList;

public class DeliveryManager {

    private ArrayList<Drone> drones;

    // Warehouse — all drones start here, all packages picked up from here
    private static final Location WAREHOUSE = new Location(0, 0);

    public DeliveryManager() {
        drones = new ArrayList<>();
    }

    // Add drone to fleet
    public void addDrone(Drone drone) {
        drones.add(drone);
    }

    // Show all drones
    public void showAllDrones() {
        System.out.println("\n========== DRONE FLEET ==========");
        for (Drone d : drones) {
            d.showStatus();
        }
    }

    // =============================================
    // DISTANCE CALCULATION — Member 2 owns this
    // Always from WAREHOUSE (0,0) to destination
    // =============================================
    public double calculateDistance(Location destination) {
        double dx = destination.getX() - WAREHOUSE.getX();
        double dy = destination.getY() - WAREHOUSE.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // =============================================
    // MAIN ASSIGNMENT LOGIC
    // Selects best drone based on:
    // 1. Availability
    // 2. Weight capacity
    // 3. Battery level
    // 4. Drone type (URGENT = ExpressDrone)
    // =============================================
    public Drone assignDrone(DeliveryPackage pkg) {
        System.out.println("\nSearching drone for Package " + pkg.getPackageId()
                + " [Priority: " + pkg.getPriority()
                + ", Weight: " + pkg.getWeight() + " kg]");
        System.out.println("Pickup   : WAREHOUSE " + WAREHOUSE);
        System.out.println("Delivery : " + pkg.getDestination());

        double maxCapacity = getMaxDroneCapacity();
        if (pkg.getWeight() > maxCapacity) {
            System.out.println("ERROR: Package weight " + pkg.getWeight() + " kg exceeds maximum available drone capacity of "
                    + String.format("%.2f", maxCapacity) + " kg.");
            System.out.println("Package " + pkg.getPackageId() + " cannot be assigned to any drone.");
            return null;
        }

        double oneSideDistance = calculateDistance(pkg.getDestination());
        double requiredBattery = oneSideDistance * 2;

        System.out.println("Distance : " + String.format("%.2f", oneSideDistance) + " meters");
        System.out.println("Required Battery (round trip baseline): "
                + String.format("%.2f", requiredBattery) + "%");
        System.out.println("Preferred Drone Type: " + pkg.getPreferredDroneType());

        Drone  bestDrone   = null;
        double bestBattery = -1;

        String preferredType = pkg.getPreferredDroneType();
        if (!preferredType.isBlank() && !preferredType.equals("ANY")) {
            Class<?> preferredClass = null;
            switch (preferredType) {
                case "STANDARD" -> preferredClass = StandardDrone.class;
                case "EXPRESS"  -> preferredClass = ExpressDrone.class;
                case "HEAVY"    -> preferredClass = HeavyLiftDrone.class;
            }

            if (preferredClass != null) {
                System.out.println("Trying preferred drone type: " + preferredType);
                bestBattery = -1;
                for (Drone drone : drones) {
                    if (!preferredClass.isInstance(drone))   continue;
                    if (!drone.getStatus().equals("AVAILABLE"))  continue;
                    if (drone.getCapacity() < pkg.getWeight())   continue;
                    if (!hasEnoughBatteryForRoute(drone, oneSideDistance)) continue;

                    if (drone.getBattery().getLevel() > bestBattery) {
                        bestBattery = drone.getBattery().getLevel();
                        bestDrone   = drone;
                    }
                }

                if (bestDrone != null) {
                    bestDrone.setStatus("ASSIGNED");
                    System.out.println("Assigned : Drone " + bestDrone.getDroneId()
                            + " (" + bestDrone.getClass().getSimpleName() + ")"
                            + " | Battery: " + bestDrone.getBattery().getLevel() + "%");
                    return bestDrone;
                }

                System.out.println("Preferred drone type not available or suitable. Searching alternatives...");
            }
        }

        // --- PASS 1: URGENT → ExpressDrone preferred ---
        for (Drone drone : drones) {
            if (!drone.getStatus().equals("AVAILABLE"))  continue;
            if (drone.getCapacity() < pkg.getWeight())   continue;
            if (!hasEnoughBatteryForRoute(drone, oneSideDistance)) continue;

            if (pkg.getPriority().equals("URGENT")) {
                if (!(drone instanceof ExpressDrone)) continue;
            }

            // Pick drone with highest battery
            if (drone.getBattery().getLevel() > bestBattery) {
                bestBattery = drone.getBattery().getLevel();
                bestDrone   = drone;
            }
        }

        // --- PASS 2: URGENT fallback → any suitable drone ---
        if (bestDrone == null && pkg.getPriority().equals("URGENT")) {
            System.out.println("No ExpressDrone available. Trying any suitable drone...");
            bestBattery = -1;

            for (Drone drone : drones) {
                if (!drone.getStatus().equals("AVAILABLE"))  continue;
                if (drone.getCapacity() < pkg.getWeight())   continue;
                if (!hasEnoughBatteryForRoute(drone, oneSideDistance)) continue;

                if (drone.getBattery().getLevel() > bestBattery) {
                    bestBattery = drone.getBattery().getLevel();
                    bestDrone   = drone;
                }
            }
        }

        // --- PASS 3: Light package fallback → StandardDrone ---
        if (bestDrone == null && pkg.getWeight() <= 5.0) {
            System.out.println("Trying StandardDrone as fallback...");
            bestBattery = -1;

            for (Drone drone : drones) {
                if (!(drone instanceof StandardDrone))       continue;
                if (!drone.getStatus().equals("AVAILABLE"))  continue;
                if (!hasEnoughBatteryForRoute(drone, oneSideDistance)) continue;

                if (drone.getBattery().getLevel() > bestBattery) {
                    bestBattery = drone.getBattery().getLevel();
                    bestDrone   = drone;
                }
            }
        }

        // --- PASS 4: Heavy package fallback → HeavyLiftDrone ---
        if (bestDrone == null && pkg.getWeight() > 5.0) {
            System.out.println("Trying HeavyLiftDrone as fallback...");
            bestBattery = -1;

            for (Drone drone : drones) {
                if (!(drone instanceof HeavyLiftDrone))      continue;
                if (!drone.getStatus().equals("AVAILABLE"))  continue;
                if (!hasEnoughBatteryForRoute(drone, oneSideDistance)) continue;

                if (drone.getBattery().getLevel() > bestBattery) {
                    bestBattery = drone.getBattery().getLevel();
                    bestDrone   = drone;
                }
            }
        }

        // --- Final Result ---
        if (bestDrone == null) {
            System.out.println("No suitable drone found for Package "
                    + pkg.getPackageId());
            return null;
        }

        // Lock drone — no other package can grab it
        bestDrone.setStatus("ASSIGNED");

        System.out.println("Assigned : Drone " + bestDrone.getDroneId()
                + " (" + bestDrone.getClass().getSimpleName() + ")"
                + " | Battery: " + bestDrone.getBattery().getLevel() + "%");

        return bestDrone;
    }

    private double getMaxDroneCapacity() {
        double maxCapacity = 0;
        for (Drone drone : drones) {
            if (drone.getCapacity() > maxCapacity) {
                maxCapacity = drone.getCapacity();
            }
        }
        return maxCapacity;
    }

    private boolean hasEnoughBatteryForRoute(Drone drone, double oneSideDistance) {
        double requiredBattery = oneSideDistance * 2;

        if (drone instanceof ExpressDrone) {
            requiredBattery = oneSideDistance * 3;
        } else if (drone instanceof HeavyLiftDrone) {
            requiredBattery = oneSideDistance * 4;
        }

        return drone.getBattery().getLevel() >= requiredBattery;
    }
}