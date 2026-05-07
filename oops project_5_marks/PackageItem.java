import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Package Class for Package Delivery Management
 * Stores all package details and delivery information
 */
public class PackageItem {
    private String packageId;
    private double weight;
    private Location destination;
    private Location source;
    private Priority priority;
    private String status;
    private String assignedDroneId;
    private LocalDateTime createdTime;
    private LocalDateTime deliveryTime;

    // Constants
    private static final String[] VALID_STATUSES = {
            "PENDING",
            "ASSIGNED",
            "IN_DELIVERY",
            "DELIVERED",
            "FAILED"
    };

    // Constructor
    public PackageItem(String packageId, double weight, Location destination,
                       Location source, Priority priority) {
        if (packageId == null || packageId.trim().isEmpty()) {
            throw new IllegalArgumentException("Package ID cannot be null or empty");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Package weight must be greater than 0");
        }
        if (destination == null) {
            throw new IllegalArgumentException("Destination cannot be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Source location cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }

        this.packageId = packageId;
        this.weight = weight;
        this.destination = destination;
        this.source = source;
        this.priority = priority;
        this.status = "PENDING";
        this.assignedDroneId = null;
        this.createdTime = LocalDateTime.now();
        this.deliveryTime = null;
    }

    // Getters
    public String getPackageId() {
        return packageId;
    }

    public double getWeight() {
        return weight;
    }

    public Location getDestination() {
        return destination;
    }

    public Location getSource() {
        return source;
    }

    public Priority getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedDroneId() {
        return assignedDroneId;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    // Setters
    public void setStatus(String newStatus) {
        if (newStatus == null || newStatus.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be null or empty");
        }

        boolean isValidStatus = false;
        for (String validStatus : VALID_STATUSES) {
            if (validStatus.equalsIgnoreCase(newStatus)) {
                isValidStatus = true;
                break;
            }
        }

        if (!isValidStatus) {
            throw new IllegalArgumentException("Invalid status. Valid statuses are: " +
                    String.join(", ", VALID_STATUSES));
        }

        this.status = newStatus.toUpperCase();
    }

    public void setAssignedDroneId(String droneId) {
        this.assignedDroneId = droneId;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * Get delivery distance from source to destination
     */
    public double getDeliveryDistance() {
        return source.calculateDistance(destination);
    }

    /**
     * Get simple distance calculation
     */
    public double getSimpleDeliveryDistance() {
        return source.calculateSimpleDistance(destination);
    }

    /**
     * Mark package as delivered
     */
    public void markDelivered() {
        this.status = "DELIVERED";
        this.deliveryTime = LocalDateTime.now();
    }

    /**
     * Mark package as failed
     */
    public void markFailed() {
        this.status = "FAILED";
    }

    /**
     * Get delivery duration in seconds (if delivered)
     */
    public long getDeliveryDurationSeconds() {
        if (deliveryTime == null) {
            return -1;
        }
        return java.time.temporal.ChronoUnit.SECONDS.between(createdTime, deliveryTime);
    }

    @Override
    public String toString() {
        return "PackageItem{" +
                "packageId='" + packageId + '\'' +
                ", weight=" + weight + " kg" +
                ", priority=" + priority +
                ", status='" + status + '\'' +
                ", destination=" + destination +
                ", assignedDrone=" + (assignedDroneId != null ? assignedDroneId : "Not Assigned") +
                ", createdTime=" + createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                '}';
    }

    /**
     * Get detailed information about the package
     */
    public String getDetailedInfo() {
        StringBuilder info = new StringBuilder();
        info.append("\n=== PACKAGE DETAILS ===\n");
        info.append("Package ID: ").append(packageId).append("\n");
        info.append("Weight: ").append(weight).append(" kg\n");
        info.append("Priority: ").append(priority).append("\n");
        info.append("Status: ").append(status).append("\n");
        info.append("Source: ").append(source.getAddress()).append("\n");
        info.append("Destination: ").append(destination.getAddress()).append("\n");
        info.append("Distance: ").append(getDeliveryDistance()).append(" km\n");
        info.append("Assigned Drone: ").append(assignedDroneId != null ? assignedDroneId : "Not Assigned").append("\n");
        info.append("Created: ").append(createdTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
        if (deliveryTime != null) {
            info.append("Delivered: ").append(deliveryTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
            info.append("Duration: ").append(getDeliveryDurationSeconds()).append(" seconds\n");
        }
        return info.toString();
    }
}
