package cg2.lights;

import cg2.vecmath.Color;
import cg2.vecmath.Vector;

public class LightSource extends Light{
	
	private final Vector position;

	public LightSource(float intensity, Color color,Vector position, boolean enabled) {
		super(intensity, color, enabled);
		this.position = position;
	}
	
	public Vector getPosition(){
		return position;
	}
}
