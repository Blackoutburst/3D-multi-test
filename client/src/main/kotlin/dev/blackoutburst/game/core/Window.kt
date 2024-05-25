package dev.blackoutburst.game.core

import dev.blackoutburst.game.graphics.Color
import dev.blackoutburst.game.maths.Vector2i
import dev.blackoutburst.game.utils.*
import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.system.exitProcess


class Window {
    enum class FullScreenMode {
        NONE,
        FULL,
        BORDERLESS
    }

    private var title: String = "OpenGL"

    private var toggleMousePressed = false

    private var shouldClose = false

    private var fullScreen: FullScreenMode = FullScreenMode.NONE

    init {
        GLFWErrorCallback.createPrint(System.err).set()

        check(glfwInit()) { "Unable to initialize GLFW" }
    }

    private fun setFullScreen() {
        val monitor = glfwGetPrimaryMonitor()
        val videoMode = glfwGetVideoMode(monitor)
        val center = Vector2i(videoMode!!.width() / 2 - width / 2, videoMode.height() / 2 - height / 2)

        when (fullScreen) {
            FullScreenMode.BORDERLESS -> glfwSetWindowMonitor(
                id,
                NULL,
                0,
                0,
                videoMode.width(),
                videoMode.height(),
                videoMode.refreshRate()
            )

            FullScreenMode.FULL -> glfwSetWindowMonitor(
                id,
                monitor,
                0,
                0,
                videoMode.width(),
                videoMode.height(),
                videoMode.refreshRate()
            )

            FullScreenMode.NONE -> glfwSetWindowMonitor(
                id,
                NULL,
                center.x,
                center.y,
                width,
                height,
                videoMode.refreshRate()
            )
        }
    }

    fun close() {
        this.shouldClose = true
    }

    fun create(): Window {
        glfwWindowHint(GLFW_SAMPLES, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2)
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        id = glfwCreateWindow(width, height, title, NULL, NULL)

        if (id == NULL) throw RuntimeException("Failed to create the GLFW window")

        glfwMakeContextCurrent(id)
        glfwShowWindow(id)
        glfwSwapInterval(0)

        GL.createCapabilities()

        setFullScreen()

        glfwSetCursorPosCallback(id, MousePositionCallBack())
        glfwSetMouseButtonCallback(id, MouseButtonCallBack())
        glfwSetScrollCallback(id, MouseScrollCallBack())

        glfwSetInputMode(id, GLFW_CURSOR, GLFW_CURSOR_DISABLED)

        return (this)
    }

    fun clear(color: Color = Color.DARK_GRAY) {
        glClearColor(color.r, color.g, color.b, color.a)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun update() {
        Time.updateDelta()
        Mouse.leftButton.reset()
        Mouse.rightButton.reset()
        Mouse.middleButton.reset()
        Mouse.scroll = 0f

        if (Keyboard.isKeyDown(Keyboard.ESCAPE)) {
            close()
        }

        if (Keyboard.isKeyDown(Keyboard.LEFT_ALT) && !toggleMousePressed) {
            showCursor = !showCursor
            glfwSetInputMode(id, GLFW_CURSOR, if (showCursor) GLFW_CURSOR_NORMAL else GLFW_CURSOR_DISABLED)
        }
        toggleMousePressed = Keyboard.isKeyDown(Keyboard.LEFT_ALT)

        glfwPollEvents()
        glfwSwapBuffers(id)
    }

    fun setFullscreenMode(mode: FullScreenMode): Window {
        if (mode != FullScreenMode.NONE) this.setDecoration(false)

        fullScreen = mode

        if (id != NULL) setFullScreen()

        return (this)
    }

    val isOpen: Boolean
        get() = (!shouldClose && !glfwWindowShouldClose(id))

    fun destroy() {
        glfwFreeCallbacks(id)
        glfwDestroyWindow(id)
        glfwTerminate()
        glfwSetErrorCallback(null)!!.free()
        exitProcess(0)
    }

    fun setTitle(title: String): Window {
        if (id != NULL) {
            System.err.println("Warning [Title] must be set BEFORE creating the window!")
        } else {
            this.title = title
        }
        return (this)
    }

    fun setDecoration(decorated: Boolean): Window {
        if (id != NULL) {
            System.err.println("Warning [Decoration] must be set before creating the window!")
        } else {
            glfwWindowHint(GLFW_DECORATED, if (decorated) GLFW_TRUE else GLFW_FALSE)
        }
        return (this)
    }

    companion object {
        var showCursor = false
        var id: Long = 0
        val width = 1280
        val height = 720
    }
}