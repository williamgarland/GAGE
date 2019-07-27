package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class Matrix4f {

	private static final Matrix4f IDENTITY = new Matrix4f(
			new float[] { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1 });

	public float m00;
	public float m01;
	public float m02;
	public float m03;
	public float m10;
	public float m11;
	public float m12;
	public float m13;
	public float m20;
	public float m21;
	public float m22;
	public float m23;
	public float m30;
	public float m31;
	public float m32;
	public float m33;

	private boolean identity;

	public Matrix4f() {
		this(IDENTITY);
	}

	public Matrix4f(Matrix4f other) {
		m00 = other.m00;
		m01 = other.m01;
		m02 = other.m02;
		m03 = other.m03;
		m10 = other.m10;
		m11 = other.m11;
		m12 = other.m12;
		m13 = other.m13;
		m20 = other.m20;
		m21 = other.m21;
		m22 = other.m22;
		m23 = other.m23;
		m30 = other.m30;
		m31 = other.m31;
		m32 = other.m32;
		m33 = other.m33;

		checkIdentity();
	}

	public Matrix4f(float[] data) {
		m00 = data[0];
		m01 = data[1];
		m02 = data[2];
		m03 = data[3];
		m10 = data[4];
		m11 = data[5];
		m12 = data[6];
		m13 = data[7];
		m20 = data[8];
		m21 = data[9];
		m22 = data[10];
		m23 = data[11];
		m30 = data[12];
		m31 = data[13];
		m32 = data[14];
		m33 = data[15];

		checkIdentity();
	}

	public Matrix4f(FloatBuffer data) {
		data.mark();
		m00 = data.get();
		m01 = data.get();
		m02 = data.get();
		m03 = data.get();
		m10 = data.get();
		m11 = data.get();
		m12 = data.get();
		m13 = data.get();
		m20 = data.get();
		m21 = data.get();
		m22 = data.get();
		m23 = data.get();
		m30 = data.get();
		m31 = data.get();
		m32 = data.get();
		m33 = data.get();
		data.reset();

		checkIdentity();
	}

	public Matrix4f(ByteBuffer data) {
		data.mark();
		m00 = data.getFloat();
		m01 = data.getFloat();
		m02 = data.getFloat();
		m03 = data.getFloat();
		m10 = data.getFloat();
		m11 = data.getFloat();
		m12 = data.getFloat();
		m13 = data.getFloat();
		m20 = data.getFloat();
		m21 = data.getFloat();
		m22 = data.getFloat();
		m23 = data.getFloat();
		m30 = data.getFloat();
		m31 = data.getFloat();
		m32 = data.getFloat();
		m33 = data.getFloat();
		data.reset();

		checkIdentity();
	}

	public Matrix4f set(Matrix4f other) {
		m00 = other.m00;
		m01 = other.m01;
		m02 = other.m02;
		m03 = other.m03;
		m10 = other.m10;
		m11 = other.m11;
		m12 = other.m12;
		m13 = other.m13;
		m20 = other.m20;
		m21 = other.m21;
		m22 = other.m22;
		m23 = other.m23;
		m30 = other.m30;
		m31 = other.m31;
		m32 = other.m32;
		m33 = other.m33;
		checkIdentity();
		return this;
	}

	public Matrix4f set(float[] data) {
		m00 = data[0];
		m01 = data[1];
		m02 = data[2];
		m03 = data[3];
		m10 = data[4];
		m11 = data[5];
		m12 = data[6];
		m13 = data[7];
		m20 = data[8];
		m21 = data[9];
		m22 = data[10];
		m23 = data[11];
		m30 = data[12];
		m31 = data[13];
		m32 = data[14];
		m33 = data[15];
		checkIdentity();
		return this;
	}

	public Matrix4f set(FloatBuffer data) {
		data.mark();
		m00 = data.get();
		m01 = data.get();
		m02 = data.get();
		m03 = data.get();
		m10 = data.get();
		m11 = data.get();
		m12 = data.get();
		m13 = data.get();
		m20 = data.get();
		m21 = data.get();
		m22 = data.get();
		m23 = data.get();
		m30 = data.get();
		m31 = data.get();
		m32 = data.get();
		m33 = data.get();
		data.reset();

		checkIdentity();

		return this;
	}

	public Matrix4f set(ByteBuffer data) {
		data.mark();
		m00 = data.getFloat();
		m01 = data.getFloat();
		m02 = data.getFloat();
		m03 = data.getFloat();
		m10 = data.getFloat();
		m11 = data.getFloat();
		m12 = data.getFloat();
		m13 = data.getFloat();
		m20 = data.getFloat();
		m21 = data.getFloat();
		m22 = data.getFloat();
		m23 = data.getFloat();
		m30 = data.getFloat();
		m31 = data.getFloat();
		m32 = data.getFloat();
		m33 = data.getFloat();
		data.reset();

		checkIdentity();

		return this;
	}

	private void checkIdentity() {
		identity = this.equals(IDENTITY);
	}

	public Vector4f getRow(int row) {
		switch (row) {
		case 0:
			return new Vector4f(m00, m01, m02, m03);
		case 1:
			return new Vector4f(m10, m11, m12, m13);
		case 2:
			return new Vector4f(m20, m21, m22, m23);
		case 3:
			return new Vector4f(m30, m31, m32, m33);
		default:
			return null;
		}
	}

	public Vector4f getCol(int col) {
		switch (col) {
		case 0:
			return new Vector4f(m00, m10, m20, m30);
		case 1:
			return new Vector4f(m01, m11, m21, m31);
		case 2:
			return new Vector4f(m02, m12, m22, m32);
		case 3:
			return new Vector4f(m03, m13, m23, m33);
		default:
			return null;
		}
	}

	public Matrix4f setCol(int col, Vector4f data) {
		switch (col) {
		case 0:
			m00 = data.x;
			m10 = data.y;
			m20 = data.z;
			m30 = data.w;
			break;
		case 1:
			m01 = data.x;
			m11 = data.y;
			m21 = data.z;
			m31 = data.w;
			break;
		case 2:
			m02 = data.x;
			m12 = data.y;
			m22 = data.z;
			m32 = data.w;
			break;
		case 3:
			m03 = data.x;
			m13 = data.y;
			m23 = data.z;
			m33 = data.w;
			break;
		default:
			break;
		}

		checkIdentity();
		return this;
	}

	public Matrix4f setRow(int row, Vector4f data) {
		switch (row) {
		case 0:
			m00 = data.x;
			m01 = data.y;
			m02 = data.z;
			m03 = data.w;
			break;
		case 1:
			m10 = data.x;
			m11 = data.y;
			m12 = data.z;
			m13 = data.w;
			break;
		case 2:
			m20 = data.x;
			m21 = data.y;
			m22 = data.z;
			m23 = data.w;
			break;
		case 3:
			m30 = data.x;
			m31 = data.y;
			m32 = data.z;
			m33 = data.w;
			break;
		default:
			break;
		}

		checkIdentity();
		return this;
	}

	public Matrix4f add(Matrix4f other) {
		m00 += other.m00;
		m01 += other.m01;
		m02 += other.m02;
		m03 += other.m03;
		m10 += other.m10;
		m11 += other.m11;
		m12 += other.m12;
		m13 += other.m13;
		m20 += other.m20;
		m21 += other.m21;
		m22 += other.m22;
		m23 += other.m23;
		m30 += other.m30;
		m31 += other.m31;
		m32 += other.m32;
		m33 += other.m33;
		checkIdentity();
		return this;
	}

	public Matrix4f add(float scalar) {
		m00 += scalar;
		m01 += scalar;
		m02 += scalar;
		m03 += scalar;
		m10 += scalar;
		m11 += scalar;
		m12 += scalar;
		m13 += scalar;
		m20 += scalar;
		m21 += scalar;
		m22 += scalar;
		m23 += scalar;
		m30 += scalar;
		m31 += scalar;
		m32 += scalar;
		m33 += scalar;
		checkIdentity();
		return this;
	}

	public Matrix4f sub(Matrix4f other) {
		m00 -= other.m00;
		m01 -= other.m01;
		m02 -= other.m02;
		m03 -= other.m03;
		m10 -= other.m10;
		m11 -= other.m11;
		m12 -= other.m12;
		m13 -= other.m13;
		m20 -= other.m20;
		m21 -= other.m21;
		m22 -= other.m22;
		m23 -= other.m23;
		m30 -= other.m30;
		m31 -= other.m31;
		m32 -= other.m32;
		m33 -= other.m33;
		checkIdentity();
		return this;
	}

	public Matrix4f sub(float scalar) {
		m00 -= scalar;
		m01 -= scalar;
		m02 -= scalar;
		m03 -= scalar;
		m10 -= scalar;
		m11 -= scalar;
		m12 -= scalar;
		m13 -= scalar;
		m20 -= scalar;
		m21 -= scalar;
		m22 -= scalar;
		m23 -= scalar;
		m30 -= scalar;
		m31 -= scalar;
		m32 -= scalar;
		m33 -= scalar;
		checkIdentity();
		return this;
	}

	public Matrix4f mulComponentWise(Matrix4f other) {
		m00 *= other.m00;
		m01 *= other.m01;
		m02 *= other.m02;
		m03 *= other.m03;
		m10 *= other.m10;
		m11 *= other.m11;
		m12 *= other.m12;
		m13 *= other.m13;
		m20 *= other.m20;
		m21 *= other.m21;
		m22 *= other.m22;
		m23 *= other.m23;
		m30 *= other.m30;
		m31 *= other.m31;
		m32 *= other.m32;
		m33 *= other.m33;
		checkIdentity();
		return this;
	}

	public Matrix4f mul(float scalar) {
		m00 *= scalar;
		m01 *= scalar;
		m02 *= scalar;
		m03 *= scalar;
		m10 *= scalar;
		m11 *= scalar;
		m12 *= scalar;
		m13 *= scalar;
		m20 *= scalar;
		m21 *= scalar;
		m22 *= scalar;
		m23 *= scalar;
		m30 *= scalar;
		m31 *= scalar;
		m32 *= scalar;
		m33 *= scalar;
		checkIdentity();
		return this;
	}

	public Matrix4f divComponentWise(Matrix4f other) {
		m00 /= other.m00;
		m01 /= other.m01;
		m02 /= other.m02;
		m03 /= other.m03;
		m10 /= other.m10;
		m11 /= other.m11;
		m12 /= other.m12;
		m13 /= other.m13;
		m20 /= other.m20;
		m21 /= other.m21;
		m22 /= other.m22;
		m23 /= other.m23;
		m30 /= other.m30;
		m31 /= other.m31;
		m32 /= other.m32;
		m33 /= other.m33;
		checkIdentity();
		return this;
	}

	public Matrix4f div(float scalar) {
		m00 /= scalar;
		m01 /= scalar;
		m02 /= scalar;
		m03 /= scalar;
		m10 /= scalar;
		m11 /= scalar;
		m12 /= scalar;
		m13 /= scalar;
		m20 /= scalar;
		m21 /= scalar;
		m22 /= scalar;
		m23 /= scalar;
		m30 /= scalar;
		m31 /= scalar;
		m32 /= scalar;
		m33 /= scalar;
		checkIdentity();
		return this;
	}

	public Matrix4f mul(Matrix4f right) {
		if (identity)
			return right;
		if (right.identity)
			return this;

		float nm00 = m00 * right.m00 + m10 * right.m01 + m20 * right.m02 + m30 * right.m03;
		float nm01 = m01 * right.m00 + m11 * right.m01 + m21 * right.m02 + m31 * right.m03;
		float nm02 = m02 * right.m00 + m12 * right.m01 + m22 * right.m02 + m32 * right.m03;
		float nm03 = m03 * right.m00 + m13 * right.m01 + m23 * right.m02 + m33 * right.m03;

		float nm10 = m00 * right.m10 + m10 * right.m11 + m20 * right.m12 + m30 * right.m13;
		float nm11 = m01 * right.m10 + m11 * right.m11 + m21 * right.m12 + m31 * right.m13;
		float nm12 = m02 * right.m10 + m12 * right.m11 + m22 * right.m12 + m32 * right.m13;
		float nm13 = m03 * right.m10 + m13 * right.m11 + m23 * right.m12 + m33 * right.m13;

		float nm20 = m00 * right.m20 + m10 * right.m21 + m20 * right.m22 + m30 * right.m23;
		float nm21 = m01 * right.m20 + m11 * right.m21 + m21 * right.m22 + m31 * right.m23;
		float nm22 = m02 * right.m20 + m12 * right.m21 + m22 * right.m22 + m32 * right.m23;
		float nm23 = m03 * right.m20 + m13 * right.m21 + m23 * right.m22 + m33 * right.m23;

		float nm30 = m00 * right.m30 + m10 * right.m31 + m20 * right.m32 + m30 * right.m33;
		float nm31 = m01 * right.m30 + m11 * right.m31 + m21 * right.m32 + m31 * right.m33;
		float nm32 = m02 * right.m30 + m12 * right.m31 + m22 * right.m32 + m32 * right.m33;
		float nm33 = m03 * right.m30 + m13 * right.m31 + m23 * right.m32 + m33 * right.m33;

		m00 = nm00;
		m01 = nm01;
		m02 = nm02;
		m03 = nm03;
		m10 = nm10;
		m11 = nm11;
		m12 = nm12;
		m13 = nm13;
		m20 = nm20;
		m21 = nm21;
		m22 = nm22;
		m23 = nm23;
		m30 = nm30;
		m31 = nm31;
		m32 = nm32;
		m33 = nm33;

		checkIdentity();

		return this;
	}

	public Vector4f mul(Vector4f right) {
		if (identity)
			return new Vector4f(right);

		float x = m00 * right.x + m10 * right.y + m20 * right.z + m30 * right.w;
		float y = m01 * right.x + m11 * right.y + m21 * right.z + m31 * right.w;
		float z = m02 * right.x + m12 * right.y + m22 * right.z + m32 * right.w;
		float w = m03 * right.x + m13 * right.y + m23 * right.z + m33 * right.w;

		return new Vector4f(x, y, z, w);
	}

	public Matrix4f div(Matrix4f right) {
		return mul(new Matrix4f(right.invert()));
	}

	public Matrix4f invert() {
		if (identity)
			return this;

		float det = 1f / determinant();

		return adjugate().mul(det);
	}

	public float determinant() {
		if (identity)
			return 1;

		float a = m00 * (m11 * (m22 * m33 - m32 * m23) - m21 * (m12 * m33 - m32 * m13) + m31 * (m12 * m23 - m22 * m13));
		float b = m10 * (m01 * (m22 * m33 - m32 * m23) - m21 * (m02 * m33 - m32 * m03) + m31 * (m02 * m23 - m22 * m03));
		float c = m20 * (m01 * (m12 * m33 - m32 * m13) - m11 * (m02 * m33 - m32 * m03) + m31 * (m02 * m13 - m12 * m03));
		float d = m30 * (m01 * (m12 * m23 - m22 * m13) - m11 * (m02 * m23 - m22 * m03) + m21 * (m02 * m13 - m12 * m03));

		return a - b + c - d;
	}

	public Matrix4f transpose() {
		if (identity)
			return this;

		float tm10 = m10;
		float tm20 = m20;
		float tm30 = m30;
		float tm21 = m21;
		float tm31 = m31;
		float tm23 = m23;

		m10 = m01;
		m20 = m02;
		m30 = m03;
		m21 = m12;
		m31 = m13;
		m23 = m32;
		m01 = tm10;
		m02 = tm20;
		m03 = tm30;
		m12 = tm21;
		m13 = tm31;
		m32 = tm23;

		checkIdentity();

		return this;
	}

	public Matrix4f adjugate() {
		if (identity)
			return this;

		float tm00 = m11 * (m22 * m33 - m32 * m23) - m21 * (m12 * m33 - m32 * m03) + m31 * (m12 * m23 - m22 * m03);
		float tm01 = m10 * (m22 * m33 - m32 * m23) - m20 * (m12 * m33 - m32 * m03) + m30 * (m12 * m23 - m22 * m03);
		float tm02 = m10 * (m21 * m33 - m31 * m23) - m20 * (m11 * m33 - m31 * m13) + m30 * (m11 * m23 - m21 * m13);
		float tm03 = m10 * (m21 * m32 - m31 * m22) - m20 * (m11 * m32 - m31 * m12) + m30 * (m11 * m22 - m21 * m12);

		float tm10 = m01 * (m22 * m33 - m32 * m23) - m21 * (m12 * m33 - m32 * m03) + m31 * (m12 * m23 - m22 * m03);
		float tm11 = m00 * (m22 * m33 - m32 * m23) - m20 * (m02 * m33 - m32 * m03) + m30 * (m02 * m23 - m22 * m03);
		float tm12 = m00 * (m21 * m33 - m31 * m23) - m20 * (m02 * m33 - m31 * m03) + m30 * (m02 * m23 - m21 * m03);
		float tm13 = m00 * (m21 * m32 - m31 * m22) - m20 * (m01 * m32 - m31 * m02) + m30 * (m01 * m22 - m21 * m02);

		float tm20 = m01 * (m12 * m33 - m32 * m13) - m22 * (m02 * m33 - m32 * m03) + m31 * (m02 * m13 - m12 * m03);
		float tm21 = m10 * (m12 * m33 - m32 * m13) - m20 * (m02 * m33 - m32 * m03) + m30 * (m02 * m13 - m12 * m03);
		float tm22 = m00 * (m11 * m33 - m31 * m13) - m10 * (m01 * m33 - m31 * m03) + m30 * (m01 * m13 - m11 * m03);
		float tm23 = m00 * (m11 * m32 - m31 * m12) - m10 * (m01 * m32 - m31 * m02) + m30 * (m01 * m12 - m11 * m02);

		float tm30 = m01 * (m12 * m23 - m22 * m13) - m11 * (m02 * m23 - m22 * m03) + m21 * (m02 * m13 - m12 * m03);
		float tm31 = m00 * (m12 * m23 - m22 * m13) - m10 * (m02 * m23 - m22 * m03) + m20 * (m02 * m13 - m12 * m03);
		float tm32 = m00 * (m11 * m23 - m21 * m13) - m10 * (m01 * m23 - m21 * m03) + m20 * (m01 * m13 - m11 * m03);
		float tm33 = m00 * (m11 * m22 - m21 * m12) - m10 * (m01 * m22 - m21 * m02) + m20 * (m01 * m12 - m11 * m02);

		m00 = tm00;
		m01 = -tm01;
		m02 = tm02;
		m03 = -tm03;
		m10 = -tm10;
		m11 = tm11;
		m12 = -tm12;
		m13 = tm13;
		m20 = tm20;
		m21 = -tm21;
		m22 = tm22;
		m23 = -tm23;
		m30 = -tm30;
		m31 = tm31;
		m32 = -tm32;
		m33 = tm33;

		return transpose();
	}

	public Matrix4f zero() {
		m00 = 0;
		m01 = 0;
		m02 = 0;
		m03 = 0;
		m10 = 0;
		m11 = 0;
		m12 = 0;
		m13 = 0;
		m20 = 0;
		m21 = 0;
		m22 = 0;
		m23 = 0;
		m30 = 0;
		m31 = 0;
		m32 = 0;
		m33 = 0;
		return this;
	}

	public Matrix4f toIdentity() {
		m00 = 1;
		m01 = 0;
		m02 = 0;
		m03 = 0;
		m10 = 0;
		m11 = 1;
		m12 = 0;
		m13 = 0;
		m20 = 0;
		m21 = 0;
		m22 = 1;
		m23 = 0;
		m30 = 0;
		m31 = 0;
		m32 = 0;
		m33 = 1;
		identity = true;
		return this;
	}

	public Matrix4f translate(Vector3f translation) {
		return translate(translation.x, translation.y, translation.z);
	}

	public Matrix4f translate(float x, float y, float z) {
		m30 += x;
		m31 += y;
		m32 += z;
		checkIdentity();
		return this;
	}

	public Matrix4f translate(float xyz) {
		return setTranslation(xyz, xyz, xyz);
	}

	public Matrix4f translateX(float x) {
		m30 += x;
		return this;
	}

	public Matrix4f translateY(float y) {
		m31 += y;
		return this;
	}

	public Matrix4f translateZ(float z) {
		m32 += z;
		return this;
	}

	public Matrix4f setTranslation(Vector3f translation) {
		return setTranslation(translation.x, translation.y, translation.z);
	}

	public Matrix4f setTranslation(float x, float y, float z) {
		m30 = x;
		m31 = y;
		m32 = z;
		checkIdentity();
		return this;
	}

	public Matrix4f setTranslation(float xyz) {
		return setTranslation(xyz, xyz, xyz);
	}

	public Matrix4f setTranslationX(float x) {
		m30 = x;
		return this;
	}

	public Matrix4f setTranslationY(float y) {
		m31 = y;
		return this;
	}

	public Matrix4f setTranslationZ(float z) {
		m32 = z;
		return this;
	}
	
	public Vector3f getTranslation() {
		return new Vector3f(m30, m31, m32);
	}

	public Matrix4f rotate(Quaternionf rotation) {
		return rotate(rotation, false);
	}

	public Matrix4f rotate(Quaternionf rotation, boolean assumeScale) {
		float sx = 1, sy = 1, sz = 1;
		if (assumeScale) {
			sx = m00;
			sy = m11;
			sz = m22;
		}
		float tm00 = rotation.w * rotation.w + rotation.x * rotation.x - rotation.z * rotation.z
				- rotation.y * rotation.y;
		float tm01 = 2 * rotation.x * rotation.y + 2 * rotation.z * rotation.w;
		float tm02 = 2 * rotation.x * rotation.z - 2 * rotation.y * rotation.w;
		float tm10 = -2 * rotation.z * rotation.w + 2 * rotation.x * rotation.y;
		float tm11 = rotation.y * rotation.y - rotation.z * rotation.z + rotation.w * rotation.w
				- rotation.x * rotation.x;
		float tm12 = 2 * rotation.y * rotation.z + 2 * rotation.x * rotation.w;
		float tm20 = 2 * rotation.y * rotation.w + 2 * rotation.x * rotation.z;
		float tm21 = 2 * rotation.y * rotation.z - 2 * rotation.x * rotation.w;
		float tm22 = rotation.z * rotation.z - rotation.y * rotation.y - rotation.x * rotation.x
				+ rotation.w * rotation.w;
		float nm00 = m00 * tm00 + m10 * tm01 + m20 * tm02;
		float nm01 = m01 * tm00 + m11 * tm01 + m21 * tm02;
		float nm02 = m02 * tm00 + m12 * tm01 + m22 * tm02;
		float nm03 = m03 * tm00 + m13 * tm01 + m23 * tm02;
		float nm10 = m00 * tm10 + m10 * tm11 + m20 * tm12;
		float nm11 = m01 * tm10 + m11 * tm11 + m21 * tm12;
		float nm12 = m02 * tm10 + m12 * tm11 + m22 * tm12;
		float nm13 = m03 * tm10 + m13 * tm11 + m23 * tm12;
		float nm20 = m00 * tm20 + m10 * tm21 + m20 * tm22;
		float nm21 = m01 * tm20 + m11 * tm21 + m21 * tm22;
		float nm22 = m02 * tm20 + m12 * tm21 + m22 * tm22;
		float nm23 = m03 * tm20 + m13 * tm21 + m23 * tm22;

		m00 = sx * nm00;
		m01 = nm01;
		m02 = nm02;
		m03 = nm03;
		m10 = nm10;
		m11 = sy * nm11;
		m12 = nm12;
		m13 = nm13;
		m20 = nm20;
		m21 = nm21;
		m22 = sz * nm22;
		m23 = nm23;

		checkIdentity();
		return this;
	}

	public Matrix4f rotate(AxisAnglef rotation) {
		return rotate(rotation.axis.x, rotation.axis.y, rotation.axis.z, rotation.angle);
	}

	public Matrix4f rotate(AxisAnglef rotation, boolean assumeScale) {
		return rotate(rotation.axis.x, rotation.axis.y, rotation.axis.z, rotation.angle, assumeScale);
	}

	public Matrix4f rotate(float x, float y, float z, float angle) {
		return rotate(x, y, z, angle, false);
	}

	public Matrix4f rotateX(float angle) {
		return rotate(1, 0, 0, angle);
	}

	public Matrix4f rotateY(float angle) {
		return rotate(0, 1, 0, angle);
	}

	public Matrix4f rotateZ(float angle) {
		return rotate(0, 0, 1, angle);
	}

	public Matrix4f rotateX(float angle, boolean assumeScale) {
		return rotate(1, 0, 0, angle, assumeScale);
	}

	public Matrix4f rotateY(float angle, boolean assumeScale) {
		return rotate(0, 1, 0, angle, assumeScale);
	}

	public Matrix4f rotateZ(float angle, boolean assumeScale) {
		return rotate(0, 0, 1, angle, assumeScale);
	}

	public Matrix4f rotate(float x, float y, float z, float angle, boolean assumeScale) {
		float sx = 1, sy = 1, sz = 1;
		if (assumeScale) {
			sx = m00;
			sy = m11;
			sz = m22;
		}
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		float icos = 1f - cos;
		float tm00 = x * x * icos + cos;
		float tm01 = x * y * icos + z * sin;
		float tm02 = x * z * icos - y * sin;
		float tm10 = x * y * icos - z * sin;
		float tm11 = y * y * icos + cos;
		float tm12 = y * z * icos + x * sin;
		float tm20 = x * z * icos + y * sin;
		float tm21 = y * z * icos - x * sin;
		float tm22 = z * z * icos + cos;

		float nm00 = m00 * tm00 + m10 * tm01 + m20 * tm02;
		float nm01 = m01 * tm00 + m11 * tm01 + m21 * tm02;
		float nm02 = m02 * tm00 + m12 * tm01 + m22 * tm02;
		float nm03 = m03 * tm00 + m13 * tm01 + m23 * tm02;

		float nm10 = m00 * tm10 + m10 * tm11 + m20 * tm12;
		float nm11 = m01 * tm10 + m11 * tm11 + m21 * tm12;
		float nm12 = m02 * tm10 + m12 * tm11 + m22 * tm12;
		float nm13 = m03 * tm10 + m13 * tm11 + m23 * tm12;

		float nm20 = m00 * tm20 + m10 * tm21 + m20 * tm22;
		float nm21 = m01 * tm20 + m11 * tm21 + m21 * tm22;
		float nm22 = m02 * tm20 + m12 * tm21 + m22 * tm22;
		float nm23 = m03 * tm20 + m13 * tm21 + m23 * tm22;

		m00 = sx * nm00;
		m01 = nm01;
		m02 = nm02;
		m03 = nm03;
		m10 = nm10;
		m11 = sy * nm11;
		m12 = nm12;
		m13 = nm13;
		m20 = nm20;
		m21 = nm21;
		m22 = sz * nm22;
		m23 = nm23;

		checkIdentity();
		return this;
	}

	public Matrix4f setRotation(Quaternionf rotation) {
		return setRotation(rotation, false);
	}

	public Matrix4f setRotation(Quaternionf rotation, boolean assumeScale) {
		float sx = 1, sy = 1, sz = 1;
		if (assumeScale) {
			sx = m00;
			sy = m11;
			sz = m22;
		}
		m00 = sx * (rotation.w * rotation.w + rotation.x * rotation.x - rotation.z * rotation.z
				- rotation.y * rotation.y);
		m01 = 2 * rotation.x * rotation.y + 2 * rotation.x * rotation.w;
		m02 = 2 * rotation.x * rotation.z - 2 * rotation.y * rotation.w;
		m10 = -2 * rotation.z * rotation.w + 2 * rotation.x * rotation.y;
		m11 = sy * (rotation.y * rotation.y - rotation.z * rotation.z + rotation.w * rotation.w
				- rotation.x * rotation.x);
		m12 = 2 * rotation.y * rotation.z + 2 * rotation.x * rotation.w;
		m20 = 2 * rotation.y * rotation.w + 2 * rotation.x * rotation.z;
		m21 = 2 * rotation.y * rotation.z - 2 * rotation.x * rotation.w;
		m22 = sz * (rotation.z * rotation.z - rotation.y * rotation.y - rotation.x * rotation.x
				+ rotation.w * rotation.w);
		checkIdentity();
		return this;
	}

	public Matrix4f setRotation(AxisAnglef rotation) {
		return setRotation(rotation.axis.x, rotation.axis.y, rotation.axis.z, rotation.angle);
	}

	public Matrix4f setRotation(AxisAnglef rotation, boolean assumeScale) {
		return setRotation(rotation.axis.x, rotation.axis.y, rotation.axis.z, rotation.angle, assumeScale);
	}

	public Matrix4f setRotation(float x, float y, float z, float angle) {
		return setRotation(x, y, z, angle, false);
	}

	public Matrix4f setRotationX(float angle) {
		return setRotation(1, 0, 0, angle);
	}

	public Matrix4f setRotationY(float angle) {
		return setRotation(0, 1, 0, angle);
	}

	public Matrix4f setRotationZ(float angle) {
		return setRotation(0, 0, 1, angle);
	}

	public Matrix4f setRotationX(float angle, boolean assumeScale) {
		return setRotation(1, 0, 0, angle, assumeScale);
	}

	public Matrix4f setRotationY(float angle, boolean assumeScale) {
		return setRotation(0, 1, 0, angle, assumeScale);
	}

	public Matrix4f setRotationZ(float angle, boolean assumeScale) {
		return setRotation(0, 0, 1, angle, assumeScale);
	}

	public Matrix4f setRotation(float x, float y, float z, float angle, boolean assumeScale) {
		float sx = 1, sy = 1, sz = 1;
		if (assumeScale) {
			sx = m00;
			sy = m11;
			sz = m22;
		}
		float sin = (float) Math.sin(angle);
		float cos = (float) Math.cos(angle);
		float icos = 1f - cos;
		float nm00 = cos + x * x * icos;
		float nm01 = x * y * icos + z * sin;
		float nm02 = x * z * icos - y * sin;
		float nm10 = x * y * icos - z * sin;
		float nm11 = cos + y * y * icos;
		float nm12 = y * z * icos + x * sin;
		float nm20 = x * z * icos + y * sin;
		float nm21 = y * z * icos - x * sin;
		float nm22 = cos + z * z * icos;

		m00 = sx * nm00;
		m01 = nm01;
		m02 = nm02;
		m10 = nm10;
		m11 = sy * nm11;
		m12 = nm12;
		m20 = nm20;
		m21 = nm21;
		m22 = sz * nm22;

		checkIdentity();

		return this;
	}

	public Matrix4f scale(Vector3f scale) {
		return scale(scale.x, scale.y, scale.z);
	}

	public Matrix4f scale(float x, float y, float z) {
		m00 *= x;
		m01 *= x;
		m02 *= x;
		m03 *= x;
		m10 *= y;
		m11 *= y;
		m12 *= y;
		m13 *= y;
		m20 *= z;
		m21 *= z;
		m22 *= z;
		m23 *= z;
		checkIdentity();
		return this;
	}

	public Matrix4f scale(float xyz) {
		return scale(xyz, xyz, xyz);
	}

	public Matrix4f scaleX(float x) {
		m00 *= x;
		m01 *= x;
		m02 *= x;
		m03 *= x;
		checkIdentity();
		return this;
	}

	public Matrix4f scaleY(float y) {
		m10 *= y;
		m11 *= y;
		m12 *= y;
		m13 *= y;
		checkIdentity();
		return this;
	}

	public Matrix4f scaleZ(float z) {
		m20 *= z;
		m21 *= z;
		m22 *= z;
		m23 *= z;
		checkIdentity();
		return this;
	}

	public Matrix4f setScaling(Vector3f scale) {
		return setScaling(scale.x, scale.y, scale.z);
	}

	public Matrix4f setScaling(float x, float y, float z) {
		m00 = x;
		m11 = y;
		m22 = z;
		checkIdentity();
		return this;
	}

	public Matrix4f setScaling(float xyz) {
		return setScaling(xyz, xyz, xyz);
	}

	public Matrix4f setScalingX(float x) {
		m00 = x;
		checkIdentity();
		return this;
	}

	public Matrix4f setScalingY(float y) {
		m11 = y;
		checkIdentity();
		return this;
	}

	public Matrix4f setScalingZ(float z) {
		m22 = z;
		checkIdentity();
		return this;
	}

	public Matrix4f ortho(float left, float right, float bottom, float top, float zNear, float zFar) {
		float rm00 = 2f / (right - left);
		float rm11 = 2f / (top - bottom);
		float rm22 = 2f / (zNear - zFar);
		float rm30 = (left + right) / (left - right);
		float rm31 = (top + bottom) / (bottom - top);
		float rm32 = (zFar + zNear) / (zNear - zFar);
		m30 = m00 * rm30 + m10 * rm31 + m20 * rm32 + m30;
		m31 = m01 * rm30 + m11 * rm31 + m21 * rm32 + m31;
		m32 = m02 * rm30 + m12 * rm31 + m22 * rm32 + m32;
		m33 = m03 * rm30 + m13 * rm31 + m23 * rm32 + m33;
		m00 *= rm00;
		m01 *= rm00;
		m02 *= rm00;
		m03 *= rm00;
		m10 *= rm11;
		m11 *= rm11;
		m12 *= rm11;
		m13 *= rm11;
		m20 *= rm22;
		m21 *= rm22;
		m22 *= rm22;
		m23 *= rm22;
		checkIdentity();
		return this;
	}

	public Matrix4f ortho2D(float left, float right, float bottom, float top) {
		return ortho(left, right, bottom, top, -1, 1);
	}

	public Matrix4f setOrtho(float left, float right, float bottom, float top, float zNear, float zFar) {
		m00 = 2f / (right - left);
		m01 = 0;
		m02 = 0;
		m03 = 0;
		m10 = 0;
		m11 = 2f / (top - bottom);
		m12 = 0;
		m13 = 0;
		m20 = 0;
		m21 = 0;
		m22 = -2f / (zFar - zNear);
		m23 = 0;
		m30 = -(right + left) / (right - left);
		m31 = -(top + bottom) / (top - bottom);
		m32 = -(zFar + zNear) / (zFar - zNear);
		m33 = 1;
		checkIdentity();
		return this;
	}

	public Matrix4f setOrtho2D(float left, float right, float bottom, float top) {
		return setOrtho(left, right, bottom, top, -1, 1);
	}

	public Matrix4f rotateAround(Quaternionf rotation, Vector3f origin) {
		float w2 = rotation.w * rotation.w, x2 = rotation.x * rotation.x;
		float y2 = rotation.y * rotation.y, z2 = rotation.z * rotation.z;
		float zw = rotation.z * rotation.w, dzw = zw + zw, xy = rotation.x * rotation.y, dxy = xy + xy;
		float xz = rotation.x * rotation.z, dxz = xz + xz, yw = rotation.y * rotation.w, dyw = yw + yw;
		float yz = rotation.y * rotation.z, dyz = yz + yz, xw = rotation.x * rotation.w, dxw = xw + xw;
		float rm00 = w2 + x2 - z2 - y2;
		float rm01 = dxy + dzw;
		float rm02 = dxz - dyw;
		float rm10 = -dzw + dxy;
		float rm11 = y2 - z2 + w2 - x2;
		float rm12 = dyz + dxw;
		float rm20 = dyw + dxz;
		float rm21 = dyz - dxw;
		float rm22 = z2 - y2 - x2 + w2;
		float tm30 = m00 * origin.x + m10 * origin.y + m20 * origin.z + m30;
		float tm31 = m01 * origin.x + m11 * origin.y + m21 * origin.z + m31;
		float tm32 = m02 * origin.x + m12 * origin.y + m22 * origin.z + m32;
		float nm00 = m00 * rm00 + m10 * rm01 + m20 * rm02;
		float nm01 = m01 * rm00 + m11 * rm01 + m21 * rm02;
		float nm02 = m02 * rm00 + m12 * rm01 + m22 * rm02;
		float nm03 = m03 * rm00 + m13 * rm01 + m23 * rm02;
		float nm10 = m00 * rm10 + m10 * rm11 + m20 * rm12;
		float nm11 = m01 * rm10 + m11 * rm11 + m21 * rm12;
		float nm12 = m02 * rm10 + m12 * rm11 + m22 * rm12;
		float nm13 = m03 * rm10 + m13 * rm11 + m23 * rm12;
		m20 = m00 * rm20 + m10 * rm21 + m20 * rm22;
		m21 = m01 * rm20 + m11 * rm21 + m21 * rm22;
		m22 = m02 * rm20 + m12 * rm21 + m22 * rm22;
		m23 = m03 * rm20 + m13 * rm21 + m23 * rm22;
		m00 = nm00;
		m01 = nm01;
		m02 = nm02;
		m03 = nm03;
		m10 = nm10;
		m11 = nm11;
		m12 = nm12;
		m13 = nm13;
		m30 = -nm00 * origin.x - nm10 * origin.y - m20 * origin.z + tm30;
		m31 = -nm01 * origin.x - nm11 * origin.y - m21 * origin.z + tm31;
		m32 = -nm02 * origin.x - nm12 * origin.y - m22 * origin.z + tm32;
		checkIdentity();
		return this;
	}
	
	public Matrix4f setRotationAround(Quaternionf rotation, Vector3f origin) {
		float w2 = rotation.w * rotation.w, x2 = rotation.x * rotation.x;
		float y2 = rotation.y * rotation.y, z2 = rotation.z * rotation.z;
		float zw = rotation.z * rotation.w, dzw = zw + zw, xy = rotation.x * rotation.y, dxy = xy + xy;
		float xz = rotation.x * rotation.z, dxz = xz + xz, yw = rotation.y * rotation.w, dyw = yw + yw;
		float yz = rotation.y * rotation.z, dyz = yz + yz, xw = rotation.x * rotation.w, dxw = xw + xw;
		m20 = dyw + dxz;
		m21 = dyz - dxw;
		m22 = z2 - y2 - x2 + w2;
		m23 = 0.0f;
		m00 = w2 + x2 - z2 - y2;
		m01 = dxy + dzw;
		m02 = dxz - dyw;
		m03 = 0.0f;
		m10 = -dzw + dxy;
		m11 = y2 - z2 + w2 - x2;
		m12 = dyz + dxw;
		m13 = 0.0f;
		m30 = -m00 * origin.x - m10 * origin.y - m20 * origin.z + origin.x;
		m31 = -m01 * origin.x - m11 * origin.y - m21 * origin.z + origin.y;
		m32 = -m02 * origin.x - m12 * origin.y - m22 * origin.z + origin.z;
		m33 = 1.0f;
		checkIdentity();
		return this;
	}

	// TODO: perspective, lookAlong, lookAt, frustum

	public float getM00() {
		return m00;
	}

	public Matrix4f setM00(float m00) {
		this.m00 = m00;
		return this;
	}

	public float getM01() {
		return m01;
	}

	public Matrix4f setM01(float m01) {
		this.m01 = m01;
		return this;
	}

	public float getM02() {
		return m02;
	}

	public Matrix4f setM02(float m02) {
		this.m02 = m02;
		return this;
	}

	public float getM03() {
		return m03;
	}

	public Matrix4f setM03(float m03) {
		this.m03 = m03;
		return this;
	}

	public float getM10() {
		return m10;
	}

	public Matrix4f setM10(float m10) {
		this.m10 = m10;
		return this;
	}

	public float getM11() {
		return m11;
	}

	public Matrix4f setM11(float m11) {
		this.m11 = m11;
		return this;
	}

	public float getM12() {
		return m12;
	}

	public Matrix4f setM12(float m12) {
		this.m12 = m12;
		return this;
	}

	public float getM13() {
		return m13;
	}

	public Matrix4f setM13(float m13) {
		this.m13 = m13;
		return this;
	}

	public float getM20() {
		return m20;
	}

	public Matrix4f setM20(float m20) {
		this.m20 = m20;
		return this;
	}

	public float getM21() {
		return m21;
	}

	public Matrix4f setM21(float m21) {
		this.m21 = m21;
		return this;
	}

	public float getM22() {
		return m22;
	}

	public Matrix4f setM22(float m22) {
		this.m22 = m22;
		return this;
	}

	public float getM23() {
		return m23;
	}

	public Matrix4f setM23(float m23) {
		this.m23 = m23;
		return this;
	}

	public float getM30() {
		return m30;
	}

	public Matrix4f setM30(float m30) {
		this.m30 = m30;
		return this;
	}

	public float getM31() {
		return m31;
	}

	public Matrix4f setM31(float m31) {
		this.m31 = m31;
		return this;
	}

	public float getM32() {
		return m32;
	}

	public Matrix4f setM32(float m32) {
		this.m32 = m32;
		return this;
	}

	public float getM33() {
		return m33;
	}

	public Matrix4f setM33(float m33) {
		this.m33 = m33;
		return this;
	}

	public boolean isIdentity() {
		return identity;
	}

	public FloatBuffer toFloatBuffer() {
		return toFloatBuffer(MathUtils.allocateFloatBuffer(16));
	}

	public FloatBuffer toFloatBuffer(FloatBuffer dest) {
		dest.mark();
		dest.put(m00);
		dest.put(m01);
		dest.put(m02);
		dest.put(m03);
		dest.put(m10);
		dest.put(m11);
		dest.put(m12);
		dest.put(m13);
		dest.put(m20);
		dest.put(m21);
		dest.put(m22);
		dest.put(m23);
		dest.put(m30);
		dest.put(m31);
		dest.put(m32);
		dest.put(m33);
		dest.reset();
		return dest;
	}

	public ByteBuffer toByteBuffer() {
		return toByteBuffer(MathUtils.allocateByteBuffer(16 * Float.BYTES));
	}

	public ByteBuffer toByteBuffer(ByteBuffer dest) {
		dest.mark();
		dest.putFloat(m00);
		dest.putFloat(m01);
		dest.putFloat(m02);
		dest.putFloat(m03);
		dest.putFloat(m10);
		dest.putFloat(m11);
		dest.putFloat(m12);
		dest.putFloat(m13);
		dest.putFloat(m20);
		dest.putFloat(m21);
		dest.putFloat(m22);
		dest.putFloat(m23);
		dest.putFloat(m30);
		dest.putFloat(m31);
		dest.putFloat(m32);
		dest.putFloat(m33);
		dest.reset();
		return dest;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(m00);
		result = prime * result + Float.floatToIntBits(m01);
		result = prime * result + Float.floatToIntBits(m02);
		result = prime * result + Float.floatToIntBits(m03);
		result = prime * result + Float.floatToIntBits(m10);
		result = prime * result + Float.floatToIntBits(m11);
		result = prime * result + Float.floatToIntBits(m12);
		result = prime * result + Float.floatToIntBits(m13);
		result = prime * result + Float.floatToIntBits(m20);
		result = prime * result + Float.floatToIntBits(m21);
		result = prime * result + Float.floatToIntBits(m22);
		result = prime * result + Float.floatToIntBits(m23);
		result = prime * result + Float.floatToIntBits(m30);
		result = prime * result + Float.floatToIntBits(m31);
		result = prime * result + Float.floatToIntBits(m32);
		result = prime * result + Float.floatToIntBits(m33);
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
		Matrix4f other = (Matrix4f) obj;
		if (Float.floatToIntBits(m00) != Float.floatToIntBits(other.m00))
			return false;
		if (Float.floatToIntBits(m01) != Float.floatToIntBits(other.m01))
			return false;
		if (Float.floatToIntBits(m02) != Float.floatToIntBits(other.m02))
			return false;
		if (Float.floatToIntBits(m03) != Float.floatToIntBits(other.m03))
			return false;
		if (Float.floatToIntBits(m10) != Float.floatToIntBits(other.m10))
			return false;
		if (Float.floatToIntBits(m11) != Float.floatToIntBits(other.m11))
			return false;
		if (Float.floatToIntBits(m12) != Float.floatToIntBits(other.m12))
			return false;
		if (Float.floatToIntBits(m13) != Float.floatToIntBits(other.m13))
			return false;
		if (Float.floatToIntBits(m20) != Float.floatToIntBits(other.m20))
			return false;
		if (Float.floatToIntBits(m21) != Float.floatToIntBits(other.m21))
			return false;
		if (Float.floatToIntBits(m22) != Float.floatToIntBits(other.m22))
			return false;
		if (Float.floatToIntBits(m23) != Float.floatToIntBits(other.m23))
			return false;
		if (Float.floatToIntBits(m30) != Float.floatToIntBits(other.m30))
			return false;
		if (Float.floatToIntBits(m31) != Float.floatToIntBits(other.m31))
			return false;
		if (Float.floatToIntBits(m32) != Float.floatToIntBits(other.m32))
			return false;
		if (Float.floatToIntBits(m33) != Float.floatToIntBits(other.m33))
			return false;
		return true;
	}

}
