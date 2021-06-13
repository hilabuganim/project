package geometries;

import primitives.Ray;

public class Box {
    protected final double x0;
    protected final double x1;
    protected final double y0;
    protected final double y1;
    protected final double z0;
    protected final double z1;

    /**
     * @param x0
     * @param x1
     * @param y0
     * @param y1
     * @param z0
     * @param z1
     */
    public Box(double x0, double x1, double y0, double y1, double z0, double z1) {
        this.x0 = x0;
        this.x1 = x1;
        this.y0 = y0;
        this.y1 = y1;
        this.z0 = z0;
        this.z1 = z1;
    }

    /**
     * @return the x0
     */
    public double getX0() {
        return x0;
    }

    /**
     * @return the x1
     */
    public double getX1() {
        return x1;
    }

    /**
     * @return the y0
     */
    public double getY0() {
        return y0;
    }

    /**
     * @return the y1
     */
    public double getY1() {
        return y1;
    }

    /**
     * @return the z0
     */
    public double getZ0() {
        return z0;
    }

    /**
     * @return the z1
     */
    public double getZ1() {
        return z1;
    }

    /**
     * Returns true if the ray intersects the box
     *
     * @param ray
     * @return True/False
     */
    public boolean intersectionBox(Ray ray) {
        double txmin = (x0 - ray.getP0().getX()) / ray.getDir().getHead().getX();
        double txmax = (x1 - ray.getP0().getX()) / ray.getDir().getHead().getX();
        if (txmin > txmax) {
            double temp = txmin;
            txmin = txmax;
            txmax = temp;
        }
        double tymin = (y0 - ray.getP0().getY()) / ray.getDir().getHead().getY();
        double tymax = (y1 - ray.getP0().getY()) / ray.getDir().getHead().getY();
        if (tymin > tymax) {
            double temp = tymin;
            tymin = tymax;
            tymax = temp;
        }
        if ((txmin > tymax) || (tymin > txmax))
            return false;
        if (tymin > txmin)
            txmin = tymin;
        if (tymax < txmax)
            txmax = tymax;
        double tzmin = (z0 - ray.getP0().getZ()) / ray.getDir().getHead().getZ();
        double tzmax = (z1 - ray.getP0().getZ()) / ray.getDir().getHead().getZ();
        if (tzmin > tzmax) {
            double temp = tzmin;
            tzmin = tzmax;
            tzmax = temp;
        }
        return (!(txmin > tzmax)) && (!(tzmin > txmax));
    }
}