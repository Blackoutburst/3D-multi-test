package dev.blackoutburst.game.core;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import dev.blackoutburst.game.Main;
import dev.blackoutburst.game.graphics.Color;
import dev.blackoutburst.game.maths.Matrix;
import dev.blackoutburst.game.maths.Vector2f;
import dev.blackoutburst.game.maths.Vector2i;
import dev.blackoutburst.game.utils.*;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

/**
 * <h1>Display</h1>
 * 
 * <p>
 * Create and manager window
 * </p>
 * 
 * @since 0.1
 * @author Blackoutburst
 */
public class Display {
	
	/**
	 * <h1>FullScreenMode</h1>
	 * 
	 * <p>
	 * Define the window fullscreen mode available
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public enum FullScreenMode{
		NONE,
		FULL,
		BORDERLESS
	}
	
	protected static long window;


	private static boolean toggleMousePressed = false;
	private static boolean toggleFullscreenPressed = false;

	public static boolean shouldClose = false;
	public static boolean showCursor = true;
	
	protected static int width = 1280;
	protected static int height = 720;
	
	protected String title = "Bogel2D Window";
	
	public static Color clearColor = new Color(0.1f);
	
	protected FullScreenMode fullScreen = FullScreenMode.NONE;
	
	/**
	 * <p>
	 * Initialize GLFW<br>
	 * <b>Must be called before anything</b>
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	public void close() {
		shouldClose = true;
	}
	
	/**
	 * <p>
	 * Set the window fullscreen mode
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	private void setFullScreen() {
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode videoMode = glfwGetVideoMode(monitor);
		Vector2i center = new Vector2i(videoMode.width() / 2 - width / 2, videoMode.height() / 2 - height / 2);

		switch(fullScreen) {
			case BORDERLESS:
				glfwSetWindowMonitor(window, NULL, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());	
			break;
			case FULL:
				glfwSetWindowMonitor(window, monitor, 0, 0, videoMode.width(), videoMode.height(), videoMode.refreshRate());	
			break;
			case NONE:
				glfwSetWindowMonitor(window, NULL, center.getX(), center.getY(), width, height, videoMode.refreshRate());
			break;
		}
	}
	
	/**
	 * <p>
	 * Create the window and setup GLFW<br>
	 * <b>Must be called before attempting any render</b>
	 * </p>
	 * 
	 * @return Display
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display create() {
		glfwWindowHint(GLFW_SAMPLES, 4);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
		
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		
		if (window == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		setIcons("icon.png");
		
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
		GL.createCapabilities();
		
		setFullScreen();
		
		glfwSetWindowSizeCallback(window, new WindowCallBack());
		glfwSetCursorPosCallback(window, new MousePositionCallBack());
		glfwSetMouseButtonCallback(window, new MouseButtonCallBack());
		glfwSetScrollCallback(window, new MouseScrollCallBack());
		
		return (this);
	}
	
	/**
	 * <p>
	 * Clear the Color and Depth buffer
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void clear() {
		glClearColor(clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA());
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	/**
	 * <p>
	 * Do the render and poll the events
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void update() {
		Time.INSTANCE.updateDelta();
		Mouse.INSTANCE.getLeftButton().reset();
		Mouse.INSTANCE.getRightButton().reset();
		Mouse.INSTANCE.getMiddleButton().reset();
		Mouse.INSTANCE.setScroll(0f);

		if (Keyboard.INSTANCE.isKeyDown(Keyboard.ESCAPE)) {
			close();
		}

		if (Keyboard.INSTANCE.isKeyDown(Keyboard.LEFT_ALT) && !toggleMousePressed) {
			showCursor = !showCursor;
			glfwSetInputMode(window, GLFW_CURSOR, showCursor ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
		}
		toggleMousePressed = Keyboard.INSTANCE.isKeyDown(Keyboard.LEFT_ALT);

		if (Keyboard.INSTANCE.isKeyDown(Keyboard.F11) && !toggleFullscreenPressed) {
			FullScreenMode mode = fullScreen == FullScreenMode.NONE ? fullScreen = FullScreenMode.BORDERLESS : FullScreenMode.NONE;
			if (mode == FullScreenMode.NONE) {
				setSize(1280, 720);
			}
			setFullscreenMode(mode);
		}
		toggleFullscreenPressed = Keyboard.INSTANCE.isKeyDown(Keyboard.F11);

		glfwPollEvents();
		glfwSwapBuffers(window);
	}
	
	/**
	 * <p>
	 * Define which fullscreen mode the window should use<br>
	 * </p>
	 * 
	 * @return Display
	 * @param mode fullscreen mode
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setFullscreenMode(FullScreenMode mode) {
		if (mode != FullScreenMode.NONE)
			this.setDecoration(false);
		
		fullScreen = mode;
		
		if (window != NULL)
			setFullScreen();
		
		return (this);
	}
	
	/**
	 * <p>
	 * Tell if the window is still open<br>
	 * </p>
	 * 
	 * @return boolean window is open
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public boolean isOpen() {
		return (!shouldClose && !glfwWindowShouldClose(window));
	}
	
	/**
	 * <p>
	 * Set the clear color<br>
	 * </p>
	 * 
	 * @return Display
	 * @param c the new background color
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setClearColor(Color c) {
		clearColor = c;
		return (this);
	}
	
	/**
	 * <p>
	 * Get the clear color<br>
	 * </p>
	 * 
	 * @return clearColor
	 * @since 0.4
	 * @author Blackoutburst
	 */
	public static Color getClearColor() {
		return (clearColor);
	}
	
