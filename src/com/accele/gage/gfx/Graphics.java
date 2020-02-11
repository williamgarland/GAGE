package com.accele.gage.gfx;

import com.accele.gage.Cleanable;
import com.accele.gage.math.Matrix4f;

public interface Graphics extends Cleanable {

	public void begin();
	
	public void end();
	
	public void flush();
	
	public boolean isDrawing();
	
	public void drawRect(Matrix4f modelMatrix);
	
	public void drawRect(Matrix4f modelMatrix, Texture texture);
	
	public void drawRectOutline(Matrix4f modelMatrix);
	
	public void drawRectOutline(Matrix4f modelMatrix, Texture texture);
	
	public void drawString(String str, Matrix4f modelMatrix);
	
	public Color getColor();
	
	public void setColor(Color color);
	
	public Font getFont();
	
	public void setFont(Font font);
	
	public Matrix4f getViewMatrix();
	
	public void setViewMatrix(Matrix4f viewMatrix);
	
	public Matrix4f getProjectionMatrix();
	
	public void setProjectionMatrix(Matrix4f projectionMatrix);
	
}
