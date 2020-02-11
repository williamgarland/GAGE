package com.accele.gage.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Resource;

/**
 * The {@code Texture} class is used as a way to render images to the screen.
 * <p>
 * GAGE works with images through the use of textures. Textures can be applied to models which are then rendered to the screen.
 * The same texture can be used with multiple models. All textures used in the engine must be registered in the texture {@link com.accele.gage.GAGE#getTextureRegistry() Registry}.
 * Textures do not need to be manually cleaned, bound, or otherwise processed; all texture processing is handled by the {@link com.accele.gage.gfx.Graphics Graphics} instance.
 * </p>
 * <p>
 * Textures can also spawn sub-textures which contain the exact same properties as the parent texture, just with a different position and area on the original image.
 * To create sub-textures (also called sub-regions), use any variant of the {@link #subRegion(String, float, float, float, float) subRegion()} method.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class Texture implements Indexable, Cleanable {

	private String registryId;
	private int textureId;
	private int totalWidth;
	private int totalHeight;
	private float x;
	private float y;
	private float width;
	private float height;
	
	/**
	 * Creates a new {@code Texture} with the specified {@code registryId} and metadata.
	 * <p>
	 * When creating a new {@code Texture}, it must be registered in the texture {@link com.accele.gage.GAGE#getTextureRegistry() Registry} in order for GAGE 
	 * to keep track of it throughout the life cycle of the engine.
	 * </p>
	 * <p>
	 * To load a texture from a file (e.g. BMP, PNG, etc.), several different {@link com.accele.gage.ResourceLoader ResourceLoader}{@code s} 
	 * are provided in the {@link com.accele.gage.ResourceLoaders ResourceLoaders} class.
	 * </p>
	 * @param registryId the unique ID to use in the texture {@link com.accele.gage.Registry Registry}
	 * @param meta the metadata for the {@code Texture}
	 */
	public Texture(String registryId, Resource<TextureMeta> meta) {
		this.registryId = registryId;
		
		TextureMeta tm = null;
		try {
			tm = meta.get();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
		this.textureId = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tm.getWidth(), tm.getHeight(), 0, tm.getFormat(), GL11.GL_UNSIGNED_BYTE, tm.getPixels());
		
		for (int i = 0; i < tm.getParameters().length; i++) {
			int key = tm.getParameters()[i++];
			int value = tm.getParameters()[i];
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, key, value);
		}
		
		if (tm.shouldGenerateMipmaps())
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		
		this.totalWidth = tm.getWidth();
		this.totalHeight = tm.getHeight();
		this.x = 0;
		this.y = 0;
		this.width = 1;
		this.height = 1;
	}
	
	private Texture(String registryId, int textureId, int totalWidth, int totalHeight, float x, float y, float width, float height) {
		this.registryId = registryId;
		this.totalWidth = totalWidth;
		this.totalHeight = totalHeight;
		this.textureId = textureId;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Binds the {@code Texture} to the current graphics context along with the specified active texture unit.
	 * @param sample the active texture unit to use with this {@code Texture}
	 */
	public void bind(int sample) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + sample);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	
	/**
	 * Returns a view of this {@code Texture} with a new {@code registryId} and within the specified dimensions on the original texture.
	 * <p>
	 * While this method does technically return a new {@code Texture} object, it does not create a new OpenGL texture to use with it;
	 * it is simply a view of the original texture. Because of this, despite this sub-texture having a registry ID, it should not be registered in the texture registry 
	 * because the registry would then attempt to clean both the sub-texture and the parent texture. The registry ID of the sub-texture serves only to mark the texture 
	 * with a unique identifier for user purposes.
	 * </p>
	 * @param registryId the unique ID to use to identify the {@code Texture}
	 * @param x the upper-left x-coordinate on the original image to use in the sub-texture
	 * @param y the upper-left y-coordinate on the original image to use in the sub-texture
	 * @param width the width of the view on the original image to use in the sub-texture
	 * @param height the height of the view on the original image to use in the sub-texture
	 * @return a view of this {@code Texture} with a new {@code registryId} and within the specified dimensions on the original texture
	 */
	public Texture subRegion(String registryId, float x, float y, float width, float height) {
		return new Texture(registryId, textureId, totalWidth, totalHeight, x, y, width, height);
	}
	
	/**
	 * Returns a view of this {@code Texture} with a new {@code registryId} and within the specified dimensions on the original texture.
	 * <p>
	 * While this method does technically return a new {@code Texture} object, it does not create a new OpenGL texture to use with it;
	 * it is simply a view of the original texture. Because of this, despite this sub-texture having a registry ID, it should not be registered in the texture registry 
	 * because the registry would then attempt to clean both the sub-texture and the parent texture. The registry ID of the sub-texture serves only to mark the texture 
	 * with a unique identifier for user purposes. The registry ID of the sub-texture will be the registry ID of the original texture followed by {@code _sub_region_x_y_width_height},
	 * where {@code x}, {@code y}, {@code width}, and {@code height} are the values passed in to their respective parameters.
	 * </p>
	 * @param x the upper-left x-coordinate on the original image to use in the sub-texture
	 * @param y the upper-left y-coordinate on the original image to use in the sub-texture
	 * @param width the width of the view on the original image to use in the sub-texture
	 * @param height the height of the view on the original image to use in the sub-texture
	 * @return a view of this {@code Texture} with a new {@code registryId} and within the specified dimensions on the original texture
	 */
	public Texture subRegion(float x, float y, float width, float height) {
		return subRegion(registryId + "_sub_region_" + x + "_" + y + "_" + width + "_" + height, x, y, width, height);
	}
	
	@Override
	public void clean() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDeleteTextures(textureId);
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	/**
	 * Returns the total width of the {@code Texture}.
	 * <p>
	 * In a sub-texture, this value will always be consistent with its parent texture.
	 * </p>
	 * @return the total width of the texture
	 */
	public int getTotalWidth() {
		return totalWidth;
	}
	
	/**
	 * Returns the total height of the {@code Texture}.
	 * <p>
	 * In a sub-texture, this value will always be consistent with its parent texture.
	 * </p>
	 * @return the total height of the texture
	 */
	public int getTotalHeight() {
		return totalHeight;
	}

	/**
	 * Returns the upper-left x-coordinate of the {@code Texture}.
	 * <p>
	 * For original textures (non-sub-textures), this value will always be 0.
	 * </p>
	 * 
	 * @return the upper-left x-coordinate of the {@code Texture}
	 */
	public float getX() {
		return x;
	}

	/**
	 * Returns the upper-left y-coordinate of the {@code Texture}.
	 * <p>
	 * For original textures (non-sub-textures), this value will always be 0.
	 * </p>
	 * 
	 * @return the upper-left y-coordinate of the {@code Texture}
	 */
	public float getY() {
		return y;
	}

	/**
	 * Returns the width of the {@code Texture}.
	 * <p>
	 * For original textures (non-sub-textures), this value will always be 1.
	 * </p>
	 * 
	 * @return the width of the {@code Texture}
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the width of the {@code Texture}.
	 * <p>
	 * For original textures (non-sub-textures), this value will always be 1.
	 * </p>
	 * 
	 * @return the width of the {@code Texture}
	 */
	public float getHeight() {
		return height;
	}
	
	/**
	 * Returns the texture ID of the {@code Texture} used by OpenGL.
	 * 
	 * @return the texture ID of the {@code Texture} used by OpenGL
	 */
	public int getTextureId() {
		return textureId;
	}
	
}
