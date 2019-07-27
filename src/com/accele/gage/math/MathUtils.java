package com.accele.gage.math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

class MathUtils {

	public static IntBuffer allocateIntBuffer(int capacity) {
		return ByteBuffer.allocateDirect(capacity * Integer.BYTES).order(ByteOrder.nativeOrder()).asIntBuffer();
	}
	
	public static FloatBuffer allocateFloatBuffer(int capacity) {
		return ByteBuffer.allocateDirect(capacity * Float.BYTES).order(ByteOrder.nativeOrder()).asFloatBuffer();
	}
	
	public static ByteBuffer allocateByteBuffer(int capacity) {
		return ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
	}
	
	public static double unrestrictedAcos(double a) {
		return a < -1 ? Math.PI : a > 1 ? 0 : Math.acos(a);
	}
	
}
