package cg2.raytracer;

import cg2.models.Shape;
import cg2.vecmath.Vector;

public class Hit {

	private Ray ray;
	private Shape shape;
	private float rayParam;

	/**
	 * 
	 * @param ray
	 * @param shape
	 * @param rayParam
	 */
	public Hit(Ray ray, Shape shape, float rayParam) {
		this.ray = ray;
		this.shape = shape;
		this.rayParam = rayParam;
	}

	public Ray getRay() {
		return ray;
	}

	public Shape getShape() {
		return shape;
	}

	public float getRayParam() {
		return rayParam;
	}

	public Vector getIntersectionPoint() {
		return ray.getPoint(getRayParam());	
	}
	
	public Vector getNormal() {
		return shape.getNormal(ray.getPoint(getRayParam()));	
	}

}