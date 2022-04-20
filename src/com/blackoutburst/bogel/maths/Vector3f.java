package com.blackoutburst.bogel.maths;

/**
 * <h1>Vector3f</h1>
 * 
 * <p>
 * Create and manager Vector3
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Vector3f {

	/**x value*/
	public float x;
	
	/**y value*/
	public float y;
	
	/**z value*/
	public float z;
	
	/**
	 * <p>
	 * Create a new vector (0, 0, 0)
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3f() {
		this.x = 0.0f;
		this.y = 0.0f;
		this.z = 0.0f;
	}
	
	/**
	 * <p>
	 * Create a new vector (size, size, size)
	 * </p>
	 *
	 * @param size the vector size
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3f(float size) {
		this.x = size;
		this.y = size;
		this.z = size;
	}
	
	/**
	 * <p>
	 * Create a new vector (x, y, z)
	 * </p>
	 *
	 * @param x the vector x
	 * @param y the vector y
	 * @param z the vector z
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * <p>
	 * Set vector values (x, y, z)
	 * </p>
	 *
	 * @param x the vector x
	 * @param y the vector y
	 * @param z the vector z
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * <p>
	 * Get vector X value
	 * </p>
	 *
	 * @return float
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public float getX() {
		return (x);
	}

	/**
	 * <p>
	 * Set vector X value
	 * </p>
	 *
	 * @param x the vector x
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * <p>
	 * Get vector Y value
	 * </p>
	 *
	 * @return float
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public float getY() {
		return (y);
	}

	/**
	 * <p>
	 * Set vector Y value
	 * </p>
	 *
	 * @param y the vector y
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * <p>
	 * Get vector Z value
	 * </p>
	 *
	 * @return float
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public float getZ() {
		return (z);
	}

	/**
	 * <p>
	 * Set vector Z value
	 * </p>
	 *
	 * @param z the vector z
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void setZ(float z) {
		this.z = z;
	}
	
	/**
	 * <p>
	 * Normalize the vector
	 * </p>
	 *
	 * @return Vector3f
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3f normalize() {
		float mag = (float) Math.sqrt(x * x + y * y + z * z);
		if (mag != 0) {
			x /= mag;
			y /= mag;
			z /= mag;
		}
		
		return (this);
	}
	
	/**
	 * <p>
	 * Multiply two vector
	 * </p>
	 *
	 * @param v the vector scale
	 * @return Vector3f
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3f mul(float v) {
		x *= v;
		y *= v;
		z *= v;
		
		return (this);
	}
	
	/**
	 * <p>
	 * 	Return the vector length
	 * </p>
	 *
	 * @return float
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public float length() {
		return ((float) Math.sqrt(x * x + y * y + z * z));
	}
	
	/**
	 * <p>
	 * Create a copy of the Vector
	 * </p>
	 * 
	 * @return Vector3f
	 * @since 0.2
	 * @author Blackoutburst
	 */
	public Vector3f copy() {
		Vector3f newVector = new Vector3f();
		newVector.x = this.x;
		newVector.y = this.y;
		newVector.z = this.z;
		
		return (newVector);
	}

	@Override
	public String toString() {
		return "["+x+", "+y+", "+z+"]";
	}
}
