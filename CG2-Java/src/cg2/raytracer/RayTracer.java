package cg2.raytracer;

import java.util.LinkedList;

import cg2.perspective.Camera;
import cg2.scene.Scene;
import cg2.vecmath.Color;
import cg2.warmup.Painter;

public class RayTracer implements Painter {

	private final Scene scene;
	private final Camera cam;
	
	private final int RECURSION_DEPTH = 5;
	private final int ROWS_PER_THREAD = 50;
	
	private boolean isRendered = false;
	private Color[][] result;

	public RayTracer(Scene scene, Camera cam) {
		this.scene = scene;
		this.cam = cam;
	}


	public Color pixelColorAt(int x, int y, int width, int height) {
		// generate Ray from eye point to pixel center
		if (!isRendered) {
			long start = System.currentTimeMillis();
			//serialRender(width, height);
			parallelRender(width, height);
			long end = System.currentTimeMillis();
			
			System.out.println("Rendertime: "+(end-start)+"ms");
		}
		
		return result[y][x];
	}

	private void parallelRender(int width, int height) {
		result = new Color[height][width];
		
		LinkedList<RayTracerThread> threads = new LinkedList<RayTracerThread>();
		
		for (int row = 0; row < height; row+=ROWS_PER_THREAD) {
			RayTracerThread rt = new RayTracerThread(row, width, height, cam, scene, RECURSION_DEPTH, ROWS_PER_THREAD);
			rt.start();
			threads.add(rt);
		}
		
		while (!threads.isEmpty()) {
			RayTracerThread t = threads.remove();
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < t.getRowCount(); i++) {
				result[t.getRow()+i] = t.getResult()[i];
			}
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