	/**
	 * <p>
	 * Destroy the window and end the program<br>
	 * </p>
	 * 
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public void destroy() {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		System.exit(0);
	}
	
	/**
	 * <p>
	 * Return the window parameter<br>
	 * </p>
	 * 
	 * @return long window
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static long getWindow() {
		return (window);
	}
	
	/**
	 * <p>
	 * Set the window title<br>
	 * <b>Must be called before creating the window</b>
	 * </p>
	 * 
	 * @return Display
	 * @param title the new window title
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setTitle(String title) {
		if (window != NULL) {
			System.err.println("Warning [Title] must be set BEFORE creating the window!");
		} else {
			this.title = title;
		}
		return (this);
	}
	
	/**
	 * <p>
	 * Set the Vsync<br>
	 * <b>Must be called after creating the window</b>
	 * </p>
	 * 
	 * @return Display
	 * @param enabled if the vsync should be used or not
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setVSync(boolean enabled) {
		if (window != NULL) {
			glfwSwapInterval(enabled ? GLFW_TRUE : GLFW_FALSE);
		} else {
			System.err.println("Warning [Vsync] must be set AFTER creating the window!");
		}
		return (this);
	}

	/**
	 * <p>
	 * Set the window size
	 * </p>
	 * 
	 * @return Display
	 * @param w the window width
	 * @param h the window height
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setSize(int w, int h) {
		width = w;
		height = h;
		
		if (window != NULL)
			glfwSetWindowSize(window, width, height);
		
		return (this);
	}
	
	/**
	 * <p>
	 * Set the window size
	 * </p>
	 * 
	 * @return Display
	 * @param size the window size
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setSize(@NotNull Vector2i size) {
		width = size.getX();
		height = size.getY();
		
		if (window != NULL) {
			glViewport(0, 0, width, height);
			glfwSetWindowSize(window, width, height);
			Main.Companion.setProjection(new Matrix().projectionMatrix(90f, 1000f, 0.1f));
		}
		
		return (this);
	}
	
	/**
	 * <p>
	 * Set the window size
	 * </p>
	 * 
	 * @param w the window width
	 * @param h the window height
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static void callBackSetSize(int w, int h) {
		width = w;
		height = h;
		
		if (window != NULL) {
			glViewport(0, 0, width, height);
			glfwSetWindowSize(window, width, height);
			Main.Companion.setProjection(new Matrix().projectionMatrix(90f, 1000f, 0.1f));
		}
	}
	
	/**
	 * <p>
	 * Set the window resizable<br>
	 * <b>Must be called before creating the window</b>
	 * </p>
	 * 
	 * @return Display
	 * @param resizable allow the window to be resized
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setResizable(boolean resizable) {
		if (window != NULL) {
			System.err.println("Warning [Resizable] must be set BEFORE creating the window!");
		} else {
			glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		}
		return (this);
	}
	
	/**
	 * <p>
	 * Allow the framebuffer to be transparent<br>
	 * <b>Must be called before creating the window</b>
	 * </p>
	 * 
	 * @return Display
	 * @param transparent allow the framebuffer to be transparent (this doesn't work on some graphic devices)
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setTransparent(boolean transparent) {
		if (window != NULL) {
			System.err.println("Warning [Transparent] must be set BEFORE creating the window!");
		} else {
			glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, transparent ? GLFW_TRUE : GLFW_FALSE);
		}
		return (this);
	}
	
	/**
	 * <p>
	 * Set the window decoration<br>
	 * <b>Must be called before creating the window</b>
	 * </p>
	 * 
	 * @return Display
	 * @param decorated allow the window decoration (default system buttons)
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setDecoration(boolean decorated) {
		glfwWindowHint(GLFW_DECORATED , decorated ? GLFW_TRUE : GLFW_FALSE);
		return (this);
	}
	
	/**
	 * <p>
	 * Set the window position<br>
	 * </p>
	 * 
	 * @return Display
	 * @param position the new window position
	 * @since 0.2
	 * @author Blackoutburst
	 */
	public Display setPosition(@NotNull Vector2i position) {
		glfwSetWindowPos(window, position.getX(), position.getY());
		return (this);
	}
	
