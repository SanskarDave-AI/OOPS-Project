package drone.delivery.system;

/**
 * StandardDrone - Basic delivery drone for everyday packages
 * Medium capacity, medium speed, economical battery usage
 */
public class StandardDrone extends Drone {
    private static final String MODEL = "Standard Delivery Drone";
    private static final double DEFAULT_LOAD_CAPACITY = 10.0;  // kg
    private static final double DEFAULT_SPEED = 50.0;          // units/hour
    private static final double DEFAULT_BATTERY = 100.0;        // %
    
    // Default constructor
    public StandardDrone() {
        super("DRONE-S-001", MODEL, new Location("Warehouse", 0, 0),
              DEFAULT_LOAD_CAPACITY, DEFAULT_SPEED, DEFAULT_BATTERY);
    }
    
    // Parameterized constructor
    public StandardDrone(String droneId, Location location) {
        super(droneId, MODEL, location, DEFAULT_LOAD_CAPACITY, 
              DEFAULT_SPEED, DEFAULT_BATTERY);
    }
    
    // Custom constructor with all parameters
    public StandardDrone(String droneId, Location location, 
                        double maxLoadCapacity, double speed) {
        super(droneId, MODEL, location, maxLoadCapacity, speed, DEFAULT_BATTERY);
    }
    
    /**
     * Get the drone type
     * @return Type string
     */
    public String getDroneType() {
        return "Standard";
    }
    
    /**
     * Get delivery cost multiplier
     * @return Cost multiplier (1.0 = base price)
     */
    public double getCostMultiplier() {
        return 1.0;
    }
    
    @Override
    public String toString() {
        return "StandardDrone{" + 
               "id='" + getDroneId() + "'" +
               ", location=" + getCurrentLocation() +
               ", battery=" + getBattery() +
               ", status=" + getStatus() +
               ", capacity=" + getMaxLoadCapacity() + "kg" +
               ", speed=" + getSpeed() + " units/hr}";
    }
}