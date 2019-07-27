package com.accele.gage.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.accele.gage.Cleanable;
import com.accele.gage.Registry;
import com.accele.gage.Resource;
import com.accele.gage.ResourceLoaders;
import com.accele.gage.ResourceLocation;
import com.accele.gage.math.Matrix4f;
import com.accele.gage.math.Vector2f;
import com.accele.gage.math.Vector4f;

public class Graphics implements Cleanable {

	private Color color;
	private Font font;
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	private Model rect;
	private Model texturedRect;
	private VAO rectVAO;
	private VAO texturedRectVAO;
	private Shader rectShader;
	private Shader texturedRectShader;
	private Shader modelShader;
	private Shader texturedModelShader;
	private boolean useViewMatrix;
	
	public Graphics(Registry<Model> modelRegistry, Registry<Font> fontRegistry) {
		this.color = Color.WHITE;
		this.font = new Font("gage.font.default", new Resource<>(ResourceLoaders.INTERNAL_FONT_LOADER, 
				new ResourceLocation(""), new java.awt.Font("Arial", 0, 15), false));
		fontRegistry.register(font);
		this.projectionMatrix = new Matrix4f().setOrtho2D(-1, 1, -1, 1);
		this.rect = new Model("acl.rect", new Resource<>((src, args) -> new VBO[] {
				new VBO(new Resource<>((src2, args2) -> new VBO.VBOMeta(GL15.GL_ARRAY_BUFFER, new float[] {
						-1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0
				}, GL15.GL_STATIC_DRAW), null))
		}, null), new Resource<>((src, args) -> new IBO(new Resource<>((src2, args2) -> new IBO.IBOMeta(new int[] {
				0, 1, 2, 2, 3, 0
		}, GL15.GL_STATIC_DRAW), null)), null));
		this.texturedRect = new Model("acl.textured_rect", new Resource<>((src, args) -> new VBO[] {
				new VBO(new Resource<>((src2, args2) -> new VBO.VBOMeta(GL15.GL_ARRAY_BUFFER, new float[] {
						-1, -1, 0, 1, -1, 0, 1, 1, 0, -1, 1, 0
				}, GL15.GL_STATIC_DRAW), null)),
				new VBO(new Resource<>((src2, args2) -> new VBO.VBOMeta(GL15.GL_ARRAY_BUFFER, new float[] {
						0, 0, 1, 0, 1, 1, 0, 1
				}, GL15.GL_STATIC_DRAW), null))
		}, null), new Resource<>((src, args) -> new IBO(new Resource<>((src2, args2) -> new IBO.IBOMeta(new int[] {
				0, 1, 2, 2, 3, 0
		}, GL15.GL_STATIC_DRAW), null)), null));
		modelRegistry.register(rect);
		modelRegistry.register(texturedRect);
		
		this.rectVAO = new VAO(new Resource<>((src, args) -> new VAO.VertexAttributeLayout[] {
				new VAO.VertexAttributeLayout(0, 3, GL11.GL_FLOAT, false, 0, 0, rect.getBuffers()[0])
		}, null));
		
		this.texturedRectVAO = new VAO(new Resource<>((src, args) -> new VAO.VertexAttributeLayout[] {
				new VAO.VertexAttributeLayout(0, 3, GL11.GL_FLOAT, false, 0, 0, texturedRect.getBuffers()[0]),
				new VAO.VertexAttributeLayout(1, 2, GL11.GL_FLOAT, false, 0, 0, texturedRect.getBuffers()[1]),
		}, null));
		
		rectShader = new Shader(new Resource<>((src, args) -> "#version 330 core\n"
				+ "layout(location = 0) in vec3 position; uniform mat4 model; uniform mat4 view; uniform mat4 projection;"
				+ "void main() { gl_Position = projection * view * model * vec4(position, 1); }", null), 
				new Resource<>((src, args) -> "#version 330 core\nout vec4 color; uniform vec4 frag_color;"
						+ "void main() { color = frag_color; }", null));
		
		texturedRectShader = new Shader(new Resource<>((src, args) -> "#version 330 core\n"
				+ "layout(location = 0) in vec3 position; layout(location = 1) in vec2 textureCoords;"
				+ "out vec2 frag_textureCoords; uniform mat4 model; uniform mat4 view; uniform mat4 projection; uniform vec2 offset; uniform vec2 size;"
				+ "void main() { gl_Position = projection * view * model * vec4(position, 1); frag_textureCoords = textureCoords * size + offset; }", null), 
				new Resource<>((src, args) -> "#version 330 core\n in vec2 frag_textureCoords; out vec4 color; uniform sampler2D sampler; uniform vec4 frag_color;"
						+ "void main() { color = texture(sampler, frag_textureCoords) * frag_color; }", null));
		
		this.viewMatrix = new Matrix4f();
		this.useViewMatrix = true;
	}
	
	public void drawRect(float x, float y, float width, float height) {
		Matrix4f mat = new Matrix4f();
		mat.setTranslation(x + width / 2, y + height / 2, 0);
		mat.setScaling(width, height, 1);
		drawRect(mat);
	}
	
	public void drawRect(float x, float y, float width, float height, Texture texture) {
		Matrix4f mat = new Matrix4f();
		mat.setTranslation(x + width / 2, y + height / 2, 0);
		mat.setScaling(width, height, 1);
		drawRect(mat, texture);
	}
	
