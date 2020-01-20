package com.accele.gage;

import org.lwjgl.opengl.GL11;

import com.accele.gage.gfx.TextureFilterParameter;
import com.accele.gage.gfx.TextureWrapParameter;

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
	public static final TextureWrapParameter DEFAULT_TEXTURE_WRAP_S_PARAMETER = TextureWrapParameter.CLAMP_TO_EDGE;
	public static final TextureWrapParameter DEFAULT_TEXTURE_WRAP_T_PARAMETER = TextureWrapParameter.CLAMP_TO_EDGE;
	public static final TextureWrapParameter DEFAULT_REPEATING_TEXTURE_WRAP_S_PARAMETER = TextureWrapParameter.REPEAT;
	public static final TextureWrapParameter DEFAULT_REPEATING_TEXTURE_WRAP_T_PARAMETER = TextureWrapParameter.REPEAT;
	public static final TextureFilterParameter DEFAULT_TEXTURE_MAG_FILTER_PARAMETER = TextureFilterParameter.LINEAR;
	public static final TextureFilterParameter DEFAULT_TEXTURE_MIN_FILTER_PARAMETER = TextureFilterParameter.LINEAR_MIPMAP_LINEAR;
	
	private final String version = "2.0.0";
	private int fps;
	double ticksPerSecond;
	private boolean doEntityCollision;
	private float masterVolume;
	private float masterPitch;
	private boolean masterVolumeMuted;
	private TextureWrapParameter textureWrapSParameter;
	private TextureWrapParameter textureWrapTParameter;
	private TextureFilterParameter textureMagFilterParameter;
	private TextureFilterParameter textureMinFilterParameter;
	private boolean generateTextureMipmaps;
	
	GameConfiguration() {
		this.ticksPerSecond = DEFAULT_TICKS_PER_SECOND;
		this.doEntityCollision = true;
		this.masterVolume = 1;
		this.masterPitch = 1;
		this.masterVolumeMuted = false;
		this.generateTextureMipmaps = true;
		useDefaultTextureParameters();
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
	 * Returns the S wrap parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @return the S wrap parameter to use for {@code Texture} generation
	 */
	public TextureWrapParameter getTextureWrapSParameter() {
		return textureWrapSParameter;
	}
	
	/**
	 * Returns the T wrap parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @return the T wrap parameter to use for {@code Texture} generation
	 */
	public TextureWrapParameter getTextureWrapTParameter() {
		return textureWrapTParameter;
	}
	
	/**
	 * Returns the minifying filter parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @return the minifying filter parameter to use for {@code Texture} generation
	 */
	public TextureFilterParameter getTextureMinFilterParameter() {
		return textureMinFilterParameter;
	}
	
	/**
	 * Returns the magnifying filter parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @return the magnifying filter parameter to use for {@code Texture} generation
	 */
	public TextureFilterParameter getTextureMagFilterParameter() {
		return textureMagFilterParameter;
	}
	
	/**
	 * A convenience method for returning an array of all specified {@link com.accele.gage.gfx.Texture Texture} parameters in key-value order.
	 * 
	 * @return an array of {@code Texture} parameters in key-value order
	 */
	public int[] getTextureParameters() {
		return new int[] {
			GL11.GL_TEXTURE_WRAP_S, textureWrapSParameter.getNativeValue(),
			GL11.GL_TEXTURE_WRAP_T, textureWrapTParameter.getNativeValue(),
			GL11.GL_TEXTURE_MIN_FILTER, textureMinFilterParameter.getNativeValue(),
			GL11.GL_TEXTURE_MAG_FILTER, textureMagFilterParameter.getNativeValue(),
		};
	}
	
	/**
	 * Sets the S wrap parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @param textureWrapSParameter the S wrap parameter to use for {@code Texture} generation
	 */
	public void setTextureWrapSParameter(TextureWrapParameter textureWrapSParameter) {
		this.textureWrapSParameter = textureWrapSParameter;
	}
	
	/**
	 * Sets the T wrap parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @param textureWrapTParameter the T wrap parameter to use for {@code Texture} generation
	 */
	public void setTextureWrapTParameter(TextureWrapParameter textureWrapTParameter) {
		this.textureWrapTParameter = textureWrapTParameter;
	}
	
	/**
	 * Sets the minifying filter parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @param textureMinFilterParameter the minifying filter parameter to use for {@code Texture} generation
	 */
	public void setTextureMinFilterParameter(TextureFilterParameter textureMinFilterParameter) {
		this.textureMinFilterParameter = textureMinFilterParameter;
	}
	
	/**
	 * Sets the magnifying filter parameter the engine should use when generating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * 
	 * @param textureMagFilterParameter the magnifying filter parameter to use for {@code Texture} generation
	 */
	public void setTextureMagFilterParameter(TextureFilterParameter textureMagFilterParameter) {
		this.textureMagFilterParameter = textureMagFilterParameter;
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
	 * Specifies that the engine should use the default texture parameters 
	 * when creating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * <p>
	 * GAGE uses the following default values:
	 * </p>
	 * <table>
	 * <caption>Default Texture Parameters</caption>
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
	public void useDefaultTextureParameters() {
		this.textureWrapSParameter = DEFAULT_TEXTURE_WRAP_S_PARAMETER;
		this.textureWrapTParameter = DEFAULT_TEXTURE_WRAP_T_PARAMETER;
		this.textureMinFilterParameter = DEFAULT_TEXTURE_MIN_FILTER_PARAMETER;
		this.textureMagFilterParameter = DEFAULT_TEXTURE_MAG_FILTER_PARAMETER;
	}
	
	/**
	 * Specifies that the engine should use the default repeating texture parameters 
	 * when creating new instances of {@link com.accele.gage.gfx.Texture Texture}.
	 * <p>
	 * GAGE uses the following default values:
	 * </p>
	 * <table>
	 * <caption>Default Repeating Texture Parameters</caption>
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
	public void useDefaultRepeatingTextureParameters() {
		this.textureWrapSParameter = DEFAULT_REPEATING_TEXTURE_WRAP_S_PARAMETER;
		this.textureWrapTParameter = DEFAULT_REPEATING_TEXTURE_WRAP_T_PARAMETER;
		this.textureMinFilterParameter = DEFAULT_TEXTURE_MIN_FILTER_PARAMETER;
		this.textureMagFilterParameter = DEFAULT_TEXTURE_MAG_FILTER_PARAMETER;
	}
	
}
