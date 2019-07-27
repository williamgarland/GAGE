package com.accele.gage.gfx;

import com.accele.gage.math.Vector4f;

public class Color {

	public static final Color WHITE = new Color(1f, 1f, 1f);
	public static final Color BLACK = new Color(0f, 0f, 0f);
	public static final Color RED = new Color(1f, 0f, 0f);
	public static final Color GREEN = new Color(0f, 1f, 0f);
	public static final Color BLUE = new Color(0f, 0f, 1f);
	public static final Color CYAN = new Color(0f, 1f, 1f);
	public static final Color YELLOW = new Color(1f, 1f, 0f);
	public static final Color MAGENTA = new Color(1f, 0f, 1f);
	public static final Color GRAY = new Color(0.5f, 0.5f, 0.5f);
	public static final Color LIGHT_GRAY = new Color(0.25f, 0.25f, 0.25f);
	public static final Color DARK_GRAY = new Color(0.75f, 0.75f, 0.75f);
	public static final Color ORANGE = new Color(1f, 200f/255f, 0f);
	public static final Color PURPLE = new Color(0.5f, 0f, 0.5f);
	public static final Color BROWN = new Color(165f / 255f, 42f / 255f, 42f / 255f);
	
	private float r;
	private float g;
	private float b;
	private float a;
	
	public Color(int r, int g, int b) {
		this((float) r / 255f, (float) g / 255f, (float) b / 255f, 1f);
	}
	
	public Color(int r, int g, int b, int a) {
		this((float) r / 255f, (float) g / 255f, (float) b / 255f, (float) a / 255f);
	}
	
	public Color(float r, float g, float b) {
		this(r, g, b, 1f);
	}
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Color(Vector4f vector) {
		this(vector.x, vector.y, vector.z, vector.w);
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return g;
	}
	
	public float getB() {
		return b;
	}
	
	public float getA() {
		return a;
	}
	
	public Vector4f toVector() {
		return new Vector4f(r, g, b, a);
	}
	
}
