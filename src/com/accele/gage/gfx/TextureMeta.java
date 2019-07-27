package com.accele.gage.gfx;

import java.nio.ByteBuffer;

public class TextureMeta {

	private ByteBuffer pixels;
	private int width;
	private int height;
	private int format;
	
	public TextureMeta(ByteBuffer pixels, int width, int height, int format) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.format = format;
	}
	
	public ByteBuffer getPixels() {
		return pixels;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getFormat() {
		return format;
	}
	
}
