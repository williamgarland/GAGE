package com.accele.gage.gfx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;

/**
 * The {@code Window} class acts as an intermediary between the engine and the underlying windowing system and is used to display content to the screen.
 * <p>
 * GAGE uses the <i>programmable pipeline</i> version of the LWJGL (and by extension OpenGL) API to display graphics.
 * This means that the {@code Display} class, which was available in LWJGL up until version 3, is no longer available, so GLFW is now used instead.
 * </p>
 * <p>
 * GLFW provides a set of available <i>window hints</i> to specify upon window creation to modify the settings of the context.
 * In addition to using windowed mode by default, GAGE uses the following default window hints:
 * </p>
 * <pre>
 * glfwWindowHint(GLFW_SAMPLES, 4);
 * glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
 * glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
 * glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
 * glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
 * </pre>
 * <p>
 * To specify custom window hints, call the {@link com.accele.gage.GAGE#setCustomWindowHints(int[]) setCustomWindowHints(int[])} method prior to initializing the engine.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class Window implements Cleanable {

	private int width;
	private int height;
	private String title;
	private long pointer;
	private boolean contextCurrent;
	
	/**
	 * Initializes the {@code Window} using the specified {@code width}, {@code height}, {@code title}, and {@code windowHints}.
	 * <p>
	 * This constructor is not intended to be called by the user; {@code Window} creation is handled by the engine.
	 * The width, height, and title of the {@code Window} is specified using the {@link com.accele.gage.GAGE#init(int, int, String) init(int, int, String)} method.
	 * For window hints, GAGE has a list of preset hints (see the {@link com.accele.gage.gfx.Window Window} class for a complete list); to specify custom window hints, 
	 * call the {@link com.accele.gage.GAGE#setCustomWindowHints(int[]) setCustomWindowHints(int[])} method prior to initializing the engine.
	 * </p>
	 * @param width			the width of the window
	 * @param height		the height of the window
	 * @param title			the title of the window
	 * @param windowHints	the window hints to use when initializing the window
	 * 
	 * @see com.accele.gage.gfx.Window Window
	 */
	public Window(int width, int height, String title, int[] windowHints) {
		this(width, height, title, windowHints, null);
	}
	
	/**
	 * Initializes the {@code Window} using the specified {@code width}, {@code height}, {@code title}, and {@code windowHints},
	 * with {@code parent} as the parent {@code Window}.
	 * <p>
	 * This constructor is not intended to be called by the user; {@code Window} creation is handled by the engine.
	 * The width, height, and title of the {@code Window} is specified using the {@link com.accele.gage.GAGE#init(int, int, String) init(int, int, String)} method.
	 * For window hints, GAGE has a list of preset hints (see the {@link com.accele.gage.gfx.Window Window} class for a complete list); to specify custom window hints, 
	 * call the {@link com.accele.gage.GAGE#setCustomWindowHints(int[]) setCustomWindowHints(int[])} method prior to initializing the engine.
	 * </p>
	 * <p>
	 * Creating a {@code Window} using this constructor will create a GLFW window with shared resources between itself and its parent window.
	 * To create a window without sharing resources, use {@link #Window(int, int, String, int[]) Window(int, int, String, int[])}.
	 * </p>
	 * @param width			the width of the window
	 * @param height		the height of the window
	 * @param title			the title of the window
	 * @param windowHints	the window hints to use when initializing the window
	 * @param parent		the parent window of this window
	 * 
	 * @see com.accele.gage.gfx.Window Window
	 */
	public Window(int width, int height, String title, int[] windowHints, Window parent) {
		this.width = width;
		this.height = height;
		this.title = title;
		
		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW.");
		
		if (windowHints == null) {
			GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
			GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		} else {
			for (int i = 0; i < windowHints.length; i++) {
				int key = windowHints[i++];
				int value = windowHints[i];
				GLFW.glfwWindowHint(key, value);
			}
		}
			
		this.pointer = GLFW.glfwCreateWindow(width, height, title, 0, parent == null ? 0 : parent.pointer);
			
		if (pointer == 0) {
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window.");
		}
		
		if (parent != null && parent.contextCurrent)
			parent.detachContext();
			
		attachContext();
		GL.createCapabilities();
			
		GLFW.glfwSetWindowCloseCallback(pointer, window -> close());
	}
	
	/**
	 * Returns the current context status of the window.
	 * <p>
	 * GLFW requires that each window be associated with a particular OpenGL context in order to work,
	 * and only one context may be attached at any one time. When switching contexts, 
	 * make sure that the previous context is detached before attaching a new one!
	 * </p>
	 * @return the current context status of the window
	 */
	public boolean isContextCurrent() {
		return contextCurrent;
	}
	
	/**
	 * Attaches the OpenGL context associated with this window.
	 * <p>
	 * GLFW requires that each window be associated with a particular OpenGL context in order to work,
	 * and only one context may be attached at any one time. When switching contexts, 
	 * make sure that the previous context is detached before attaching a new one!
	 * </p>
	 */
	public void attachContext() {
		if (contextCurrent)
			return;
		contextCurrent = true;
		GLFW.glfwMakeContextCurrent(pointer);
	}
	
	/**
	 * Detaches the OpenGL context associated with this window.
	 * <p>
	 * GLFW requires that each window be associated with a particular OpenGL context in order to work,
	 * and only one context may be attached at any one time. When switching contexts, 
	 * make sure that the previous context is detached before attaching a new one!
	 * </p>
	 * 
	 */
	public void detachContext() {
		if (!contextCurrent)
			return;
		contextCurrent = false;
		GLFW.glfwMakeContextCurrent(0);
	}
	
	/**
	 * Returns the full screen status of the window.
	 * <p>
	 * By default, GAGE initializes its window using windowed mode.
	 * To set the window to full screen, use {@link com.accele.gage.gfx.Window#setFullscreen(boolean) setFullscreen(boolean)}.
	 * </p>
	 * @return the full screen status of the window
	 */
	public boolean isFullscreen() {
		return GLFW.glfwGetWindowMonitor(pointer) != 0;
	}
	
	/**
	 * Sets the full screen status of the window.
	 * @param fullscreen 	whether the window should be full screen
	 */
	public void setFullscreen(boolean fullscreen) {
		if (fullscreen)
			GLFW.glfwSetWindowMonitor(pointer, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, GLFW.GLFW_DONT_CARE);
		else
			GLFW.glfwSetWindowMonitor(pointer, 0, 0, 0, width, height, GLFW.GLFW_DONT_CARE);
	}
	
	/**
	 * Returns a {@link java.util.Map Map} of all currently connected monitors in which the key is the pointer to the monitor and the value is the name of the monitor.
	 * @return a {@code Map} of all currently connected monitors
	 */
	public Map<Long, String> getMonitors() {
		Map<Long, String> result = new HashMap<>();
		PointerBuffer pb = GLFW.glfwGetMonitors();
		while (pb.hasRemaining()) {
			long ptr = pb.get();
			result.put(ptr, GLFW.glfwGetMonitorName(ptr));
		}
		return result;
	}
	
	/**
	 * Returns a {@link java.util.Map Map} of all supported video modes per monitor in which the key is the pointer to the monitor 
	 * and the value is the {@link java.util.List List} of all supported video modes for that specific monitor.
	 * @return a {@code Map} of all supported video modes per monitor
	 */
	public Map<Long, List<GLFWVidMode>> getVideoModes() {
		Map<Long, List<GLFWVidMode>> result = new HashMap<>();
		PointerBuffer pb = GLFW.glfwGetMonitors();
		while (pb.hasRemaining()) {
			long ptr = pb.get();
			List<GLFWVidMode> modes = new ArrayList<>();
			GLFWVidMode.Buffer b = GLFW.glfwGetVideoModes(ptr);
			while (b.hasRemaining())
				modes.add(b.get());
			result.put(ptr, modes);
		}
		return result;
	}
	
	/**
	 * Shows the window if it was previously hidden. GAGE automatically shows the window upon calling the {@link com.accele.gage.GAGE#start() start()} method.
	 */
	public void show() {
		GLFW.glfwShowWindow(pointer);
	}
	
	/**
	 * Hides the window if it is currently being shown. GAGE automatically shows the window upon calling the {@link com.accele.gage.GAGE#start() start()} method.
	 */
	public void hide() {
		GLFW.glfwHideWindow(pointer);
	}
	
	/**
	 * Called at the beginning of every game loop cycle to clear the screen. This method is for internal use only and should not be called directly by the user.
	 */
	public void onCycleBegin() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	/**
	 * Called at the end of every game loop cycle to swap window buffers. This method is for internal use only and should not be called directly by the user.
	 */
	public void onCycleEnd() {
		GLFW.glfwSwapBuffers(pointer);
	}
	
	/**
	 * Called at the end of every game loop cycle to poll input events. This method is for internal use only and should not be called directly by the user.
	 */
	public void pollEvents() {
		GLFW.glfwPollEvents();
	}
	
	/**
	 * Brings this window to the front and sets input focus.
	 */
	public void focus() {
		GLFW.glfwFocusWindow(pointer);
	}
	
	/**
	 * Closes the window and stops the engine. This method has the same effect as calling {@link com.accele.gage.GAGE#stop() stop()}.
	 */
	public void close() {
		GAGE.getInstance().stop();
		setClosed(true);
	}
	
	/**
	 * Returns the closed status of the window. This method will return true if either the window is closed manually using the {@link com.accele.gage.gfx.Window#setClosed(boolean) setClosed(boolean)} method or if the window is closed due to an external process (i.e. pressing the close button).
	 * @return the closed status of the window
	 */
	public boolean isClosed() {
		return GLFW.glfwWindowShouldClose(pointer);
	}
	
	/**
	 * Sets the closed status of the window. This method will not stop the engine; it only closes the window.
	 * To stop the engine, use the {@link com.accele.gage.GAGE#stop() stop()} method or the {@link com.accele.gage.gfx.Window#close() close()} method.
	 * @param closed	whether the window should be closed
	 */
	private void setClosed(boolean closed) {
		GLFW.glfwSetWindowShouldClose(pointer, closed);
	}
	
	@Override
	public void clean() {
		GLFW.glfwTerminate();
	}
	
	/**
	 * Returns the width of the screen.
	 * @return the width of the screen
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height of the screen.
	 * @return the height of the screen
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the title of the window.
	 * @return the title of the window
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Returns the pointer to the window instance.
	 * @return the pointer to the window instance
	 */
	public long getPointer() {
		return pointer;
	}
	
	/**
	 * Returns the aspect ratio of the window.
	 * @return the aspect ratio of the window
	 */
	public double getAspectRatio() {
		return (double) width / (double) height;
	}
	
}
