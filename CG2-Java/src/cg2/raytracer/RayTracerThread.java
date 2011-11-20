package cg2.raytracer;

import cg2.perspective.Camera;
import cg2.scene.Scene;
import cg2.vecmath.Color;

public class RayTracerThread extends Thread {
	private int row;
	private int width;
	private int height;
	
	private int recursionDepth;
	
	private Camera camera;
	private Scene scene;
	
	private Color[] result;
	
	public RayTracerThread(int r, int width, int height, Camera cam, Scene scene, int recursionDepth) {
		this.row = r;
		this.width = width;
		this.height = height;
		this.camera = cam;
		this.scene = scene;
		this.recursionDepth = recursionDepth;
	}
	
	@Override
	public void run() {
		super.run();
		
		result = new Color[width];
		
		for (int col = 0; col < width; col++) {
			Ray ray = camera.generateRay(col, row, width, height, recursionDepth);

			result[col] = ray.trace(scene);
		}
	}
	
	public Color[] getResult() {
		return result;
	}

}
