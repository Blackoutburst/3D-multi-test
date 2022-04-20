package com.blackoutburst.bogel.maths;

/**
 * <h1>Vector2i</h1>
 * 
 * <p>
 * Create and manager Vector2
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Vector2i {

	/**x value*/
	public int x;
	
	/**y value*/
	public int y;
	
	/**
	 * <p>
	 * Create a new vector (0, 0)
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector2i() {
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * <p>
	 * Create a new vector (size, size)
	 * </p>
	 *
	 * @param size the vector size
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector2i(int size) {
		this.x = size;
		this.y = size;
	}
	
	/**
	 * <p>
	 * Create a new vector (x, y)
	 * </p>
	 *
	 * @param x the vector x
	 * @param y the vector y
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * <p>
	 * Set vector values (x, y)
	 * </p>
	 *
	 * @param x the vector x
	 * @param y the vector y
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void set(int x, int y) {
		this.x = x;
		this.y = y;
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
	 * Normalize the vector
	 * </p>
	 *
	 * @return Vector2i
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector2i normalize() {
		float mag = (float) Math.sqrt(x * x + y * y);
		if (mag != 0) {
			x /= mag;
			y /= mag;
		}
		
		return (this);
	}
	
	/**
	 * <p>
	 * Multiply two vector
	 * </p>
	 *
	 * @param v the vector scale
	 * @return Vector2i
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Vector2i mul(float v) {
		x *= v;
		y *= v;
		
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
		return ((float) Math.sqrt(x * x + y * y));
	}
	
	/**
	 * <p>
	 * Create a copy of the Vector
	 * </p>
	 * 
	 * @return Vector2i
	 * @since 0.2
	 * @author Blackoutburst
	 */
	public Vector2i copy() {
		Vector2i newVector = new Vector2i();
		newVector.x = this.x;
		newVector.y = this.y;
		
		return (newVector);
	}

	@Override
	public String toString() {
		return "["+x+", "+y+"]";
	}
}
