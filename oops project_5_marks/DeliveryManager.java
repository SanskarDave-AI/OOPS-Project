import java.util.*;
import java.util.stream.Collectors;

/**
 * DeliveryManager Class - Assignment Logic Lead
 * Responsible for:
 * 1. Drone selection and assignment
 * 2. Capacity checks
 * 3. Battery eligibility verification
 * 4. Nearest drone logic
 * 5. Distance calculation
 * 6. Priority-based sorting/filtering
 *
 * This is the BRAIN of the system!
 */
public class DeliveryManager {
    private List<IDrone> availableDrones;
    private List<PackageItem> pendingPackages;
    private List<PackageItem> deliveredPackages;
    private Location baseLocation;
    private double baseLatitude;
    private double baseLongitude;

    // Constants for battery calculation
    private static final double BATTERY_USAGE_PER_KM = 5.0;  // Battery % per km
    private static final double WEIGHT_BATTERY_MULTIPLIER = 0.5;  // Extra battery per kg
    private static final double EMERGENCY_BATTERY_THRESHOLD = 10.0;  // Keep 10% battery minimum

    /**
     * Constructor - Initialize with base location
     */
    public DeliveryManager(double baseLatitude, double baseLongitude, String baseAddress) {
        this.availableDrones = new ArrayList<>();
        this.pendingPackages = new ArrayList<>();
        this.deliveredPackages = new ArrayList<>();
        this.baseLatitude = baseLatitude;
        this.baseLongitude = baseLongitude;
        this.baseLocation = new Location(baseLatitude, baseLongitude, baseAddress);
    }

    // ==================== DRONE MANAGEMENT ====================

    /**
     * Register a drone with the delivery manager
     */
    public void registerDrone(IDrone drone) {
        if (drone == null) {
            throw new IllegalArgumentException("Drone cannot be null");
        }
        if (isDroneRegistered(drone.getId())) {
            throw new IllegalArgumentException("Drone " + drone.getId() + " is already registered");
        }
        availableDrones.add(drone);
        System.out.println("[✓] Drone " + drone.getId() + " registered successfully");
    }

    /**
     * Check if drone is already registered
     */
    public boolean isDroneRegistered(String droneId) {
        return availableDrones.stream()
                .anyMatch(drone -> drone.getId().equals(droneId));
    }

    /**
     * Get all available drones
     */
    public List<IDrone> getAvailableDrones() {
        return new ArrayList<>(availableDrones);
    }

    /**
     * Get all working drones (available for assignment)
     */
    public List<IDrone> getWorkingDrones() {
        return availableDrones.stream()
                .filter(IDrone::isAvailable)
                .collect(Collectors.toList());
    }

    // ==================== PACKAGE MANAGEMENT ====================

    /**
     * Add a new package request
     */
    public void addPackageRequest(PackageItem packageItem) {
        if (packageItem == null) {
            throw new IllegalArgumentException("Package cannot be null");
        }
        pendingPackages.add(packageItem);
        System.out.println("[✓] Package " + packageItem.getPackageId() + " added to queue");
    }

    /**
     * Get all pending packages
     */
    public List<PackageItem> getPendingPackages() {
        return new ArrayList<>(pendingPackages);
    }

    /**
     * Get all delivered packages
     */
    public List<PackageItem> getDeliveredPackages() {
        return new ArrayList<>(deliveredPackages);
    }

    // ==================== ASSIGNMENT LOGIC - BRAIN OF THE SYSTEM ====================

    /**
     * MAIN ASSIGNMENT METHOD
     * Automatically assigns the best drone to a package
     * Returns true if assignment successful, false otherwise
     */
    public boolean assignDroneToPackage(PackageItem packageItem) {
        if (packageItem == null) {
            throw new IllegalArgumentException("Package cannot be null");
        }

        System.out.println("\n[ASSIGNMENT] Processing package: " + packageItem.getPackageId());
        System.out.println("[INFO] Weight: " + packageItem.getWeight() + " kg, " +
                "Priority: " + packageItem.getPriority());

        // Step 1: Get eligible drones
        List<IDrone> eligibleDrones = filterEligibleDrones(packageItem);

        if (eligibleDrones.isEmpty()) {
            System.out.println("[✗] No eligible drones available for package " +
                    packageItem.getPackageId());
            return false;
        }

        System.out.println("[INFO] Found " + eligibleDrones.size() + " eligible drones");

        // Step 2: Rank drones based on priority and distance
        IDrone bestDrone = rankAndSelectBestDrone(eligibleDrones, packageItem);

        if (bestDrone != null) {
            // Step 3: Perform assignment
            packageItem.setAssignedDroneId(bestDrone.getId());
            packageItem.setStatus("ASSIGNED");
            System.out.println("[✓] ASSIGNED: " + packageItem.getPackageId() +
                    " → Drone " + bestDrone.getId());
            return true;
        }

        return false;
    }

