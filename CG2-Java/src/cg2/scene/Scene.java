package cg2.scene;

import java.util.ArrayList;
import java.util.List;


import cg2.lights.AmbientLight;
import cg2.lights.LightSource;
import cg2.models.Shape;
import cg2.raytracer.Hit;
import cg2.raytracer.Ray;


public class Scene {

	private List<Shape> primitives = new ArrayList<Shape>();

	private List<LightSource> lightSources = new ArrayList<LightSource>();
	private AmbientLight ambientLight;

	/**
	 * 
	 * @param primitive
	 */
	public void addShape(Shape primitive) {
		primitives.add(primitive);
	}

	/**
	 * 
	 * @param ambientLight
	 */
	public void setAmbientLight(AmbientLight ambientLight) {
		this.ambientLight = ambientLight;
	}

	/**
	 * 
	 * @param diffuseLight
	 */
	public void addLightSource(LightSource diffuseLight) {
		this.lightSources.add(diffuseLight);
	}

	/**
	 * 
	 * @param ray
	 * @return
	 */
	public Hit intersect(Ray ray) {
		float min_t = Float.POSITIVE_INFINITY;
		Shape min_primitive = null;
		for (Shape primitive : primitives) {
			Float t = primitive.intersectRay(ray);
			if (t != null) {
				if (t < min_t) {
					min_primitive = primitive;
					min_t = t;
				}
			}
		}
		if (min_t != Float.POSITIVE_INFINITY)
			return new Hit(ray, min_primitive.getShape(), min_t);
		return null;
	}

	public AmbientLight getAmbientLight() {
		return ambientLight;
	}
	
	public List<LightSource> getLightSources() {
		return lightSources;
	}
}
