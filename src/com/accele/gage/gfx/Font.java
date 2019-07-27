package com.accele.gage.gfx;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Resource;

public class Font implements Indexable, Cleanable {

	private String registryId;
	private int size;
	private Texture texture;
	private CharMeta[] chars;
	
	public Font(String registryId, Resource<FontMeta> meta) {
		this.registryId = registryId;
		
		try {
			FontMeta data = meta.get();
			
			this.size = data.getSize();
			this.texture = data.getFontTexture();
			this.chars = data.getChars();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void clean() {
		texture.clean();
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	public int getSize() {
		return size;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public CharMeta[] getChars() {
		return chars;
	}
	
	public float getCharWidth(char c) {
		return chars[c].getWidth();
	}
	
	public float getCharHeight(char c) {
		return chars[c].getHeight();
	}
	
	public float getStringWidth(String str) {
		float result = 0;
		for (char c : str.toCharArray())
			result += getCharWidth(c);
		return result;
	}
	
	public float getStringHeight(String str) {
		float highest = 0;
		for (char c : str.toCharArray()) {
			float f = getCharHeight(c);
			if (f > highest)
				highest = f;
		}
		return highest;
	}
	
}
