package com.accele.gage.state;

import com.accele.gage.Cleanable;
import com.accele.gage.Indexable;
import com.accele.gage.Renderable;
import com.accele.gage.Tickable;

/**
 * The parent class for all states in the engine.
 * 
 * <p>
 * GAGE is a state-based engine, so all actions are performed in different instances of the {@code GameState} class.
 * States are intended to be used for different parts of the target application. For instance, the main menu of an application would require one state and
 * the gameplay would require another state.
 * </p>
 * 
 * <p>
 * States must be registered before they can be used via {@link com.accele.gage.GAGE#getStateRegistry() getStateRegistry()}. Once registered, 
 * they can be set using any variation of {@link com.accele.gage.GAGE#setCurrentState(String, boolean, boolean) setCurrentState()}. 
 * GAGE requires an instance of {@code GameState} to be set as the current state of the engine. If a state is not set before starting the engine, GAGE
 * will throw an exception, saying that there is no GameState loaded in the current context.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class GameState implements Indexable, Tickable, Renderable, Cleanable {

	/**
	 * The registry ID to use in the {@link com.accele.gage.Registry Registry}.
	 * This ID must be unique within the state registry and should not be modified after initialization.
	 */
	protected final String registryId;
	
	/**
	 * Constructs a new {@code GameState} with the specified registry ID.
	 * 
	 * @param registryId the registry ID to use in the {@code Registry}
	 */
	public GameState(String registryId) {
		this.registryId = registryId;
	}
	
	/**
	 * Called when setting the current state to initialize this {@code GameState}.
	 * <p>
	 * If this {@code GameState} is the new state that is being set, 
	 * this method will only be called if either {@link com.accele.gage.GAGE#setCurrentState(String) setCurrentState(String)} was called
	 * or {@link com.accele.gage.GAGE#setCurrentState(String, boolean, boolean) setCurrentState(String, boolean, boolean)} was called with {@code initNew} set to {@code true}.
	 * </p>
	 */
	public abstract void init();
	
	/**
	 * Called when setting the current state to finalize this {@code GameState}.
	 * <p>
	 * If this {@code GameState} was already set as the current state, this method will only be called 
	 * if either {@link com.accele.gage.GAGE#setCurrentState(String) setCurrentState(String)} was called
	 * or {@link com.accele.gage.GAGE#setCurrentState(String, boolean, boolean) setCurrentState(String, boolean, boolean)} was called with {@code exitOld} set to {@code true}.
	 * </p>
	 * <p>
	 * Note that {@code newState} can be {@code null}, especially during the cleanup phase of engine termination.
	 * </p>
	 * @param newState the new {@code GameState} that will be set to the current state
	 */
	public abstract void exit(GameState newState);
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
}
