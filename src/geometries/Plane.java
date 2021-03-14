package geometries;

import primitives.Point3D;
import primitives.Vector;

public class Plane implements Geometry {
	private Point3D q0;
	private Vector normal;

	public Plane(Point3D q0, Vector normal) {
		this.q0 = q0;
		this.normal = normal;
	}

	public Plane(Point3D point1, Point3D point2, Point3D point3) {
		this.q0 = point1;
		this.normal = new Vector(point1).crossProduct(new Vector(point2));
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

}
