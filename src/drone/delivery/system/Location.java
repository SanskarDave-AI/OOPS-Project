package drone.delivery.system;

/**
 * Location class - represents a destination in the delivery system
 * Uses encapsulation to protect location coordinates
 */
public class Location {
    private String name;
    private double x;
    private double y;
    
    // Default constructor
    public Location() {
        this.name = "Unknown";
        this.x = 0.0;
        this.y = 0.0;
    }
    
    // Parameterized constructor
    public Location(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    /**
     * Calculate distance to another location using Euclidean distance
     * @param other The target location
     * @return Distance in units
     */
    public double distanceTo(Location other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    @Override
    public String toString() {
        return "Location{name='" + name + "', x=" + x + ", y=" + y + "}";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location location = (Location) obj;
        return Double.compare(location.x, x) == 0 && 
               Double.compare(location.y, y) == 0 &&
               name.equals(location.name);
    }
    
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + Double.hashCode(x);
        result = 31 * result + Double.hashCode(y);
        return result;
    }
}