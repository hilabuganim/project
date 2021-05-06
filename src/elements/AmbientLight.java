package elements;

import primitives.Color;

public class AmbientLight extends Light {

	/*
	 * Calculate lighting by multiplying the color by a reduction factor
	 */
	public AmbientLight(Color color, double Ka) {
		super(color.scale(Ka));

	}
}
