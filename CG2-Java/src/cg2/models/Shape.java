package cg2.models;

import cg2.raytracer.Ray;
import cg2.vecmath.Vector;


public abstract class Shape {

	private Material material;

	/**
	 * 
	 * @param material
	 */
	public Shape(Material material) {

		this.material = material;
	}
	
	public Material getMaterial() {
		return material;
	}

	public Shape getShape() {
		return this;
	}

	abstract public Float intersectRay(Ray ray);
	abstract public Vector getNormal(Vector pos);

}
