package com.accele.gage.gfx;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL33;
import org.lwjgl.system.MemoryUtil;

import com.accele.gage.Registry;
import com.accele.gage.Resource;
import com.accele.gage.ResourceLoaders;
import com.accele.gage.gfx.VBO.VBOMeta;
import com.accele.gage.math.Matrix4f;

public class BatchedRenderer implements Graphics {

	private final int batchSize;
	private Shader rectShader;
	private Shader texturedRectShader;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Color color;
	private Font font;
	private VBO rectModel;
	private VBO rectOutlineModel;
	private VBO batchVbo;
	private VAO rectVao;
	private VAO texturedRectVao;
	private VAO rectOutlineVao;
	private VAO texturedRectOutlineVao;
	private FloatBuffer batchBuffer;
	private int drawCount;
	private Texture texture;
	private boolean drawing;
	private int mode;
	private int vertexCount;
	
	public BatchedRenderer(int batchSize, Registry<Shader> shaderRegistry, Registry<Font> fontRegistry) {
		this.batchSize = batchSize;
		this.rectShader = new Shader("gage.rect",
				new Resource<>((src, args) -> "#version 330 core\n" + 
						"\n" + 
						"layout (location = 0) in vec3 position;\n" + 
						"layout (location = 1) in vec4 color;\n" + 
						"layout (location = 2) in mat4 model;\n" + 
						"\n" + 
						"out vec4 frag_color;\n" + 
						"\n" + 
						"uniform mat4 view;\n" + 
						"uniform mat4 projection;\n" + 
						"\n" + 
						"void main() {\n" + 
						"	frag_color = color;\n" + 
						"	gl_Position = projection * view * model * vec4(position, 1.0);\n" + 
						"}", null),
				new Resource<>((src, args) -> "#version 330 core\n" + 
						"\n" + 
						"in vec4 frag_color;\n" + 
						"\n" + 
						"out vec4 color;\n" + 
						"\n" + 
						"void main() {\n" + 
						"	color = frag_color;\n" + 
						"}", null));
		shaderRegistry.register(rectShader);
		this.texturedRectShader = new Shader("gage.textured_rect",
				new Resource<>((src, args) -> "#version 330 core\n" + 
						"\n" + 
						"layout (location = 0) in vec3 position;\n" + 
						"layout (location = 1) in vec4 textureCoords;\n" + 
						"layout (location = 2) in vec4 color;\n" + 
						"layout (location = 3) in mat4 model;\n" + 
						"\n" + 
						"out vec4 frag_color;\n" + 
						"out vec2 frag_textureCoords;\n" + 
						"\n" + 
						"uniform mat4 view;\n" + 
						"uniform mat4 projection;\n" + 
						"\n" + 
						"void main() {\n" + 
						"	frag_color = color;\n" + 
						"	frag_textureCoords = vec2((position.x * 0.5 + 0.5) * textureCoords.z + textureCoords.x, (position.y * 0.5 + 0.5) * textureCoords.w + textureCoords.y);\n" + 
						"	gl_Position = projection * view * model * vec4(position, 1.0);\n" + 
						"}", null),
				new Resource<>((src, args) -> "#version 330 core\n" + 
						"\n" + 
						"in vec4 frag_color;\n" + 
						"in vec2 frag_textureCoords;\n" + 
						"\n" + 
						"out vec4 color;\n" + 
						"\n" + 
						"uniform sampler2D sampler;\n" + 
						"\n" + 
						"void main() {\n" + 
						"	color = texture(sampler, frag_textureCoords) * frag_color;\n" + 
						"}", null));
		shaderRegistry.register(texturedRectShader);
		this.viewMatrix = new Matrix4f();
		this.projectionMatrix = new Matrix4f().setOrtho2D(-1, 1, -1, 1);
		this.color = Color.WHITE;
		this.font = new Font("gage.default", new Resource<>(ResourceLoaders.INTERNAL_FONT_LOADER,
				null, new java.awt.Font("Arial", 0, 30), true));
		fontRegistry.register(font);
		this.rectModel = new VBO(new Resource<>((src, args) -> new VBOMeta(GL15.GL_ARRAY_BUFFER, new float[] {
				-1f, -1f, 0f, 1f, -1f, 0f, 1f, 1f, 0f,
				-1f, -1f, 0f, 1f, 1f, 0f, -1f, 1f, 0f
				}, GL15.GL_STATIC_DRAW), null));
		this.batchVbo = new VBO(new Resource<>((src, args) -> new VBOMeta(GL15.GL_ARRAY_BUFFER, batchSize * 24 * Float.BYTES, GL15.GL_DYNAMIC_DRAW), null));
		this.batchBuffer = MemoryUtil.memAllocFloat(batchSize * 24);
		this.mode = -1;

		this.rectVao = new VAO(new Resource<>((src, args) -> new VAO.VertexAttributeLayout[] {
				new VAO.VertexAttributeLayout(0, 3, GL11.GL_FLOAT, false, 0, 0, rectModel),
				new VAO.VertexAttributeLayout(1, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 0, batchVbo, 1),
				new VAO.VertexAttributeLayout(2, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 4 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(3, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 8 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(4, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 12 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(5, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 16 * Float.BYTES, batchVbo, 1)
		}, null));

		this.texturedRectVao = new VAO(new Resource<>((src, args) -> new VAO.VertexAttributeLayout[] {
				new VAO.VertexAttributeLayout(0, 3, GL11.GL_FLOAT, false, 0, 0, rectModel),
				new VAO.VertexAttributeLayout(1, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 0, batchVbo, 1),
				new VAO.VertexAttributeLayout(2, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 4 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(3, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 8 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(4, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 12 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(5, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 16 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(6, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 20 * Float.BYTES, batchVbo, 1)
		}, null));
		
		this.rectOutlineModel = new VBO(new Resource<>((src, args) -> new VBOMeta(GL15.GL_ARRAY_BUFFER, new float[] {
				-1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0
		}, GL15.GL_STATIC_DRAW), null));
		this.rectOutlineVao = new VAO(new Resource<>((src, args) -> new VAO.VertexAttributeLayout[] {
				new VAO.VertexAttributeLayout(0, 3, GL11.GL_FLOAT, false, 0, 0, rectOutlineModel),
				new VAO.VertexAttributeLayout(1, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 0, batchVbo, 1),
				new VAO.VertexAttributeLayout(2, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 4 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(3, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 8 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(4, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 12 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(5, 4, GL11.GL_FLOAT, false, 20 * Float.BYTES, 16 * Float.BYTES, batchVbo, 1)
		}, null));

		this.texturedRectOutlineVao = new VAO(new Resource<>((src, args) -> new VAO.VertexAttributeLayout[] {
				new VAO.VertexAttributeLayout(0, 3, GL11.GL_FLOAT, false, 0, 0, rectOutlineModel),
				new VAO.VertexAttributeLayout(1, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 0, batchVbo, 1),
				new VAO.VertexAttributeLayout(2, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 4 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(3, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 8 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(4, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 12 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(5, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 16 * Float.BYTES, batchVbo, 1),
				new VAO.VertexAttributeLayout(6, 4, GL11.GL_FLOAT, false, 24 * Float.BYTES, 20 * Float.BYTES, batchVbo, 1)
		}, null));
	}

	@Override
	public void begin() {
		if (drawing)
			throw new IllegalStateException("Already drawing");
		drawing = true;
		drawCount = 0;
	}

	@Override
	public void end() {
		if (!drawing)
			throw new IllegalStateException("Not currently drawing");
		drawing = false;
		flushInternal();
	}

	@Override
	public void flush() {
		if (!drawing)
			throw new IllegalStateException("Not currently drawing");
		flushInternal();
	}
	
	private void flushInternal() {
		batchBuffer.flip();
		batchVbo.bind();
		//GL15.glBufferData(GL15.GL_ARRAY_BUFFER, batchBuffer.capacity() * Float.BYTES, GL15.GL_DYNAMIC_DRAW); // Buffer orphaning needed?
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, batchBuffer);

		if (texture != null) {
			texture.bind(0);
			texturedRectShader.bind();
			if (mode == GL11.GL_LINE_LOOP)
				texturedRectOutlineVao.bind();
			else
				texturedRectVao.bind();

			texturedRectShader.setUniform("view", viewMatrix);
			texturedRectShader.setUniform("projection", projectionMatrix);
		} else {
			rectShader.bind();
			if (mode == GL11.GL_LINE_LOOP)
				rectOutlineVao.bind();
			else
				rectVao.bind();

			rectShader.setUniform("view", viewMatrix);
			rectShader.setUniform("projection", projectionMatrix);
		}

		GL33.glDrawArraysInstanced(mode, 0, vertexCount, drawCount);
		
		batchBuffer.clear();
		drawCount = 0;
		texture = null;
	}

	private void checkBatchBuffer(int vertexSize) {
		if (batchBuffer.remaining() < vertexSize)
			flushInternal();
	}

	private void checkMode(int mode) {
		if (this.mode != -1 && this.mode != mode)
			flushInternal();
		this.mode = mode;
		this.vertexCount = mode == GL11.GL_LINE_LOOP ? 4 : 6;
	}
	
	private void checkTexture(Texture texture) {
		if ((this.texture == null && batchBuffer.remaining() < batchBuffer.capacity()) 
				|| (this.texture != null && this.texture.getTextureId() != texture.getTextureId()))
			flushInternal();
		this.texture = texture;
	}

	@Override
	public void drawRect(Matrix4f modelMatrix) {
		if (texture != null)
			flushInternal();
		checkBatchBuffer(20);
		checkMode(GL11.GL_TRIANGLES);
		batchBuffer.put(color.getR()).put(color.getG()).put(color.getB()).put(color.getA());
		batchBuffer.put(modelMatrix.toFloatBuffer());
		drawCount++;
	}

	@Override
	public void drawRect(Matrix4f modelMatrix, Texture texture) {
		checkBatchBuffer(24);
		checkMode(GL11.GL_TRIANGLES);
		checkTexture(texture);
		batchBuffer.put(texture.getX()).put(texture.getY()).put(texture.getWidth()).put(texture.getHeight());
		batchBuffer.put(color.getR()).put(color.getG()).put(color.getB()).put(color.getA());
		batchBuffer.put(modelMatrix.toFloatBuffer());
		drawCount++;
	}

	@Override
	public void drawRectOutline(Matrix4f modelMatrix) {
		if (texture != null)
			flushInternal();
		checkBatchBuffer(20);
		checkMode(GL11.GL_LINE_LOOP);
		batchBuffer.put(color.getR()).put(color.getG()).put(color.getB()).put(color.getA());
		batchBuffer.put(modelMatrix.toFloatBuffer());
		drawCount++;
	}

	@Override
	public void drawRectOutline(Matrix4f modelMatrix, Texture texture) {
		checkBatchBuffer(24);
		checkMode(GL11.GL_LINE_LOOP);
		checkTexture(texture);
		batchBuffer.put(texture.getX()).put(texture.getY()).put(texture.getWidth()).put(texture.getHeight());
		batchBuffer.put(color.getR()).put(color.getG()).put(color.getB()).put(color.getA());
		batchBuffer.put(modelMatrix.toFloatBuffer());
		drawCount++;
	}

	@Override
	public void drawString(String str, Matrix4f modelMatrix) {
		CharMeta[] chars = font.getChars();
		Texture texture = font.getTexture();

		float currentX = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			CharMeta meta = chars[(int) c];
			drawChar(texture, meta, currentX, modelMatrix);
			currentX += 2f * meta.getWidth();
		}
	}

	private void drawChar(Texture texture, CharMeta c, float currentX, Matrix4f modelMatrix) {
		Matrix4f mat = new Matrix4f().setTranslation(currentX + c.getWidth(), 0, 0).setScaling(c.getWidth(), c.getHeight(), 1);
		drawRect(new Matrix4f(modelMatrix).mul(mat), texture.subRegion(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
	}

	@Override
	public void clean() {
		rectModel.clean();
		batchVbo.clean();
		rectVao.clean();
		texturedRectVao.clean();
		MemoryUtil.memFree(batchBuffer);
		rectShader.clean();
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public Font getFont() {
		return font;
	}
	
	@Override
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	@Override
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setFont(Font font) {
		this.font = font;
	}
	
	@Override
	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}
	
	@Override
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public int getBatchSize() {
		return batchSize;
	}
	
	@Override
	public boolean isDrawing() {
		return drawing;
	}

}
