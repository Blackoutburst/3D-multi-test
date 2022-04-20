package com.blackoutburst.bogel.maths;

/**
 * <h1>Vector3i</h1>
 * 
 * <p>
 * Create and manager Vector3
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Vector3i {

	/**x value*/
	public int x;
	
	/**y value*/
	public int y;
	
	/**z value*/
	public int z;
	
	/**
	 * <p>
	 * Create a new vector (0, 0, 0)
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3i() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
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
	public Vector3i(int size) {
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
	public Vector3i(int x, int y, int z) {
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
	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * <p>
	 * Get vector X value
	 * </p>
	 *
	 * @return int
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public int getX() {
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
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * <p>
	 * Get vector Y value
	 * </p>
	 *
	 * @return int
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public int getY() {
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
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * <p>
	 * Get vector Z value
	 * </p>
	 *
	 * @return int
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public int getZ() {
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
	public void setZ(int z) {
		this.z = z;
	}
	
	/**
	 * <p>
	 * Normalize the vector
	 * </p>
	 *
	 * @return Vector3i
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3i normalize() {
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
	 * @return Vector3i
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector3i mul(float v) {
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
	 * @return Vector3i
	 * @since 0.2
	 * @author Blackoutburst
	 */
	public Vector3i copy() {
		Vector3i newVector = new Vector3i();
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
