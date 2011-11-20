package cg2.models;

import cg2.raytracer.Ray;
import cg2.vecmath.Vector;

public class Plane extends Shape {

	private Vector anchorPoint;
	private Vector normal;

	/**
	 * 
	 * @param color
	 * @param anchorPoint
	 * @param normal
	 * @param material
	 */
	public Plane(Material material, Vector anchorPoint, Vector normal) {
		super(material);
		this.anchorPoint = anchorPoint;
		this.normal = normal.normalize();
	}

	@Override
	public Float intersectRay(Ray ray) {

		double d = anchorPoint.dot(normal);
		double vd = ray.getDirection().dot(normal);

		if (Math.abs(vd) <= 0) {
			// ray is parallel to the plane
			return null;
		}
		double v0 = d - normal.dot(ray.getOrigin());
		Float t = (float) (v0 / vd);
		if (t < 0) {
			// opposite direction
			return null;
		}
		return t;
	}

	@Override
	public Vector getNormal(Vector foo) {
		return normal;
	}

}
