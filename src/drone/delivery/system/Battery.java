package drone.delivery.system;

/**
 * Battery class - manages drone battery with encapsulation
 * Tracks charge level and provides charging functionality
 */
public class Battery {
    private static final double MAX_CAPACITY = 100.0;
    private static final double CRITICAL_LEVEL = 20.0;
    private static final double LOW_BATTERY_THRESHOLD = 30.0;
    
    private double currentCharge;    // Current battery percentage
    private double drainRate;         // Battery drain per unit distance
    
    // Default constructor
    public Battery() {
        this.currentCharge = MAX_CAPACITY;
        this.drainRate = 1.0; // 1% per unit distance
    }
    
    // Parameterized constructor
    public Battery(double initialCharge, double drainRate) {
        this.currentCharge = Math.min(initialCharge, MAX_CAPACITY);
        this.drainRate = drainRate;
    }
    
    // Getters
    public double getCurrentCharge() {
        return currentCharge;
    }
    
    public double getDrainRate() {
        return drainRate;
    }
    
    public static double getMaxCapacity() {
        return MAX_CAPACITY;
    }
    
    public static double getCriticalLevel() {
        return CRITICAL_LEVEL;
    }
    
    public static double getLowBatteryThreshold() {
        return LOW_BATTERY_THRESHOLD;
    }
    
    // Setters
    public void setCurrentCharge(double currentCharge) {
        this.currentCharge = Math.max(0, Math.min(currentCharge, MAX_CAPACITY));
    }
    
    public void setDrainRate(double drainRate) {
        this.drainRate = Math.max(0, drainRate);
    }
    
    /**
     * Check if battery is sufficient for a given distance
     * @param distance Distance to travel
     * @return true if battery is sufficient
     */
    public boolean hasSufficientCharge(double distance) {
        double required = distance * drainRate;
        return currentCharge >= required;
    }
    
    /**
     * Drain battery for a given distance
     * @param distance Distance traveled
     */
    public void drain(double distance) {
        double drained = distance * drainRate;
        this.currentCharge = Math.max(0, currentCharge - drained);
    }
    
    /**
     * Charge the battery to full capacity
     */
    public void charge() {
        this.currentCharge = MAX_CAPACITY;
    }
    
    /**
     * Charge the battery by a specific amount
     * @param amount Amount to charge (0-100)
     */
    public void charge(double amount) {
        this.currentCharge = Math.min(MAX_CAPACITY, currentCharge + amount);
    }
    
    /**
     * Check if battery is critically low
     * @return true if battery is below critical level
     */
    public boolean isCritical() {
        return currentCharge < CRITICAL_LEVEL;
    }
    
    /**
     * Check if battery is low
     * @return true if battery is below low threshold
     */
    public boolean isLow() {
        return currentCharge < LOW_BATTERY_THRESHOLD;
    }
    
    /**
     * Get the remaining charge after traveling a distance
     * @param distance Distance to travel
     * @return Remaining charge percentage
     */
    public double getRemainingChargeAfter(double distance) {
        double required = distance * drainRate;
        return Math.max(0, currentCharge - required);
    }
    
    @Override
    public String toString() {
        return String.format("Battery{currentCharge=%.1f%%, drainRate=%.2f}", 
                           currentCharge, drainRate);
    }
}