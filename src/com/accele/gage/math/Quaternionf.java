package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Quaternionf {

	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quaternionf(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Quaternionf(Quaternionf other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.w = other.w;
	}
	
	public Quaternionf(AxisAnglef axisAngle) {
		float sin = (float) Math.sin(axisAngle.angle * 0.5);
		float cos = (float) Math.cos(axisAngle.angle * 0.5);
		this.x = axisAngle.axis.x * sin;
		this.y = axisAngle.axis.y * sin;
		this.z = axisAngle.axis.z * sin;
		this.w = cos;
	}
	
	public Quaternionf(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		this.w = data.get();
		data.reset();
	}
	
	public Quaternionf(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		this.z = data.getFloat();
		this.w = data.getFloat();
		data.reset();
	}
	
	public Quaternionf(float[] data) {
		this(data[0], data[1], data[2], data[3]);
	}
	
	public Quaternionf() {
		this(0, 0, 0, 1);
	}
	
	public float length() {
		return (float) Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public float lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	
	public Quaternionf conjugate() {
		return set(-x, -y, -z, w);
	}
	
	public Quaternionf normalize() {
		float len = length();
		x /= len;
		y /= len;
		z /= len;
		w /= len;
		return this;
	}
	
	public Quaternionf add(Quaternionf other) {
		x += other.x;
		y += other.y;
		z += other.z;
		w += other.w;
		return this;
	}
	
	public Quaternionf sub(Quaternionf other) {
		x -= other.x;
		y -= other.y;
		z -= other.z;
		w -= other.w;
		return this;
	}
	
	public Quaternionf mul(Quaternionf other) {
		x = x * other.w + w * other.x + y * other.z - z * other.y;
		y = y * other.w + w * other.y + z * other.x - x * other.z;
		z = z * other.w + w * other.z + x * other.y - y * other.x;
		w = w * other.w - x * other.x - y * other.y - z * other.z;
		return this;
	}
	
	public Quaternionf mul(Vector3f vector) {
		x = w * vector.x + y * vector.z - z * vector.y;
		y = w * vector.y + z * vector.x - x * vector.z;
		z = w * vector.z + x * vector.y - y * vector.x;
		w = -x * vector.x - y * vector.y - z * vector.z;
		return this;
	}
	
	public Quaternionf mul(AxisAnglef axisAngle) {
		return mul(axisAngle.toQuaternion());
	}
	
	public Quaternionf div(Quaternionf other) {
		return mul(new Quaternionf(other).invert());
	}
	
	public Quaternionf add(float scalar) {
		x += scalar;
		y += scalar;
		z += scalar;
		w += scalar;
		return this;
	}

	public Quaternionf sub(float scalar) {
		x -= scalar;
		y -= scalar;
		z -= scalar;
		w -= scalar;
		return this;
	}

	public Quaternionf mul(float scalar) {
		x *= scalar;
		y *= scalar;
		z *= scalar;
		w *= scalar;
		return this;
	}

	public Quaternionf div(float scalar) {
		x /= scalar;
		y /= scalar;
		z /= scalar;
		w /= scalar;
		return this;
	}
	
	public Quaternionf invert() {
		float invLen = 1.0f / lengthSquared();
		x = -x * invLen;
		y = -y * invLen;
		z = -z * invLen;
		w = w * invLen;
		return this;
	}
	
	public float dot(Quaternionf other) {
		return x * other.x + y * other.y + z * other.z + w * other.w;
	}
	
	public float getAngle() {
		float angle = 2 * (float) Math.acos(w);
		return (float) (angle <= Math.PI ? angle : Math.PI + Math.PI - angle);
	}
	
	public Quaternionf toIdentity() {
		return set(0, 0, 0, 1);
	}
	
	public boolean isIdentity() {
		return x == 0 && y == 0 && z == 0 && w == 1;
	}
	
	public Quaternionf lerp(Quaternionf other, float interpolationFactor) {
		Quaternionf tmp = sub(other);
		return add(new Quaternionf(interpolationFactor * tmp.getX(), interpolationFactor * tmp.getY(),
				interpolationFactor * tmp.getZ(), interpolationFactor * tmp.getW()));
	}

	public Quaternionf slerp(Quaternionf other, float interpolationFactor) {
		float dot = dot(other);
		dot = dot < -1 ? -1 : dot > 1 ? 1 : dot;
		float theta = (float) Math.acos(dot) * interpolationFactor;
		float cosTheta = (float) Math.cos(theta);
		float sinTheta = (float) Math.sin(theta);
		Quaternionf relative = new Quaternionf(other).sub(new Quaternionf(dot * x, dot * y, dot * z, dot * w)).normalize();
		return mul(cosTheta).add(relative.mul(sinTheta));
	}

	public Quaternionf nlerp(Quaternionf other, float interpolationFactor) {
		return lerp(other, interpolationFactor).normalize();
	}
	
	public Vector3f getForwardVector() {
		return new Vector3f(2f * (x * z + w * y), 2f * (y * z - w * x), 1f - 2f * (x * x + y * y));
	}
	
	public Vector3f getUpVector() {
		return new Vector3f(2f * (x * y - w * z), 1f - 2f * (x * x + z * z), 2f * (y * z + w * x));
	}
	
	public Vector3f getLeftVector() {
		return new Vector3f(1f - 2f * (y * y + z * z), 2f * (x * y + w * z), 2f * (x * z - w * y));
	}
	
	public Vector4f toVector() {
		return new Vector4f(x, y, z, w);
	}
	
	public AxisAnglef toAxisAngle() {
		float sqrt = (float) Math.sqrt(1.0 - w * w);
		return new AxisAnglef(x / sqrt, y / sqrt, z / sqrt, 2f * (float) MathUtils.unrestrictedAcos(w));
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
	
	public Quaternionf setX(float x) {
		this.x = x;
		return this;
	}
	
	public Quaternionf setY(float y) {
		this.y = y;
		return this;
	}
	
	public Quaternionf setZ(float z) {
		this.z = z;
		return this;
	}
	
	public Quaternionf setW(float w) {
		this.w = w;
		return this;
	}
	
	public Quaternionf set(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		return this;
	}
	
	public Quaternionf set(Quaternionf other) {
		this.x = other.x;
		this.y = other.y;
		this.z = other.z;
		this.w = other.w;
		return this;
	}
	
	public Quaternionf set(AxisAnglef axisAngle) {
		float sin = (float) Math.sin(axisAngle.angle * 0.5);
		float cos = (float) Math.cos(axisAngle.angle * 0.5);
		this.x = axisAngle.axis.x * sin;
		this.y = axisAngle.axis.y * sin;
		this.z = axisAngle.axis.z * sin;
		this.w = cos;
		return this;
	}
	
	public Quaternionf set(FloatBuffer data) {
		data.mark();
		this.x = data.get();
		this.y = data.get();
		this.z = data.get();
		this.w = data.get();
		data.reset();
		return this;
	}
	
	public Quaternionf set(ByteBuffer data) {
		data.mark();
		this.x = data.getFloat();
		this.y = data.getFloat();
		this.z = data.getFloat();
		this.w = data.getFloat();
		data.reset();
		return this;
	}
	
	public Quaternionf set(float[] data) {
		return set(data[0], data[1], data[2], data[3]);
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
		Quaternionf other = (Quaternionf) obj;
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
		return "Quaternionf[x=" + x + ", y=" + y + ", z=" + z + ", w=" + w + "]";
	}
	
}
