package com.accele.gage.gfx;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Resource;
import com.accele.gage.math.Matrix4f;
import com.accele.gage.math.Vector2f;
import com.accele.gage.math.Vector2i;
import com.accele.gage.math.Vector3f;
import com.accele.gage.math.Vector3i;
import com.accele.gage.math.Vector4f;
import com.accele.gage.math.Vector4i;

public class Shader implements Indexable, Cleanable {

	private String registryId;
	private int programId;
	private Map<String, Integer> uniforms;
	
	public Shader(String registryId, Resource<String> vertexSource, Resource<String> fragmentSource) {
		this.registryId = registryId;
		this.uniforms = new HashMap<>();
		try {
			String vs = vertexSource.get();
			String fs = fragmentSource.get();
			init(vs, fs);
			bind();
			int numUniforms = GL20.glGetProgrami(programId, GL20.GL_ACTIVE_UNIFORMS);
			for (int i = 0; i < numUniforms; i++) {
				try (MemoryStack stack = MemoryStack.stackPush()) {
					IntBuffer size = stack.mallocInt(1);
					IntBuffer type = stack.mallocInt(1);
					String name = GL20.glGetActiveUniform(programId, i, size, type);
					uniforms.put(name, GL20.glGetUniformLocation(programId, name));
				}
			}
		} catch (GAGEException e) {
			e.printStackTrace();
		}
	}
	
	private void init(String vs, String fs) {
		int vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		int fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

		GL20.glShaderSource(vertexId, vs);
		GL20.glCompileShader(vertexId);

		if (GL20.glGetShaderi(vertexId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error compiling vertex shader, details:\n " + GL20.glGetShaderInfoLog(vertexId));
		}

		GL20.glShaderSource(fragmentId, fs);
		GL20.glCompileShader(fragmentId);

		if (GL20.glGetShaderi(fragmentId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error compiling fragment shader, details:\n" + GL20.glGetShaderInfoLog(fragmentId));
		}

		int programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, vertexId);
		GL20.glAttachShader(programId, fragmentId);
		GL20.glLinkProgram(programId);

		if (GL20.glGetProgrami(programId, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Error linking shader program, details:\n" + GL20.glGetProgramInfoLog(programId));
		}

		GL20.glDetachShader(programId, vertexId);
		GL20.glDetachShader(programId, fragmentId);

		GL20.glDeleteShader(vertexId);
		GL20.glDeleteShader(fragmentId);

		this.programId = programId;
	}
	
	public void bind() {
		GL20.glUseProgram(programId);
	}
	
	public void setUniform(String name, boolean value) {
		setUniform(name, value ? 1 : 0);
	}
	
	public void setUniform(String name, int value) {
		GL20.glUniform1i(uniforms.get(name), value);
	}
	
	public void setUniform(String name, Vector2i value) {
		GL20.glUniform2i(uniforms.get(name), value.x, value.y);
	}
	
	public void setUniform(String name, Vector3i value) {
		GL20.glUniform3i(uniforms.get(name), value.x, value.y, value.z);
	}
	
	public void setUniform(String name, Vector4i value) {
		GL20.glUniform4i(uniforms.get(name), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform(String name, float value) {
		GL20.glUniform1f(uniforms.get(name), value);
	}
	
	public void setUniform(String name, Vector2f value) {
		GL20.glUniform2f(uniforms.get(name), value.x, value.y);
	}
	
	public void setUniform(String name, Vector3f value) {
		GL20.glUniform3f(uniforms.get(name), value.x, value.y, value.z);
	}
	
	public void setUniform(String name, Vector4f value) {
		GL20.glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
	}
	
	public void setUniform(String name, Matrix4f value) {
		GL20.glUniformMatrix4fv(uniforms.get(name), false, value.toFloatBuffer());
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	@Override
	public void clean() {
		GL20.glUseProgram(0);
		GL20.glDeleteProgram(programId);
	}
	
}
