package cg2.raytracer;

import cg2.perspective.Camera;
import cg2.scene.Scene;
import cg2.vecmath.Color;

public class RayTracerThread extends Thread {
	private int row;
	private int width;
	private int height;
	
	private int recursionDepth;
	
	private int rows;
	
	private Camera camera;
	private Scene scene;
	
	private Color[][] result;
	
	public RayTracerThread(int r, int width, int height, Camera cam, Scene scene, int recursionDepth, int rowsPerThread) {
		this.row = r;
		this.width = width;
		this.height = height;
		this.camera = cam;
		this.scene = scene;
		this.recursionDepth = recursionDepth;
		this.rows = Math.min(rowsPerThread, height-row);
	}
	
	@Override
	public void run() {
		super.run();
		
		result = new Color[rows][width];
		
		for (int r = 0; r < rows; r++) {
			for (int col = 0; col < width; col++) {
				Ray ray = camera.generateRay(col, row+r, width, height, recursionDepth);
	
				result[r][col] = ray.trace(scene);
			}
		}
	}
	
	public Color[][] getResult() {
		return result;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getRowCount() {
		return rows;
	}

}