    /**
     * Filter eligible drones based on:
     * 1. Availability status
     * 2. Weight capacity
     * 3. Battery level
     */
    private List<IDrone> filterEligibleDrones(PackageItem packageItem) {
        List<IDrone> eligible = new ArrayList<>();

        for (IDrone drone : availableDrones) {
            // Check 1: Drone must be available
            if (!drone.isAvailable()) {
                continue;
            }

            // Check 2: Drone must have capacity
            if (!drone.canCarryPackage(packageItem.getWeight())) {
                System.out.println("[✗] Drone " + drone.getId() + " - Insufficient capacity " +
                        "(Required: " + packageItem.getWeight() + " kg)");
                continue;
            }

            // Check 3: Battery eligibility
            double requiredBattery = calculateRequiredBattery(
                    drone.getCurrentLocation(),
                    packageItem,
                    baseLocation
            );

            if (!drone.hasEnoughBattery(requiredBattery)) {
                System.out.println("[✗] Drone " + drone.getId() + " - Insufficient battery " +
                        "(Required: " + String.format("%.1f", requiredBattery) + "% | " +
                        "Available: " + String.format("%.1f", drone.getCurrentBattery()) + "%)");
                continue;
            }

            eligible.add(drone);
        }

        return eligible;
    }

    /**
     * Calculate required battery for delivery
     * Formula: (Distance * Battery Per KM + Weight * Weight Multiplier) * Priority Multiplier + Emergency Buffer
     */
    private double calculateRequiredBattery(Location currentLocation,
                                           PackageItem packageItem,
                                           Location baseLocation) {
        // Distance: Current location → Package destination → Base
        double distanceToPackage = currentLocation.calculateDistance(packageItem.getDestination());
        double distanceToBase = packageItem.getDestination().calculateDistance(baseLocation);
        double totalDistance = distanceToPackage + distanceToBase;

        // Battery calculation
        double baseBattery = totalDistance * BATTERY_USAGE_PER_KM;
        double weightBattery = packageItem.getWeight() * WEIGHT_BATTERY_MULTIPLIER;
        double priorityMultiplier = packageItem.getPriority().getBatteryMultiplier();

        double requiredBattery = (baseBattery + weightBattery) * priorityMultiplier;
        requiredBattery += EMERGENCY_BATTERY_THRESHOLD;

        return Math.min(requiredBattery, 100.0);  // Cap at 100%
    }

    /**
     * Rank drones and select the BEST one
     * Criteria (in priority order):
     * 1. Priority-based: Higher priority packages get nearest drones
     * 2. Distance: Nearest drone (minimize travel time)
     * 3. Battery: Drone with most battery (safety margin)
     * 4. Capacity: Drone with most remaining capacity
     */
    private IDrone rankAndSelectBestDrone(List<IDrone> eligibleDrones,
                                         PackageItem packageItem) {
        System.out.println("\n[RANKING] Evaluating drones...");

        // Create scoring for each drone
        Map<IDrone, Double> droneScores = new HashMap<>();

        for (IDrone drone : eligibleDrones) {
            double distance = drone.getCurrentLocation()
                    .calculateDistance(packageItem.getDestination());

            // Scoring system (higher is better)
            double score = calculateDroneScore(drone, packageItem, distance);
            droneScores.put(drone, score);

            System.out.println("  → Drone " + drone.getId() + " | " +
                    "Distance: " + String.format("%.2f", distance) + " km | " +
                    "Battery: " + String.format("%.1f", drone.getCurrentBattery()) + "% | " +
                    "Score: " + String.format("%.2f", score));
        }

        // Get drone with highest score
        IDrone bestDrone = Collections.max(droneScores.entrySet(), Map.Entry.comparingByValue())
                .getKey();

        System.out.println("[WINNER] Drone " + bestDrone.getId() + " selected (Score: " +
                String.format("%.2f", droneScores.get(bestDrone)) + ")");

        return bestDrone;
    }

