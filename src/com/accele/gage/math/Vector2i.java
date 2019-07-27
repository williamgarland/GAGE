package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Vector2i {

	public int x;
	public int y;
	
	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2i(IntBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		data.reset();
	}
	
	public Vector2i(ByteBuffer data) {
		data.mark();
		this.x = data.getInt();
		this.y = data.getInt();
		data.reset();
	}
	
	public Vector2i(int[] data) {
		this(data[0], data[1]);
	}
	
	public Vector2i(Vector2i other) {
		this(other.x, other.y);
	}
	
	public Vector2i(int xy) {
		this(xy, xy);
	}
	
	public Vector2i() {
		this(0, 0);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Vector2i setX(int x) {
		this.x = x;
		return this;
	}
	
	public Vector2i setY(int y) {
		this.y = y;
		return this;
	}
	
	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Vector2i set(Vector2i other) {
		return set(other.x, other.y);
	}
	
	public Vector2i set(int xy) {
		return set(xy, xy);
	}
	
	public Vector2i set(IntBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		data.reset();
		return this;
	}
	
	public Vector2i set(ByteBuffer data) {
		data.mark();
		this.x = data.getInt();
		this.y = data.getInt();
		data.reset();
		return this;
	}
	
	public Vector2i set(int[] data) {
		return set(data[0], data[1]);
	}
	
	public Vector2i zero() {
		return set(0, 0);
	}
	
	public Vector2i negate() {
		return set(-x, -y);
	}
	
	public int length() {
		return (int) Math.sqrt(lengthSquared());
	}
	
	public int lengthSquared() {
		return x * x + y * y;
	}
	
	public Vector2i normalize() {
		int length = length();
		if (length != 0) {
			x /= length;
			y /= length;
		}
		return this;
	}
	
	public Vector2i add(int scalar) {
		return set(x + scalar, y + scalar);
	}
	
	public Vector2i add(Vector2i other) {
		return set(x + other.x, y + other.y);
	}
	
	public Vector2i sub(int scalar) {
		return set(x - scalar, y - scalar);
	}
	
	public Vector2i sub(Vector2i other) {
		return set(x - other.x, y - other.y);
	}
	
	public Vector2i mul(int scalar) {
		return set(x * scalar, y * scalar);
	}
	
	public Vector2i mul(Vector2i other) {
		return set(x * other.x, y * other.y);
	}
	
	public Vector2i div(int scalar) {
		return set(x / scalar, y / scalar);
	}
	
	public Vector2i div(Vector2i other) {
		return set(x / other.x, y / other.y);
	}
	
	public int dot(Vector2i other) {
		return x * other.x + y * other.y;
	}
	
	public IntBuffer toIntBuffer() {
		return toIntBuffer(MathUtils.allocateIntBuffer(2));
	}
	
	public IntBuffer toIntBuffer(IntBuffer dest) {
		dest.mark();
		dest.put(x);
		dest.put(y);
		dest.reset();
		return dest;
	}
	
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(2 * Integer.BYTES));
	}
	
	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putInt(x);
		dest.putInt(y);
		dest.reset();
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
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
		Vector2i other = (Vector2i) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vector2i[x=" + x + ", y=" + y + "]";
	}
	
}
