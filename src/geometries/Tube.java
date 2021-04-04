package geometries;

import java.util.List;

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
		double t = axicRay.getDir().dotProduct(point.subtract(axicRay.getP0()));
		Point3D O = axicRay.getP0().add(axicRay.getDir().scale(t));
		return point.subtract(O).normalize();
	}

	@Override
	public List<Point3D> findIntsersections(Ray ray) {
		// TODO Auto-generated method stub
		return null;
	}

}
