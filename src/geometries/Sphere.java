package geometries;

import java.util.ArrayList;
import java.util.List;

import geometries.Intersectable.GeoPoint;
import primitives.Point3D;
import primitives.Ray;
import primitives.Vector;

public class Sphere extends Geometry {
	private Point3D center;
	private double radius;

	public Sphere(Point3D center, double radius) {
		super();
		this.center = center;
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "Sphere [center=" + center + ", radius=" + radius + "]";
	}

	@Override
	public Vector getNormal(Point3D point) {
		return point.subtract(center).normalized();
	}

	public Point3D getCenter() {
		return center;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public List<GeoPoint> findGeoIntersections(Ray ray) {

		// u = O - P0
		Vector u = this.center.subtract(ray.getP0());

		// tm = L * V
		double tm = u.dotProduct(ray.getDir());

		// d = (|L|^2 - tm^2)^0.5
		double d = Math.sqrt(u.lengthSquared() - Math.pow(tm, 2));
		if (d > radius)
			return null;

		// th = (r^2 - d^2)^0.5
		double th = Math.sqrt(Math.pow(radius, 2) - Math.pow(d, 2));

		// t1 = tm - th
		// t2 = tm + th
		double t1 = tm - th;
		double t2 = tm + th;

		if (t1 > 0 || t2 > 0) {
			List<GeoPoint> list = new ArrayList<GeoPoint>();

			// take only t1 > 0
			if (t1 > 0) {
				// P1 = P0 + t1V
				Point3D P1 = ray.getPoint(t1);
				list.add(new GeoPoint(this, P1));
			}

			// take only t2 > 0
			if (t2 > 0) {
				// P2 = P0 + t2V
				Point3D P2 = ray.getPoint(t2);
				list.add(new GeoPoint(this, P2));
			}
			
			return list;
		}

		else
			return null;
	}

	
}
