package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Vector;

public class PointLight extends Light implements LightSource {
	private Point3D position;
	private double kC, kL, kQ;
	public PointLight(Color intensity, Point3D position, double kC, double kL, double kQ) {
		super(intensity);
		this.position = position;
		this.kC = kC;
		this.kL = kL;
		this.kQ = kQ;
	}

	/**
	 * return the color at some point 
	 */
	@Override
	public Color getIntensity(Point3D p) {
		double d = position.distance(p); 
		return getIntensity().scale(1 / (kC + kL * d + kQ * (d * d)));
	}

	@Override
	public Vector getL(Point3D p) {
		return p.subtract(position).normalize();
	}
}
