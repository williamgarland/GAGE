package com.accele.gage;

import com.accele.gage.control.ControlHandler;
import com.accele.gage.control.ControlListener;
import com.accele.gage.control.KeyListener;
import com.accele.gage.control.MouseListener;
import com.accele.gage.gfx.Graphics;
import com.accele.gage.gfx.Window;
import com.accele.gage.state.GameState;

public class GAGEContext implements Indexable, Tickable, Renderable, Cleanable {

	private String registryId;
	private Window window;
	private ControlHandler controlHandler;
	private Registry<ControlListener> controlListenerRegistry;
	private Registry<KeyListener> keyListenerRegistry;
	private Registry<MouseListener> mouseListenerRegistry;
	private Registry<GameState> stateRegistry;
	protected GameState currentState;
	
	public GAGEContext(String registryId, int screenWidth, int screenHeight, String title) {
		this(registryId, screenWidth, screenHeight, title, null);
	}
	
	public GAGEContext(String registryId, int screenWidth, int screenHeight, String title, int[] windowHints) {
		this.registryId = registryId;
		this.window = new Window(screenWidth, screenHeight, title, windowHints, GAGE.getInstance().getMainContext().window);
		this.controlListenerRegistry = new Registry<>();
		this.keyListenerRegistry = new Registry<>();
		this.mouseListenerRegistry = new Registry<>();
		this.stateRegistry = new Registry<>();
		this.controlHandler = new ControlHandler(controlListenerRegistry, keyListenerRegistry, mouseListenerRegistry, window);
		
		window.detachContext();
		GAGE.getInstance().getMainContext().window.attachContext();
	}
	
	GAGEContext(int screenWidth, int screenHeight, String title, int[] windowHints, boolean share) {
		this.registryId = "gage.main";
		this.window = new Window(screenWidth, screenHeight, title, windowHints, null);
		this.controlListenerRegistry = new Registry<>();
		this.keyListenerRegistry = new Registry<>();
		this.mouseListenerRegistry = new Registry<>();
		this.stateRegistry = new Registry<>();
		this.controlHandler = new ControlHandler(controlListenerRegistry, keyListenerRegistry, mouseListenerRegistry, window);
	}
	
	@Override
	public void tick() {
		controlHandler.tick();
		currentState.tick();
	}
	
	@Override
	public void render(Graphics g, double interpolation) {
		window.onCycleBegin();
		currentState.render(g, interpolation);
		window.onCycleEnd();
	}
	
	@Override
	public void clean() {
		currentState.exit(null);
		stateRegistry.clean();
		controlListenerRegistry.clean();
		keyListenerRegistry.clean();
		mouseListenerRegistry.clean();
		controlHandler.clean();
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	public ControlHandler getControlHandler() {
		return controlHandler;
	}
	
	public Registry<ControlListener> getControlListenerRegistry() {
		return controlListenerRegistry;
	}
	
	public Registry<KeyListener> getKeyListenerRegistry() {
		return keyListenerRegistry;
	}
	
	public Registry<MouseListener> getMouseListenerRegistry() {
		return mouseListenerRegistry;
	}
	
	public Registry<GameState> getStateRegistry() {
		return stateRegistry;
	}
	
	public Window getWindow() {
		return window;
	}
	
	public GameState getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(String registryId) {
		setCurrentState(registryId, true, true);
	}
	
	public void setCurrentState(String registryId, boolean exitOld, boolean initNew) {
		GameState newState = stateRegistry.getEntry(registryId);
		if (currentState != null && exitOld)
			currentState.exit(newState);
		if (initNew)
			newState.init(currentState);
		currentState = newState;
	}
	
}
