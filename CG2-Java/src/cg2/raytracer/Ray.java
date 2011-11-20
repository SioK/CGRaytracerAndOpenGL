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
	private final float OFFSET = 0.001f;

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

		Vector normal = firstHit.getNormal();
		for (LightSource light : scene.getLightSources()) {
			Vector offset = normal.mult(OFFSET);
			Ray shadowRay = new Ray(light.getPosition().sub(
					firstHit.getIntersectionPoint()), firstHit
					.getIntersectionPoint().add(offset), recursionDepth);

			Hit intersection = scene.intersect(shadowRay);

			if (intersection == null) {
				result = result.add(material.computeDiffuseLight(firstHit, light));
				result = result.add(material.computeSpecularLight(firstHit, light));
			}
		}

		if (recursionDepth > 0) {
			float n1 = material.getRefractionMediumInside();
			float n2 = material.getRefractionMediumOutside();
			
			float reflectionAbility = 1;

			if (n1 > 0 && n2 > 0) {
				Refraction refractionIn = getRefraction(direction, normal, n1, n2);
				
				reflectionAbility = refractionIn.reflectionAbility;

				if (refractionIn.direction != null) {
					Vector offset = normal.mult(OFFSET);
					Ray refractionRay = new Ray(
							refractionIn.direction,
							firstHit.getIntersectionPoint().sub(offset),
							recursionDepth);
					
					Hit outHit = scene.intersect(refractionRay);
					
					Refraction refractionOut = getRefraction(refractionRay.direction, outHit.getNormal().mult(-1), n2, n1);
					
					if (refractionOut.direction != null) {
						Ray outRay = new Ray(refractionOut.direction, outHit.getIntersectionPoint().add(outHit.getNormal().mult(OFFSET)), recursionDepth-1);

						result = result.add(outRay.trace(scene).modulate(refractionIn.transmissionAbility));
					}
				}
			}
			
			if (material.getReflectionCo() > 0) {
				Vector offset = normal.mult(OFFSET);
				
				Ray reflectionRay = new Ray(getReflection(direction, normal), 
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
	
	/*private Refraction getRefraction(Vector direction, Vector normal, float n1, float n2) {
		Refraction refraction = new Refraction();
		direction = direction.normalize().mult(-1);
		
		//float rindex = prim->GetMaterial()->GetRefrIndex();
		float n = n1 / n2;
		float cosI = normal.dot(direction);
		float cosT2 = 1.0f - n * n * (1.0f - cosI * cosI);
		if (cosT2 > 0.0f)
		{
			refraction.direction = direction.mult(n).add(normal.mult(n * cosI - (float)Math.sqrt( cosT2 )));
			refraction.reflectionAbility = 0;
			refraction.transmissionAbility = 1;
		}
		
		return refraction;
	}*/
	
	private Refraction getRefraction(Vector direction, Vector normal, float n1, float n2) {
		
		Refraction refraction = new Refraction();
		
		float n = n1 / n2;
		Vector D = direction.normalize().mult(n);
		
		float c1 = -normal.dot(direction.normalize());
		float thetaT = (float) (Math.pow(n, 2) * (1 - Math.pow(c1, 2)));
		
		if (thetaT <= 1) {
			float sqrtPart = (float) (1 - thetaT);

			float c2 = (float) Math.sqrt(sqrtPart);
			Vector B = normal.mult(n*c1-c2);
			refraction.direction = D.add(B);

			// schlick approximation
			float r0 = (float) Math.pow((n2 - 1) / (n2 + 1), 2);
			refraction.reflectionAbility = (float) (r0 + (1 - r0) * Math.pow((float) (1 - Math.pow(Math.cos(c1), 2)), 5));
			refraction.transmissionAbility = 1 - refraction.reflectionAbility;
			
		} else {
			refraction.direction = null;
			refraction.reflectionAbility = 1;
			refraction.transmissionAbility = 0;
		}
		
		return refraction;
	}
	
	/*private Refraction getRefraction(Vector direction, Vector normal, float n1, float n2) {
		
		Refraction refraction = new Refraction();
		
		float refractionIndex = n1 / n2;
		Vector D = direction.mult(-refractionIndex);
		
		float thetaI = normal.dot(direction.mult(-1));
		float thetaT = (float) (Math.pow(refractionIndex, 2) * (1 - Math.pow(Math.cos(thetaI), 2)));
		
		if (thetaT <= 1) {
			float sqrtPart = (float) (1 - thetaT);
			
			if (sqrtPart >= 0) {
				Vector B = normal.mult((float) (refractionIndex * Math.cos(thetaI) - Math.sqrt(sqrtPart)));
				refraction.direction = D.add(B).normalize();

				// schlick approximation
				float r0 = (float) Math.pow((n2 - 1) / (n2 + 1), 2);
				refraction.reflectionAbility = (float) (r0 + (1 - r0) * Math.pow((float) (1 - Math.pow(Math.cos(thetaI), 2)), 5));
				refraction.transmissionAbility = 1 - refraction.reflectionAbility;
			}
		} else {
			refraction.direction = null;
			refraction.reflectionAbility = 1;
			refraction.transmissionAbility = 0;
		}
		
		return refraction;
	}*/
	
	private Vector getReflection(Vector direction, Vector normal) {
		Vector rayDirection = direction.normalize().mult(-1);

		return normal.mult(normal.dot(rayDirection) * 2).sub(rayDirection);
	}
}
