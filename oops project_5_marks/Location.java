/**
 * Location Class for Coordinate Management
 * Stores and calculates distances between locations
 */
public class Location {
    private double latitude;
    private double longitude;
    private String address;

    // Constructor
    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    // Alternative constructor with just coordinates
    public Location(double latitude, double longitude) {
        this(latitude, longitude, "Unknown Location");
    }

    // Getters
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Calculate distance using Haversine Formula
     * More accurate for real-world coordinates (in km)
     */
    public double calculateDistance(Location other) {
        if (other == null) {
            throw new IllegalArgumentException("Other location cannot be null");
        }

        final int EARTH_RADIUS_KM = 6371;
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon2 = Math.toRadians(other.longitude);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dlon / 2) * Math.sin(dlon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS_KM * c;

        return Math.round(distance * 100.0) / 100.0; // Round to 2 decimal places
    }

    /**
     * Simpler distance calculation using Euclidean distance
     * Useful for simulation in a flat plane
     */
    public double calculateSimpleDistance(Location other) {
        if (other == null) {
            throw new IllegalArgumentException("Other location cannot be null");
        }

        double dx = this.latitude - other.latitude;
        double dy = this.longitude - other.longitude;
        double distance = Math.sqrt(dx * dx + dy * dy);

        return Math.round(distance * 100.0) / 100.0;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Location location = (Location) obj;
        return Double.compare(location.latitude, latitude) == 0 &&
               Double.compare(location.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(latitude, longitude);
    }
}
