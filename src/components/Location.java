
// File: components/Location.java

package components;

public class Location {
    private double x;
    private double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }

    // Setters
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }

    /**
     * Calculate Euclidean distance to another location
     * Formula: sqrt((x2-x1)^2 + (y2-y1)^2)
     */
    public double distanceTo(Location other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        return Double.compare(this.x, other.x) == 0 && 
               Double.compare(this.y, other.y) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y);
    }
}