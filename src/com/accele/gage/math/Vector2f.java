package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Vector2f {

	public float x;
	public float y;
	
	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		data.reset();
	}
	
	public Vector2f(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		data.reset();
	}
	
	public Vector2f(float[] data) {
		this(data[0], data[1]);
	}
	
	public Vector2f(Vector2f other) {
		this(other.x, other.y);
	}
	
	public Vector2f(float xy) {
		this(xy, xy);
	}
	
	public Vector2f() {
		this(0, 0);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public Vector2f setX(float x) {
		this.x = x;
		return this;
	}
	
	public Vector2f setY(float y) {
		this.y = y;
		return this;
	}
	
	public Vector2f set(float x, float y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Vector2f set(Vector2f other) {
		return set(other.x, other.y);
	}
	
	public Vector2f set(float xy) {
		return set(xy, xy);
	}
	
	public Vector2f set(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		data.reset();
		return this;
	}
	
	public Vector2f set(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		data.reset();
		return this;
	}
	
	public Vector2f set(float[] data) {
		return set(data[0], data[1]);
	}
	
	public Vector2f zero() {
		return set(0, 0);
	}
	
	public Vector2f negate() {
		return set(-x, -y);
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public float lengthSquared() {
		return x * x + y * y;
	}
	
	public Vector2f normalize() {
		float length = length();
		if (length != 0) {
			x /= length;
			y /= length;
		}
		return this;
	}
	
	public Vector2f add(float scalar) {
		return set(x + scalar, y + scalar);
	}
	
	public Vector2f add(Vector2f other) {
		return set(x + other.x, y + other.y);
	}
	
	public Vector2f sub(float scalar) {
		return set(x - scalar, y - scalar);
	}
	
	public Vector2f sub(Vector2f other) {
		return set(x - other.x, y - other.y);
	}
	
	public Vector2f mul(float scalar) {
		return set(x * scalar, y * scalar);
	}
	
	public Vector2f mul(Vector2f other) {
		return set(x * other.x, y * other.y);
	}
	
	public Vector2f div(float scalar) {
		return set(x / scalar, y / scalar);
	}
	
	public Vector2f div(Vector2f other) {
		return set(x / other.x, y / other.y);
	}
	
	public float dot(Vector2f other) {
		return x * other.x + y * other.y;
	}
	
	public FloatBuffer toFloatBuffer() {
		return toFloatBuffer(MathUtils.allocateFloatBuffer(2));
	}
	
	public FloatBuffer toFloatBuffer(FloatBuffer dest) {
		dest.mark();
		dest.put(x);
		dest.put(y);
		dest.reset();
		return dest;
	}
	
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(2 * Float.BYTES));
	}
	
	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putFloat(x);
		dest.putFloat(y);
		dest.reset();
		return dest;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vector2f other = (Vector2f) obj;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vector2f[x=" + x + ", y=" + y + "]";
	}
	
}
