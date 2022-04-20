package com.blackoutburst.bogel.core;

import com.blackoutburst.bogel.maths.Matrix;
import com.blackoutburst.bogel.maths.Vector2f;
import com.blackoutburst.bogel.maths.Vector3f;

/**
 * <h1>Camera</h1>
 * 
 * <p>
 * This is the view matrix<br>
 * Use this to move everything around<br>
 * A Camera is not mandatory for the library to work<br>
 * </p>
 * 
 * <h2>Editing the Camera Z position is deprecated until the zoom is fixed</h2><br>
 * 
 * @since 0.1
 * @author Blackoutburst
 *
 */
public class Camera {

	protected static Vector3f position = new Vector3f();

	protected static Matrix matrix = new Matrix();
	
	/**
	 * <p>
	 * Create a new Camera object placed at position <b>(0, 0, 1)</b>
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Camera() {
		position = new Vector3f(0, 0, 1);
		matrix = new Matrix();
		update();
	}
	
	/**
	 * <p>
	 * Create a new Camera object placed at position <b>(x, y, z)</b>
	 * </p>
	 * 
	 * @deprecated
	 * @param p the position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Camera(Vector3f p) {
		position = p;
		matrix = new Matrix();
		update();
	}
	
	/**
	 * <p>
	 * Create a new Camera object placed at position <b>(x, y, z)</b>
	 * </p>
	 * 
	 * @deprecated
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Camera(float x, float y, float z) {
		position = new Vector3f(x, y, z);
		matrix = new Matrix();
		update();
	}
	
	/**
	 * <p>
	 * Create a new Camera object placed at position <b>(x, y, 1)</b>
	 * </p>
	 * 
	 * @param p the position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Camera(Vector2f p) {
		position = new Vector3f(p.x, p.y, 1);
		matrix = new Matrix();
		update();
	}
	
	/**
	 * <p>
	 * Create a new Camera object placed at position <b>(x, y, 1)</b>
	 * </p>
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Camera(float x, float y) {
		position = new Vector3f(x, y, 1);
		matrix = new Matrix();
		update();
	}

	
	/**
	 * <p>
	 * Get the camera position
	 * </p>
	 * 
	 * @return Vector3f position
 	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static Vector3f getPosition() {
		return position;
	}

	/**
	 * <p>
	 * Set the camera position <b>(x, y, z)</b>
	 * </p>
	 * 
	 * @deprecated
	 * @param p the new position
 	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPosition(Vector3f p) {
		position = p;
		update();
	}
	
	/**
	 * <p>
	 * Set the camera position <b>(x, y, z)</b>
	 * </p>
	 * 
	 * @deprecated
	 * @param x the x position
	 * @param y the y position
	 * @param z the z position
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPosition(float x, float y, float z) {
		position.x = x;
		position.y = y;
		position.z = z;
		update();
	}
	
	/**
	 * <p>
	 * Set the camera position <b>(x, y)</b>
	 * </p>
	 * 
	 * @param p the new position
 	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPosition(Vector2f p) {
		position.x = p.x;
		position.y = p.y;
		update();
	}
	
	/**
	 * <p>
	 * Set the camera position <b>(x, y)</b>
	 * </p>
	 * 
	 * @param x the x position
	 * @param y the y position
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPosition(float x, float y) {
		position.x = x;
		position.y = y;
		update();
	}
	
	/**
	 * <p>
	 * Set the camera X position
	 * </p>
	 * 
	 * @param x the x position
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPositionX(float x) {
		position.x = x;
		update();
	}
	
	/**
	 * <p>
	 * Set the camera Y position
	 * </p>
	 * 
	 * @param y the y position
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPositionY(float y) {
		position.y = y;
		update();
	}
	
	/**
	 * <p>
	 * Set the camera Z position
	 * </p>
	 * 
	 * @deprecated
	 * @param z the z position
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setPositionZ(float z) {
		position.z = z;
		update();
	}
	
	/**
	 * <p>
	 * Return the Camera view matrix
	 * </p>
	 * 
	 * @return Matrix matrix
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static Matrix getMatrix() {
		if (matrix == null)
			matrix = new Matrix();
		return matrix;
	}
	
	/**
	 * <p>
	 * Update the Camera matrix<br>
	 * Translation using position X and Y<br>
	 * Scale using position Z
	 * </p>
	 * 
  	 * @since 0.1
	 * @author Blackoutburst
	 */
	private void update() {
		if (position.z < 0) position.z = 0;
		Matrix.setIdentity(matrix);
		Matrix.translate(new Vector2f(-position.x, -position.y), matrix);
		Matrix.scale(new Vector2f(position.z), matrix);
	}
}
