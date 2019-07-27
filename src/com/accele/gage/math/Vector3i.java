package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Vector3i {

	public int x;
	public int y;
	public int z;
	
	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3i(IntBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		data.reset();
	}
	
	public Vector3i(ByteBuffer data) {
		data.mark();
		this.x = data.getInt();
		this.y = data.getInt();
		this.z = data.getInt();
		data.reset();
	}
	
	public Vector3i(int[] data) {
		this(data[0], data[1], data[2]);
	}
	
	public Vector3i(Vector3i other) {
		this(other.x, other.y, other.z);
	}
	
	public Vector3i(int xyz) {
		this(xyz, xyz, xyz);
	}
	
	public Vector3i() {
		this(0, 0, 0);
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
	
	public Vector3i setX(int x) {
		this.x = x;
		return this;
	}
	
	public Vector3i setY(int y) {
		this.y = y;
		return this;
	}
	
	public Vector3i setZ(int z) {
		this.z = z;
		return this;
	}
	
	public Vector3i set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Vector3i set(Vector3i other) {
		return set(other.x, other.y, other.z);
	}
	
	public Vector3i set(int xyz) {
		return set(xyz, xyz, xyz);
	}
	
	public Vector3i set(IntBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		data.reset();
		return this;
	}
	
	public Vector3i set(ByteBuffer data) {
		data.mark();
		this.x = data.getInt();
		this.y = data.getInt();
		this.z = data.getInt();
		data.reset();
		return this;
	}
	
	public Vector3i set(int[] data) {
		return set(data[0], data[1], data[2]);
	}
	
	public Vector3i zero() {
		return set(0, 0, 0);
	}
	
	public Vector3i negate() {
		return set(-x, -y, -z);
	}
	
	public int length() {
		return (int) Math.sqrt(lengthSquared());
	}
	
	public int lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	public Vector3i normalize() {
		int length = length();
		if (length != 0) {
			x /= length;
			y /= length;
			z /= length;
		}
		return this;
	}
	
	public Vector3i add(int scalar) {
		return set(x + scalar, y + scalar, z + scalar);
	}
	
	public Vector3i add(Vector3i other) {
		return set(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector3i sub(int scalar) {
		return set(x - scalar, y - scalar, z - scalar);
	}
	
	public Vector3i sub(Vector3i other) {
		return set(x - other.x, y - other.y, z - other.z);
	}
	
	public Vector3i mul(int scalar) {
		return set(x * scalar, y * scalar, z * scalar);
	}
	
	public Vector3i mul(Vector3i other) {
		return set(x * other.x, y * other.y, z * other.z);
	}
	
	public Vector3i div(int scalar) {
		return set(x / scalar, y / scalar, z / scalar);
	}
	
	public Vector3i div(Vector3i other) {
		return set(x / other.x, y / other.y, z / other.z);
	}
	
	public int dot(Vector3i other) {
		return x * other.x + y * other.y + z * other.z;
	}
	
	public Vector3i cross(Vector3i other) {
		return set(y * other.getZ() - z * other.getY(), z * other.getX() - x * other.getZ(), x * other.getY() - y * other.getX());
	}
	
	public IntBuffer toIntBuffer() {
		return toIntBuffer(MathUtils.allocateIntBuffer(3));
	}
	
	public IntBuffer toIntBuffer(IntBuffer dest) {
		dest.mark();
		dest.put(x);
		dest.put(y);
		dest.put(z);
		dest.reset();
		return dest;
	}
	
	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(3 * Integer.BYTES));
	}
	
	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putInt(x);
		dest.putInt(y);
		dest.putInt(z);
		dest.reset();
		return dest;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		Vector3i other = (Vector3i) obj;
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
		return "Vector3i[x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