	public void drawRectOutline(float x, float y, float width, float height) {
		Matrix4f mat = new Matrix4f();
		mat.setTranslation(x + width / 2, y + height / 2, 0);
		mat.setScaling(width, height, 1);
		drawRectOutline(mat);
	}
	
	public void drawRectOutline(float x, float y, float width, float height, Texture texture) {
		Matrix4f mat = new Matrix4f();
		mat.setTranslation(x + width / 2, y + height / 2, 0);
		mat.setScaling(width, height, 1);
		drawRectOutline(mat, texture);
	}
	
	public void drawRect(Matrix4f modelMatrix) {
		rectVAO.bind();
		rectShader.bind();
		rect.getIndices().bind();
		
		rectShader.setUniform("projection", projectionMatrix);
		rectShader.setUniform("view", useViewMatrix ? viewMatrix : new Matrix4f());
		rectShader.setUniform("model", modelMatrix);
		rectShader.setUniform("frag_color", new Vector4f(color.getR(), color.getG(), color.getB(), color.getA()));
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, rect.getIndices().getSize(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void drawRect(Matrix4f modelMatrix, Texture texture) {
		texturedRectVAO.bind();
		texture.bind(0);
		texturedRectShader.bind();
		texturedRect.getIndices().bind();
		
		texturedRectShader.setUniform("offset", new Vector2f(texture.getX(), texture.getY()));
		texturedRectShader.setUniform("size", new Vector2f(texture.getWidth(), texture.getHeight()));
		texturedRectShader.setUniform("projection", projectionMatrix);
		texturedRectShader.setUniform("view", useViewMatrix ? viewMatrix : new Matrix4f());
		texturedRectShader.setUniform("model", modelMatrix);
		texturedRectShader.setUniform("frag_color", new Vector4f(color.getR(), color.getG(), color.getB(), color.getA()));
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, texturedRect.getIndices().getSize(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void drawRectOutline(Matrix4f modelMatrix) {
		rectVAO.bind();
		rectShader.bind();
		rect.getIndices().bind();
		
		rectShader.setUniform("projection", viewMatrix);
		rectShader.setUniform("view", useViewMatrix ? viewMatrix : new Matrix4f());
		rectShader.setUniform("model", modelMatrix);
		rectShader.setUniform("frag_color", new Vector4f(color.getR(), color.getG(), color.getB(), color.getA()));
		
		GL11.glDrawElements(GL11.GL_LINE_LOOP, rect.getIndices().getSize(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void drawRectOutline(Matrix4f modelMatrix, Texture texture) {
		texturedRectVAO.bind();
		texture.bind(0);
		texturedRectShader.bind();
		texturedRect.getIndices().bind();
		
		texturedRectShader.setUniform("offset", new Vector2f(texture.getX(), texture.getY()));
		texturedRectShader.setUniform("size", new Vector2f(texture.getWidth(), texture.getHeight()));
		texturedRectShader.setUniform("projection", projectionMatrix);
		texturedRectShader.setUniform("view", useViewMatrix ? viewMatrix : new Matrix4f());
		texturedRectShader.setUniform("model", modelMatrix);
		texturedRectShader.setUniform("frag_color", new Vector4f(color.getR(), color.getG(), color.getB(), color.getA()));
		
		GL11.glDrawElements(GL11.GL_LINE_LOOP, texturedRect.getIndices().getSize(), GL11.GL_UNSIGNED_INT, 0);
	}
	
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
		Matrix4f mat = new Matrix4f().setTranslation(currentX + c.getWidth(), 0, 0).scale(c.getWidth(), c.getHeight(), 1);
		drawRect(new Matrix4f(modelMatrix).mul(mat), texture.subRegion(c.getX(), c.getY(), c.getWidth(), c.getHeight()));
	}
	
	public void drawModel(Model model, Matrix4f modelMatrix) {
		modelShader.bind();
		model.bind();
		
		modelShader.setUniform("projection", projectionMatrix);
		modelShader.setUniform("view", useViewMatrix ? viewMatrix : new Matrix4f());
		modelShader.setUniform("model", modelMatrix);
		modelShader.setUniform("frag_color", new Vector4f(color.getR(), color.getG(), color.getB(), color.getA()));
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndices().getSize(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public void drawModel(Model model, Texture texture, Matrix4f modelMatrix) {
		texturedModelShader.bind();
		model.bind();
		texture.bind(0);
		
		texturedModelShader.setUniform("projection", projectionMatrix);
		texturedModelShader.setUniform("view", useViewMatrix ? viewMatrix : new Matrix4f());
		texturedModelShader.setUniform("model", modelMatrix);
		texturedModelShader.setUniform("frag_color", new Vector4f(color.getR(), color.getG(), color.getB(), color.getA()));
		
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndices().getSize(), GL11.GL_UNSIGNED_INT, 0);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Font getFont() {
		return font;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	
	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}
	
	public boolean useViewMatrix() {
		return useViewMatrix;
	}
	
	public void setUseViewMatrix(boolean useViewMatrix) {
		this.useViewMatrix = useViewMatrix;
	}
	
	@Override
	public void clean() {	
		rectShader.clean();
		texturedRectShader.clean();
		rectVAO.clean();
		texturedRectVAO.clean();
	}
}