    /**
     * Calculate drone score for ranking
     * Factors:
     * - Distance to package (lower = better) → Multiply by 100 for visibility
     * - Battery level (higher = better) → Already 0-100
     * - Capacity efficiency (higher = better)
     * - Priority level (urgent packages get priority bonus)
     */
    private double calculateDroneScore(IDrone drone,
                                      PackageItem packageItem,
                                      double distance) {
        // Inverse distance scoring (closer = higher score)
        double distanceScore = Math.max(100 - (distance * 2), 0);  // 2 = distance penalty

        // Battery scoring (higher battery = higher score)
        double batteryScore = drone.getCurrentBattery();

        // Capacity efficiency scoring
        double capacityScore = (drone.getCapacity() - packageItem.getWeight()) / drone.getCapacity() * 50;

        // Priority bonus (urgent packages get faster drones)
        double priorityBonus = packageItem.getPriority().getLevel() * 5;

        // Weighted sum
        double finalScore = (distanceScore * 0.4) +  // 40% weight on distance
                            (batteryScore * 0.3) +    // 30% weight on battery
                            (capacityScore * 0.2) +    // 20% weight on capacity
                            (priorityBonus * 0.1);     // 10% weight on priority

        return finalScore;
    }

    /**
     * Process all pending packages
     * Tries to assign each package to the best available drone
     */
    public void processPendingPackages() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PROCESSING " + pendingPackages.size() + " PENDING PACKAGES");
        System.out.println("=".repeat(60));

        List<PackageItem> toRemove = new ArrayList<>();

        for (PackageItem packageItem : pendingPackages) {
            if (assignDroneToPackage(packageItem)) {
                toRemove.add(packageItem);
            }
        }

        // Remove assigned packages from pending list
        pendingPackages.removeAll(toRemove);

        System.out.println("\n[SUMMARY] " + toRemove.size() + " packages assigned, " +
                pendingPackages.size() + " packages still pending");
    }

    /**
     * Mark package as delivered and remove from assignment
     */
    public void markPackageDelivered(String packageId) {
        for (PackageItem packageItem : pendingPackages) {
            if (packageItem.getPackageId().equals(packageId)) {
                packageItem.markDelivered();
                deliveredPackages.add(packageItem);
                pendingPackages.remove(packageItem);
                System.out.println("[✓] Package " + packageId + " marked as DELIVERED");
                return;
            }
        }
        System.out.println("[✗] Package " + packageId + " not found");
    }

    // ==================== STATISTICS & REPORTING ====================

    /**
     * Get fleet status report
     */
    public void printFleetStatus() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("FLEET STATUS REPORT");
        System.out.println("=".repeat(60));
        System.out.println("Total Drones: " + availableDrones.size());
        System.out.println("Available Drones: " + getWorkingDrones().size());
        System.out.println("Pending Packages: " + pendingPackages.size());
        System.out.println("Delivered Packages: " + deliveredPackages.size());
        System.out.println("\nDrone Details:");

        for (IDrone drone : availableDrones) {
            System.out.println("  " + drone.getDroneInfo());
        }
    }

    /**
     * Get assignment statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDrones", availableDrones.size());
        stats.put("availableDrones", getWorkingDrones().size());
        stats.put("pendingPackages", pendingPackages.size());
        stats.put("deliveredPackages", deliveredPackages.size());
        stats.put("successRate", deliveredPackages.size() * 100.0 /
                (deliveredPackages.size() + pendingPackages.size() + 0.001));
        return stats;
    }

    /**
     * Get base location
     */
    public Location getBaseLocation() {
        return baseLocation;
    }

    /**
     * Get specific drone by ID
     */
    public IDrone getDroneById(String droneId) {
        return availableDrones.stream()
                .filter(drone -> drone.getId().equals(droneId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get specific package by ID
     */
    public PackageItem getPackageById(String packageId) {
        for (PackageItem pkg : pendingPackages) {
            if (pkg.getPackageId().equals(packageId)) {
                return pkg;
            }
        }
        for (PackageItem pkg : deliveredPackages) {
            if (pkg.getPackageId().equals(packageId)) {
                return pkg;
            }
        }
        return null;
    }
}
