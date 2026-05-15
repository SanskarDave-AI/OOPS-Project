
// File: drones/HeavyLiftDrone.java

package drones;

import components.Location;

public class HeavyLiftDrone extends Drone {

    public HeavyLiftDrone(int droneId) {
        super(droneId, 20.0); // Max capacity: 20 kg
        this.speed = 6.0;
    }

    @Override
    public void flyTo(Location destination) {
        double distance = location.distanceTo(destination);
        double batteryUsage = distance * 4;

        if (!battery.hasSufficientCharge(batteryUsage)) {
            System.out.printf("HeavyLift Drone %d has insufficient battery for %.2f meters. " +
                    "Required: %.2f%%, Available: %.2f%%%n",
                    droneId, distance, batteryUsage, battery.getLevel());
            return;
        }

        status = "HEAVY DELIVERY";
        System.out.printf("\nHeavyLift Drone %d moving toward %s at %.2f m/s%n",
                droneId, destination, speed);
        battery.drainBattery(batteryUsage);
        location = destination;
        System.out.printf("Battery after heavy flight: %.2f%%%n", battery.getLevel());
    }
}
