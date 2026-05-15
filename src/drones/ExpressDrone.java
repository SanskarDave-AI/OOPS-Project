
// File: drones/ExpressDrone.java

package drones;

import components.Location;

public class ExpressDrone extends Drone {

    public ExpressDrone(int droneId) {
        super(droneId, 3.0); // Max capacity: 3 kg — lighter, faster
        this.speed = 20.0;
    }

    @Override
    public void flyTo(Location destination) {
        double distance = location.distanceTo(destination);
        double batteryUsage = distance * 3;

        if (!battery.hasSufficientCharge(batteryUsage)) {
            System.out.printf("Express Drone %d has insufficient battery for %.2f meters. " +
                    "Required: %.2f%%, Available: %.2f%%%n",
                    droneId, distance, batteryUsage, battery.getLevel());
            return;
        }

        status = "FLYING FAST";
        System.out.printf("\nExpress Drone %d flying quickly to %s at %.2f m/s%n",
                droneId, destination, speed);
        battery.drainBattery(batteryUsage);
        location = destination;
        System.out.printf("Battery after fast flight: %.2f%%%n", battery.getLevel());
    }
}