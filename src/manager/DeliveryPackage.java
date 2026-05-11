

// file: manager/DeliveryPackage.java

package manager;

import components.Location;

public class DeliveryPackage {
    private int packageId;
    private double weight;
    private String priority;        // "NORMAL" or "URGENT"
    private Location destination;
    private boolean delivered;

    public DeliveryPackage(int packageId, double weight,
                           String priority, Location destination) {
        this.packageId   = packageId;
        this.weight      = weight;
        this.priority    = priority.toUpperCase();
        this.destination = destination;
        this.delivered   = false;
    }

    // Getters
    public int      getPackageId()   { return packageId; }
    public double   getWeight()      { return weight; }
    public String   getPriority()    { return priority; }
    public Location getDestination() { return destination; }
    public boolean  isDelivered()    { return delivered; }

    // Setter
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    @Override
    public String toString() {
        return "\nPackage ID  : " + packageId +
               "\nWeight      : " + weight + " kg" +
               "\nPriority    : " + priority +
               "\nDestination : " + destination +
               "\nDelivered   : " + delivered;
    }
}