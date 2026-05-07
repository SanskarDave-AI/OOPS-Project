/**
 * Demo Class - Testing Package & Assignment Logic
 * Member 2's Complete Module Demonstration
 * Shows all assignment features working together
 */
public class AssignmentDemo {

    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("SMART MULTI-DRONE PACKAGE DELIVERY SIMULATOR - ASSIGNMENT MODULE");
        System.out.println("Member 2: Package & Assignment Logic Lead");
        System.out.println("=".repeat(70));

        // ==================== SETUP ====================
        System.out.println("\n[SETUP] Initializing Delivery System...\n");

        // Create base location
        DeliveryManager deliveryManager = new DeliveryManager(
                28.7041,
                77.1025,
                "Delivery Hub - New Delhi"
        );

        // ==================== CREATE MOCK DRONES ====================
        System.out.println("[DRONES] Creating drone fleet...\n");

        MockDrone drone1 = new MockDrone("D001", 2.0, 100.0, 100.0,
                new Location(28.7041, 77.1025, "Base Station"));
        deliveryManager.registerDrone(drone1);

        MockDrone drone2 = new MockDrone("D002", 5.0, 100.0, 80.0,
                new Location(28.7041, 77.1025, "Base Station"));
        deliveryManager.registerDrone(drone2);

        MockDrone drone3 = new MockDrone("D003", 10.0, 150.0, 60.0,
                new Location(28.7041, 77.1025, "Base Station"));
        deliveryManager.registerDrone(drone3);

        // ==================== CREATE LOCATIONS ====================
        System.out.println("\n[LOCATIONS] Setting up delivery destinations...\n");

        Location source = new Location(28.7041, 77.1025, "New Delhi Hub");
        Location dest1 = new Location(28.5355, 77.3910, "Gurgaon");
        Location dest2 = new Location(28.6139, 77.2090, "Delhi Downtown");
        Location dest3 = new Location(28.4595, 77.0266, "Noida");

        // ==================== CREATE PACKAGES ====================
        System.out.println("[PACKAGES] Creating package requests...\n");

        PackageItem pkg1 = new PackageItem("PKG001", 1.5, dest1, source, Priority.HIGH);
        PackageItem pkg2 = new PackageItem("PKG002", 4.5, dest2, source, Priority.URGENT);
        PackageItem pkg3 = new PackageItem("PKG003", 8.0, dest3, source, Priority.MEDIUM);
        PackageItem pkg4 = new PackageItem("PKG004", 2.0, dest1, source, Priority.LOW);

        // Add packages to manager
        deliveryManager.addPackageRequest(pkg1);
        deliveryManager.addPackageRequest(pkg2);
        deliveryManager.addPackageRequest(pkg3);
        deliveryManager.addPackageRequest(pkg4);

        // ==================== DISPLAY INITIAL STATUS ====================
        deliveryManager.printFleetStatus();

        // ==================== DEMONSTRATE ASSIGNMENT LOGIC ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ASSIGNMENT ALGORITHM - DEMONSTRATING MEMBER 2's LOGIC");
        System.out.println("=".repeat(70));

        // Process packages with detailed output
        deliveryManager.processPendingPackages();

        // ==================== DISPLAY RESULTS ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ASSIGNMENT RESULTS");
        System.out.println("=".repeat(70));

        for (PackageItem pkg : deliveryManager.getDeliveredPackages()) {
            System.out.println(pkg.getDetailedInfo());
        }

        // ==================== DEMONSTRATE SORTING & FILTERING ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("UTILITY FEATURES - SORTING & FILTERING");
        System.out.println("=".repeat(70));

        // Sort by priority
        System.out.println("\n[SORTING] Packages by Priority (Highest First):");
        var prioritySorted = AssignmentUtils.sortPackagesByPriority(
                deliveryManager.getPendingPackages()
        );
        for (PackageItem pkg : prioritySorted) {
            System.out.println("  → " + pkg.getPackageId() + ": " + pkg.getPriority());
        }

        // Sort drones by distance
        System.out.println("\n[SORTING] Drones by Distance to Gurgaon (Nearest First):");
        var distanceSorted = AssignmentUtils.sortDronesByDistance(
                deliveryManager.getAvailableDrones(),
                dest1
        );
        for (IDrone drone : distanceSorted) {
            double distance = drone.getCurrentLocation().calculateDistance(dest1);
            System.out.println("  → " + drone.getId() + ": " +
                    String.format("%.2f", distance) + " km away");
        }

        // Sort drones by battery
        System.out.println("\n[SORTING] Drones by Battery Level (Highest First):");
        var batterySorted = AssignmentUtils.sortDronesByBattery(
                deliveryManager.getAvailableDrones()
        );
        for (IDrone drone : batterySorted) {
            System.out.println("  → " + drone.getId() + ": " +
                    String.format("%.1f", drone.getCurrentBattery()) + "%");
        }

