package drone.delivery.system;

/**
 * HeavyLiftDrone - High-capacity drone for large/heavy packages
 * High capacity, lower speed, efficient battery usage for heavy loads
 */
public class HeavyLiftDrone extends Drone {
    private static final String MODEL = "Heavy Lift Delivery Drone";
    private static final double DEFAULT_LOAD_CAPACITY = 30.0;  // kg
    private static final double DEFAULT_SPEED = 40.0;          // units/hour
    private static final double DEFAULT_BATTERY = 100.0;        // %
    private static final double HEAVY_DRAIN_RATE = 0.8;        // More efficient for heavy loads
    
    // Default constructor
    public HeavyLiftDrone() {
        super("DRONE-H-001", MODEL, new Location("Warehouse", 0, 0),
              DEFAULT_LOAD_CAPACITY, DEFAULT_SPEED, DEFAULT_BATTERY);
        getBattery().setDrainRate(HEAVY_DRAIN_RATE);
    }
    
    // Parameterized constructor
    public HeavyLiftDrone(String droneId, Location location) {
        super(droneId, MODEL, location, DEFAULT_LOAD_CAPACITY, 
              DEFAULT_SPEED, DEFAULT_BATTERY);
        getBattery().setDrainRate(HEAVY_DRAIN_RATE);
    }
    
    // Custom constructor with all parameters
    public HeavyLiftDrone(String droneId, Location location, 
                         double maxLoadCapacity, double speed) {
        super(droneId, MODEL, location, maxLoadCapacity, speed, DEFAULT_BATTERY);
        getBattery().setDrainRate(HEAVY_DRAIN_RATE);
    }
    
    /**
     * Get the drone type
     * @return Type string
     */
    public String getDroneType() {
        return "HeavyLift";
    }
    
    /**
     * Get delivery cost multiplier
     * @return Cost multiplier (2.0 = double base price)
     */
    public double getCostMultiplier() {
        return 2.0;
    }
    
    @Override
    public String toString() {
        return "HeavyLiftDrone{" + 
               "id='" + getDroneId() + "'" +
               ", location=" + getCurrentLocation() +
               ", battery=" + getBattery() +
               ", status=" + getStatus() +
               ", capacity=" + getMaxLoadCapacity() + "kg" +
               ", speed=" + getSpeed() + " units/hr}";
    }
}