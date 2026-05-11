
// File: drones/HeavyLiftDrone.java

package drones;

import components.Location;

public class HeavyLiftDrone extends Drone {

    private static final double BATTERY_DRAIN = 25.0; // Heaviest load = most drain
    private static final double MIN_BATTERY   = 30.0;

    public HeavyLiftDrone(int droneId) {
        super(droneId, 20.0); // Max capacity: 20 kg
    }

    @Override
    public void flyTo(Location destination) {
        if (!battery.hasSufficientCharge(MIN_BATTERY)) {
            System.out.println("HeavyLift Drone " + droneId
                    + " has insufficient battery! Please charge first.");
            return;
        }

        status = "HEAVY DELIVERY";
        System.out.println("\nHeavyLift Drone " + droneId
                + " moving toward " + destination);
        battery.drainBattery(BATTERY_DRAIN);
        location = destination;
        System.out.println("Battery after heavy flight: " + battery.getLevel() + "%");
    }
}
