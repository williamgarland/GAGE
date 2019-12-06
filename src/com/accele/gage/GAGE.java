package com.accele.gage;

import java.util.ArrayDeque;
import java.util.Deque;
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
import com.accele.gage.gfx.Font;
import com.accele.gage.gfx.Graphics;
import com.accele.gage.gfx.Model;
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
	private static int[] customWindowHints;
	
	private Window window;
	private Graphics graphics;
	private GameConfiguration config;
	private ControlHandler controlHandler;
	private EntityHandler entityHandler;
	private SoundHandler soundHandler;
	private Random rand;
	private Logger logger;
	private Registry<GameState> stateRegistry;
	private Registry<Texture> textureRegistry;
	private Registry<Configuration> configurationRegistry;
	private Registry<Font> fontRegistry;
	private Registry<TileMap> tileMapRegistry;
	private Registry<ControlListener> controlListenerRegistry;
	private Registry<KeyListener> keyListenerRegistry;
	private Registry<MouseListener> mouseListenerRegistry;
	private Registry<Shader> shaderRegistry;
	private Registry<Model> modelRegistry;
	private Registry<Animation> animationRegistry;
	private Registry<SoundBuffer> soundBufferRegistry;
	private Registry<SoundSource> soundSourceRegistry;
	private GameState currentState;
	private boolean running;
	private Deque<Consumer<GAGE>> deferredEvents;
	double ticksPerSecond;
	
	private GAGE(int width, int height, String title) {
		performChecks();
		
		System.gc();
		
		this.window = new Window(width, height, title, customWindowHints);
		this.soundHandler = new SoundHandler();
		this.modelRegistry = new Registry<>();
		this.fontRegistry = new Registry<>();
		this.graphics = new Graphics(modelRegistry, fontRegistry);
		this.config = new GameConfiguration();
		this.stateRegistry = new Registry<>();
		this.textureRegistry = new Registry<>();
		this.configurationRegistry = new Registry<>();
		this.tileMapRegistry = new Registry<>();
		this.controlListenerRegistry = new Registry<>();
		this.keyListenerRegistry = new Registry<>();
		this.mouseListenerRegistry = new Registry<>();
		this.shaderRegistry = new Registry<>();
		this.animationRegistry = new Registry<>();
		this.soundBufferRegistry = new Registry<>();
		this.soundSourceRegistry = new Registry<>();
		this.controlHandler = new ControlHandler(controlListenerRegistry, keyListenerRegistry, mouseListenerRegistry, window.getPointer());
		this.entityHandler = new EntityHandler(config);
		this.deferredEvents = new ArrayDeque<>();
		this.rand = new Random();
		this.logger = new Logger(System.out, "[" + this.getClass().getCanonicalName() + "]");
	}
	
	private void performChecks() {
		if (Thread.currentThread().isDaemon())
			throw new RuntimeException("Cannot run GAGE on a daemon thread.");
	}
	
	private void performStartChecks() {
		if (currentState == null)
			throw new RuntimeException("There is no GameState loaded in the current context.");
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
		
		while (running) {
			int loops = 0;
			
			while (getGameTime() > nextTick && loops < maxFrameskip) {
				controlHandler.tick();
				currentState.tick();
				
				nextTick += skipTicks;
				loops++;
			}
			
			double interpolation = (getGameTime() + skipTicks - nextTick) / skipTicks;
			
			window.onCycleBegin();
			currentState.render(graphics, interpolation);
			window.onCycleEnd();
			
			while (!deferredEvents.isEmpty())
				deferredEvents.pop().accept(this);
			
			if (getGameTime() - prev >= 1) {
				config.setFps((int) (frames / (getGameTime() - prev)));
				frames = 0;
				prev = getGameTime();
			} else
				frames++;
		}
		
		clean();
	}
	
	private void clean() {
		currentState.exit();
		graphics.clean();
		controlHandler.clean();
		tileMapRegistry.getEntries().forEach(e -> e.clean());
		stateRegistry.getEntries().forEach(e -> e.clean());
		textureRegistry.getEntries().forEach(e -> e.clean());
		fontRegistry.getEntries().forEach(e -> e.clean());
		shaderRegistry.getEntries().forEach(e -> e.clean());
		window.clean();
		soundHandler.clean();
		
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
	}
	
	/**
	 * Forces the engine to stop. This method will immediately terminate the engine and the running JVM instance and attempt to release any held system resources.
	 * 
	 * <p>
	 * This method should only be used as a last resort; Whatever frame the engine was currently on will not be allowed to finish execution. To stop the engine under
	 * normal conditions, use {@link #stop()}.
	 * </p>
	 */
	public void forceQuit() {
		clean();
		System.exit(1);
	}
	
	/**
	 * Returns the instance of {@link com.accele.gage.gfx.Window Window} used by GAGE.
	 * 
	 * @return	the window used by the running instance of the engine
	 */
	public Window getWindow() {
		return window;
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
	 * Returns the instance of {@link com.accele.gage.control.ControlHandler ControlHandler} used by GAGE.
	 * 
	 * @return	the control handler used by the running instance of the engine
	 */
	public ControlHandler getControlHandler() {
		return controlHandler;
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
	 * for registering instances of {@link com.accele.gage.state.GameState GameState}.
	 * 
	 * @return	the state registry used by the running instance of the engine
	 */
	public Registry<GameState> getStateRegistry() {
		return stateRegistry;
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
	 * for registering instances of {@link com.accele.gage.control.ControlListener ControlListener}.
	 * 
	 * @return	the control listener registry used by the running instance of the engine
	 */
	public Registry<ControlListener> getControlListenerRegistry() {
		return controlListenerRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.control.KeyListener KeyListener}.
	 * 
	 * @return	the key listener registry used by the running instance of the engine
	 */
	public Registry<KeyListener> getKeyListenerRegistry() {
		return keyListenerRegistry;
	}
	
	/**
	 * Returns the {@link com.accele.gage.Registry Registry} used by GAGE 
	 * for registering instances of {@link com.accele.gage.control.MouseListener MouseListener}.
	 * 
	 * @return	the mouse listener registry used by the running instance of the engine
	 */
	public Registry<MouseListener> getMouseListenerRegistry() {
		return mouseListenerRegistry;
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
	
	/**
	 * Adds the specified event to the event queue. All events in the event queue will run in an arbitrary order at the end of the next frame.
	 * 
	 * @param event	the event to add to the event queue
	 */
	public void deferEvent(Consumer<GAGE> event) {
		deferredEvents.push(event);
	}
	
	/**
	 * Returns the current state used by GAGE. If no state is set, this method will throw an {@link java.lang.IllegalStateException IllegalStateException}.
	 * 
	 * @return the instance of {@link com.accele.gage.state.GameState GameState} used by the running instance of the engine
	 * @throws IllegalStateException	if no instance of {@code GameState} is loaded in the current context
	 */
	public GameState getCurrentState() {
		if (currentState == null)
			throw new IllegalStateException("There is no GameState loaded in the current context.");
		return currentState;
	}
	
	/**
	 * Sets the state to be used by the engine. If the specified state is not registered in the state registry, 
	 * this method will throw an {@link java.lang.IllegalArgumentException IllegalArgumentException}.
	 * 
	 * @param registryId	the registry id of the {@link com.accele.gage.state.GameState GameState} to use
	 * @param exitOld	whether the {@link com.accele.gage.state.GameState#exit exit()} method of the currently set {@code GameState} should be called.
	 * If no state is currently loaded, setting this to {@code true} will have no effect.
	 * @param initNew	whether the {@link com.accele.gage.state.GameState#init init()} method of the new {@code GameState} should be called
	 * 
	 * @throws IllegalArgumentException		if the specified {@code GameState} is not registered in the state registry
	 */
	public void setCurrentState(String registryId, boolean exitOld, boolean initNew) {
		if (currentState != null && exitOld)
			currentState.exit();
		GameState newState = stateRegistry.getEntry(registryId);
		if (initNew)
			newState.init();
		currentState = newState;
	}
	
	/**
	 * Sets the state to be used by the engine. If the specified state is not registered in the state registry, 
	 * this method will throw an {@link java.lang.IllegalArgumentException IllegalArgumentException}.
	 * This method will call the {@link com.accele.gage.state.GameState#exit exit()} method of the currently set {@code GameState}
	 * (if applicable) and the {@link com.accele.gage.state.GameState#init init()} method of the new {@code GameState}.
	 * 
	 * @param registryId	the registry id of the {@link com.accele.gage.state.GameState GameState} to use
	 * 
	 * @throws IllegalArgumentException		if the specified {@code GameState} is not registered in the state registry
	 */
	public void setCurrentState(String registryId) {
		setCurrentState(registryId, true, true);
	}
	
	/**
	 * Initializes the engine using the specified screen width, screen height, and title.
	 * This method must be called before using any other method in the engine and may only
	 * be called once per instance of the engine.
	 * 
	 * @param screenWidth	the width of the viewable screen area in pixels
	 * @param screenHeight	the height of the viewable screen area in pixels
	 * @param title			the title of the window
	 * 
	 * @throws RuntimeException if GAGE was already initialized or if the engine failed any necessary startup checks
	 */
	public static void init(int screenWidth, int screenHeight, String title) {
		if (instance != null)
			throw new RuntimeException("GAGE is already initialized.");
		instance = new GAGE(screenWidth, screenHeight, title);
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
	 * Sets custom window hints to use when initializing the {@link com.accele.gage.gfx.Window Window}.
	 * <p>
	 * GAGE uses the programmable pipeline version of the LWJGL API, and as a result also uses the GLFW API as the windowing system, 
	 * so all available window hints can be found in the {@link org.lwjgl.glfw.GLFW GLFW} class.
	 * By default, GAGE uses a predefined set of window hints upon initializing the {@code Window} (see the {@link com.accele.gage.gfx.Window Window} class for details).
	 * Calling this method will override these presets and instead use the specified custom hints.
	 * This method must be called prior to calling the {@link #init(int, int, String)} method; calling it after GAGE has already been initialized will have no effect.
	 * </p>
	 * <p>
	 * Note that this method expects hints to be provided in key-value order; the key for a given window hint is immediately followed by its matching value, 
	 * which then is followed by subsequent key-value pairs.
	 * </p>
	 * <p>Example:</p>
	 * <pre>
	 * setCustomWindowHints(new int[] { GLFW_SAMPLES, 4, GLFW_CONTEXT_VERSION_MAJOR, 3, GLFW_CONTEXT_VERSION_MINOR, 2 });
	 * </pre>
	 * @param hints		the array of window hints to use when initializing the {@code Window} in key-value order
	 * @see com.accele.gage.gfx.Window Window
	 * @see org.lwjgl.glfw.GLFW GLFW
	 */
	public static void setCustomWindowHints(int[] hints) {
		if (hints.length % 2 != 0)
			throw new IllegalArgumentException("Invalid number of window hints; hints must be specified in key-value pairs.");
		customWindowHints = hints;
	}
	
	/**
	 * Tells the engine to use the default window hints when initializing the {@link com.accele.gage.gfx.Window Window}.
	 * <p>
	 * GAGE uses a predefined set of window hints by default, so calling this method without also having called {@link #setCustomWindowHints(int[])} will have no effect.
	 * Additionally, this method must be called prior to calling the {@link #init(int, int, String)} method; calling it after GAGE has already been initialized will have no effect.
	 * </p>
	 * <p>
	 * For the list of default window hints used by GAGE, see the {@link com.accele.gage.gfx.Window Window} class.
	 * </p>
	 * @see com.accele.gage.gfx.Window Window
	 */
	public static void useDefaultWindowHints() {
		customWindowHints = null;
	}
	
}
