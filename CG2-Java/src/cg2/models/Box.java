package cg2.models;

import cg2.raytracer.Ray;
import cg2.vecmath.Vector;

public class Box extends Shape {
	

	/**
	 * 
	 * @param color
	 * @param material
	 */
	public Box(Material material) {
		super(material);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Float intersectRay(Ray ray) {
		// TODO Axis Align Box intersection

		return null;
	}

	@Override
	public Vector getNormal(Vector pos) {
		// TODO Auto-generated method stub
		return null;
	}
}
