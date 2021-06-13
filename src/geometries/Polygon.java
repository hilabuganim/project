package geometries;


import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */
public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected List<Point3D> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected Plane plane;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point3D... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);

        // build the box
        double x1 = Double.NEGATIVE_INFINITY;
        double x0 = Double.POSITIVE_INFINITY;
        double y1 = Double.NEGATIVE_INFINITY;
        double y0 = Double.POSITIVE_INFINITY;
        double z1 = Double.NEGATIVE_INFINITY;
        double z0 = Double.POSITIVE_INFINITY;
        for (Point3D v : vertices) {
            if (v.getX() < x0) x0 = v.getX();
            if (v.getX() > x1) x1 = v.getX();
            if (v.getY() < y0) y0 = v.getY();
            if (v.getY() > y1) y1 = v.getY();
            if (v.getZ() < z0) z0 = v.getZ();
            if (v.getZ() > z1) z1 = v.getZ();
        }
        this.box = new Box(x0, x1, y0, y1, z0, z1);

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (vertices.length == 3)
            return; // no need for more tests for a Triangle

        Vector n = plane.getNormal();

        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[vertices.length - 1].subtract(vertices[vertices.length - 2]);
        Vector edge2 = vertices[0].subtract(vertices[vertices.length - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180 deg. It is hold by the sign of its dot product
        // with
        // the normal. If all the rest consequent edges will generate the same sign -
        // the
        // polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (int i = 1; i < vertices.length; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point3D point) {
        return plane.getNormal();
    }

    @Override
    public Point3D getCenter() {
        double x =0, y =0, z = 0;
        int len = vertices.size();
        for (Point3D point: vertices) {
            x += point.getX();
            y += point.getY();
            z += point.getZ();
        }
        return new Point3D(x / len, y / len, z / len);
    }

    /**
     * Create a list of points that keeps the points of intersection with the ray
     * Create a new plane by three points
     * Insert in the list the number of points of intersection of the plane with the ray
     */
    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        if (!box.intersectionBox(ray))
            return null;

        Plane plane = new Plane(vertices.get(0), vertices.get(1), vertices.get(2));
        List<GeoPoint> result = plane.findGeoIntersections(ray);
        /**
         * When there are no points of intersection
         */
        if (result == null)
            return null;
        /**
         * Builds a list of vectors
         * and for each vector makes an end point less a start
         */
        List<Vector> v = new LinkedList<>();
        for (int i = 0; i < vertices.size(); i++) {
            v.add(vertices.get(i).subtract(ray.getP0()));
        }
        /**
         * Define two variables that preserve whether the vector is positive or negative
         * Goes through all the vectors and makes a vector product between them and normalizes
         * do a scalar product with the direction
         */
        int positive = 0, negative = 0;
        for (int i = 0; i < vertices.size(); i++) {
            Vector N = v.get(i).crossProduct(v.get((i + 1) % vertices.size())).normalize();
            double scalar = N.dotProduct(ray.getDir());
            /**
             * Summarized the results of the vectors whether positive negative or zero
             */
            if (Util.isZero(scalar))
                return null;

            if (scalar > 0)
                positive++;
            else
                negative++;
        }
        /**
         * If both positive and negative are not equal to zero
         * Otherwise returns the number of points of intersection
         */
        if (positive != 0 && negative != 0)
            return null;
        else {
            for (GeoPoint point : result)
                point.geometry = this;
            return result;
        }
    }

}
