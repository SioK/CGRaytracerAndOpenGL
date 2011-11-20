package cg2.loader;

import cg2.lights.AmbientLight;
import cg2.lights.LightSource;
import cg2.models.Material;
import cg2.models.Plane;
import cg2.models.Sphere;
import cg2.models.Triangle;
import cg2.perspective.Camera;
import cg2.raytracer.RayTracer;
import cg2.scene.Scene;
import cg2.vecmath.Color;
import cg2.vecmath.Vector;
import cg2.warmup.ImageGenerator;

public class Main {

	public static void main(String[] args) {

		final int width = 1280;
		final int height = 720;
		
		final Vector eyePosition = new Vector(0f, 0f, 0f);
		final Vector viewingDirection = new Vector(0f, 0f, 0f);
		final float fieldOfViewX = 85*(float)(Math.PI/180);

		Camera cam = new Camera(eyePosition, viewingDirection, fieldOfViewX);

		// generate Material
		Material glossySphereRed = new Material();
		glossySphereRed.setAmbientCo(new Color(0.21f, 0.13f, 0.05f));
		glossySphereRed.setDiffuseCo(new Color(0.71f, 0.43f, 0.18f));
		glossySphereRed.setSpecularCo(new Color(0.39f, 0.27f, 0.17f));
		glossySphereRed.setPhongExponent(26);
		glossySphereRed.setReflectionCo(0.5f);
		
		Material glassSphereBlue = new Material();
		glassSphereBlue.setAmbientCo(new Color(0.1f, 0.1f, 0.5f));
		glassSphereBlue.setDiffuseCo(new Color(0.1f, 0.1f, 0.5f));
		glassSphereBlue.setSpecularCo(new Color(0.5f, 0.5f, 0.5f));
		glassSphereBlue.setPhongExponent(26);
		glassSphereBlue.setRefractionMediumOutside(1.000292f);
		glassSphereBlue.setRefractionMediumInside(1.55f);
		glassSphereBlue.setReflectionCo(0.2f);
		
		Material glossySphereBlue = new Material();
		glossySphereBlue.setAmbientCo(new Color(0, 0, 1));
		glossySphereBlue.setDiffuseCo(new Color(0.0f, 0.0f, 1.0f));
		glossySphereBlue.setSpecularCo(new Color(0.8f, 0.8f, 0.8f));
		glossySphereBlue.setPhongExponent(50);
		glossySphereBlue.setReflectionCo(0.5f);
		
		Material glossySphereGreen = new Material();
		glossySphereGreen.setAmbientCo(new Color(0, 1, 0));
		glossySphereGreen.setDiffuseCo(new Color(0.0f, 1.0f, 0.0f));
		glossySphereGreen.setSpecularCo(new Color(0.8f, 0.8f, 0.8f));
		glossySphereGreen.setPhongExponent(50);
		glossySphereGreen.setReflectionCo(0.5f);

		Material glossyPlane = new Material();
		glossyPlane.setAmbientCo(new Color(0.5f, 0.5f, 0.5f));
		glossyPlane.setDiffuseCo(new Color(0.5f, 0.5f, 0.5f));
		glossyPlane.setSpecularCo(new Color(0.8f, 0.8f, 0.8f));
		glossyPlane.setPhongExponent(50);
		glossyPlane.setReflectionCo(0.5f);
		

		// add shapes to scene
		Scene scene = new Scene();

		scene.addShape(new Plane(glossyPlane, new Vector(0, -1, 0), new Vector(0.0f,
				1f, 0.0f)));
		scene.addShape(new Sphere(glossySphereBlue, new Vector(-1.4f, 0.5f, -5f), 0.1f));
		scene.addShape(new Sphere(glossySphereGreen, new Vector(3.5f, 0.5f, -10f), 0.1f));
		scene.addShape(new Sphere(glossySphereRed, new Vector(0.7f, 0.5f, -7f), 0.1f));
		//scene.addShape(new Sphere(glassSphereBlue, new Vector(0.7f, 0.5f, -7f), 0.1f));
		scene.addShape(new Sphere(glassSphereBlue, new Vector(0.2f, 0.5f, -3f), 0.1f));
		
		// add ambient light to scene
		scene.setAmbientLight(new AmbientLight(0.2f,
				new Color(0.0f, 0.0f, 1.0f), true));

		scene.addLightSource(new LightSource(0.8f, new Color(1.0f, 1.0f, 0.0f),
				new Vector(3, 9, 3), true));
		scene.addLightSource(new LightSource(0.8f, new Color(1.0f, 0.0f, 1.0f),
				new Vector(-3, 1, -5), true));
		scene.addLightSource(new LightSource(0.8f, new Color(1.0f, 1.0f, 1.0f),
				new Vector(12, 18, 5), true));

		RayTracer rt = new RayTracer(scene, cam);

		String path = System.getProperty("user.home");
		String filename = path + "/" + "raytraced.png";
		
		long start = System.currentTimeMillis();
		new ImageGenerator(rt, width, height, filename, "png");
		long end = System.currentTimeMillis();
		
		System.out.println("Rendertime: "+(end-start)+"ms");
		
		ImageGenerator.showImage(filename);
	}

}
