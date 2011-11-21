package cg2.models;

import cg2.raytracer.Ray;
import cg2.vecmath.Vector;

public class Sphere extends Shape {

	private Vector center;
	private float radius;

	public Sphere(Material material, Vector center, float radius) {
		super(material);
		this.center = center;
		this.radius = radius;
	}

	@Override
	public Float intersectRay(Ray ray) {

		Float t1, t2;
		Vector distance = center.sub(ray.getOrigin());
		Vector direction = ray.getDirection();
		double B = direction.dot(distance);
		double D = (B * B) - (distance.dot(distance)) + Math.pow(radius,2);

		t1 = (float) (B - Math.sqrt(D));
		t2 = (float) (B + Math.sqrt(D));
		
		// no intersection with sphere
		if (D < 0 || t2 < 0) {
			return null;
		}
		
		// the first intersection on sphere
		if (t1 < 0)
			return t2;
		else
			return t1;
	}

	@Override
	public Vector getNormal(Vector pos) {
		return pos.sub(center).normalize();
	}
}
