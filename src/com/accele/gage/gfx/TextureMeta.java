package com.accele.gage.gfx;

import java.nio.ByteBuffer;

public class TextureMeta {

	private ByteBuffer pixels;
	private int width;
	private int height;
	private int format;
	private int[] parameters;
	private boolean generateMipmaps;
	
	public TextureMeta(ByteBuffer pixels, int width, int height, int format, int[] parameters, boolean generateMipmaps) {
		this.pixels = pixels;
		this.width = width;
		this.height = height;
		this.format = format;
		this.parameters = parameters;
		this.generateMipmaps = generateMipmaps;
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
	
	public int[] getParameters() {
		return parameters;
	}
	
	public boolean shouldGenerateMipmaps() {
		return generateMipmaps;
	}
	
}
