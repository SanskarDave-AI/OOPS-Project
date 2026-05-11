
// File: drones/ExpressDrone.java

package drones;

import components.Location;

public class ExpressDrone extends Drone {

    private static final double BATTERY_DRAIN = 20.0; // Faster = more drain
    private static final double MIN_BATTERY   = 25.0;

    public ExpressDrone(int droneId) {
        super(droneId, 3.0); // Max capacity: 3 kg — lighter, faster
    }

    @Override
    public void flyTo(Location destination) {
        if (!battery.hasSufficientCharge(MIN_BATTERY)) {
            System.out.println("Express Drone " + droneId
                    + " has insufficient battery! Please charge first.");
            return;
        }

        status = "FLYING FAST";
        System.out.println("\nExpress Drone " + droneId
                + " flying quickly to " + destination);
        battery.drainBattery(BATTERY_DRAIN);
        location = destination;
        System.out.println("Battery after fast flight: " + battery.getLevel() + "%");
    }
}