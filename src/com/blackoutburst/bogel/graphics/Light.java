package com.blackoutburst.bogel.graphics;

import com.blackoutburst.bogel.maths.Vector2f;

/**
 * <p>
 * Light object class
 * </p>
 * 
 * @author Blackoutburst
 * @since 0.2
 */
public class Light {
	protected Vector2f position;
	protected Color color;
	protected float intensity;
	
	/**
	 * <p>
	 * Create a new light object and automatically add it to the lights list
	 * </p>
	 * 
	 * @param position the light position
	 * @param color the light color
	 * @param intensity the light intensity
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public Light(Vector2f position, Color color, float intensity) {
		this.position = position;
		this.color = color;
		this.intensity = intensity;
		
		Lights.lights.add(this);
	}

	/**
	 * <p>
	 * Get the light position
	 * </p>
	 * 
	 * @return position
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public Vector2f getPosition() {
		return position;
	}

	/**
	 * <p>
	 * Set the light position
	 * </p>
	 * 
	 * @param position the light position
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public void setPosition(Vector2f position) {
		this.position = position;
	}

	/**
	 * <p>
	 * Get the light color
	 * </p>
	 * 
	 * @return color
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * <p>
	 * Set the light color
	 * </p>
	 * 
	 * @param color the light color
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * <p>
	 * Get the light intensity
	 * </p>
	 * 
	 * @return intensity
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public float getIntensity() {
		return intensity;
	}

	/**
	 * <p>
	 * Set the light intensity
	 * </p>
	 * 
	 * @param intensity the light intensity
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}
	
	/**
	 * Delete the light and remove it from the lights list
	 * @author Blackoutburst
	 * @since 0.2
	 */
	public void delete() {
		Lights.lights.remove(this);
	}
	
}
