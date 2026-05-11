// ======================================
// File: drones/StandardDrone.java
// ======================================
package drones;

import components.Location;

public class StandardDrone extends Drone {

    private static final double BATTERY_DRAIN = 10.0; // Fixed drain per flight
    private static final double MIN_BATTERY   = 15.0; // Minimum to fly

    public StandardDrone(int droneId) {
        super(droneId, 5.0); // Max capacity: 5 kg
    }

    @Override
    public void flyTo(Location destination) {
        if (!battery.hasSufficientCharge(MIN_BATTERY)) {
            System.out.println("Standard Drone " + droneId
                    + " has insufficient battery! Please charge first.");
            return;
        }

        status = "FLYING";
        System.out.println("\nStandard Drone " + droneId + " flying to " + destination);
        battery.drainBattery(BATTERY_DRAIN);
        location = destination;
        System.out.println("Battery after flight: " + battery.getLevel() + "%");
    }
}