package drone.delivery.system;

/**
 * Main class to test the Drone Delivery System - Member 1 Implementation
 * Demonstrates OOP concepts: Encapsulation, Inheritance, Polymorphism
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║     DRONE DELIVERY SYSTEM - MEMBER 1 IMPLEMENTATION        ║");
        System.out.println("║          Core Drone & OOP Architecture                       ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        // Test Location class
        System.out.println("=== TESTING LOCATION CLASS ===");
        Location warehouse = new Location("Central Warehouse", 0, 0);
        Location destination1 = new Location("Downtown", 25, 30);
        Location destination2 = new Location("Suburbs", 50, 20);
        Location chargingStation = new Location("Charging Hub", 10, 10);
        
        System.out.println("Warehouse: " + warehouse);
        System.out.println("Destination 1: " + destination1);
        System.out.println("Distance Warehouse → Destination 1: " + 
                           String.format("%.2f", warehouse.distanceTo(destination1)) + " units");
        System.out.println();
        
        // Test Battery class
        System.out.println("=== TESTING BATTERY CLASS ===");
        Battery battery = new Battery(100.0, 1.0);
        System.out.println("Initial " + battery);
        System.out.println("Draining 15 units of battery...");
        battery.drain(15);
        System.out.println("After drain: " + battery);
        System.out.println("Is critical: " + battery.isCritical());
        System.out.println("Charging battery...");
        battery.charge();
        System.out.println("After charge: " + battery);
        System.out.println();
        
        // Test Drone classes - Inheritance demonstration
        System.out.println("=== TESTING DRONE HIERARCHY (INHERITANCE) ===");
        
        // Create different drone types
        Drone standardDrone = new StandardDrone("DRONE-S-001", 
            new Location("Base A", 0, 0));
        Drone expressDrone = new ExpressDrone("DRONE-E-001", 
            new Location("Base B", 5, 5));
        Drone heavyLiftDrone = new HeavyLiftDrone("DRONE-H-001", 
            new Location("Base C", 10, 10));
        
        System.out.println("\n1. Standard Drone:");
        System.out.println("   " + standardDrone);
        System.out.println("   Type: " + ((StandardDrone)standardDrone).getDroneType());
        System.out.println("   Cost Multiplier: " + ((StandardDrone)standardDrone).getCostMultiplier());
        
        System.out.println("\n2. Express Drone:");
        System.out.println("   " + expressDrone);
        System.out.println("   Type: " + ((ExpressDrone)expressDrone).getDroneType());
        System.out.println("   Cost Multiplier: " + ((ExpressDrone)expressDrone).getCostMultiplier());
        
        System.out.println("\n3. Heavy Lift Drone:");
        System.out.println("   " + heavyLiftDrone);
        System.out.println("   Type: " + ((HeavyLiftDrone)heavyLiftDrone).getDroneType());
        System.out.println("   Cost Multiplier: " + ((HeavyLiftDrone)heavyLiftDrone).getCostMultiplier());
        System.out.println();
        
        // Test polymorphism
        System.out.println("=== TESTING POLYMORPHISM ===");
        Drone[] drones = {standardDrone, expressDrone, heavyLiftDrone};
        
        System.out.println("\nAll drones flying to Destination 1:");
        for (Drone drone : drones) {
            boolean success = drone.flyTo(destination1);
            System.out.println("   " + drone.getDroneId() + " → " + 
                             (success ? "SUCCESS" : "FAILED") +
                             " | Battery remaining: " + 
                             String.format("%.1f%%", drone.getBattery().getCurrentCharge()));
        }
        System.out.println();
        
        // Test encapsulation - accessing via getters only
        System.out.println("=== TESTING ENCAPSULATION ===");
        System.out.println("Accessing drone data only through getters:");
        System.out.println("   Drone ID: " + standardDrone.getDroneId());
        System.out.println("   Model: " + standardDrone.getModelName());
        System.out.println("   Location: " + standardDrone.getCurrentLocation().getName());
        System.out.println("   Max Load: " + standardDrone.getMaxLoadCapacity() + " kg");
        System.out.println("   Speed: " + standardDrone.getSpeed() + " units/hr");
        System.out.println("   Status: " + standardDrone.getStatus());
        System.out.println();
        
        // Test canReach functionality
        System.out.println("=== TESTING BATTERY & NAVIGATION ===");
        System.out.println("Can Standard Drone reach Destination 2? " + 
                         standardDrone.canReach(destination2));
        System.out.println("Estimated time: " + 
                         String.format("%.2f", standardDrone.getEstimatedTime(destination2)) + " hours");
        System.out.println("Can carry 8kg package? " + standardDrone.canCarry(8.0));
        System.out.println("Can carry 15kg package? " + standardDrone.canCarry(15.0));
        System.out.println();
        
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║          MEMBER 1 IMPLEMENTATION COMPLETE ✓                 ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
}