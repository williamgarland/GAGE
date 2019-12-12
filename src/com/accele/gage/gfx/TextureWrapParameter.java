package com.accele.gage.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;

import com.accele.gage.NativeValueHoldable;

/**
 * Represent values used for texture wrap parameters.
 * 
 * <p>
 * These values are designed for use with the {@link com.accele.gage.GameConfiguration#setTextureWrapSParameter(TextureWrapParameter) setTextureWrapSParameter(TextureWrapParameter)} and
 * {@link com.accele.gage.GameConfiguration#setTextureWrapTParameter(TextureWrapParameter) setTextureWrapTParameter(TextureWrapParameter)} methods.
 * </p>
 */
public enum TextureWrapParameter implements NativeValueHoldable {

	/**
	 * Causes the texture to repeat on areas larger than the actual texture size.
	 */
	REPEAT(GL11.GL_REPEAT), 
	
	/**
	 * Causes the texture to repeat mirrored at the edge on areas larger than the actual texture size.
	 */
	MIRRORED_REPEAT(GL14.GL_MIRRORED_REPEAT), 
	
	/**
	 * Causes the texture to clamp its image to its original size.
	 * <p>
	 * If the target area is larger than the texture, the texture will extend its border pixels to the edge of the area.
	 * </p>
	 */
	CLAMP_TO_EDGE(GL12.GL_CLAMP_TO_EDGE), 
	
	/**
	 * Causes the texture to clamp its image to its original size.
	 * <p>
	 * If the target area is larger than the texture, the pixels outside of the range will be given a specified border color (usually black by default).
	 * </p>
	 */
	CLAMP_TO_BORDER(GL13.GL_CLAMP_TO_BORDER);
	
	private int nativeValue;
	
	private TextureWrapParameter(int nativeValue) {
		this.nativeValue = nativeValue;
	}
	
	@Override
	public int getNativeValue() {
		return nativeValue;
	}
	
	/**
	 * Returns the {@code TextureWrapParameter} with the specified {@code nativeValue}.
	 * 
	 * @param nativeValue the native value of the target {@code TextureWrapParameter}
	 * 
	 * @return the {@code TextureWrapParameter} with the specified {@code nativeValue}
	 */
	public static TextureWrapParameter fromNativeValue(int nativeValue) {
		for (TextureWrapParameter param : values())
			if (param.getNativeValue() == nativeValue)
				return param;
		return null;
	}
	
}
