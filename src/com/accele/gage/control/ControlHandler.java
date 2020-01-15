package com.accele.gage.control;

import org.lwjgl.glfw.GLFW;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.Registry;
import com.accele.gage.Tickable;

public class ControlHandler implements Tickable, Cleanable {

	protected Registry<ControlListener> controlListenerRegistry;
	protected Registry<KeyListener> keyListenerRegistry;
	protected Registry<MouseListener> mouseListenerRegistry;
	
	private static final int KEY_INACTIVE = 0;
	private static final int KEY_PRESSED = 1;
	private static final int KEY_RELEASED = 2;
	private static final int KEY_HELD = 3;
	
	private int[] keys;
	private int modifiers;
	private double mx;
	private double my;
	
	public ControlHandler(Registry<ControlListener> controlListenerRegistry,
			Registry<KeyListener> keyListenerRegistry, Registry<MouseListener> mouseListenerRegistry, long windowPointer) {
		this.controlListenerRegistry = controlListenerRegistry;
		this.keyListenerRegistry = keyListenerRegistry;
		this.mouseListenerRegistry = mouseListenerRegistry;
		GLFW.glfwSetKeyCallback(windowPointer, (window, key, scancode, action, mods) -> {
			if (key != -1) {
				keys[key] = action == GLFW.GLFW_RELEASE ? KEY_RELEASED : action == GLFW.GLFW_PRESS ? KEY_PRESSED : keys[key];
				modifiers = mods;
			}
		});
		GLFW.glfwSetMouseButtonCallback(windowPointer, (window, button, action, mods) -> {
			MouseEvent event = new MouseEvent(button, mods, mx, my);
			if (action == GLFW.GLFW_PRESS)
				mouseListenerRegistry.getEntries().forEach(ml -> ml.mouseButtonPressed(event));
			else if (action == GLFW.GLFW_RELEASE)
				mouseListenerRegistry.getEntries().forEach(ml -> ml.mouseButtonReleased(event));
		});
		GLFW.glfwSetCursorPosCallback(windowPointer, (window, x, y) -> {
			mx = convertRange(x / GAGE.getInstance().getWindow().getWidth(), 0, 1, -1, 1);
			my = convertRange(y / GAGE.getInstance().getWindow().getHeight(), 0, 1, -1, 1) * -1;
			MouseMoveEvent event = new MouseMoveEvent(x, y);
			mouseListenerRegistry.getEntries().forEach(ml -> ml.mouseMoved(event));
		});
		GLFW.glfwSetScrollCallback(windowPointer, (window, x, y) -> {
			MouseWheelEvent event = new MouseWheelEvent(x, y);
			mouseListenerRegistry.getEntries().forEach(ml -> ml.mouseWheelMoved(event));
		});
		this.keys = new int[GLFW.GLFW_KEY_LAST];
	}
	
	private static double convertRange(double oldValue, double oldMin, double oldMax, double newMin, double newMax) {
		double oldRange = oldMax - oldMin;
		if (oldRange == 0)
			return newMin;
		else {
			double newRange = newMax - newMin;
			return (((oldValue - oldMin) * newRange) / oldRange) + newMin;
		}
	}

	@Override
	public void tick() {
		for (int i = 0; i < keys.length; i++) {
			if (keys[i] == KEY_PRESSED) {
				final int key = i;
				keyListenerRegistry.getEntries().forEach(kl -> kl.keyPressed(new KeyEvent(key, GLFW.glfwGetKeyScancode(key), modifiers)));
				keys[i] = KEY_HELD;
			} else if (keys[i] == KEY_RELEASED) {
				final int key = i;
				keyListenerRegistry.getEntries().forEach(kl -> kl.keyReleased(new KeyEvent(key, GLFW.glfwGetKeyScancode(key), modifiers)));
				keys[i] = KEY_INACTIVE;
			} else if (keys[i] == KEY_HELD) {
				final int key = i;
				keyListenerRegistry.getEntries().forEach(kl -> kl.keyHeld(new KeyEvent(key, GLFW.glfwGetKeyScancode(key), modifiers)));
			}
		}
	}

	@Override
	public void clean() {
		
	}
	
}
