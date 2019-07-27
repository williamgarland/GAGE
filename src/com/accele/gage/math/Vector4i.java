package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Vector4i {

	public int x;
	public int y;
	public int z;
	public int w;
	
	public Vector4i(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4i(IntBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		this.w = data.get();
		data.reset();
	}
	
	public Vector4i(ByteBuffer data) {
		data.mark();
		this.x = data.getInt();
		this.y = data.getInt();
		this.z = data.getInt();
		this.w = data.getInt();
		data.reset();
	}
	
	public Vector4i(int[] data) {
		this(data[0], data[1], data[2], data[3]);
	}
	
	public Vector4i(Vector4i other) {
		this(other.x, other.y, other.z, other.w);
	}
	
	public Vector4i(int xyz, int w) {
		this(xyz, xyz, xyz, w);
	}
	
	public Vector4i(int xyzw) {
		this(xyzw, xyzw, xyzw, xyzw);
	}
	
	public Vector4i() {
		this(0, 0, 0, 1);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getW() {
		return w;
	}
	
	public Vector4i setX(int x) {
		this.x = x;
		return this;
	}
	
	public Vector4i setY(int y) {
		this.y = y;
		return this;
	}
	
	public Vector4i setZ(int z) {
		this.z = z;
		return this;
	}
	
	public Vector4i setW(int w) {
		this.w = w;
		return this;
	}
	
	public Vector4i set(int x, int y, int z, int w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		return this;
	}
	
	public Vector4i set(Vector4i other) {
		return set(other.x, other.y, other.z, other.w);
	}
	
	public Vector4i set(int xyz, int w) {
		return set(xyz, xyz, xyz, w);
	}
	
	public Vector4i set(int xyzw) {
		return set(xyzw, xyzw, xyzw, xyzw);
	}
	
	public Vector4i set(IntBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		this.w = data.get();
		data.reset();
		return this;
	}
	
	public Vector4i set(ByteBuffer data) {
		data.mark();
		this.x = data.getInt();
		this.y = data.getInt();
		this.z = data.getInt();
		this.w = data.getInt();
		data.reset();
		return this;
	}
	
	public Vector4i set(int[] data) {
		return set(data[0], data[1], data[2], data[3]);
	}
	
	public Vector4i zero() {
		return set(0, 0, 0, 0);
	}
	
	public Vector4i negate() {
		return set(-x, -y, -z, -w);
	}
	
	public int length() {
		return (int) Math.sqrt(lengthSquared());
	}
	
	public int lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	
	public Vector4i normalize() {
		int length = length();
		if (length != 0) {
			x /= length;
			y /= length;
			z /= length;
			w /= length;
		}
		return this;
	}
	
	public Vector4i add(int scalar) {
		return set(x + scalar, y + scalar, z + scalar, w + scalar);
	}
	
	public Vector4i add(Vector4i other) {
		return set(x + other.x, y + other.y, z + other.z, w + other.w);
	}
	
	public Vector4i sub(int scalar) {
		return set(x - scalar, y - scalar, z - scalar, w - scalar);
	}
	
	public Vector4i sub(Vector4i other) {
		return set(x - other.x, y - other.y, z - other.z, w - other.w);
	}
	
	public Vector4i mul(int scalar) {
		return set(x * scalar, y * scalar, z * scalar, w * scalar);
	}
	
	public Vector4i mul(Vector4i other) {
		return set(x * other.x, y * other.y, z * other.z, w * other.w);
	}
	
	public Vector4i div(int scalar) {
		return set(x / scalar, y / scalar, z / scalar, w / scalar);
	}
	
	public Vector4i div(Vector4i other) {
		return set(x / other.x, y / other.y, z / other.z, w / other.w);
	}
	
	public int dot(Vector4i other) {
		return x * other.x + y * other.y + z * other.z + w * other.w;
	}
	
	public IntBuffer toIntBuffer() {
		return toIntBuffer(MathUtils.allocateIntBuffer(4));
	}
	
	public IntBuffer toIntBuffer(IntBuffer dest) {
		dest.mark();
		dest.put(x);
		dest.put(y);
		dest.put(z);
		dest.put(w);
		dest.reset();
		return dest;
	}
	
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(4 * Integer.BYTES));
	}
	
	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putInt(x);
		dest.putInt(y);
		dest.putInt(z);
		dest.putInt(w);
		dest.reset();
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + w;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
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
		Vector4i other = (Vector4i) obj;
		if (w != other.w)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vector4i[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
	
}
