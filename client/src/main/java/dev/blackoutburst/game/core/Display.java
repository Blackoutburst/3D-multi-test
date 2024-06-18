package dev.blackoutburst.game.core;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import dev.blackoutburst.game.Main;
import dev.blackoutburst.game.graphics.Color;
import dev.blackoutburst.game.maths.Matrix;
import dev.blackoutburst.game.maths.Vector2f;
import dev.blackoutburst.game.maths.Vector2i;
import dev.blackoutburst.game.utils.*;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.*;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.*;
import org.lwjgl.system.MemoryStack;

public class Display {

	public enum FullScreenMode{
		NONE,
		FULL,
		BORDERLESS
	}
	
	protected static long window;

	public NK nk;

	private static boolean toggleMousePressed = false;
	private static boolean toggleFullscreenPressed = false;

	public static boolean shouldClose = false;
	public static boolean showCursor = true;
	
	protected static int width = 1280;
	protected static int height = 720;
	
	protected String title = "Window";
	
	public static Color clearColor = new Color(0.1f);
	
	protected FullScreenMode fullScreen = FullScreenMode.NONE;

	public Display() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");
	}

	public void close() {
		shouldClose = true;
	}

	private void setFullScreen() {
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode videoMode = glfwGetVideoMode(monitor);
        assert videoMode != null;

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
		GL.createCapabilities();

		nk = new NK();

		nk.init(window);

		manageCallBacks();

		glfwShowWindow(window);


		setFullScreen();
		return (this);
	}

	private void manageCallBacks() {
		GLFWScrollCallbackI nkScroll = (window, xoffset, yoffset) -> {
			try (MemoryStack stack = stackPush()) {
				NkVec2 scroll = NkVec2.malloc(stack)
						.x((float)xoffset)
						.y((float)yoffset);
				nk_input_scroll(nk.ctx, scroll);
			}
		};

		GLFWCharCallbackI nkChar = (window, codepoint) -> nk_input_unicode(nk.ctx, codepoint);
		GLFWKeyCallbackI nkKey = (window, key, scancode, action, mods) -> {
			boolean press = action == GLFW_PRESS;
			switch (key) {
				case GLFW_KEY_ESCAPE:
					glfwSetWindowShouldClose(window, true);
					break;
				case GLFW_KEY_DELETE:
					nk_input_key(nk.ctx, NK_KEY_DEL, press);
					break;
				case GLFW_KEY_ENTER:
					nk_input_key(nk.ctx, NK_KEY_ENTER, press);
					break;
				case GLFW_KEY_TAB:
					nk_input_key(nk.ctx, NK_KEY_TAB, press);
					break;
				case GLFW_KEY_BACKSPACE:
					nk_input_key(nk.ctx, NK_KEY_BACKSPACE, press);
					break;
				case GLFW_KEY_UP:
					nk_input_key(nk.ctx, NK_KEY_UP, press);
					break;
				case GLFW_KEY_DOWN:
					nk_input_key(nk.ctx, NK_KEY_DOWN, press);
					break;
				case GLFW_KEY_HOME:
					nk_input_key(nk.ctx, NK_KEY_TEXT_START, press);
					nk_input_key(nk.ctx, NK_KEY_SCROLL_START, press);
					break;
				case GLFW_KEY_END:
					nk_input_key(nk.ctx, NK_KEY_TEXT_END, press);
					nk_input_key(nk.ctx, NK_KEY_SCROLL_END, press);
					break;
				case GLFW_KEY_PAGE_DOWN:
					nk_input_key(nk.ctx, NK_KEY_SCROLL_DOWN, press);
					break;
				case GLFW_KEY_PAGE_UP:
					nk_input_key(nk.ctx, NK_KEY_SCROLL_UP, press);
					break;
				case GLFW_KEY_LEFT_SHIFT:
				case GLFW_KEY_RIGHT_SHIFT:
					nk_input_key(nk.ctx, NK_KEY_SHIFT, press);
					break;
				case GLFW_KEY_LEFT_CONTROL:
				case GLFW_KEY_RIGHT_CONTROL:
					if (press) {
						nk_input_key(nk.ctx, NK_KEY_COPY, glfwGetKey(window, GLFW_KEY_C) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_PASTE, glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_CUT, glfwGetKey(window, GLFW_KEY_X) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_TEXT_UNDO, glfwGetKey(window, GLFW_KEY_Z) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_TEXT_REDO, glfwGetKey(window, GLFW_KEY_R) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_TEXT_WORD_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_TEXT_WORD_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_TEXT_LINE_START, glfwGetKey(window, GLFW_KEY_B) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_TEXT_LINE_END, glfwGetKey(window, GLFW_KEY_E) == GLFW_PRESS);
					} else {
						nk_input_key(nk.ctx, NK_KEY_LEFT, glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_RIGHT, glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS);
						nk_input_key(nk.ctx, NK_KEY_COPY, false);
						nk_input_key(nk.ctx, NK_KEY_PASTE, false);
						nk_input_key(nk.ctx, NK_KEY_CUT, false);
						nk_input_key(nk.ctx, NK_KEY_SHIFT, false);
					}
					break;
			}
		};

		GLFWCursorPosCallbackI nkCursor = (window, xpos, ypos) -> nk_input_motion(nk.ctx, (int)xpos, (int)ypos);
		GLFWMouseButtonCallbackI nkMouseButton = (window, button, action, mods) -> {
			try (MemoryStack stack = stackPush()) {
				DoubleBuffer cx = stack.mallocDouble(1);
				DoubleBuffer cy = stack.mallocDouble(1);

				glfwGetCursorPos(window, cx, cy);

				int x = (int)cx.get(0);
				int y = (int)cy.get(0);

				int nkButton;
				switch (button) {
					case GLFW_MOUSE_BUTTON_RIGHT:
						nkButton = NK_BUTTON_RIGHT;
						break;
					case GLFW_MOUSE_BUTTON_MIDDLE:
						nkButton = NK_BUTTON_MIDDLE;
						break;
					default:
						nkButton = NK_BUTTON_LEFT;
				}
				nk_input_button(nk.ctx, nkButton, x, y, action == GLFW_PRESS);
			}
		};

		GLFWWindowSizeCallbackI mcWindowSize = new WindowCallBack();
		GLFWScrollCallbackI mckScroll = new MouseScrollCallBack();
		GLFWCursorPosCallbackI mcCursor = (window, xpos, ypos) -> new MousePositionCallBack();
		GLFWMouseButtonCallbackI mcMouseButton = (window, button, action, mods) -> new MouseButtonCallBack();

		glfwSetWindowSizeCallback(window, mcWindowSize);

		glfwSetScrollCallback(window, (window, xoffset, yoffset) -> {
			nkScroll.invoke(window, xoffset, yoffset);
			mckScroll.invoke(window, xoffset, yoffset);
		});

		glfwSetCharCallback(window, nkChar);
		glfwSetKeyCallback(window, nkKey);

		glfwSetCursorPosCallback(window, (window, xpos, ypos) -> {
			nkCursor.invoke(window, xpos, ypos);
			mcCursor.invoke(window, xpos, ypos);
		});

		glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			nkMouseButton.invoke(window, button, action, mods);
			mcMouseButton.invoke(window, button, action, mods);
		});

	}

	public void clear() {
		GL11.glClearColor(clearColor.getR(), clearColor.getG(), clearColor.getB(), clearColor.getA());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

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

		nk.newFrame(window);
		nk.render(NK_ANTI_ALIASING_ON);
		glfwSwapBuffers(window);
	}

	public Display setFullscreenMode(FullScreenMode mode) {
		if (mode != FullScreenMode.NONE)
			this.setDecoration(false);
		
		fullScreen = mode;
		
		if (window != NULL)
			setFullScreen();
		
		return (this);
	}

	public boolean isOpen() {
		return (!shouldClose && !glfwWindowShouldClose(window));
	}

	public Display setClearColor(Color c) {
		clearColor = c;
		return (this);
	}

	public static Color getClearColor() {
		return (clearColor);
	}

	public void destroy() {
		nk.shutdown();
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
		System.exit(0);
	}

	public static long getWindow() {
		return (window);
	}

	public Display setTitle(String title) {
		if (window != NULL) {
			System.err.println("Warning [Title] must be set BEFORE creating the window!");
		} else {
			this.title = title;
		}
		return (this);
	}

	public Display setVSync(boolean enabled) {
		if (window != NULL) {
			glfwSwapInterval(enabled ? GLFW_TRUE : GLFW_FALSE);
		} else {
			System.err.println("Warning [Vsync] must be set AFTER creating the window!");
		}
		return (this);
	}

	public Display setSize(int w, int h) {
		width = w;
		height = h;

		if (window != NULL)
			glfwSetWindowSize(window, width, height);

		return (this);
	}


	public Display setSize(@NotNull Vector2i size) {
		width = size.getX();
		height = size.getY();

		if (window != NULL) {
			GL11.glViewport(0, 0, width, height);
			glfwSetWindowSize(window, width, height);
			Main.Companion.setProjection(new Matrix().projectionMatrix(90f, 1000f, 0.1f));
		}

		return (this);
	}

	public static void callBackSetSize(int w, int h) {
		width = w;
		height = h;
		
		if (window != NULL) {
			GL11.glViewport(0, 0, width, height);
			glfwSetWindowSize(window, width, height);
			Main.Companion.setProjection(new Matrix().projectionMatrix(90f, 1000f, 0.1f));
		}
	}

	public Display setResizable(boolean resizable) {
		if (window != NULL) {
			System.err.println("Warning [Resizable] must be set BEFORE creating the window!");
		} else {
			glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
		}
		return (this);
	}

	public Display setTransparent(boolean transparent) {
		if (window != NULL) {
			System.err.println("Warning [Transparent] must be set BEFORE creating the window!");
		} else {
			glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, transparent ? GLFW_TRUE : GLFW_FALSE);
		}
		return (this);
	}

	public Display setDecoration(boolean decorated) {
		glfwWindowHint(GLFW_DECORATED , decorated ? GLFW_TRUE : GLFW_FALSE);
		return (this);
	}

	public Display setPosition(@NotNull Vector2i position) {
		glfwSetWindowPos(window, position.getX(), position.getY());
		return (this);
	}

	public Display setPosition(int x, int y) {
		glfwSetWindowPos(window, x, y);
		return (this);
	}

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

	public static @NotNull Vector2f getSizeF() {
		Vector2i tmp = getSize();

		return (new Vector2f(tmp.getX(), tmp.getY()));
	}

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