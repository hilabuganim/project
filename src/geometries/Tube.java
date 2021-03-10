package geometries;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;
public class Tube implements Geometry {
private Ray axicRay;
private double radius;
public Ray getAxicRay() {
	return axicRay;
}
public double getRadius() {
	return radius;
}
@Override
public String toString() {
	return "Tube [axicRay=" + axicRay + ", radius=" + radius + "]";
}
public Tube(Ray axicRay, double radius) {
	super();
	this.axicRay = axicRay;
	this.radius = radius;
}
@Override
public Vector getNormal(Point3D point) {
	return null;
}

}
