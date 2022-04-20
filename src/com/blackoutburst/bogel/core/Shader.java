package com.blackoutburst.bogel.core;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * <h1>Shader</h1>
 * 
 * <p>
 * Create and manager shaders
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Shader {
	
	/** The shader id*/
	protected int id;
	
	/** Vertex shader type */
	public static final int VERTEX = GL_VERTEX_SHADER;
	
	/** Fragment shader type */
	public static final int FRAGMENT = GL_FRAGMENT_SHADER;

	/** Default vertex shader with no texture */
	public static final Shader DEFAULT_VERTEX = loadShader(VERTEX, "quadNoTexture.vert");

	/** Default fragment shader with no texture */
	public static final Shader DEFAULT_FRAGMENT = loadShader(FRAGMENT, "quadNoTexture.frag");

	/** Default vertex shader */
	public static final Shader VERTEX_TEXTURE = loadShader(VERTEX, "quad.vert");

	/** Default fragment shader */
	public static final Shader FRAGMENT_TEXTURE = loadShader(FRAGMENT, "quad.frag");

	/** Default fragment shader with light */
	public static final Shader FRAGMENT_TEXTURE_LIGHT = loadShader(FRAGMENT, "quadLight.frag");
	
	/** Default fragment shader with no texture and light */
	public static final Shader FRAGMENT_LIGHT = loadShader(FRAGMENT, "quadNoTextureLight.frag");
	
	/** Default fragment shader for lights */
	public static final Shader LIGHT = loadShader(FRAGMENT, "lights.frag");

	/**
	 * <p>
	 * Create a new empty shader with a specific id
	 * </p>
	 * 
	 * @param id the shader id
	 * @since 0.1
	 * @author Blackoutburst
	 */
	private Shader(int id) {
		this.id = id;
	}
	
	/**
	 * <p>
	 * Create a new shader from a file
	 * </p>
	 * 
	 * @param shaderType the type of shader used
	 * @param filePath the path to the shader
	 * @return Shader shader
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static Shader loadShader(int shaderType, String filePath) {
		int shader = glCreateShader(shaderType);
		
		StringBuilder shaderSource = new StringBuilder();
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(Shader.class.getResourceAsStream("/"+filePath)));
			String line;
			while((line = reader.readLine())!=null){
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		}catch(Exception e){
			System.err.println("["+filePath+"] Doesn't exist loading default shader instead");
			return (DEFAULT_FRAGMENT);
		}
		glShaderSource(shader, shaderSource);
		glCompileShader(shader);
		if (glGetShaderInfoLog(shader).length() != 0) System.err.println("Error in: ["+filePath+"]\n"+glGetShaderInfoLog(shader));
		return (new Shader(shader));
	}
}
