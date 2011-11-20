package cg2.models;

import cg2.lights.AmbientLight;
import cg2.lights.LightSource;
import cg2.raytracer.Hit;
import cg2.vecmath.Color;
import cg2.vecmath.Vector;

public class Material {

	private Color ambientCo = new Color(0,0,0);
	private Color diffuseCo = new Color(0,0,0);
	private Color specularCo = new Color(0,0,0);
	private float phongExponent = 0;
	private float reflectionCo = 0;
	private float refractionMediumOutside = 0;
	private float refractionMediumInside = 0;
	

	public Material() {
		
	}
	
	public Material(Color ambientCo, Color diffuseCo, Color specularCo, float phongExponent) {
		this.ambientCo = ambientCo;
		this.diffuseCo = diffuseCo;
		this.specularCo = specularCo;
		this.phongExponent = phongExponent;
	}
	
	public Material(Color ambientCo, Color diffuseCo, Color specularCo, float phongExponent, float reflectionCo) {
		this.ambientCo = ambientCo;
		this.diffuseCo = diffuseCo;
		this.specularCo = specularCo;
		this.phongExponent = phongExponent;
		this.reflectionCo = reflectionCo;
	}
	
	
	public Material(Color ambientCo, Color diffuseCo, Color specularCo, float phongExponent, float reflectionCo, float refractionMediumOutside , float refractionMediumInside) {
		this.ambientCo = ambientCo;
		this.diffuseCo = diffuseCo;
		this.specularCo = specularCo;
		this.phongExponent = phongExponent;
		this.reflectionCo = reflectionCo;
		this.refractionMediumOutside = refractionMediumOutside;
		this.refractionMediumInside = refractionMediumInside;
	}

	public Color getAmbientCo() {
		return ambientCo;
	}

	public void setAmbientCo(Color ambientCo) {
		this.ambientCo = ambientCo;
	}

	public Color getDiffuseCo() {
		return diffuseCo;
	}

	public void setDiffuseCo(Color diffuseCo) {
		this.diffuseCo = diffuseCo;
	}

	public Color getSpecularCo() {
		return specularCo;
	}

	public void setSpecularCo(Color specularCo) {
		this.specularCo = specularCo;
	}

	public float getPhongExponent() {
		return phongExponent;
	}

	public void setPhongExponent(float phongExponent) {
		this.phongExponent = phongExponent;
	}
	
	public void setReflectionCo(float reflectionCoeff) {
		this.reflectionCo = reflectionCoeff;
	}

	public float getReflectionCo() {
		return reflectionCo;
	}
	
	public void setRefractionMediumOutside(float refractionMediumOutside) {
		this.refractionMediumOutside = refractionMediumOutside;
	}
	
	public float getRefractionMediumOutside() {
		return refractionMediumOutside;
	}
	
	public void setRefractionMediumInside(float refractionMediumInside) {
		this.refractionMediumInside = refractionMediumInside;
	}
	
	public float getRefractionMediumInside() {
		return refractionMediumInside;
	}
	
	
	public Color computeAmbient(AmbientLight light) {
		Color ambientLight = new Color(0,0,0);
		if (light.isEnabled()) {
			ambientLight = light.getLightQuantity();
			return ambientLight.modulate(ambientCo);
		}
		return ambientLight;
	}

	public Color computeDiffuseLight(Hit hit, LightSource light) {
		Color diffuseLight = new Color(0, 0, 0);
		if (light.isEnabled()) {
			Color lj = light.getLightQuantity();
			Vector hitPoint = hit.getIntersectionPoint();
			Vector s = light.getPosition().sub(hitPoint).normalize();
			Vector n = hit.getNormal();
			float cosa = n.dot(s);
			if (cosa > 0) {
				return diffuseLight.add(lj.modulate(cosa).modulate(diffuseCo));
			}
		}
		return diffuseLight;
	}

	public Color computeSpecularLight(Hit hit, LightSource light) {
		Color specularLight = new Color(0, 0, 0);
		if (light.isEnabled()) {
			Color lj = light.getLightQuantity();
			
			Vector v = hit.getRay().getDirection().normalize().mult(-1);

			
			Vector s = light.getPosition().sub(hit.getIntersectionPoint()).normalize();
			Vector n = hit.getNormal();
			
			Vector r = n.mult(n.dot(s)*2).sub(s);
			
			float vDotr = v.dot(r);
			
			if (vDotr > 0 && n.dot(s) > 0) {
				specularLight = lj.modulate((float)Math.pow(vDotr, phongExponent)).modulate(specularCo);
			}
		}
		return specularLight;
	}
}
