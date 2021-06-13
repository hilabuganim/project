package primitives;

public class Point3D {
    /**
     * Defining the zero-beginning points of the axes
     */
    public static Point3D ZERO = new Point3D(0, 0, 0);
    public final Coordinate x;
    public final Coordinate y;
    public final Coordinate z;

    /**
     * constructor of Coordinate
     *
     * @param x
     * @param y
     * @param z
     */
    public Point3D(Coordinate x, Coordinate y, Coordinate z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * constructor of double
     *
     * @param x
     * @param y
     * @param z
     */
    public Point3D(double x, double y, double z) {
        this.x = new Coordinate(x);
        this.y = new Coordinate(y);
        this.z = new Coordinate(z);
    }

    /**
     * @param vector
     * @return Connection of two vectors
     */
    public Point3D add(Vector vector) {
        return new Point3D(this.x.coord + vector.head.x.coord, this.y.coord + vector.head.y.coord, this.z.coord + vector.head.z.coord);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Point3D other = (Point3D) obj;
        if (x == null) {
            if (other.x != null)
                return false;
        } else if (!x.equals(other.x))
            return false;
        if (y == null) {
            if (other.y != null)
                return false;
        } else if (!y.equals(other.y))
            return false;
        if (z == null) {
            if (other.z != null)
                return false;
        } else if (!z.equals(other.z))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    /**
     * @param point
     * @return A function that gets a point and calculates the subtraction between two vectors
     */
    public Vector subtract(Point3D point) {
        return new Vector(this.x.coord - point.x.coord, this.y.coord - point.y.coord, this.z.coord - point.z.coord);
    }

    /**
     * A function that gets a point and calculates the distance squared between them
     *
     * @param point
     * @return distance Squared
     */
    public double distanceSquarde(Point3D point) {
        double a = (this.x.coord - point.x.coord) * (this.x.coord - point.x.coord);
        double b = (this.y.coord - point.y.coord) * (this.y.coord - point.y.coord);
        double c = (this.z.coord - point.z.coord) * (this.z.coord - point.z.coord);
        return a + b + c;
    }

    /**
     * A function that calculates the distance with the function sqrt
     * on the previous function with the given point
     *
     * @param point
     * @return distance
     */
    public double distance(Point3D point) {
        return Math.sqrt(distanceSquarde(point));
    }

    public double getX() {
        return this.x.coord;
    }

    public double getY() {
        return this.y.coord;
    }

    public double getZ() {
        return this.z.coord;
    }
}
