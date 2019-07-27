package com.accele.gage.gfx;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Resource;

public class VAO implements Cleanable {

	private int vaoId;
	private VertexAttributeLayout[] layouts;
	
	public VAO(Resource<VertexAttributeLayout[]> layouts) {
		try {
			this.layouts = layouts.get();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
		
		this.vaoId = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoId);
		
		for (VertexAttributeLayout layout : this.layouts) {
			GL15.glBindBuffer(layout.buffer.getTarget(), layout.buffer.getVboId());
			GL20.glEnableVertexAttribArray(layout.index);
			GL20.glVertexAttribPointer(layout.index, layout.size, layout.type, layout.normalized, layout.stride, layout.pointer);
			GL15.glBindBuffer(layout.buffer.getTarget(), 0);
		}
		
		unbind();
	}
	
	public void bind() {
		GL30.glBindVertexArray(vaoId);
		for (VertexAttributeLayout layout : layouts)
			GL20.glEnableVertexAttribArray(layout.index);
	}
	
	public void unbind() {
		for (VertexAttributeLayout layout : layouts)
			GL20.glDisableVertexAttribArray(layout.index);
		GL30.glBindVertexArray(0);
	}
	
	@Override
	public void clean() {
		unbind();
		GL30.glDeleteVertexArrays(vaoId);
	}
	
	public void link(Model model) {
		
	}
	
	public static class VertexAttributeLayout {
		private int index;
		private int size;
		private int type;
		private boolean normalized;
		private int stride;
		private int pointer;
		private VBO buffer;
		
		public VertexAttributeLayout(int index, int size, int type, boolean normalized, int stride,
				int pointer, VBO buffer) {
			this.index = index;
			this.size = size;
			this.type = type;
			this.normalized = normalized;
			this.stride = stride;
			this.pointer = pointer;
			this.buffer = buffer;
		}
		
		public int getIndex() {
			return index;
		}
		
		public int getSize() {
			return size;
		}
		
		public int getType() {
			return type;
		}
		
		public boolean isNormalized() {
			return normalized;
		}
		
		public int getStride() {
			return stride;
		}
		
		public int getPointer() {
			return pointer;
		}
		
		public VBO getBuffer() {
			return buffer;
		}
	}
	
}