        // ==================== DEMONSTRATE UTILITY METHODS ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("ASSIGNMENT UTILITIES - USEFUL FUNCTIONS");
        System.out.println("=".repeat(70));

        System.out.println("\n[STATS] Fleet Statistics:");
        System.out.println("  Average Battery: " +
                String.format("%.1f", AssignmentUtils.getAverageBatteryLevel(
                        deliveryManager.getAvailableDrones())) + "%");
        System.out.println("  Total Package Weight: " +
                AssignmentUtils.getTotalPackageWeight(
                        deliveryManager.getPendingPackages()) + " kg");
        System.out.println("  Total Delivery Distance: " +
                String.format("%.2f", AssignmentUtils.getTotalDeliveryDistance(
                        deliveryManager.getPendingPackages())) + " km");

        System.out.println("\n[QUERIES] Nearest Drone to Gurgaon:");
        IDrone nearest = AssignmentUtils.findNearestDrone(
                deliveryManager.getAvailableDrones(),
                dest1
        );
        if (nearest != null) {
            System.out.println("  → Drone: " + nearest.getId() + " at " +
                    String.format("%.2f", nearest.getCurrentLocation()
                            .calculateDistance(dest1)) + " km");
        }

        System.out.println("\n[QUERIES] Drone with Highest Battery:");
        IDrone maxBattery = AssignmentUtils.findDroneWithHighestBattery(
                deliveryManager.getAvailableDrones()
        );
        if (maxBattery != null) {
            System.out.println("  → Drone: " + maxBattery.getId() + " with " +
                    String.format("%.1f", maxBattery.getCurrentBattery()) + "%");
        }

        // ==================== SUMMARY STATISTICS ====================
        System.out.println("\n" + "=".repeat(70));
        System.out.println("FINAL SUMMARY");
        System.out.println("=".repeat(70));

        var stats = deliveryManager.getStatistics();
        System.out.println("\nTotal Drones: " + stats.get("totalDrones"));
        System.out.println("Available Drones: " + stats.get("availableDrones"));
        System.out.println("Pending Packages: " + stats.get("pendingPackages"));
        System.out.println("Delivered Packages: " + stats.get("deliveredPackages"));
        System.out.println("Success Rate: " + String.format("%.1f", (double)stats.get("successRate")) + "%");

        System.out.println("\n" + "=".repeat(70));
        System.out.println("MEMBER 2's ASSIGNMENT MODULE - ALL FEATURES DEMONSTRATED ✓");
        System.out.println("=".repeat(70) + "\n");
    }
}

/**
 * MockDrone - A simple implementation of IDrone for testing purposes
 * Real implementation will be done by Member 1
 */
class MockDrone implements IDrone {
    private String id;
    private double capacity;
    private double currentBattery;
    private double maxBattery;
    private Location currentLocation;
    private String status;

    public MockDrone(String id, double capacity, double maxBattery,
                     double currentBattery, Location currentLocation) {
        this.id = id;
        this.capacity = capacity;
        this.maxBattery = maxBattery;
        this.currentBattery = currentBattery;
        this.currentLocation = currentLocation;
        this.status = "AVAILABLE";
    }

    @Override
    public String getId() { return id; }

    @Override
    public double getCapacity() { return capacity; }

    @Override
    public double getCurrentBattery() { return currentBattery; }

    @Override
    public double getMaxBattery() { return maxBattery; }

    @Override
    public Location getCurrentLocation() { return currentLocation; }

    @Override
    public String getStatus() { return status; }

    @Override
    public void flyTo(Location destination) {
        this.currentLocation = destination;
        this.status = "FLYING";
    }

    @Override
    public void deliver(PackageItem packageItem) {
        this.status = "DELIVERING";
    }

    @Override
    public void charge() {
        this.currentBattery = maxBattery;
        this.status = "CHARGING";
    }

    @Override
    public void returnToBase(Location baseLocation) {
        this.currentLocation = baseLocation;
        this.status = "AVAILABLE";
    }

    @Override
    public double calculateBatteryUsage(Location from, Location to, double weight, Priority priority) {
        double distance = from.calculateDistance(to);
        return (distance * 5.0 + weight * 0.5) * priority.getBatteryMultiplier();
    }

    @Override
    public boolean hasEnoughBattery(double requiredBattery) {
        return currentBattery >= requiredBattery;
    }

    @Override
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }

    @Override
    public boolean canCarryPackage(double weight) {
        return weight <= capacity;
    }

    @Override
    public String getDroneInfo() {
        return String.format("Drone %s | Status: %s | Battery: %.1f%% | Capacity: %.1f kg | Location: %s",
                id, status, currentBattery, capacity, currentLocation.getAddress());
    }
}
