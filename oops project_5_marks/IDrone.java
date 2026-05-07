/**
 * Drone Interface for Drone Management
 * Defines the contract for all drone types
 * This interface will be implemented by Member 1's drone classes
 */
public interface IDrone {
    // Getters
    String getId();
    double getCapacity();
    double getCurrentBattery();
    double getMaxBattery();
    Location getCurrentLocation();
    String getStatus();

    // Drone Operations
    void flyTo(Location destination);
    void deliver(PackageItem packageItem);
    void charge();
    void returnToBase(Location baseLocation);

    // Battery Operations
    double calculateBatteryUsage(Location from, Location to, double weight, Priority priority);
    boolean hasEnoughBattery(double requiredBattery);

    // Status checks
    boolean isAvailable();
    boolean canCarryPackage(double weight);

    // Information
    String getDroneInfo();
}
