package com.accele.gage;

/**
 * Represents a type which has an underlying value used in direct OpenGL API calls.
 * 
 * <p>
 * This interface is used to act as an intermediary between GAGE API calls and its underlying implementation, OpenGL.
 * For example, {@link com.accele.gage.gfx.TextureWrapParameter TextureWrapParameter} uses this interface to provide 
 * the actual integer value used to specify the target parameter in OpenGL.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public interface NativeValueHoldable {

	/**
	 * Returns the native value used in direct OpenGL API calls.
	 * 
	 * @return the native OpenGL value
	 */
	public int getNativeValue();
	
}
