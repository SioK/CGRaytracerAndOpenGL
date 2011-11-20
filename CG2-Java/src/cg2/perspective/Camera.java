package cg2.perspective;

import cg2.raytracer.Ray;
import cg2.vecmath.Vector;

public class Camera {

	private Vector eyePoint;
	//private Vector viewingDirection;
	private double fieldOfViewX;

	/**
	 * 
	 * @param eyePoint
	 * @param viewingDirection
	 * @param fieldOfViewX
	 */
	public Camera(Vector eyePoint, Vector viewingDirection, double fieldOfViewX) {
		this.eyePoint = eyePoint;
		//this.viewingDirection = viewingDirection;
		this.fieldOfViewX = fieldOfViewX;
	}

	public Ray generateRay(int i, int j, int width, int height, int recursionDepth) {
		
		int w = width;
		int h = height;

		float z_alpha = (float) (w / (2 * Math.tan(fieldOfViewX / 2)));
		float x_i = (-(w / 2) + (i + 0.5f) * (w / w));
		float y_j = (-(h / 2) + (j + 0.5f) * (h / h));

		Vector d = new Vector(x_i, y_j, -z_alpha);

		return new Ray(d, eyePoint, recursionDepth);
	}
}
