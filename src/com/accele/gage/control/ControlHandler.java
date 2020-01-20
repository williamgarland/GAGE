package com.accele.gage.control;

import org.lwjgl.glfw.GLFW;

import com.accele.gage.Cleanable;
import com.accele.gage.Registry;
import com.accele.gage.Tickable;
import com.accele.gage.gfx.Window;

public class ControlHandler implements Tickable, Cleanable {

	protected Registry<ControlListener> controlListenerRegistry;
	protected Registry<KeyListener> keyListenerRegistry;
	protected Registry<MouseListener> mouseListenerRegistry;
	
	private static final int KEY_INACTIVE = 0;
	private static final int KEY_PRESSED = 1;
	private static final int KEY_RELEASED = 2;
	private static final int KEY_HELD = 3;
	
	private Window window;
	private int[] keys;
	private int modifiers;
	private double mx;
	private double my;
	
	public ControlHandler(Registry<ControlListener> controlListenerRegistry,
			Registry<KeyListener> keyListenerRegistry, Registry<MouseListener> mouseListenerRegistry, Window window) {
		this.window = window;
		this.controlListenerRegistry = controlListenerRegistry;
		this.keyListenerRegistry = keyListenerRegistry;
		this.mouseListenerRegistry = mouseListenerRegistry;
		GLFW.glfwSetKeyCallback(window.getPointer(), (windowPointer, key, scancode, action, mods) -> {
			if (key != -1) {
				keys[key] = action == GLFW.GLFW_RELEASE ? KEY_RELEASED : action == GLFW.GLFW_PRESS ? KEY_PRESSED : keys[key];
				modifiers = mods;
				keyListenerRegistry.getEntries().forEach(kl -> {
					if (kl.canReceiveEvents() && action == GLFW.GLFW_PRESS)
						kl.keyPressed(new KeyEvent(window, key, GLFW.glfwGetKeyScancode(key), modifiers));
					else if (kl.canReceiveEvents() && action == GLFW.GLFW_RELEASE)
						kl.keyReleased(new KeyEvent(window, key, GLFW.glfwGetKeyScancode(key), modifiers));
				});
			}
		});
		GLFW.glfwSetMouseButtonCallback(window.getPointer(), (windowPointer, button, action, mods) -> {
			MouseEvent event = new MouseEvent(window, button, mods, mx, my);
			if (action == GLFW.GLFW_PRESS)
				mouseListenerRegistry.getEntries().forEach(ml -> { if (ml.canReceiveEvents()) ml.mouseButtonPressed(event); });
			else if (action == GLFW.GLFW_RELEASE)
				mouseListenerRegistry.getEntries().forEach(ml -> { if (ml.canReceiveEvents()) ml.mouseButtonReleased(event); });
		});
		GLFW.glfwSetCursorPosCallback(window.getPointer(), (windowPointer, x, y) -> {
			mx = convertRange(x / window.getWidth(), 0, 1, -1, 1);
			my = convertRange(y / window.getHeight(), 0, 1, -1, 1) * -1;
			MouseMoveEvent event = new MouseMoveEvent(window, mx, my);
			mouseListenerRegistry.getEntries().forEach(ml -> { if (ml.canReceiveEvents()) ml.mouseMoved(event); });
		});
		GLFW.glfwSetScrollCallback(window.getPointer(), (windowPointer, x, y) -> {
			MouseWheelEvent event = new MouseWheelEvent(window, x, y);
			mouseListenerRegistry.getEntries().forEach(ml -> { if (ml.canReceiveEvents()) ml.mouseWheelMoved(event); });
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
				//final int key = i;
				//keyListenerRegistry.getEntries().forEach(kl -> { if (kl.canReceiveEvents()) kl.keyPressed(new KeyEvent(key, GLFW.glfwGetKeyScancode(key), modifiers)); });
				keys[i] = KEY_HELD;
			} else if (keys[i] == KEY_RELEASED) {
				//final int key = i;
				//keyListenerRegistry.getEntries().forEach(kl -> { if (kl.canReceiveEvents()) kl.keyReleased(new KeyEvent(key, GLFW.glfwGetKeyScancode(key), modifiers)); });
				keys[i] = KEY_INACTIVE;
			} else if (keys[i] == KEY_HELD) {
				final int key = i;
				keyListenerRegistry.getEntries().forEach(kl -> { if (kl.canReceiveEvents()) kl.keyHeld(new KeyEvent(window, key, GLFW.glfwGetKeyScancode(key), modifiers)); });
			}
		}
	}

	@Override
	public void clean() {
		
	}
	
}
