
// File: drones/Drone.java

package drones;

import components.Battery;
import components.Location;

public abstract class Drone {
    protected int droneId;
    protected double capacity;
    protected Battery battery;
    protected Location location;
    protected String status;

    public Drone(int droneId, double capacity) {
        this.droneId   = droneId;
        this.capacity  = capacity;
        this.battery   = new Battery(100);
        this.location  = new Location(0, 0);
        this.status    = "AVAILABLE";/*"AVAILABLE" ,  "DELIVERING" ,CHARGING"   */
    }

    // Abstract — each drone type implements its own fly behavior
    public abstract void flyTo(Location destination);

    // Deliver package at current location
    public void deliverPackage() {
        status = "DELIVERING";
        System.out.println("Drone " + droneId + " is delivering the package at " + location);
    }

    // Return drone back to base (0,0)
    public void returnToBase() {
        System.out.println("Drone " + droneId + " returning to base...");
        flyTo(new Location(0, 0));
       
        System.out.println("Drone " + droneId + " is back at base  and AVAILABLE after 5 min of charging.");
         status = "AVAILABLE";
    }

    // Charge drone battery
    public void charge() {
        status = "CHARGING";
        battery.chargeBattery();
        status = "AVAILABLE";
    }

    // Print current drone status
    public void showStatus() {
        System.out.println("\n----- Drone Status -----");
        System.out.println("Drone ID  : " + droneId);
        System.out.println("Type      : " + this.getClass().getSimpleName());/*getClass() asks the object: "What class were you actually created from?"

The object answers: */
        System.out.println("Capacity  : " + capacity + " kg");
        System.out.println("Battery   : " + battery.getLevel() + "%");
        System.out.println("Location  : " + location);
        System.out.println("Status    : " + status);
    }

    

    // Getters
    public int      getDroneId()    { return droneId; }
    public double   getCapacity()   { return capacity; }
    public Battery  getBattery()    { return battery; }
    public Location getLocation()   { return location; }
    public String   getStatus()     { return status; }

    // Setters
    public void setStatus(String status)       { this.status = status; }
    public void setLocation(Location location) { this.location = location; }
}