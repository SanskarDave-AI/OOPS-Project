// ============================================
// File: simulation/DeliverySimulator.java
// ============================================
package simulation;

import drones.Drone;
import manager.DeliveryPackage;
import components.Location;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DeliverySimulator {

    private static final Location WAREHOUSE = new Location(0, 0);
    private static final DateTimeFormatter TIME_FORMAT = 
        DateTimeFormatter.ofPattern("HH:mm:ss");

    // =====================================================
    // MAIN DELIVERY EXECUTION ENGINE
    // =====================================================
    public void executeDelivery(Drone drone, DeliveryPackage pkg) {
        
        try {
            // ===== 1. SHOW DELIVERY START BANNER =====
            System.out.println("\n" + "=".repeat(70));
            System.out.println(" 🚁 DELIVERY SIMULATION STARTED - " + 
                getTimestamp());
            System.out.println("=".repeat(70));

            // ===== 2. SHOW ASSIGNED DRONE DETAILS =====
            System.out.println("\n[DRONE ASSIGNMENT]");
            System.out.println("  Drone ID    : " + drone.getDroneId());
            System.out.println("  Type        : " + drone.getClass().getSimpleName());
            System.out.println("  Capacity    : " + drone.getCapacity() + " kg");
            System.out.println("  Speed       : " + drone.getSpeed() + " m/s");
            System.out.println("  Current Loc : " + drone.getLocation());
            System.out.println("  Battery     : " + drone.getBattery().getLevel() + "%");

            // ===== 3. SHOW PACKAGE DETAILS =====
            System.out.println("\n[PACKAGE DETAILS]");
            System.out.println("  Package ID  : " + pkg.getPackageId());
            System.out.println("  Weight      : " + pkg.getWeight() + " kg");
            System.out.println("  Priority    : " + pkg.getPriority());
            System.out.println("  Destination : " + pkg.getDestination());

            // ===== 4. CALCULATE DISTANCES & TIME =====
            System.out.println("\n[DISTANCE & TIME CALCULATION]");
            
            double oneSideDistance = distanceTo(WAREHOUSE, pkg.getDestination());
            double roundTripDistance = oneSideDistance * 2;
            double estimatedTime = oneSideDistance / drone.getSpeed();
            
            System.out.println("  One-side Distance   : " + 
                String.format("%.2f", oneSideDistance) + " meters");
            System.out.println("  Round-trip Distance : " + 
                String.format("%.2f", roundTripDistance) + " meters");
            System.out.println("  Drone Speed         : " + drone.getSpeed() + " m/s");
            System.out.println("  Estimated Time      : " + 
                String.format("%.2f", estimatedTime) + " seconds");

            // ===== 5. FLY DRONE TO DESTINATION =====
            System.out.println("\n[FLIGHT TO DESTINATION]");
            System.out.println("  ✈ Taking off from " + drone.getLocation());
            System.out.println("  ✈ Flying to destination " + pkg.getDestination());
            pause();
            
            drone.flyTo(pkg.getDestination());
            
            System.out.println("  ✈ Arrived at destination");
            System.out.println("  ✈ Battery after flight: " + 
                String.format("%.2f", drone.getBattery().getLevel()) + "%");

            // ===== 6. VERIFY DRONE REACHED DESTINATION =====
            System.out.println("\n[DESTINATION VERIFICATION]");
            if (drone.getLocation().equals(pkg.getDestination()) || 
                distanceTo(drone.getLocation(), pkg.getDestination()) < 0.01) {
                System.out.println("  ✓ Drone successfully reached destination");
            } else {
                System.out.println("  ✗ ERROR: Drone not at destination!");
                return;
            }

            // ===== 7. DELIVER PACKAGE =====
            System.out.println("\n[PACKAGE DELIVERY]");
            pause();
            drone.setStatus("DELIVERING");
            System.out.println("  📦 Delivering package " + pkg.getPackageId() + 
                " at " + pkg.getDestination());
            System.out.println("  📦 Package weight: " + pkg.getWeight() + " kg");
            pause();
            System.out.println("  ✓ Package delivered successfully");

            // ===== 8. MARK PACKAGE DELIVERED =====
            pkg.setDelivered(true);
            System.out.println("  ✓ Package " + pkg.getPackageId() + 
                " marked as DELIVERED");

            // ===== 9. RETURN DRONE TO WAREHOUSE =====
            System.out.println("\n[RETURN TO WAREHOUSE]");
            drone.setStatus("RETURNING");
            System.out.println("  ← Returning to warehouse from " + 
                drone.getLocation());
            pause();
            
            drone.setLocation(WAREHOUSE);
            // NOTE: Battery NOT drained during return (already accounted in round trip)
            
            System.out.println("  ← Drone arrived at warehouse: " + 
                drone.getLocation());

            // ===== 10. CHECK BATTERY AFTER RETURN =====
            System.out.println("\n[BATTERY CHECK]");
            double remainingBattery = drone.getBattery().getLevel();
            System.out.println("  Battery Level: " + 
                String.format("%.2f", remainingBattery) + "%");
            checkBattery(drone);

            // ===== 11-14. CHARGING IF NEEDED =====
            if (remainingBattery <= 30) {
                System.out.println("\n[AUTO-CHARGING]");
                System.out.println("  🔋 Battery level is " + 
                    String.format("%.2f", remainingBattery) + 
                    "% (≤ 30%) — Initiating charging sequence");
                
                drone.setStatus("CHARGING");
                System.out.println("  🔋 Drone Status: CHARGING (NOT AVAILABLE)");
                
                // Charging simulation for 30 seconds (shown with progress)
                simulateCharging(drone);
                
                drone.getBattery().chargeBattery();
                drone.setStatus("AVAILABLE");
                System.out.println("  ✓ Charging complete!");
                System.out.println("  ✓ Drone Status: AVAILABLE");
            } else {
                drone.setStatus("AVAILABLE");
                System.out.println("  ✓ Battery sufficient — Drone Status: AVAILABLE");
            }

            // ===== 15. SHOW FINAL DRONE STATUS =====
            System.out.println("\n[FINAL DRONE STATUS]");
            System.out.println("  Drone ID    : " + drone.getDroneId());
            System.out.println("  Type        : " + 
                drone.getClass().getSimpleName());
            System.out.println("  Location    : " + drone.getLocation());
            System.out.println("  Battery     : " + 
                String.format("%.2f", drone.getBattery().getLevel()) + "%");
            System.out.println("  Status      : " + drone.getStatus());

            System.out.println("\n" + "=".repeat(70));
            System.out.println(" ✓ DELIVERY SIMULATION COMPLETE - " + 
                getTimestamp());
            System.out.println("=".repeat(70));

        } catch (InterruptedException e) {
            System.out.println("ERROR: Delivery simulation interrupted!");
            e.printStackTrace();
        }
    }

    // =====================================================
    // HELPER METHODS
    // =====================================================

    /**
     * Calculate battery multiplier based on drone type
     * StandardDrone: distance * 2
     * ExpressDrone: distance * 3
     * HeavyLiftDrone: distance * 4
     */
    /**
     * Calculate Euclidean distance between two locations (in meters)
     */
    private double distanceTo(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check battery status and provide feedback
     */
    private void checkBattery(Drone drone) {
        double battery = drone.getBattery().getLevel();
        
        if (battery > 50) {
            System.out.println("  ✓ Battery Status: EXCELLENT (" + 
                String.format("%.2f", battery) + "%)");
        } else if (battery > 30) {
            System.out.println("  ⚠ Battery Status: GOOD (" + 
                String.format("%.2f", battery) + "%)");
        } else if (battery > 10) {
            System.out.println("  ⚠ Battery Status: LOW (" + 
                String.format("%.2f", battery) + "%)");
        } else {
            System.out.println("  ⚠ Battery Status: CRITICAL (" + 
                String.format("%.2f", battery) + "%)");
        }
    }

    /**
     * Pause execution for 1.5 seconds
     */
    private void pause() throws InterruptedException {
        Thread.sleep(1500);
    }

    /**
     * Simulate charging process for 30 seconds with progress
     */
    private void simulateCharging(Drone drone) throws InterruptedException {
        int totalSteps = 30;
        System.out.println("  🔋 Charging in progress:");
        
        for (int i = 1; i <= totalSteps; i++) {
            Thread.sleep(1000);  // 1 second per step
            int percent = (i * 100) / totalSteps;
            System.out.print("  [" + percent + "%] ");
            
            // Progress bar
            int barLength = 25;
            int filledLength = (i * barLength) / totalSteps;
            System.out.print("[");
            for (int j = 0; j < barLength; j++) {
                if (j < filledLength) {
                    System.out.print("=");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println("]");
        }
    }

    /**
     * Get current timestamp for logging
     */
    private String getTimestamp() {
        return LocalDateTime.now().format(TIME_FORMAT);
    }
}
