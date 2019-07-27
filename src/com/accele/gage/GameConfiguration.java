package com.accele.gage;

/**
 * The primary settings for the engine.
 * 
 * <p>
 * This class contains engine-wide settings such as the engine version, current FPS, current ticks per second, and others.
 * These settings can be read from externally using a {@link com.accele.gage.config.GameConfigurationReader GameConfigurationReader}
 * and written to externally using a {@link com.accele.gage.config.GameConfigurationWriter GameConfigurationWriter}.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameConfiguration {

	private static final double DEFAULT_TICKS_PER_SECOND = 25.0;
	
	private final String version = "1.0.3";
	private int fps;
	double ticksPerSecond;
	
	GameConfiguration() {
		this.ticksPerSecond = DEFAULT_TICKS_PER_SECOND;
	}
	
	/**
	 * Returns the current version of the engine.
	 * <p>
	 * The version number is split into three parts: major, minor, and revision.
	 * </p>
	 * <ol>
	 * <li>Major updates are changes to the API that do not maintain backwards compatibility.
	 * Deprecated features will be removed.</li>
	 * <li>Minor updates are changes to the API that also maintain backwards compatibility.
	 * Previous features that should not be used will be marked as deprecated.</li>
	 * <li>Revision updates are bug fixes only; they do not indicate any critical API changes.</li>
	 * </ol>
	 * 
	 * @return the current version of the engine
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Returns the number of frames per second the engine will run at.
	 * The engine will attempt to render as fast as the implementing system will allow, so this value is determined by the system's capabilities.
	 * 
	 * @return the number of frames per second the engine will run at
	 */
	public int getFps() {
		return fps;
	}
	
	/**
	 * Returns the number of ticks per second the engine will run at. The default value is {@value #DEFAULT_TICKS_PER_SECOND}.
	 * 
	 * @return the number of ticks per second the engine will run at
	 */
	public double getTicksPerSecond() {
		return ticksPerSecond;
	}
	
	/**
	 * Sets the number of ticks per second the game loop will run at.
	 * Calling this method after calling {@link com.accele.gage.GAGE#start start()} will have no effect.
	 * 
	 * @param ticksPerSecond the number of ticks per second the game loop should run at
	 */
	public void setTicksPerSecond(double ticksPerSecond) {
		this.ticksPerSecond = ticksPerSecond;
		GAGE.getInstance().ticksPerSecond = ticksPerSecond;
	}
	
	void setFps(int fps) {
		this.fps = fps;
	}
	
}
