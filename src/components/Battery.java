
// File: components/Battery.java

package components;

public class Battery {
    private double level;

    public Battery(double level) {
        this.level = level;
    }

    // Getters
    public double getLevel() { return level; }

    // Setters
    public void setLevel(double level) { this.level = level; }

    // Drain battery, floor at 0
    public void drainBattery(double amount) {
        level -= amount;
        if (level < 0) level = 0;
    }

    // Check if enough charge available  ,,
    public boolean hasSufficientCharge(double required) {
        return level >= required;
    }

    // full charge
    public void chargeBattery() {
      //  System.out.println("wait for 5 min");
        level = 100;
        System.out.println("Battery fully charged to 100%.");
    }
}
