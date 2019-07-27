package com.accele.gage.gfx;

import org.lwjgl.opengl.GL15;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Resource;

public class IBO implements Cleanable {

	private int iboId;
	private int size;
	
	public IBO(Resource<IBOMeta> data) {
		IBOMeta meta = null;
		try {
			meta = data.get();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
		this.iboId = GL15.glGenBuffers();
		this.size = meta.data.length;
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, meta.data, meta.usage);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	public void bind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iboId);
	}
	
	public void unbind() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	@Override
	public void clean() {
		unbind();
		GL15.glDeleteBuffers(iboId);
	}
	
	public int getIboId() {
		return iboId;
	}
	
	public int getSize() {
		return size;
	}
	
	public static class IBOMeta {
		private int[] data;
		private int usage;
		
		public IBOMeta(int[] data, int usage) {
			this.data = data;
			this.usage = usage;
		}
		
		public int[] getData() {
			return data;
		}
		
		public int getUsage() {
			return usage;
		}
	}
	
}
