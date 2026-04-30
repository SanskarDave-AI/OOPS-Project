package drone.delivery.system;

/**
 * ExpressDrone - Fast delivery drone for urgent packages
 * Lower capacity, higher speed, higher battery drain rate
 */
public class ExpressDrone extends Drone {
    private static final String MODEL = "Express Delivery Drone";
    private static final double DEFAULT_LOAD_CAPACITY = 5.0;   // kg
    private static final double DEFAULT_SPEED = 80.0;          // units/hour
    private static final double DEFAULT_BATTERY = 100.0;        // %
    private static final double EXPRESS_DRAIN_RATE = 1.5;      // Higher drain rate
    
    // Default constructor
    public ExpressDrone() {
        super("DRONE-E-001", MODEL, new Location("Warehouse", 0, 0),
              DEFAULT_LOAD_CAPACITY, DEFAULT_SPEED, DEFAULT_BATTERY);
        getBattery().setDrainRate(EXPRESS_DRAIN_RATE);
    }
    
    // Parameterized constructor
    public ExpressDrone(String droneId, Location location) {
        super(droneId, MODEL, location, DEFAULT_LOAD_CAPACITY, 
              DEFAULT_SPEED, DEFAULT_BATTERY);
        getBattery().setDrainRate(EXPRESS_DRAIN_RATE);
    }
    
    // Custom constructor with all parameters
    public ExpressDrone(String droneId, Location location, 
                       double maxLoadCapacity, double speed) {
        super(droneId, MODEL, location, maxLoadCapacity, speed, DEFAULT_BATTERY);
        getBattery().setDrainRate(EXPRESS_DRAIN_RATE);
    }
    
    /**
     * Get the drone type
     * @return Type string
     */
    public String getDroneType() {
        return "Express";
    }
    
    /**
     * Get delivery cost multiplier
     * @return Cost multiplier (1.5 = 50% premium over base)
     */
    public double getCostMultiplier() {
        return 1.5;
    }
    
    @Override
    public String toString() {
        return "ExpressDrone{" + 
               "id='" + getDroneId() + "'" +
               ", location=" + getCurrentLocation() +
               ", battery=" + getBattery() +
               ", status=" + getStatus() +
               ", capacity=" + getMaxLoadCapacity() + "kg" +
               ", speed=" + getSpeed() + " units/hr}";
    }
}