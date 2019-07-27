package com.accele.gage.gfx;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Resource;

public class Model implements Indexable, Cleanable {

	private String registryId;
	private VBO[] buffers;
	private IBO indices;
	
	public Model(String registryId, Resource<VBO[]> data, Resource<IBO> indices) {
		this.registryId = registryId;
		try {
			this.buffers = data.get();
			this.indices = indices.get();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
	}
	
	public void bind() {
		for (VBO buffer : buffers)
			buffer.bind();
		indices.bind();
	}
	
	public void unbind() {
		for (VBO buffer : buffers)
			buffer.unbind();
		indices.unbind();
	}
	
	public VBO[] getBuffers() {
		return buffers;
	}
	
	public IBO getIndices() {
		return indices;
	}

	@Override
	public void clean() {
		for (VBO buffer : buffers)
			buffer.clean();
		indices.clean();
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
}
