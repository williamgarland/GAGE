package com.accele.gage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

import org.lwjgl.glfw.GLFW;

import com.accele.gage.config.Configuration;
import com.accele.gage.control.ControlHandler;
import com.accele.gage.control.ControlListener;
import com.accele.gage.control.KeyListener;
import com.accele.gage.control.MouseListener;
import com.accele.gage.entity.EntityHandler;
import com.accele.gage.gfx.Animation;
import com.accele.gage.gfx.BatchedRenderer;
import com.accele.gage.gfx.Font;
import com.accele.gage.gfx.Graphics;
import com.accele.gage.gfx.ImmediateRenderer;
import com.accele.gage.gfx.Model;
import com.accele.gage.gfx.RenderingMode;
import com.accele.gage.gfx.Shader;
import com.accele.gage.gfx.Texture;
import com.accele.gage.gfx.Window;
import com.accele.gage.log.Logger;
import com.accele.gage.sfx.SoundBuffer;
import com.accele.gage.sfx.SoundHandler;
import com.accele.gage.sfx.SoundSource;
import com.accele.gage.state.GameState;
import com.accele.gage.tile.TileMap;

/**
 * The primary class for interfacing with the engine.
 * 
 * All operations relating to GAGE must ultimately be done through this class. 
 * Only one instance of this class may be present in any given instance of the JVM,
 * and it must be created on the main thread. GAGE may spawn
 * additional threads at any given point during its runtime to offload different processes.
 * 
 * <p>
 * Before interacting with any part of GAGE, it must first be initialized using the {@link #init(int, int, String)} method.
 * Once initialized, the GAGE instance can be accessed using {@link #getInstance()}. After initializing the engine
 * and any additional resources, the engine can be started using the {@link #start()} method. For example:
 * </p>
 * <pre>
 * GAGE.init(640, 480, "My Game");
 * GAGE.getInstance().getStateRegistry().register(new ExampleState());
 * GAGE.getInstance().setCurrentState("example_state");
 * GAGE.getInstance().start();
 * </pre>
 * 
 * <p>
 * Because GAGE is a state-based engine, it requires an implementation of the {@link com.accele.gage.state.GameState GameState} class.
 * This implementation must be registered in the state registry using {@link #getStateRegistry()} and then set using {@link #setCurrentState(String)}.
 * Without calling these two methods first, calling {@code start()} will throw an error.
 * </p>
 * 
 * <p>
 * To stop the engine at any point after calling {@code start()}, either {@link #stop()} or {@link #forceQuit()} may be used, although {@code stop()}
 * is preferable. <b>Do not call {@code System.exit(n)} or {@code Runtime.getRuntime().halt(n)} as there are necessary shutdown hooks GAGE must run
 * before terminating. These hooks are not registered in the JVM's shutdown hook registry, so they must run on their own before terminating the program.</b>
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class GAGE {
	
	private static GAGE instance;
	
	private Graphics graphics;
	private RenderingMode renderingMode;
	private Map<RenderingMode, Graphics> renderers;
	private GameConfiguration config;
	private EntityHandler entityHandler;
	private SoundHandler soundHandler;
	private Random rand;
	private Logger logger;
	private Registry<Texture> textureRegistry;
	private Registry<Configuration> configurationRegistry;
	private Registry<Font> fontRegistry;
	private Registry<TileMap> tileMapRegistry;
	private Registry<Shader> shaderRegistry;
	private Registry<Model> modelRegistry;
	private Registry<Animation> animationRegistry;
	private Registry<SoundBuffer> soundBufferRegistry;
	private Registry<SoundSource> soundSourceRegistry;
	private Registry<GAGEContext> contextRegistry;
	private boolean running;
	double ticksPerSecond;
	
	private GAGEContext mainContext;
	private GAGEContext currentContext;
	
	private GAGE(int width, int height, String title, InitEnvironment initEnvironment) {
		this.logger = new Logger(this, initEnvironment.getLoggerDestination(), initEnvironment.getLoggerErrorDestination(), 
				initEnvironment.getLoggerPrefix(), initEnvironment.getLoggerDateFormat());
		
		performChecks();
		
		logger.info("Initializing GAGE with dimensions " + width + "x" + height + " and title \"" + title + "\"");
		
		System.gc();
		
		GAGEContext mainContext = new GAGEContext(width, height, title, initEnvironment.getCustomWindowHints(), false);
		
		this.soundHandler = new SoundHandler();
		this.modelRegistry = new Registry<>();
		this.fontRegistry = new Registry<>();
		this.shaderRegistry = new Registry<>();
		this.renderers = new HashMap<>();
		renderers.put(RenderingMode.IMMEDIATE, new ImmediateRenderer(modelRegistry, fontRegistry, shaderRegistry));
		renderers.put(RenderingMode.BATCHED, new BatchedRenderer(initEnvironment.getDrawBatchSize(), shaderRegistry, fontRegistry));
		this.renderingMode = RenderingMode.BATCHED;
		this.graphics = renderers.get(renderingMode);
		this.config = new GameConfiguration();
		this.textureRegistry = new Registry<>();
		this.configurationRegistry = new Registry<>();
		this.tileMapRegistry = new Registry<>();
		this.animationRegistry = new Registry<>();
		this.soundBufferRegistry = new Registry<>();
		this.soundSourceRegistry = new Registry<>();
		this.contextRegistry = new Registry<>();
		this.entityHandler = new EntityHandler(config);
		this.rand = new Random();
		
		contextRegistry.register(mainContext);
		this.mainContext = mainContext;
		this.currentContext = mainContext;
		
		logger.info("GAGE initialized");
	}
	
	private void performChecks() {
		logger.info("", "Performing initialization checks...", false);
		if (Thread.currentThread().isDaemon())
			throw new RuntimeException("Cannot run GAGE on a daemon thread.");
		logger.log("OK");
	}
	
	private void performStartChecks() {
		logger.info("", "Performing engine startup checks...", false);
		if (mainContext.getCurrentState() == null)
			throw new RuntimeException("There is no GameState loaded in the current context.");
		logger.log("OK");
	}
	
	/**
	 * Starts the engine. This method will perform any necessary checks (such as checking for a loaded {@link com.accele.gage.state.GameState GameState})
	 * before actually running the primary game loop. Once this method is called, normal execution will not return to the caller until {@link #stop()} is called.
	 * 
	 * <p>
	 * Because GAGE is a state-based engine, it requires an implementation of the {@code GameState} class.
	 * This implementation must be registered in the state registry using {@link #getStateRegistry()} and then set using {@link #setCurrentState(String)}.
	 * Without calling these two methods first, calling {@code start()} will throw an error.
	 * </p>
	 * 
	 * @throws RuntimeException	if no instance of {@code GameState} is loaded in the current context
	 */
	public void start() {
		performStartChecks();
		logger.info("Starting engine");
		running = true;
		run();
	}
	
	private void run() {
		GLFW.glfwSetTime(0);
		
		ticksPerSecond = config.ticksPerSecond;
		
		final double skipTicks = 1.0 / ticksPerSecond;
		final double maxFrameskip = 5;
		
		double nextTick = getGameTime();
		
		double prev = getGameTime();
		int frames = 0;
		
		try {
			while (running) {
				int loops = 0;
				
				while (getGameTime() > nextTick && loops < maxFrameskip) {
					currentContext.tick();
					
					nextTick += skipTicks;
					loops++;
				}
				
				double interpolation = (getGameTime() + skipTicks - nextTick) / skipTicks;
				
				currentContext.render(graphics, interpolation);
				currentContext.getWindow().pollEvents();
				
				soundSourceRegistry.getEntries().forEach(e -> e.tick());
				
				contextRegistry.getEntries().forEach(ctx -> {
					if (ctx.doBackgroundRendering())
						hotSwapContext(ctx.getRegistryId(), gage -> ctx.render(graphics, interpolation));
					ctx.fireEvents();
				});
				
				if (getGameTime() - prev >= 1) {
					config.setFps((int) (frames / (getGameTime() - prev)));
					frames = 0;
					prev = getGameTime();
				} else
					frames++;
			}
		} finally {
			clean();			
		}
	}
	
	private void clean() {
		contextRegistry.clean();
		entityHandler.clean();
		graphics.clean();
		animationRegistry.clean();
		configurationRegistry.clean();
		fontRegistry.clean();
		modelRegistry.clean();
		shaderRegistry.clean();
		soundBufferRegistry.clean();
		soundSourceRegistry.clean();
		textureRegistry.clean();
		tileMapRegistry.clean();
		soundHandler.clean();
		mainContext.getWindow().clean();
		
		instance = null;
		System.gc();
	}
	
	/**
	 * Stops the engine. This method will allow the engine to terminate normally and is the preferred method to stop the engine.
	 * Calling this method will cause all system resources held by the engine to be released, 
	 * so starting another instance of the engine in the same JVM instance must be done in the exact same way as it was initially created.
	 * 
	 * <p>
	 * Note that although this method will stop the engine, it will not terminate immediately; the engine will begin its shutdown process at the end of the current game loop cycle.
	 * Once all necessary shutdown hooks have run, execution will return to the caller of the {@code start()} method. To stop the engine immediately, use {@link #forceQuit()}.
	 * </p>
	 */
	public void stop() {
		running = false;
		logger.info("Stopping engine");
	}
	
	/**
	 * Forces the engine to stop. This method will immediately terminate the engine and the running JVM instance and attempt to release any held system resources.
	 * 
	 * <p>
	 * This method should only be used as a last resort; Whatever frame the engine was currently on will not be allowed to finish execution. To stop the engine under
	 * normal conditions, use {@link #stop()}.
	 * </p>
	 * <p>
	 * This method will always cause the JVM to terminate with a non-zero exit code.
	 * </p>
	 */
	public void forceQuit() {
		logger.fatal("Force quitting engine");
		clean();
		System.exit(1);
	}
	
	/**
	 * Returns the time in seconds since the engine was started.
	 * 
	 * <p>
	 * The resolution of the timer implementation is system-dependent, but is usually
	 * accurate to the microsecond or the nanosecond.
	 * </p>
	 * 
	 * @return	the time, in seconds, since the engine was started
	 */
	public double getGameTime() {
		return GLFW.glfwGetTime();
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.GameConfiguration GameConfiguration} used by GAGE.
	 * 
	 * @return	the game configuration used by the running instance of the engine
	 */
	public GameConfiguration getConfig() {
		return config;
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.entity.EntityHandler EntityHandler} used by GAGE.
	 * 
	 * @return	the entity handler used by the running instance of the engine
	 */
	public EntityHandler getEntityHandler() {
		return entityHandler;
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.sfx.SoundHandler SoundHandler} used by GAGE.
	 * 
	 * @return	the sound handler used by the running instance of the engine
	 */
	public SoundHandler getSoundHandler() {
		return soundHandler;
	}
	
	/**
	 * Returns the instance of {@link java.util.Random Random} used by GAGE.
	 * 
	 * <p>
	 * GAGE uses the default implementation of {@code Random}, which employs use of the current system time
	 * to generate a unique seed.
	 * </p>
	 * 
	 * @return	the instance of {@code Random} used by the running instance of the engine
	 */
	public Random getRand() {
		return rand;
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.log.Logger Logger} used by GAGE.
	 * 
	 * @return	the logger used by the running instance of the engine
	 */
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @return	the texture registry used by the running instance of the engine
	 */
	public Registry<Texture> getTextureRegistry() {
		return textureRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.config.Configuration Configuration}.
	 * 
	 * @return	the configuration registry used by the running instance of the engine
	 */
	public Registry<Configuration> getConfigurationRegistry() {
		return configurationRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.gfx.Font Font}.
	 * 
	 * @return	the font registry used by the running instance of the engine
	 */
	public Registry<Font> getFontRegistry() {
		return fontRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.tile.TileMap TileMap}.
	 * 
	 * @return	the tile map registry used by the running instance of the engine
	 */
	public Registry<TileMap> getTileMapRegistry() {
		return tileMapRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.gfx.Shader Shader}.
	 * 
	 * @return	the shader registry used by the running instance of the engine
	 */
	public Registry<Shader> getShaderRegistry() {
		return shaderRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.gfx.Model Model}.
	 * 
	 * @return	the model registry used by the running instance of the engine
	 */
	public Registry<Model> getModelRegistry() {
		return modelRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.gfx.Animation Animation}.
	 * 
	 * @return	the animation registry used by the running instance of the engine
	 */
	public Registry<Animation> getAnimationRegistry() {
		return animationRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.sfx.SoundBuffer SoundBuffer}.
	 * 
	 * @return	the sound buffer registry used by the running instance of the engine
	 */
	public Registry<SoundBuffer> getSoundBufferRegistry() {
		return soundBufferRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.sfx.SoundSource SoundSource}.
	 * 
	 * @return	the sound source registry used by the running instance of the engine
	 */
	public Registry<SoundSource> getSoundSourceRegistry() {
		return soundSourceRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.GAGEContext GAGEContext}.
	 * 
	 * @return	the context registry used by the running instance of the engine
	 */
	public Registry<GAGEContext> getContextRegistry() {
		return contextRegistry;
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.gfx.Graphics Graphics} used by GAGE.
	 * <p>
	 * This method is intended to yield the {@code Graphics} instance for retrieving and modifying graphical settings only; 
	 * at no point should the draw functions be called outside of the {@link com.accele.gage.Renderable#render(Graphics, double) render(Graphics, double)} method.
	 * </p>
	 * 
	 * @return	the {@code Graphics} used by the running instance of the engine
	 */
	public Graphics getGraphics() {
		return graphics;
	}
	
	public RenderingMode getRenderingMode() {
		return renderingMode;
	}
	
	public Map<RenderingMode, Graphics> getRenderers() {
		return Collections.unmodifiableMap(renderers);
	}
	
	public void setRenderingMode(RenderingMode renderingMode) {
		this.renderingMode = renderingMode;
		this.graphics = renderers.get(renderingMode);
		logger.info("Set rendering mode to " + renderingMode.toString());
	}
	
	/**
	 * Adds the specified event to the event queue of the current context.
	 * All events in the event queue will run in the order in which they were added at the end of the next frame.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @param event	the event to add to the event queue
	 */
	public void deferEvent(Consumer<GAGE> event) {
		currentContext.deferEvent(event);
	}
	
	/**
	 * Returns the primary context used by GAGE.
	 * 
	 * @return the primary context used by GAGE
	 */
	public GAGEContext getMainContext() {
		return mainContext;
	}
	
	/**
	 * Returns the current context used by GAGE.
	 * 
	 * @return the current context used by GAGE
	 */
	public GAGEContext getCurrentContext() {
		return currentContext;
	}
	
	/**
	 * Sets the current context to the context with the specified {@code registryId}.
	 * <p>
	 * When switching contexts, this method will first detach the previous context by calling {@link com.accele.gage.gfx.Window#detachContext() detachContext()} 
	 * and then attach the new one by calling {@link com.accele.gage.gfx.Window#attachContext() attachContext()}. This is required by GLFW and OpenGL
	 * because only one context may be set at any one time.
	 * </p>
	 * 
	 * @param registryId	the registry ID of the target context
	 */
	public void setCurrentContext(String registryId) {
		GAGEContext newContext = contextRegistry.getEntry(registryId);
		currentContext.getWindow().detachContext();
		currentContext = newContext;
		currentContext.getWindow().attachContext();
		logger.info("Set current context to \"" + registryId + "\"");
	}
	
	/**
	 * Returns the {@link com.accele.gage.control.ControlHandler ControlHandler} used by the current context.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the {@code ControlHandler} used by the current context
	 */
	public ControlHandler getControlHandler() {
		return currentContext.getControlHandler();
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by the current context 
	 * for registering instances of {@link com.accele.gage.control.ControlListener ControlListener}.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the control listener registry used by the current context
	 */
	public Registry<ControlListener> getControlListenerRegistry() {
		return currentContext.getControlListenerRegistry();
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by the current context 
	 * for registering instances of {@link com.accele.gage.control.KeyListener KeyListener}.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the key listener registry used by the current context
	 */
	public Registry<KeyListener> getKeyListenerRegistry() {
		return currentContext.getKeyListenerRegistry();
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by the current context 
	 * for registering instances of {@link com.accele.gage.control.MouseListener MouseListener}.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the mouse listener registry used by the current context
	 */
	public Registry<MouseListener> getMouseListenerRegistry() {
		return currentContext.getMouseListenerRegistry();
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by the current context 
	 * for registering instances of {@link com.accele.gage.state.GameState GameState}.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the state registry used by the current context
	 */
	public Registry<GameState> getStateRegistry() {
		return currentContext.getStateRegistry();
	}
	
	/**
	 * Returns the {@link com.accele.gage.gfx.Window Window} used by the current context.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the {@code Window} used by the current context
	 */
	public Window getWindow() {
		return currentContext.getWindow();
	}
	
	/**
	 * Returns the current {@link com.accele.gage.state.GameState GameState} used by the current context.
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @return the current {@code GameState} used by the current context
	 */
	public GameState getCurrentState() {
		return currentContext.getCurrentState();
	}
	
	/**
	 * Sets the state to be used by the current context.
	 * <p>
	 * If the specified state is not registered in the state registry, this method will throw an {@code IllegalArgumentException}.
	 * This method will call the {@link com.accele.gage.state.GameState#exit(GameState) exit(GameState)} method of the currently set {@code GameState} (if applicable) 
	 * and the {@link com.accele.gage.state.GameState#init(GameState) init(GameState)} method of the new GameState.
	 * </p>
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @param registryId	the registry ID of the target state
	 */
	public void setCurrentState(String registryId) {
		currentContext.setCurrentState(registryId, true, true);
	}
	
	/**
	 * Sets the state to be used by the current context.
	 * <p>
	 * If the specified state is not registered in the state registry, this method will throw an {@code IllegalArgumentException}.
	 * </p>
	 * <p>
	 * This is a convenience method for accessing the specified resource contained in the current context.
	 * To directly access this resource, among others, use {@link #getCurrentContext() getCurrentContext()}.
	 * </p>
	 * 
	 * @param registryId	the registry ID of the target state
	 * @param exitOld		whether the {@link com.accele.gage.state.GameState#exit(GameState) exit(GameState)} method of the currently set {@code GameState} should be called. If no state is currently loaded, setting this to true will have no effect.
	 * @param initNew		whether the {@link com.accele.gage.state.GameState#init(GameState) init(GameState)} method of the new {@code GameState} should be called
	 */
	public void setCurrentState(String registryId, boolean exitOld, boolean initNew) {
		currentContext.setCurrentState(registryId, exitOld, initNew);
	}
	
	/**
	 * Sets the current context to be the main context.
	 * <p>
	 * This is a convenience method for setting the main context and is equivalent to calling {@code setCurrentContext("gage.main")}.
	 * </p>
	 */
	public void useMainContext() {
		if (currentContext == mainContext)
			return;
		currentContext.getWindow().detachContext();
		currentContext = mainContext;
		currentContext.getWindow().attachContext();
		
		logger.info("Set current context to \"" + mainContext.getRegistryId() + "\"");
	}
	
	/**
	 * Sets the current context and the state to be used by the specified context.
	 * <p>
	 * If the specified context is not registered in the context registry or
	 * if the specified state is not registered in the context's state registry, this method will throw an {@code IllegalArgumentException}.
	 * </p>
	 * <p>
	 * This is a convenience method for setting the context and changing the state. It is designed to be used when switching between states that are in different contexts.
	 * This method is equivalent to calling:
	 * </p>
	 * <pre>
	 * GAGEContext newContext = GAGE.getInstance().getContextRegistry().getEntry(contextRegistryId);
	 * GameState newState = newContext.getStateRegistry().getEntry(stateRegistryId);
	 * 
	 * if (exitOld)
	 * 	GAGE.getInstance().getCurrentState().exit(newState);
	 * GAGE.getInstance().setCurrentContext(contextRegistryId);
	 * GAGE.getInstance().setCurrentState(stateRegistryId, false, initNew);
	 * </pre>
	 * 
	 * @param contextRegistryId	the registry ID of the target context
	 * @param stateRegistryId	the registry ID of the target state
	 * @param exitOld			whether the {@link com.accele.gage.state.GameState#exit(GameState) exit(GameState)} method of the currently set {@code GameState} should be called. If no state is currently loaded, setting this to true will have no effect.
	 * @param initNew			whether the {@link com.accele.gage.state.GameState#init(GameState) init(GameState)} method of the new {@code GameState} should be called
	 */
	public void setCurrentState(String contextRegistryId, String stateRegistryId, boolean exitOld, boolean initNew) {
		GAGEContext newContext = contextRegistry.getEntry(contextRegistryId);
		GameState newState = newContext.getStateRegistry().getEntry(stateRegistryId);
		if (exitOld)
			currentContext.getCurrentState().exit(newState);
		currentContext.getWindow().detachContext();
		newContext.getWindow().attachContext();
		GameState oldState = currentContext.currentState;
		currentContext = newContext;
		if (initNew)
			newState.init(oldState);
		newContext.currentState = newState;
		logger.info("Set the current context to \"" + newContext.getRegistryId() + "\" and the current state to \"" + newState.getRegistryId() + "\"");
	}
	
	/**
	 * Sets the current context and the state to be used by the specified context.
	 * <p>
	 * If the specified context is not registered in the context registry or
	 * if the specified state is not registered in the context's state registry, this method will throw an {@code IllegalArgumentException}.
	 * </p>
	 * <p>
	 * This is a convenience method for setting the context and changing the state. It is designed to be used when switching between states that are in different contexts.
	 * This method is equivalent to calling:
	 * </p>
	 * <pre>
	 * GAGEContext newContext = GAGE.getInstance().getContextRegistry().getEntry(contextRegistryId);
	 * GameState newState = newContext.getStateRegistry().getEntry(stateRegistryId);
	 * 
	 * GAGE.getInstance().getCurrentState().exit(newState);
	 * GAGE.getInstance().setCurrentContext(contextRegistryId);
	 * GAGE.getInstance().setCurrentState(stateRegistryId, false, true);
	 * </pre>
	 * 
	 * @param contextRegistryId	the registry ID of the target context
	 * @param stateRegistryId	the registry ID of the target state
	 */
	public void setCurrentState(String contextRegistryId, String stateRegistryId) {
		setCurrentState(contextRegistryId, stateRegistryId, true, true);
	}
	
	/**
	 * Changes the current context only to perform the specified action.
	 * <p>
	 * This method will switch the current context to the one with the specified registry ID,
	 * run the specified action, and then immediately switch back to the previous context.
	 * </p>
	 * 
	 * @param contextRegistryId	the registry ID of the target context
	 * @param action			the action to be performed in the specified context
	 */
	public void hotSwapContext(String contextRegistryId, Consumer<GAGE> action) {
		logger.info("Performing hot-swap on context \"" + contextRegistryId + "\"");
		GAGEContext prev = currentContext;
		setCurrentContext(contextRegistryId);
		action.accept(this);
		currentContext.getWindow().detachContext();
		prev.getWindow().attachContext();
		currentContext = prev;
		logger.info("Set current context to \"" + currentContext.getRegistryId() + "\"");
	}
	
	/**
	 * Initializes the engine using the specified screen width, screen height, and title.
	 * This method must be called before using any other method in the engine and may only
	 * be called once per instance of the engine.
	 * <p>
	 * Calling this method will initialize the engine with default initialization parameters via a default implementation of {@code InitEnvironment}.
	 * </p>
	 * 
	 * @param screenWidth	the width of the viewable screen area in pixels
	 * @param screenHeight	the height of the viewable screen area in pixels
	 * @param title			the title of the window
	 * 
	 * @throws RuntimeException if GAGE was already initialized or if the engine failed any necessary startup checks
	 */
	public static void init(int screenWidth, int screenHeight, String title) {
		init(screenWidth, screenHeight, title, new InitEnvironment());
	}
	
	/**
	 * Initializes the engine using the specified screen width, screen height, title, and {@code InitEnvironment}.
	 * This method must be called before using any other method in the engine and may only
	 * be called once per instance of the engine.
	 * 
	 * @param screenWidth		the width of the viewable screen area in pixels
	 * @param screenHeight		the height of the viewable screen area in pixels
	 * @param title				the title of the window
	 * @param initEnvironment	the {@code InitEnvironment} to use when initializing the engine
	 * 
	 * @throws RuntimeException if GAGE was already initialized or if the engine failed any necessary startup checks
	 */
	public static void init(int screenWidth, int screenHeight, String title, InitEnvironment initEnvironment) {
		if (instance != null)
			throw new RuntimeException("GAGE is already initialized.");
		instance = new GAGE(screenWidth, screenHeight, title, initEnvironment);
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.GAGE GAGE} used to interface with the engine. All actions relating to the
	 * engine must be done through the instance returned by this method.
	 * 
	 * @return the instance of {@code GAGE} used to interface with the engine
	 */
	public static GAGE getInstance() {
		if (instance == null)
			throw new RuntimeException("GAGE has not been initialized.");
		return instance;
	}
	
	/**
	 * Returns whether GAGE has already been initialized.
	 * 
	 * @return whether GAGE has already been initialized
	 */
	public static boolean isInitialized() {
		return instance != null;
	}
	
}
