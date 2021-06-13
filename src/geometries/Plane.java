package geometries;

import primitives.Point3D;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

public class Plane extends Geometry {
    private Point3D q0;
    private Vector normal;

    public Plane(Point3D q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal;
        this.box = new Box(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * constructor with point and normal
     * t
     *
     * @param point1
     * @param point2
     * @param point3
     */
    public Plane(Point3D point1, Point3D point2, Point3D point3) {
        this.q0 = point1;
        Vector v1 = point1.subtract(point2);
        Vector v2 = point1.subtract(point3);
        this.normal = v1.crossProduct(v2).normalize();
        this.box = new Box(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    public Point3D getQ0() {
        return q0;
    }

    public Vector getNormal() {
        return normal;
    }

    @Override
    public String toString() {
        return "Plane [q0=" + q0 + ", normal=" + normal + "]";
    }

    @Override
    public Vector getNormal(Point3D point) {
        return normal;
    }

    @Override
    public Point3D getCenter() {
        return q0;
    }

    @Override
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        if (!box.intersectionBox(ray))
            return null;

        // -N
        Vector revN = normal.scale(-1);

        // (P0 - Q0)
        Vector P0Q0 = ray.getP0().subtract(this.q0);
        try {
        } catch (Exception ex) {
            return null;
        }

        // (N*V)

        double NV = normal.dotProduct(ray.getDir());
        if (Util.isZero(NV)) {
            return null;
        }

        // (P0 - Q0)/(N*V)
        Vector P0Q0_NV = P0Q0.scale(1 / NV);

        // t = - N*(P0 - Q0)/(N*V)
        double t = revN.dotProduct(P0Q0_NV);

        if (t > 0)
            // P = P0 + tV
            return List.of(new GeoPoint(this, ray.getPoint(t)));

        return null;
    }


}
