package com.accele.gage.gfx;

public class FontMeta {

	private int size;
	private Texture fontTexture;
	private CharMeta[] chars;
	
	public FontMeta(int size, Texture fontTexture, CharMeta[] chars) {
		this.size = size;
		this.fontTexture = fontTexture;
		this.chars = chars;
	}

	public int getSize() {
		return size;
	}
	
	public Texture getFontTexture() {
		return fontTexture;
	}
	
	public CharMeta[] getChars() {
		return chars;
	}
	
}
