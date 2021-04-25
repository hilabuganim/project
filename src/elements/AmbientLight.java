package elements;

import primitives.Color;

public class AmbientLight {
	private Color intensity;

	/*
	 * Calculate lighting by multiplying the color by a reduction factor
	 */
	public AmbientLight(Color color, double Ka) {
		intensity = color.scale(Ka);

	}

	public Color getIntensity() {
		return intensity;
	}

}
