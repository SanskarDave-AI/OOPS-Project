package drone.delivery.system;

/**
 * Base Drone class - Abstract foundation for all drone types
 * Implements core drone functionality with encapsulation
 */
public abstract class Drone {
    private String droneId;
    private String modelName;
    private Location currentLocation;
    private Battery battery;
    private DroneStatus status;
    private double maxLoadCapacity;    // in kg
    private double speed;              // units per hour
    
    // Default constructor
    public Drone() {
        this.droneId = "DRONE-000";
        this.modelName = "Generic Drone";
        this.currentLocation = new Location("Base", 0, 0);
        this.battery = new Battery();
        this.status = DroneStatus.AVAILABLE;
        this.maxLoadCapacity = 5.0;
        this.speed = 60.0;
    }
    
    // Parameterized constructor
    public Drone(String droneId, String modelName, Location location, 
                 double maxLoadCapacity, double speed, double batteryCharge) {
        this.droneId = droneId;
        this.modelName = modelName;
        this.currentLocation = location;
        this.battery = new Battery(batteryCharge, 1.0);
        this.status = DroneStatus.AVAILABLE;
        this.maxLoadCapacity = maxLoadCapacity;
        this.speed = speed;
    }
    
    // Getters
    public String getDroneId() {
        return droneId;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }
    
    public Battery getBattery() {
        return battery;
    }
    
    public DroneStatus getStatus() {
        return status;
    }
    
    public double getMaxLoadCapacity() {
        return maxLoadCapacity;
    }
    
    public double getSpeed() {
        return speed;
    }
    
    // Setters
    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
    
    public void setStatus(DroneStatus status) {
        this.status = status;
    }
    
    public void setMaxLoadCapacity(double maxLoadCapacity) {
        this.maxLoadCapacity = maxLoadCapacity;
    }
    
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    
    /**
     * Fly to a destination location
     * @param destination Target location
     * @return true if flight was successful
     */
    public boolean flyTo(Location destination) {
        double distance = currentLocation.distanceTo(destination);
        
        // Check if battery is sufficient
        if (!battery.hasSufficientCharge(distance)) {
            return false;
        }
        
        // Drain battery during flight
        battery.drain(distance);
        
        // Update location
        this.currentLocation = destination;
        return true;
    }
    
    /**
     * Charge the drone battery
     */
    public void charge() {
        battery.charge();
    }
    
    /**
     * Charge the drone by a specific amount
     * @param amount Amount to charge
     */
    public void charge(double amount) {
        battery.charge(amount);
    }
    
    /**
     * Check if drone can deliver to a destination
     * @param destination Target location
     * @return true if delivery is possible
     */
    public boolean canReach(Location destination) {
        double distance = currentLocation.distanceTo(destination);
        return battery.hasSufficientCharge(distance);
    }
    
    /**
     * Calculate estimated time to reach a destination
     * @param destination Target location
     * @return Time in hours
     */
    public double getEstimatedTime(Location destination) {
        double distance = currentLocation.distanceTo(destination);
        return distance / speed;
    }
    
    /**
     * Check if drone is available for delivery
     * @return true if drone is available and has sufficient battery
     */
    public boolean isAvailableForDelivery() {
        return status == DroneStatus.AVAILABLE && !battery.isCritical();
    }
    
    /**
     * Check if drone can carry a given load weight
     * @param weight Weight to check
     * @return true if drone can carry the weight
     */
    public boolean canCarry(double weight) {
        return weight <= maxLoadCapacity;
    }
    
    @Override
    public String toString() {
        return String.format("Drone{id='%s', model='%s', location=%s, battery=%s, status=%s, load=%.1fkg, speed=%.1f}", 
                           droneId, modelName, currentLocation, battery, status, maxLoadCapacity, speed);
    }
}