package com.accele.gage.gfx;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;

public class Window implements Cleanable {

	private int width;
	private int height;
	private String title;
	private long pointer;
	
	public Window(int width, int height, String title) {
		this.width = width;
		this.height = height;
		this.title = title;
		
		if (!GLFW.glfwInit())
			throw new RuntimeException("Failed to initialize GLFW.");
			
		GLFW.glfwWindowHint(GLFW.GLFW_SAMPLES, 4);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
			
		this.pointer = GLFW.glfwCreateWindow(width, height, title, 0, 0);
			
		if (pointer == 0) {
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create GLFW window.");
		}
			
		GLFW.glfwMakeContextCurrent(pointer);
		GL.createCapabilities();
			
		GLFW.glfwSetWindowCloseCallback(pointer, window -> close());
	}
	
	public void show() {
		GLFW.glfwShowWindow(pointer);
	}
	
	public void hide() {
		GLFW.glfwHideWindow(pointer);
	}
	
	public void onCycleBegin() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
	
	public void onCycleEnd() {
		GLFW.glfwSwapBuffers(pointer);
		GLFW.glfwPollEvents();
	}
	
	public void close() {
		GAGE.getInstance().stop();
		setClosed(true);
	}
	
	public boolean isClosed() {
		return GLFW.glfwWindowShouldClose(pointer);
	}
	
	private void setClosed(boolean closed) {
		GLFW.glfwSetWindowShouldClose(pointer, closed);
	}
	
	@Override
	public void clean() {
		GLFW.glfwTerminate();
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String getTitle() {
		return title;
	}
	
	public long getPointer() {
		return pointer;
	}
	
}
