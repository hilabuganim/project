package elements;

import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.isZero;


public class Camera {
    private Point3D p;
    private Vector Vup, Vto, Vright;
    private double width;
    private double height;
    private double distance;

    /**
     * constructor
     *
     * @param p
     * @param vup
     * @param vto
     */
    public Camera(Point3D p, Vector vto, Vector vup) {
        super();
        this.p = p;
        Vup = vup.normalize();
        Vto = vto.normalize();
        if (Vup.dotProduct(Vto) != 0)
            throw new IllegalArgumentException("Vup and Vto are not ortogonal");
        Vright = Vto.crossProduct(Vup).normalize();

    }

    /**
     * set
     *
     * @param width
     * @param height
     * @return this
     */
    public Camera setViewPlaneSize(double width, double height) {
        this.height = height;
        this.width = width;
        return this;
    }

    public Camera setDistance(double distance) {
        this.distance = distance;
        return this;
    }

    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        Point3D Pc = p.add(Vto.scale(distance));
        double Ry = height / nY;
        double Rx = width / nX;
        double Xj = (j - ((nX - 1) / 2.0)) * Rx;
        double Yi = -(i - ((nY - 1) / 2.0)) * Ry;

        Point3D Pij = Pc;
        if (!isZero(Xj)) Pij = Pij.add(Vright.scale(Xj));
        if (!isZero(Yi)) Pij = Pij.add(Vup.scale(Yi));

        Vector Vij = Pij.subtract(p).normalize();
        return new Ray(p, Vij);
    }


}
