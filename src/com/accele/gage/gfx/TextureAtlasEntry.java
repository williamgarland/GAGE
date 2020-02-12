package com.accele.gage.gfx;

public class TextureAtlasEntry {

	private float x;
	private float y;
	private float width;
	private float height;
	private int pixelX;
	private int pixelY;
	private int pixelWidth;
	private int pixelHeight;
	
	public TextureAtlasEntry(int pixelX, int pixelY, int pixelWidth, int pixelHeight, int atlasWidth, int atlasHeight) {
		this.pixelX = pixelX;
		this.pixelY = pixelY;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		
		this.x = (float) pixelX / (float) atlasWidth;
		this.y = (float) pixelY / (float) atlasHeight;
		this.width = (float) pixelWidth / (float) atlasWidth;
		this.height = (float) pixelHeight / (float) atlasHeight;
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

	public int getPixelX() {
		return pixelX;
	}

	public int getPixelY() {
		return pixelY;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}
	
}
