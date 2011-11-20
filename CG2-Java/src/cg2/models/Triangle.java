package cg2.models;

import cg2.raytracer.Ray;
import cg2.vecmath.Vector;

public class Triangle extends Shape {

	private final Vector p0;
	private final Vector p1;
	private final Vector p2;

	/**
	 * 
	 * @param color
	 * @param p0
	 * @param p1
	 * @param p2
	 * @param material
	 */
	public Triangle(Material material, Vector p0, Vector p1, Vector p2) {
		super(material);
		this.p0 = p0;
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public Float intersectRay(Ray ray) {

		Vector normal = getNormal(null);
		Float t = getT(ray, normal, p0);

		if (t != null) {

			Vector p = ray.getPoint(t);
			if (PointInTriangle(p, p0, p1, p2)) {
				return t;
			}
		}
		return null;
	}

	private Float getT(Ray ray, Vector normal, Vector point) {

		double d = point.dot(normal);
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

	private boolean PointInTriangle(Vector p, Vector p0, Vector p1, Vector p2) {

		double n1 = new Vector(p1.x - p0.x, p1.y - p0.y, p1.z - p0.z)
				.cross(new Vector(p.x - p0.x, p.y - p0.y, p.z - p0.z)).z;
		double n2 = new Vector(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)
				.cross(new Vector(p.x - p1.x, p.y - p1.y, p.z - p1.z)).z;
		double n3 = new Vector(p0.x - p2.x, p0.y - p2.y, p0.z - p2.z)
				.cross(new Vector(p.x - p2.x, p.y - p2.y, p.z - p2.z)).z;

		if (n1 * n2 > 0.0 && n1 * n3 > 0.0 && n2 * n3 > 0.0) {
			return true;
		}

		return false;
	}

	public Vector getNormal(Vector foo) {

		Vector normal = p1.sub(p0).cross(p2.sub(p0));

		if (normal.length() < 0) {
			return null;
		}

		return normal.normalize();
	}

}
