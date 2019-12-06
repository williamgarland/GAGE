package com.accele.gage;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
	private static final int[] BUILTIN_DEFAULT_TEXTURE_PARAMETERS = new int[] {
			GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE,
			GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE,
			GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR,
			GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR
	};
	private static final int[] BUILTIN_DEFAULT_REPEATING_TEXTURE_PARAMETERS = new int[] {
			GL11.GL_TEXTURE_WRAP_S, GL12.GL_REPEAT,
			GL11.GL_TEXTURE_WRAP_T, GL12.GL_REPEAT,
			GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR,
			GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR
	};
	
	private final String version = "1.1.5";
	private int fps;
	double ticksPerSecond;
	private boolean doEntityCollision;
	private float masterVolume;
	private float masterPitch;
	private boolean masterVolumeMuted;
	private int[] defaultTextureParameters;
	private boolean generateTextureMipmaps;
	
	GameConfiguration() {
		this.ticksPerSecond = DEFAULT_TICKS_PER_SECOND;
		this.doEntityCollision = true;
		this.masterVolume = 1;
		this.masterPitch = 1;
		this.masterVolumeMuted = false;
		this.defaultTextureParameters = BUILTIN_DEFAULT_TEXTURE_PARAMETERS;
		this.generateTextureMipmaps = true;
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
	
	/**
	 * Returns whether the {@link com.accele.gage.entity.EntityHandler EntityHandler} should perform entity collision detection.
	 * 
	 * @return whether the {@code EntityHandler} should perform entity collision detection
	 */
	public boolean doEntityCollision() {
		return doEntityCollision;
	}
	
	/**
	 * Sets whether the {@link com.accele.gage.entity.EntityHandler EntityHandler} should perform entity collision detection.
	 * <p>
	 * Note that this method will not change the value until the end of the current game loop cycle.
	 * </p>
	 * 
	 * @param doEntityCollision whether the {@code EntityHandler} should perform entity collision detection
	 */
	public void setEntityCollision(boolean doEntityCollision) {
		GAGE.getInstance().deferEvent(gage -> this.doEntityCollision = doEntityCollision);
	}
	
	/**
	 * Returns the master volume for all sounds in the engine.
	 * @return the master volume for all sounds in the engine
	 */
	public float getMasterVolume() {
		return masterVolume;
	}
	
	/**
	 * Sets the master volume for all sounds in the engine.
	 * @param masterVolume the value to use for the master volume
	 */
	public void setMasterVolume(float masterVolume) {
		this.masterVolume = masterVolume;
		GAGE.getInstance().getSoundSourceRegistry().getEntries().forEach(c -> {
			if (masterVolume < c.getVolume())
				c.setVolume(masterVolume);
		});
	}
	
	/**
	 * Returns the master pitch for all sounds in the engine.
	 * @return the master pitch for all sounds in the engine
	 */
	public float getMasterPitch() {
		return masterPitch;
	}
	
	/**
	 * Sets the master pitch for all sounds in the engine.
	 * @param masterPitch the pitch to use for the master volume
	 */
	public void setMasterPitch(float masterPitch) {
		this.masterPitch = masterPitch;
		GAGE.getInstance().getSoundSourceRegistry().getEntries().forEach(c -> {
			if (masterPitch < c.getPitch())
				c.setPitch(masterPitch);
		});
	}
	
	/**
	 * Returns whether the master volume is muted for all sounds in the engine.
	 * @return whether the master volume is muted for all sounds in the engine
	 */
	public boolean isMasterVolumeMuted() {
		return masterVolumeMuted;
	}
	
	/**
	 * Sets whether the master volume for all sounds in the engine should be muted.
	 * @param masterVolumeMuted whether the master volume should be muted
	 */
	public void setMasterVolumeMuted(boolean masterVolumeMuted) {
		this.masterVolumeMuted = masterVolumeMuted;
		GAGE.getInstance().getSoundSourceRegistry().getEntries().forEach(c -> c.setVolume(0));
	}
	
	/**
	 * Returns the default texture parameters used for generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * <p>
	 * By default, this method will return the built-in default texture parameters as if {@link #useBuiltinDefaultTextureParameters()} was invoked.
	 * </p>
	 * @return the default texture parameters used for generating new instances of {@code Texture}
	 */
	public int[] getDefaultTextureParameters() {
		return defaultTextureParameters;
	}
	
	/**
	 * Sets the default texture parameters to use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * <p>
	 * Texture parameters should be specified in key-value pair order, where each element is a key followed by its value and then any subsequent key-value pairs proceeding them.
	 * </p>
	 * @param defaultTextureParameters the default texture parameters to use when generating new instances of {@code Texture}
	 */
	public void setDefaultTextureParameters(int[] defaultTextureParameters) {
		this.defaultTextureParameters = defaultTextureParameters;
	}
	
	/**
	 * Returns whether mipmaps should be generated when creating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * @return whether mipmaps should be generated when creating new instances of {@code Texture}
	 */
	public boolean shouldGenerateTextureMipmaps() {
		return generateTextureMipmaps;
	}
	
	/**
	 * Sets whether mipmaps should be generated when creating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * @param generateTextureMipmaps whether mipmaps should be generated when creating new instances of {@code Texture}
	 */
	public void setShouldGenerateTextureMipmaps(boolean generateTextureMipmaps) {
		this.generateTextureMipmaps = generateTextureMipmaps;
	}
	
	/**
	 * Specifies that the engine should use the built-in texture parameters as the 
	 * default texture parameters to use when creating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * <p>
	 * GAGE uses the following default values:
	 * </p>
	 * <table>
	 * <tr>
	 * <th>Parameter</th>
	 * <th>Value</th>
	 * </tr>
	 * <tr><td>GL_TEXTURE_WRAP_S</td><td>GL_CLAMP_TO_EDGE</td></tr>
	 * <tr><td>GL_TEXTURE_WRAP_T</td><td>GL_CLAMP_TO_EDGE</td></tr>
	 * <tr><td>GL_TEXTURE_MAG_FILTER</td><td>GL_LINEAR</td></tr>
	 * <tr><td>GL_TEXTURE_MIN_FILTER</td><td>GL_LINEAR_MIPMAP_LINEAR</td></tr>
	 * </table>
	 */
	public void useBuiltinDefaultTextureParameters() {
		this.defaultTextureParameters = BUILTIN_DEFAULT_TEXTURE_PARAMETERS;
	}
	
	/**
	 * Specifies that the engine should use the built-in repeating texture parameters as the 
	 * default texture parameters to use when creating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * <p>
	 * GAGE uses the following default values:
	 * </p>
	 * <table>
	 * <tr>
	 * <th>Parameter</th>
	 * <th>Value</th>
	 * </tr>
	 * <tr><td>GL_TEXTURE_WRAP_S</td><td>GL_REPEAT</td></tr>
	 * <tr><td>GL_TEXTURE_WRAP_T</td><td>GL_REPEAT</td></tr>
	 * <tr><td>GL_TEXTURE_MAG_FILTER</td><td>GL_LINEAR</td></tr>
	 * <tr><td>GL_TEXTURE_MIN_FILTER</td><td>GL_LINEAR_MIPMAP_LINEAR</td></tr>
	 * </table>
	 */
	public void useBuiltinDefaultRepeatingTextureParameters() {
		this.defaultTextureParameters = BUILTIN_DEFAULT_REPEATING_TEXTURE_PARAMETERS;
	}
	
}
