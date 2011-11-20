package cg2.lights;

import cg2.vecmath.Color;

public class AmbientLight extends Light {

	public AmbientLight(float intensity, Color color, boolean enabled) {
		super(intensity, color, enabled);
	}

}
