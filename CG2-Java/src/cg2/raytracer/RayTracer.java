package cg2.raytracer;

import cg2.perspective.Camera;
import cg2.scene.Scene;
import cg2.vecmath.Color;
import cg2.warmup.Painter;

public class RayTracer implements Painter {

	private final Scene scene;
	private final Camera cam;
	
	private final int RECURSION_DEPTH = 5;
	
	private boolean isRendered = false;
	private Color[][] result;

	public RayTracer(Scene scene, Camera cam) {
		this.scene = scene;
		this.cam = cam;
	}


	public Color pixelColorAt(int x, int y, int width, int height) {
		// generate Ray from eye point to pixel center
		if (!isRendered)
			//serialRender(width, height);
			parallelRender(width, height);
		
		return result[y][x];
	}

	private void parallelRender(int width, int height) {
		result = new Color[height][width];
		
		RayTracerThread[] threads = new RayTracerThread[height];
		
		for (int row = 0; row < height; row++) {
			RayTracerThread rt = new RayTracerThread(row, width, height, cam, scene, RECURSION_DEPTH);
			rt.start();
			threads[row] = rt;
		}
		
		for (int row = 0; row < height; row++) {
			try {
				threads[row].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			result[row] = threads[row].getResult();
		}
		
		isRendered = true;
	}
	
	private void serialRender(int width, int height) {
		result = new Color[height][width];
		
		for (int row = 0; row < height; row++) {
			for (int col = 0; col < width; col++) {
				Ray ray = cam.generateRay(col, row, width, height, RECURSION_DEPTH);
				result[row][col] = ray.trace(scene);
			}
		}
		
		isRendered = true;
	}

}
