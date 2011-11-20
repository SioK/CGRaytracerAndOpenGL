package cg2.lights;

import cg2.vecmath.Color;

public abstract class Light { 
	
	private float intensity;
	private boolean enabled;
	private Color color;
	

	/**
	 * 
	 * @param intensity
	 * @param color
	 * @param enabled
	 */
	public Light(float intensity, Color color, boolean enabled) {
		this.intensity = intensity;
		this.color = color;
		this.enabled = enabled;
		clipIntensity();
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean lightEnabled) {
		this.enabled = lightEnabled;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getLightQuantity() {
		return color.modulate(intensity);
	}
	
	public float clipIntensity() {
		  float i = Math.min(intensity, 1);
		  return Math.max(i, 0);
	 }
}
