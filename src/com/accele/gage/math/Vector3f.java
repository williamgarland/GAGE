package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Vector3f {

	public float x;
	public float y;
	public float z;
	
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		data.reset();
	}
	
	public Vector3f(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		this.z = data.getFloat();
		data.reset();
	}
	
	public Vector3f(float[] data) {
		this(data[0], data[1], data[2]);
	}
	
	public Vector3f(Vector3f other) {
		this(other.x, other.y, other.z);
	}
	
	public Vector3f(float xyz) {
		this(xyz, xyz, xyz);
	}
	
	public Vector3f() {
		this(0, 0, 0);
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
	
	public Vector3f setX(float x) {
		this.x = x;
		return this;
	}
	
	public Vector3f setY(float y) {
		this.y = y;
		return this;
	}
	
	public Vector3f setZ(float z) {
		this.z = z;
		return this;
	}
	
	public Vector3f set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Vector3f set(Vector3f other) {
		return set(other.x, other.y, other.z);
	}
	
	public Vector3f set(float xyz) {
		return set(xyz, xyz, xyz);
	}
	
	public Vector3f set(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		data.reset();
		return this;
	}
	
	public Vector3f set(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		this.z = data.getFloat();
		data.reset();
		return this;
	}
	
	public Vector3f set(float[] data) {
		return set(data[0], data[1], data[2]);
	}
	
	public Vector3f zero() {
		return set(0, 0, 0);
	}
	
	public Vector3f negate() {
		return set(-x, -y, -z);
	}
	
	public float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public float lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public Vector3f normalize() {
		float length = length();
		if (length != 0) {
			x /= length;
			y /= length;
			z /= length;
		}
		return this;
	}
	
	public Vector3f add(float scalar) {
		return set(x + scalar, y + scalar, z + scalar);
	}
	
	public Vector3f add(Vector3f other) {
		return set(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3f sub(float scalar) {
		return set(x - scalar, y - scalar, z - scalar);
	}
	
	public Vector3f sub(Vector3f other) {
		return set(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector3f mul(float scalar) {
		return set(x * scalar, y * scalar, z * scalar);
	}
	
	public Vector3f mul(Vector3f other) {
		return set(x * other.x, y * other.y, z * other.z);
	}
	
	public Vector3f div(float scalar) {
		return set(x / scalar, y / scalar, z / scalar);
	}
	
	public Vector3f div(Vector3f other) {
		return set(x / other.x, y / other.y, z / other.z);
	}
	
	public float dot(Vector3f other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	public Vector3f cross(Vector3f other) {
		return set(y * other.getZ() - z * other.getY(), z * other.getX() - x * other.getZ(), x * other.getY() - y * other.getX());
	}
	
	public FloatBuffer toFloatBuffer() {
		return toFloatBuffer(MathUtils.allocateFloatBuffer(3));
	}
	
	public FloatBuffer toFloatBuffer(FloatBuffer dest) {
		dest.mark();
		dest.put(x);
		dest.put(y);
		dest.put(z);
		dest.reset();
		return dest;
	}
	
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(3 * Float.BYTES));
	}
	
	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putFloat(x);
		dest.putFloat(y);
		dest.putFloat(z);
		dest.reset();
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Vector3f other = (Vector3f) obj;
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
		return "Vector3f[x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
