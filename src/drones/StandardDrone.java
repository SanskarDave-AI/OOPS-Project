// ======================================
// File: drones/StandardDrone.java
// ======================================
package drones;

import components.Location;

public class StandardDrone extends Drone {

    public StandardDrone(int droneId) {
        super(droneId, 5.0); // Max capacity: 5 kg
        this.speed = 10.0;
    }

    @Override
    public void flyTo(Location destination) {
        double distance = location.distanceTo(destination);
        double batteryUsage = distance * 2;

        if (!battery.hasSufficientCharge(batteryUsage)) {
            System.out.printf("Standard Drone %d has insufficient battery for %.2f meters. " +
                    "Required: %.2f%%, Available: %.2f%%%n",
                    droneId, distance, batteryUsage, battery.getLevel());
            return;
        }

        status = "FLYING";
        System.out.printf("\nStandard Drone %d flying to %s at %.2f m/s%n",
                droneId, destination, speed);
        battery.drainBattery(batteryUsage);
        location = destination;
        System.out.printf("Battery after flight: %.2f%%%n", battery.getLevel());
    }
}