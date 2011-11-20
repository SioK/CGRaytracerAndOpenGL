package cg2.raytracer;

import cg2.lights.LightSource;
import cg2.models.Material;
import cg2.scene.Scene;
import cg2.vecmath.Color;
import cg2.vecmath.Vector;

public class Ray {

	private Vector origin;
	private Vector direction;
	private int recursionDepth;
	private final float OFFSET = 0.0005f;

	/**
	 * 
	 * @param direction
	 * @param origin
	 * @param recursionDepth
	 */
	public Ray(Vector direction, Vector origin, int recursionDepth) {
		this.direction = direction.normalize();
		this.origin = origin;
		this.recursionDepth = recursionDepth;
	}

	public Vector getOrigin() {
		return origin;
	}

	public Vector getDirection() {
		return direction;
	}

	public Vector getPoint(float t) {
		return origin.add(direction.mult(t));
	}

	public Color trace(Scene scene) {
		// find first intersection point with scene objects
		Hit firstHit = scene.intersect(this);
		// calculate light intensity/color at hit point
		if (firstHit == null) {
			return new Color(0, 0, 0);
		}

		Material material = firstHit.getShape().getMaterial();

		Color result = new Color(0, 0, 0);

		result = result.add(material.computeAmbient(scene.getAmbientLight()));

		for (LightSource light : scene.getLightSources()) {
			Vector direction = light.getPosition().sub(firstHit.getIntersectionPoint()).normalize();
			Vector offset = direction.mult(OFFSET);
			
			Ray shadowRay = new Ray(direction,
					firstHit.getIntersectionPoint().add(offset), recursionDepth);

			Hit intersection = scene.intersect(shadowRay);

			if (intersection == null) {
				result = result.add(material.computeDiffuseLight(firstHit, light));
				result = result.add(material.computeSpecularLight(firstHit, light));
			}
		}

		if (recursionDepth > 0) {
			float n1 = material.getRefractionMediumOutside();
			float n2 = material.getRefractionMediumInside();
			
			float reflectionAbility = 1;

			if (n1 > 0 && n2 > 0) {
				Refraction refractionIn = getRefraction(direction, firstHit.getNormal(), n1, n2);
				
				if (refractionIn != null) {
					reflectionAbility = refractionIn.reflectionAbility;
					Vector offset = refractionIn.direction.mult(OFFSET);
					Ray refractionRay = new Ray(
							refractionIn.direction,
							firstHit.getIntersectionPoint().sub(offset),
							recursionDepth);
					
					Hit outHit = scene.intersect(refractionRay);
					
					Refraction refractionOut = getRefraction(refractionRay.direction, outHit.getNormal().mult(-1), n2, n1);
					
					if (refractionOut != null) {
						Ray outRay = new Ray(refractionOut.direction, outHit.getIntersectionPoint().add(refractionOut.direction.mult(OFFSET)), recursionDepth-1);

						result = result.add(outRay.trace(scene).modulate(refractionIn.transmissionAbility));
					}
				}
			}
			
			if (material.getReflectionCo() > 0) {
				Vector direction = getReflection(this.direction, firstHit.getNormal());
				Vector offset = direction.mult(OFFSET);
				
				Ray reflectionRay = new Ray(direction, 
						firstHit.getIntersectionPoint().add(offset), recursionDepth - 1);
				
				result = result.add(reflectionRay.trace(scene)
						.modulate(material.getReflectionCo()*reflectionAbility));
			}

		}

		return result;
	}
	
	private class Refraction {
		Vector direction;
		
		float reflectionAbility;
		float transmissionAbility;
	}
	
	/**
	 * Ported from paper "Reflections and Refractions in Ray Tracing" by Bram de Greve (2006)
	 * @param incident
	 * @param normal
	 * @param n1
	 * @param n2
	 * @return
	 */
	private Refraction getRefraction(Vector incident, Vector normal, float n1, float n2) {
		float n = n1 / n2;
		float cosI = -normal.dot(incident);
		float sinT2 = n * n * (1.0f - cosI * cosI);
		
		if (sinT2 > 1.0) { // TIR
			return null;
		}
		
		float cosT = (float)Math.sqrt(1.0f - sinT2);
		
		Refraction res = new Refraction();
		res.direction = incident.mult(n).add(normal.mult(n * cosI - cosT));
		
		// Schlick approximation
		float r0 = (n1 - n2) / (n1 + n2);
		r0 *= r0;
		
		float cosX = cosI;
		if (n1 > n2) {
			cosX = cosT;
		}
		float x = 1.0f - cosX;
		
		res.reflectionAbility = r0 + (1.0f -r0) * x * x * x * x * x;
		res.transmissionAbility = 1.0f - res.reflectionAbility;
		
		return res;
	}
	
	private Vector getReflection(Vector direction, Vector normal) {
		Vector rayDirection = direction.normalize().mult(-1);

		return normal.mult(normal.dot(rayDirection) * 2).sub(rayDirection);
	}
}
