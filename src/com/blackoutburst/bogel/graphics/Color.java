package com.blackoutburst.bogel.graphics;



/**
 * <h1>Color</h1>
 * 
 * <p>
 * Create and use predefined color
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Color {
	
	/**
	 * White color RGBA(1, 1, 1, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(255, 255, 255);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color WHITE = new Color(1.0f);
	
	/**
	 * Black color RGBA(0, 0, 0, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(0, 0, 0);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color BLACK = new Color(0.0f);
	
	/**
	 * Gray color RGBA(0.5, 0.5, 0.5, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(127, 127, 127);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color GRAY = new Color(0.5f);
	
	/**
	 * Light Gray color RGBA(0.75, 0.75, 0.75, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(191, 191, 191);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color LIGHT_GRAY = new Color(0.75f);
	
	/**
	 * Dark Gray color RGBA(0.25, 0.25, 0.25, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(63, 63, 63);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color DARK_GRAY = new Color(0.25f);
	
	/**
	 * Red color RGBA(1, 0, 0, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(255, 0, 0);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color RED = new Color(1.0f, 0.0f, 0.0f);
	
	/**
	 * Green color RGBA(0, 1, 0, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(0, 255, 0);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color GREEN = new Color(0.0f, 1.0f, 0.0f);
	
	/**
	 * Blue color RGBA(0, 0, 1, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(0, 0, 255);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color BLUE = new Color(0.0f, 0.0f, 1.0f);
	
	/**
	 * Yellow color RGBA(1, 1, 0, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(255, 255, 0);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color YELLOW = new Color(1.0f, 1.0f, 0.0f);
	
	/**
	 * Magenta color RGBA(1, 0, 1, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(255, 0, 255);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f);
	
	/**
	 * Cyan color RGBA(0, 1, 1, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(0, 255, 255);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
	
	/**
	 * Orange color RGBA(1, 0.5, 0, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(255, 127, 0);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color ORANGE = new Color(1.0f, 0.5f, 0.0f);
	
	/**
	 * Light Blue color RGBA(0, 0.5, 1, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(0, 127, 255);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color LIGHT_BLUE = new Color(0.0f, 0.5f, 1.0f);
	
	/**
	 * Transparent color RGBA(0, 0, 0, 0)
	 * <div style="border:2px solid white;width:30px;height:30px;border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.1
	 */
	public static final Color TRANSPARENT = new Color(0.0f, 0.0f, 0.0f, 0.0f);
	
	/**
	 * Purple color RGBA(0.5, 0, 1, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(127, 0, 255);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.2
	 */
	public static final Color PURPLE = new Color(0.5f, 0.0f, 1.0f, 1.0f);
	
	/**
	 * Bogel color RGBA(0.25, 0, 0.27, 1)
	 * <div style="width:30px;height:30px;background-color:rgb(64, 0, 71);border-radius: 20px;float:right;margin: 0 10px 0 0"></div>
	 *
	 * @since 0.2
	 */
	public static final Color BOGEL = new Color(0.25f, 0.0f, 0.27f, 1.0f);
	
	/**Red value*/
	public float r;
	
	/**Green value*/
	public float g;
	
	/**Blue value*/
	public float b;
	
	/**Alpha value*/
	public float a;

	/**
	 * <p>
	 * Create a new color RGBA(0, 0, 0, 1)
	 * </p>
	 * 
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color() {
		this.r = 0.0f;
		this.g = 0.0f;
		this.b = 0.0f;
		this.a = 1.0f;
	}
	
	/**
	 * <p>
	 * Create a new color RGBA(c, c, c, 1)
	 * </p>
	 * 
	 * @param c the color values
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color(float c) {
		this.r = c;
		this.g = c;
		this.b = c;
		this.a = 1.0f;
	}
	
	/**
	 * <p>
	 * Create a new color RGBA(r, g, b, 1)
	 * </p>
	 * 
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = 1.0f;
	}
	
	/**
	 * <p>
	 * Create a new color RGBA(r, g, b, a)
	 * </p>
	 * 
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @param a the alpha value
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	/**
	 * <p>
	 * Get the red value
	 * </p>
	 * 
	 * @return float r
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public float getR() {
		return r;
	}

	/**
	 * <p>
	 * Set the red value
	 * </p>
	 * 
	 * @param r the red value
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public void setR(float r) {
		this.r = r;
	}

	/**
	 * <p>
	 * Get the green value
	 * </p>
	 * 
	 * @return float g
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public float getG() {
		return g;
	}

	/**
	 * <p>
	 * Set the green value
	 * </p>
	 * 
	 * @param g the green value
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public void setG(float g) {
		this.g = g;
	}

	/**
	 * <p>
	 * Get the blue value
	 * </p>
	 * 
	 * @return float b
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public float getB() {
		return b;
	}

	/**
	 * <p>
	 * Set the blue value
	 * </p>
	 * 
	 * @param b the blue value
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public void setB(float b) {
		this.b = b;
	}

	/**
	 * <p>
	 * Get the alpha value
	 * </p>
	 * 
	 * @return float a
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public float getA() {
		return a;
	}

	/**
	 * <p>
	 * Set the alpha value
	 * </p>
	 * 
	 * @param a the alpha value
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public void setA(float a) {
		this.a = a;
	}
	
	/**
	 * <p>
	 * Make the color darker
	 * </p>
	 * 
	 * @return Color
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color darker() {
		r -= 0.1f;
		g -= 0.1f;
		b -= 0.1f;
		
		if (r < 0.0f) r = 0.0f;
		if (g < 0.0f) g = 0.0f;
		if (b < 0.0f) b = 0.0f;
		
		return (this);
	}
	
	/**
	 * <p>
	 * Make the color lighter
	 * </p>
	 * 
	 * @return Color
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color lighter() {
		r += 0.1f;
		g += 0.1f;
		b += 0.1f;
		
		if (r > 1.0f) r = 1.0f;
		if (g > 1.0f) g = 1.0f;
		if (b > 1.0f) b = 1.0f;
		
		return (this);
	}
	
	/**
	 * <p>
	 * Add two color
	 * </p>
	 * 
	 * @param c the value added to the color
	 * @return Color
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color add(Color c) {
		r += c.r;
		g += c.g;
		b += c.b;
		
		if (r > 1.0f) r = 1.0f;
		if (g > 1.0f) g = 1.0f;
		if (b > 1.0f) b = 1.0f;
		
		return(this);
	}
	
	/**
	 * <p>
	 * Subtract two color
	 * </p>
	 * 
	 * @param c the color used to subtract
	 * @return Color
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color sub(Color c) {
		r -= c.r;
		g -= c.g;
		b -= c.b;
		
		if (r < 0.0f) r = 0.0f;
		if (g < 0.0f) g = 0.0f;
		if (b < 0.0f) b = 0.0f;
		
		return(this);
	}
	
	/**
	 * <p>
	 * Multiply two color
	 * </p>
	 * 
	 * @param c the color used to multiply
	 * @return Color
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color mul(Color c) {
		r *= c.r;
		g *= c.g;
		b *= c.b;
		
		if (r < 0.0f) r = 0.0f;
		if (g < 0.0f) g = 0.0f;
		if (b < 0.0f) b = 0.0f;
		if (r > 1.0f) r = 1.0f;
		if (g > 1.0f) g = 1.0f;
		if (b > 1.0f) b = 1.0f;
		
		return(this);
	}
	
	/**
	 * <p>
	 * Divide two color
	 * </p>
	 * 
	 * @param c the color used to divide
	 * @return Color
	 * @author Blackoutburst
	 * @since 0.1
	 */
	public Color div(Color c) {
		if (c.r != 0) r /= c.r;
		if (c.g != 0) g /= c.g;
		if (c.b != 0) b /= c.b;
		
		if (r < 0.0f) r = 0.0f;
		if (g < 0.0f) g = 0.0f;
		if (b < 0.0f) b = 0.0f;
		if (r > 1.0f) r = 1.0f;
		if (g > 1.0f) g = 1.0f;
		if (b > 1.0f) b = 1.0f;
		
		return(this);
	}
}