	/**
	 * <p>
	 * Set the window position<br>
	 * </p>
	 * 
	 * @return Display
	 * @param x the x position
	 * @param y the y position
	 * @since 0.2
	 * @author Blackoutburst
	 */
	public Display setPosition(int x, int y) {
		glfwSetWindowPos(window, x, y);
		return (this);
	}
	
	/**
	 * <p>
	 * Get the window width
	 * </p>
	 * 
	 * @return int
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static int getWidth() {
		int w = 0;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			glfwGetWindowSize(window, width, height);
			w = width.get();
			
			((Buffer)width).clear();
			((Buffer)height).clear();
		} catch (Exception e) {
			System.err.println("Error while getting display width: "+e);
		}
		return (w);
	}
	
	/**
	 * <p>
	 * Get the window height
	 * </p>
	 * 
	 * @return int
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static int getHeight() {
		int h = 0;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			glfwGetWindowSize(window, width, height);
			h = height.get();
			((Buffer)width).clear();
			((Buffer)height).clear();
		} catch (Exception e) {
			System.err.println("Error while getting display height: "+e);
		}
		return (h);
	}
	
	
	/**
	 * <p>
	 * Get the window size
	 * </p>
	 * 
	 * @return Vector2i
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static @NotNull Vector2i getSize() {
		Vector2i size = new Vector2i();
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer width = stack.mallocInt(1);
			IntBuffer height = stack.mallocInt(1);
			glfwGetWindowSize(window, width, height);
			size.set(width.get(), height.get());
			((Buffer)width).clear();
			((Buffer)height).clear();
		} catch (Exception e) {
			System.err.println("Error while getting display size: "+e);
		}
		return (size);
	}
	
	/**
	 * <p>
	 * Get the window size to float
	 * </p>
	 * 
	 * @return Vector2f
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public static @NotNull Vector2f getSizeF() {
		Vector2i tmp = getSize();

		return (new Vector2f(tmp.getX(), tmp.getY()));
	}
	
	/**
	 * <p>
	 * Set the window icon<br>
	 * </p>
	 * 
	 * @return Display
	 * @param filePath the icon path
	 * @since 0.1
	 * @author Blackoutburst
	 */
	public Display setIcons(String filePath) {
		if (Main.Companion.getOsName().contains("mac")) return (this);

		GLFWImage image = GLFWImage.malloc(); 
		GLFWImage.Buffer imageBuffer = GLFWImage.malloc(1);
		try {
			Vector2i imageSize = new Vector2i();
			ByteBuffer byteBuffer = loadIcons(filePath, imageSize);
			
			image.set(imageSize.getX(), imageSize.getY(), byteBuffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		imageBuffer.put(0, image);
		glfwSetWindowIcon(window, imageBuffer);
		return (this);
	}
	
	/**
	 * <p>
	 * Load the window icon<br>
	 * </p>
	 * 
	 * @throws Exception Do you really want the program to crash because the icon is missing ?
	 * @return ByteBuffer
	 * @param path the icon path
	 * @param imageSize the icon size
	 * @since 0.1
	 * @author Blackoutburst
	 */
	private ByteBuffer loadIcons(String path, @NotNull Vector2i imageSize) throws Exception {
		ByteBuffer image;
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer comp = stack.mallocInt(1);
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			
			image = STBImage.stbi_load_from_memory(IOUtils.ioResourceToByteBuffer(path, 1024), w, h, comp, 4);
			imageSize.set(w.get(), h.get());
			
			((Buffer)comp).clear();
			((Buffer)w).clear();
			((Buffer)h).clear();
			if (image == null) {
				throw new Exception("Failed to load icons");
			}
		}
		return image;
	}
}	