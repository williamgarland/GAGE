package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class AxisAnglef {

	public Vector3f axis;
	public float angle;
	
	public AxisAnglef(Vector3f axis, float angle) {
		this.axis = new Vector3f(axis);
		this.angle = angle;
	}
	
	public AxisAnglef(float x, float y, float z, float angle) {
		this.axis = new Vector3f(x, y, z);
		this.angle = angle;
	}
	
	public AxisAnglef(AxisAnglef other) {
		this.axis = new Vector3f(other.axis);
		this.angle = other.angle;
	}
	
	public AxisAnglef(Quaternionf quaternion) {
		this.axis = new Vector3f();
		float sqrt = (float) Math.sqrt(1.0 - quaternion.w * quaternion.w);
		this.axis.x = quaternion.x / sqrt;
		this.axis.y = quaternion.y / sqrt;
		this.axis.z = quaternion.z / sqrt;
		this.angle = 2f * (float) MathUtils.unrestrictedAcos(quaternion.w);
	}
	
	public AxisAnglef(FloatBuffer data) {
		this.axis = new Vector3f();
		data.mark();
		this.axis.x = data.get();
		this.axis.y = data.get();
		this.axis.z = data.get();
		this.angle = data.get();
		data.reset();
	}
	
	public AxisAnglef(ByteBuffer data) {
		this.axis = new Vector3f();
		data.mark();
		this.axis.x = data.getFloat();
		this.axis.y = data.getFloat();
		this.axis.z = data.getFloat();
		this.angle = data.getFloat();
		data.reset();
	}
	
	public AxisAnglef(float[] data) {
		this(data[0], data[1], data[2], data[3]);
	}
	
	public AxisAnglef() {
		this.axis = new Vector3f(0, 0, 1);
		this.angle = 0;
	}
	
	public Vector3f getAxis() {
		return axis;
	}
	
	public float getAngle() {
		return angle;
	}
	
	public AxisAnglef setAxis(Vector3f axis) {
		this.axis = new Vector3f(axis);
		return this;
	}
	
	public AxisAnglef setAngle(float angle) {
		this.angle = angle;
		return this;
	}
	
	public AxisAnglef set(float x, float y, float z, float angle) {
		this.axis.x = x;
		this.axis.y = y;
		this.axis.z = z;
		this.angle = angle;
		return this;
	}
	
	public AxisAnglef set(AxisAnglef other) {
		this.axis = new Vector3f(other.axis);
		this.angle = other.angle;
		return this;
	}
	
	public AxisAnglef set(Quaternionf quaternion) {
		this.axis = new Vector3f();
		float sqrt = (float) Math.sqrt(1.0 - quaternion.w * quaternion.w);
		this.axis.x = quaternion.x / sqrt;
		this.axis.y = quaternion.y / sqrt;
		this.axis.z = quaternion.z / sqrt;
		this.angle = 2f * (float) MathUtils.unrestrictedAcos(quaternion.w);
		return this;
	}
	
	public AxisAnglef set(FloatBuffer data) {
		this.axis = new Vector3f();
		data.mark();
		this.axis.x = data.get();
		this.axis.y = data.get();
		this.axis.z = data.get();
		this.angle = data.get();
		data.reset();
		return this;
	}
	
	public AxisAnglef set(ByteBuffer data) {
		this.axis = new Vector3f();
		data.mark();
		this.axis.x = data.getFloat();
		this.axis.y = data.getFloat();
		this.axis.z = data.getFloat();
		this.angle = data.getFloat();
		data.reset();
		return this;
	}
	
	public AxisAnglef set(float[] data) {
		return set(data[0], data[1], data[2], data[3]);
	}
	
	public AxisAnglef rotate(float angle) {
		this.angle += angle;
		this.angle = (float) ((this.angle < 0.0 ? Math.PI + Math.PI + this.angle % (Math.PI + Math.PI) : this.angle)
				% (Math.PI + Math.PI));
		return this;
	}
	
	public AxisAnglef normalize() {
		axis.mul(1.0f / axis.length());
		return this;
	}
	
	public Vector4f toVector() {
		return new Vector4f(axis.x, axis.y, axis.z, angle);
	}
	
	public Quaternionf toQuaternion() {
		float sin = (float) Math.sin(angle * 0.5);
		float cos = (float) Math.cos(angle * 0.5);
		return new Quaternionf(axis.x * sin, axis.y * sin, axis.z * sin, cos);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(angle);
		result = prime * result + ((axis == null) ? 0 : axis.hashCode());
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
		AxisAnglef other = (AxisAnglef) obj;
		if (Float.floatToIntBits(angle) != Float.floatToIntBits(other.angle))
			return false;
		if (axis == null) {
			if (other.axis != null)
				return false;
		} else if (!axis.equals(other.axis))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "AxisAnglef[x=" + axis.x + ", y=" + axis.y + ", z=" + axis.z + ", angle=" + angle + "]";
	}
	
}
