package com.accele.gage.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Resource;

public class Texture implements Indexable, Cleanable {

	private String registryId;
	private int textureId;
	private int totalWidth;
	private int totalHeight;
	private float x;
	private float y;
	private float width;
	private float height;
	
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
		
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
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
	
	public void bind(int sample) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + sample);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
	}
	
	public Texture subRegion(String registryId, float x, float y, float width, float height) {
		return new Texture(registryId, textureId, totalWidth, totalHeight, x, y, width, height);
	}
	
	public Texture subRegion(float x, float y, float width, float height) {
		return subRegion(registryId + "_sub_region", x, y, width, height);
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
	
	public int getTotalWidth() {
		return totalWidth;
	}
	
	public int getTotalHeight() {
		return totalHeight;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
	
}
