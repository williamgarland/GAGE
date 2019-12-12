package com.accele.gage.gfx;

import org.lwjgl.opengl.GL11;

import com.accele.gage.NativeValueHoldable;

/**
 * Represent values used for texture filter parameters.
 * 
 * <p>
 * These values are designed for use with the {@link com.accele.gage.GameConfiguration#setTextureMinFilterParameter(TextureFilterParameter) setTextureMinFilterParameter(TextureFilterParameter)} and
 * {@link com.accele.gage.GameConfiguration#setTextureMagFilterParameter(TextureFilterParameter) setTextureMagFilterParameter(TextureFilterParameter)} methods.
 * </p>
 */
public enum TextureFilterParameter implements NativeValueHoldable {

	/**
	 * Returns the pixels closest to the coordinates.
	 */
	NEAREST(GL11.GL_NEAREST), 
	
	/**
	 * Returns the weighted average of the four pixels surrounding the given coordinates.
	 */
	LINEAR(GL11.GL_LINEAR), 
	
	/**
	 * Uses the mipmap that most closely matches the size of the pixel being textured and samples with nearest neighbor interpolation.
	 */
	NEAREST_MIPMAP_NEAREST(GL11.GL_NEAREST_MIPMAP_NEAREST), 
	
	/**
	 * Samples the closest mipmap with linear interpolation.
	 */
	NEAREST_MIPMAP_LINEAR(GL11.GL_NEAREST_MIPMAP_LINEAR), 
	
	/**
	 * Uses the two mipmaps that most closely match the size of the pixel being textured and samples with nearest neighbor interpolation.
	 */
	LINEAR_MIPMAP_NEAREST(GL11.GL_LINEAR_MIPMAP_NEAREST), 
	
	/**
	 * Samples the two closest mipmaps with linear interpolation.
	 */
	LINEAR_MIPMAP_LINEAR(GL11.GL_LINEAR_MIPMAP_LINEAR);
	
	private int nativeValue;
	
	private TextureFilterParameter(int nativeValue) {
		this.nativeValue = nativeValue;
	}
	
	@Override
	public int getNativeValue() {
		return nativeValue;
	}
	
	/**
	 * Returns the {@code TextureFilterParameter} with the specified {@code nativeValue}.
	 * 
	 * @param nativeValue the native value of the target {@code TextureFilterParameter}
	 * 
	 * @return the {@code TextureFilterParameter} with the specified {@code nativeValue}
	 */
	public static TextureFilterParameter fromNativeValue(int nativeValue) {
		for (TextureFilterParameter param : values())
			if (param.getNativeValue() == nativeValue)
				return param;
		return null;
	}
	
}
