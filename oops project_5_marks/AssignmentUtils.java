import java.util.*;

/**
 * AssignmentUtils - Helper Utilities for Package Assignment
 * Contains utility methods for:
 * - Drone filtering
 * - Distance calculations
 * - Sorting and ranking
 * - Validation checks
 */
public class AssignmentUtils {

    /**
     * Sort drones by distance to a location (nearest first)
     */
    public static List<IDrone> sortDronesByDistance(List<IDrone> drones, Location targetLocation) {
        List<IDrone> sortedDrones = new ArrayList<>(drones);
        sortedDrones.sort((drone1, drone2) -> {
            double distance1 = drone1.getCurrentLocation().calculateDistance(targetLocation);
            double distance2 = drone2.getCurrentLocation().calculateDistance(targetLocation);
            return Double.compare(distance1, distance2);
        });
        return sortedDrones;
    }

    /**
     * Sort drones by battery level (highest first)
     */
    public static List<IDrone> sortDronesByBattery(List<IDrone> drones) {
        List<IDrone> sortedDrones = new ArrayList<>(drones);
        sortedDrones.sort((drone1, drone2) ->
                Double.compare(drone2.getCurrentBattery(), drone1.getCurrentBattery())
        );
        return sortedDrones;
    }

    /**
     * Sort drones by capacity (highest first)
     */
    public static List<IDrone> sortDronesByCapacity(List<IDrone> drones) {
        List<IDrone> sortedDrones = new ArrayList<>(drones);
        sortedDrones.sort((drone1, drone2) ->
                Double.compare(drone2.getCapacity(), drone1.getCapacity())
        );
        return sortedDrones;
    }

    /**
     * Sort packages by priority (highest first)
     */
    public static List<PackageItem> sortPackagesByPriority(List<PackageItem> packages) {
        List<PackageItem> sortedPackages = new ArrayList<>(packages);
        sortedPackages.sort((pkg1, pkg2) ->
                Integer.compare(pkg2.getPriority().getLevel(), pkg1.getPriority().getLevel())
        );
        return sortedPackages;
    }

    /**
     * Find nearest drone to a location
     */
    public static IDrone findNearestDrone(List<IDrone> drones, Location targetLocation) {
        if (drones == null || drones.isEmpty()) {
            return null;
        }

        IDrone nearest = drones.get(0);
        double minDistance = nearest.getCurrentLocation().calculateDistance(targetLocation);

        for (IDrone drone : drones) {
            double distance = drone.getCurrentLocation().calculateDistance(targetLocation);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = drone;
            }
        }

        return nearest;
    }

    /**
     * Find drone with highest battery
     */
    public static IDrone findDroneWithHighestBattery(List<IDrone> drones) {
        if (drones == null || drones.isEmpty()) {
            return null;
        }

        IDrone best = drones.get(0);
        for (IDrone drone : drones) {
            if (drone.getCurrentBattery() > best.getCurrentBattery()) {
                best = drone;
            }
        }
        return best;
    }

    /**
     * Find drone with highest capacity
     */
    public static IDrone findDroneWithHighestCapacity(List<IDrone> drones) {
        if (drones == null || drones.isEmpty()) {
            return null;
        }

        IDrone best = drones.get(0);
        for (IDrone drone : drones) {
            if (drone.getCapacity() > best.getCapacity()) {
                best = drone;
            }
        }
        return best;
    }

    /**
     * Check if any drone in the list can carry the package
     */
    public static boolean canAnyDroneCarry(List<IDrone> drones, double weight) {
        return drones.stream().anyMatch(drone -> drone.canCarryPackage(weight));
    }

    /**
     * Check if any drone has sufficient battery
     */
    public static boolean hasAnyDroneEnoughBattery(List<IDrone> drones, double requiredBattery) {
        return drones.stream().anyMatch(drone -> drone.hasEnoughBattery(requiredBattery));
    }

    /**
     * Calculate average battery level across all drones
     */
    public static double getAverageBatteryLevel(List<IDrone> drones) {
        if (drones == null || drones.isEmpty()) {
            return 0;
        }
        return drones.stream()
                .mapToDouble(IDrone::getCurrentBattery)
                .average()
                .orElse(0);
    }

    /**
     * Calculate total package weight
     */
    public static double getTotalPackageWeight(List<PackageItem> packages) {
        return packages.stream()
                .mapToDouble(PackageItem::getWeight)
                .sum();
    }

    /**
     * Group packages by priority
     */
    public static Map<Priority, List<PackageItem>> groupPackagesByPriority(
            List<PackageItem> packages) {
        Map<Priority, List<PackageItem>> grouped = new HashMap<>();

        for (PackageItem pkg : packages) {
            grouped.computeIfAbsent(pkg.getPriority(), k -> new ArrayList<>())
                    .add(pkg);
        }

        return grouped;
    }

    /**
     * Calculate total delivery distance for a list of packages
     */
    public static double getTotalDeliveryDistance(List<PackageItem> packages) {
        return packages.stream()
                .mapToDouble(PackageItem::getDeliveryDistance)
                .sum();
    }

    /**
     * Validate package assignment
     */
    public static boolean isValidAssignment(IDrone drone, PackageItem packageItem,
                                           double requiredBattery) {
        return drone.isAvailable() &&
               drone.canCarryPackage(packageItem.getWeight()) &&
               drone.hasEnoughBattery(requiredBattery);
    }

    /**
     * Generate assignment report
     */
    public static String generateAssignmentReport(IDrone drone, PackageItem packageItem) {
        StringBuilder report = new StringBuilder();
        report.append("\n=== ASSIGNMENT REPORT ===\n");
        report.append("Drone ID: ").append(drone.getId()).append("\n");
        report.append("Package ID: ").append(packageItem.getPackageId()).append("\n");
        report.append("Distance: ").append(String.format("%.2f",
                drone.getCurrentLocation().calculateDistance(packageItem.getDestination())))
                .append(" km\n");
        report.append("Package Weight: ").append(packageItem.getWeight()).append(" kg\n");
        report.append("Drone Capacity: ").append(drone.getCapacity()).append(" kg\n");
        report.append("Current Battery: ").append(String.format("%.1f", drone.getCurrentBattery()))
                .append("%\n");
        report.append("Priority: ").append(packageItem.getPriority()).append("\n");
        return report.toString();
    }
}
