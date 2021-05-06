package elements;

import primitives.Color;

abstract class Light {
	private Color intensity;

	public Color getIntensity() {
		return intensity;
	}

	protected Light(Color intensity) {
		super();
		this.intensity = intensity;
	}

}
