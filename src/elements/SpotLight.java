package elements;

import primitives.Color;
import primitives.Point3D;
import primitives.Util;
import primitives.Vector;

public class SpotLight extends PointLight {
	private Vector direction;

	public SpotLight(Color intensity, Point3D position, Vector direction, double kC, double kL, double kQ) {
		super(intensity, position, kC, kL, kQ);
		this.direction = direction.normalize();
	}

	@Override
	public Color getIntensity(Point3D p) {
		double temp = direction.dotProduct(getL(p));
		if (Util.isZero(temp)) { //if the vectors are ortogonal return black
			return Color.BLACK;
		}
		temp=Math.max(0, temp); //if temp is negative, then temp equals 0
		Color color = super.getIntensity(p).scale(temp); //scale the intensity with temp
		return color;
	}
}