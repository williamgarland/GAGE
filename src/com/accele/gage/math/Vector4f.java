package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Vector4f {

	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4f(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		this.w = data.get();
		data.reset();
	}
	
	public Vector4f(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		this.z = data.getFloat();
		this.w = data.getFloat();
		data.reset();
	}
	
	public Vector4f(float[] data) {
		this(data[0], data[1], data[2], data[3]);
	}
	
	public Vector4f(Vector4f other) {
		this(other.x, other.y, other.z, other.w);
	}
	
	public Vector4f(float xyz, float w) {
		this(xyz, xyz, xyz, w);
	}
	
	public Vector4f(float xyzw) {
		this(xyzw, xyzw, xyzw, xyzw);
	}
	
	public Vector4f() {
		this(0, 0, 0, 1);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getZ() {
		return z;
	}
	
	public float getW() {
		return w;
	}
	
	public Vector4f setX(float x) {
		this.x = x;
		return this;
	}
	
	public Vector4f setY(float y) {
		this.y = y;
		return this;
	}
	
	public Vector4f setZ(float z) {
		this.z = z;
		return this;
	}
	
	public Vector4f setW(float w) {
		this.w = w;
		return this;
	}
	
	public Vector4f set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		return this;
	}
	
	public Vector4f set(Vector4f other) {
		return set(other.x, other.y, other.z, other.w);
	}
	
	public Vector4f set(float xyz, float w) {
		return set(xyz, xyz, xyz, w);
	}
	
	public Vector4f set(float xyzw) {
		return set(xyzw, xyzw, xyzw, xyzw);
	}
	
	public Vector4f set(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		this.w = data.get();
		data.reset();
		return this;
	}
	
	public Vector4f set(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		this.z = data.getFloat();
		this.w = data.getFloat();
		data.reset();
		return this;
	}
	
	public Vector4f set(float[] data) {
		return set(data[0], data[1], data[2], data[3]);
	}
	
	public Vector4f zero() {
		return set(0, 0, 0, 0);
	}
	
	public Vector4f negate() {
		return set(-x, -y, -z, -w);
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	
	public Vector4f normalize() {
		float length = length();
		if (length != 0) {
			x /= length;
			y /= length;
			z /= length;
			w /= length;
		}
		return this;
	}
	
	public Vector4f add(float scalar) {
		return set(x + scalar, y + scalar, z + scalar, w + scalar);
	}
	
	public Vector4f add(Vector4f other) {
		return set(x + other.x, y + other.y, z + other.z, w + other.w);
	}
	
	public Vector4f sub(float scalar) {
		return set(x - scalar, y - scalar, z - scalar, w - scalar);
	}
	
	public Vector4f sub(Vector4f other) {
		return set(x - other.x, y - other.y, z - other.z, w - other.w);
	}
	
	public Vector4f mul(float scalar) {
		return set(x * scalar, y * scalar, z * scalar, w * scalar);
	}
	
	public Vector4f mul(Vector4f other) {
		return set(x * other.x, y * other.y, z * other.z, w * other.w);
	}
	
	public Vector4f div(float scalar) {
		return set(x / scalar, y / scalar, z / scalar, w / scalar);
	}
	
	public Vector4f div(Vector4f other) {
		return set(x / other.x, y / other.y, z / other.z, w / other.w);
	}
	
	public float dot(Vector4f other) {
		return x * other.x + y * other.y + z * other.z + w * other.w;
	}
	
	public FloatBuffer toFloatBuffer() {
		return toFloatBuffer(MathUtils.allocateFloatBuffer(4));
	}
	
	public FloatBuffer toFloatBuffer(FloatBuffer dest) {
		dest.mark();
		dest.put(x);
		dest.put(y);
		dest.put(z);
		dest.put(w);
		dest.reset();
		return dest;
	}
	
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(4 * Float.BYTES));
	}
	
	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putFloat(x);
		dest.putFloat(y);
		dest.putFloat(z);
		dest.putFloat(w);
		dest.reset();
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(w);
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		result = prime * result + Float.floatToIntBits(z);
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
		Vector4f other = (Vector4f) obj;
		if (Float.floatToIntBits(w) != Float.floatToIntBits(other.w))
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		if (Float.floatToIntBits(z) != Float.floatToIntBits(other.z))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Vector4f[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
	
}
