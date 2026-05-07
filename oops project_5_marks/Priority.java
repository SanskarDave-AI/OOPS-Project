/**
 * Priority Enum for Package Delivery
 * Defines priority levels for package delivery
 * Higher priority = Faster delivery
 */
public enum Priority {
    LOW(1, 0.8),          // Low priority, 20% battery discount
    MEDIUM(2, 1.0),       // Normal priority, standard battery usage
    HIGH(3, 1.2),         // High priority, 20% extra battery usage
    URGENT(4, 1.5);       // Urgent delivery, 50% extra battery usage

    private final int level;
    private final double batteryMultiplier;

    Priority(int level, double batteryMultiplier) {
        this.level = level;
        this.batteryMultiplier = batteryMultiplier;
    }

    public int getLevel() {
        return level;
    }

    public double getBatteryMultiplier() {
        return batteryMultiplier;
    }

    @Override
    public String toString() {
        return name() + " (Level: " + level + ")";
    }
}
