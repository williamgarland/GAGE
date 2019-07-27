package com.accele.gage.gfx;

import org.lwjgl.opengl.GL15;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Resource;

public class VBO implements Cleanable {

	private int vboId;
	private int target;
	private int size;
	
	public VBO(Resource<VBOMeta> data) {
		VBOMeta meta = null;
		try {
			meta = data.get();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
		this.vboId = GL15.glGenBuffers();
		this.target = meta.target;
		this.size = meta.data.length;
		GL15.glBindBuffer(target, vboId);
		GL15.glBufferData(target, meta.data, meta.usage);
		GL15.glBindBuffer(target, 0);
	}
	
	public void bind() {
		GL15.glBindBuffer(target, vboId);
	}
	
	public void unbind() {
		GL15.glBindBuffer(target, 0);
	}
	
	@Override
	public void clean() {
		unbind();
		GL15.glDeleteBuffers(vboId);
	}
	
	public int getVboId() {
		return vboId;
	}
	
	public int getTarget() {
		return target;
	}
	
	public int getSize() {
		return size;
	}
	
	public static class VBOMeta {
		private int target;
		private float[] data;
		private int usage;
		
		public VBOMeta(int target, float[] data, int usage) {
			this.target = target;
			this.data = data;
			this.usage = usage;
		}
		
		public float[] getData() {
			return data;
		}
		
		public int getTarget() {
			return target;
		}
		
		public int getUsage() {
			return usage;
		}
	}
	
}
