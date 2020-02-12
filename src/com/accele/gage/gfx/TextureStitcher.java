package com.accele.gage.gfx;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.accele.gage.GAGEException;
import com.accele.gage.Resource;
import com.accele.gage.gfx.TextureMeta;

public class TextureStitcher {

	private List<Resource<TextureMeta>> textures;
	private TextureMeta[] metas;
	
	public TextureStitcher(List<Resource<TextureMeta>> textures) {
		this.textures = textures;
	}
	
	public TextureAtlas stitchToTextureAtlas(String atlasRegistryId) throws GAGEException {
		this.metas = new TextureMeta[textures.size()];
		for (int i = 0; i < textures.size(); i++) {
			metas[i] = textures.get(i).get();
		}
		
		int[] normalizedSize = getNormalizedSize(metas);
		ByteBuffer[] normalizedTextures = getNormalizedTextures(metas, normalizedSize);
		int[] atlasDimensions = getAtlasDimensions(normalizedSize, metas.length);
		TextureAtlasEntry[] entries = getEntries(normalizedTextures, normalizedSize, atlasDimensions);
		ByteBuffer dest = BufferUtils.createByteBuffer(atlasDimensions[0] * atlasDimensions[1] * 4);
		
		for (int i = 0; i < normalizedTextures.length; i++) {
			ByteBuffer buf = normalizedTextures[i];
			
			for (int childY = 0; childY < entries[i].getPixelHeight(); childY++) {
				for (int childX = 0; childX < entries[i].getPixelWidth(); childX++) {
					int atlasY = entries[i].getPixelY() + childY;
					int atlasX = entries[i].getPixelX() + childX;
					
					byte r = buf.get();
					byte g = buf.get();
					byte b = buf.get();
					byte a = buf.get();
					
					int actualPos = atlasDimensions[0] * 4 * atlasY + atlasX * 4;
					
					dest.put(actualPos, r);
					dest.put(actualPos + 1, g);
					dest.put(actualPos + 2, b);
					dest.put(actualPos + 3, a);
				}
			}
		}
		
		TextureMeta tm = new TextureMeta(dest, atlasDimensions[0], atlasDimensions[1], GL11.GL_RGBA, metas[0].getParameters(), false);
		TextureAtlasMeta meta = new TextureAtlasMeta(tm, entries);
		
		return new TextureAtlas(atlasRegistryId, new Resource<>((src, args) -> meta, null));
	}
	
	public BufferedImage stitchToBufferedImage() throws GAGEException {
		this.metas = new TextureMeta[textures.size()];
		for (int i = 0; i < textures.size(); i++) {
			metas[i] = textures.get(i).get();
		}
		
		int[] normalizedSize = getNormalizedSize(metas);
		ByteBuffer[] normalizedTextures = getNormalizedTextures(metas, normalizedSize);
		int[] atlasDimensions = getAtlasDimensions(normalizedSize, metas.length);
		TextureAtlasEntry[] entries = getEntries(normalizedTextures, normalizedSize, atlasDimensions);
		ByteBuffer dest = BufferUtils.createByteBuffer(atlasDimensions[0] * atlasDimensions[1] * 4);
		
		for (int i = 0; i < normalizedTextures.length; i++) {
			ByteBuffer buf = normalizedTextures[i];
			
			for (int childY = 0; childY < entries[i].getPixelHeight(); childY++) {
				for (int childX = 0; childX < entries[i].getPixelWidth(); childX++) {
					int atlasX = entries[i].getPixelX() + childX;
					int atlasY = entries[i].getPixelY() + (entries[i].getPixelHeight() - childY - 1);
					
					byte r = buf.get();
					byte g = buf.get();
					byte b = buf.get();
					byte a = buf.get();
					
					int actualPos = atlasDimensions[0] * 4 * atlasY + atlasX * 4;
					
					dest.put(actualPos, r);
					dest.put(actualPos + 1, g);
					dest.put(actualPos + 2, b);
					dest.put(actualPos + 3, a);
				}
			}
		}
		
		return rawPixelsToBufferedImage(dest, atlasDimensions[0], atlasDimensions[1]);
	}
	
	private int[] getNormalizedSize(TextureMeta[] metas) {
		int maxWidth = 0;
		int maxHeight = 0;
		
		for (TextureMeta meta : metas) {
			if (meta.getWidth() > maxWidth)
				maxWidth = meta.getWidth();
			if (meta.getHeight() > maxHeight)
				maxHeight = meta.getHeight();
		}
		
		if (maxWidth < maxHeight)
			maxWidth = maxHeight;
		if (maxHeight < maxWidth)
			maxHeight = maxWidth;
		int n = getLargestPowerOf2(maxWidth);
		return new int[] { n, n };
	}
	
	private ByteBuffer[] getNormalizedTextures(TextureMeta[] metas, int[] normalizedSize) {
		ByteBuffer[] result = new ByteBuffer[metas.length];
		for (int i = 0; i < metas.length; i++) {
			result[i] = getNormalizedTexture(metas[i], normalizedSize);
		}
		return result;
	}
	
	private ByteBuffer getNormalizedTexture(TextureMeta meta, int[] normalizedSize) {
		if (meta.getWidth() == normalizedSize[0] && meta.getHeight() == normalizedSize[1])
			return meta.getPixels();
		
		ByteBuffer dest = BufferUtils.createByteBuffer(normalizedSize[0] * normalizedSize[1] * 4);
		for (int y = 0; y < normalizedSize[1]; y++) {
			if (y >= meta.getHeight()) {
				for (int x = 0; x < normalizedSize[0]; x++)
					dest.putInt(0);
			} else {
				for (int x = 0; x < normalizedSize[0]; x++) {
					if (x >= meta.getWidth())
						dest.putInt(0);
					else {
						dest.put(meta.getPixels().get());
						dest.put(meta.getPixels().get());
						dest.put(meta.getPixels().get());
						dest.put(meta.getPixels().get());
					}
				}
			}
		}
		dest.flip();
		return dest;
	}
	
	private int[] getAtlasDimensions(int[] normalizedSize, int numTextures) {
		int side = (int) Math.ceil(Math.sqrt(numTextures)) * normalizedSize[0];
		double log = Math.log(side) / Math.log(2);
		if (Math.ceil(log) != Math.floor(log))
			side = getLargestPowerOf2(side);
		return new int[] { side, side };
	}
	
	private int getLargestPowerOf2(int n) {
		int result = 2;
		int exp = 1;
		while (result < n) {
			result = (int) Math.pow(2, ++exp);
		}
		return result;
	}
	
	private TextureAtlasEntry[] getEntries(ByteBuffer[] normalizedTextures, int[] normalizedSize, int[] atlasDimensions) {
		TextureAtlasEntry[] result = new TextureAtlasEntry[normalizedTextures.length];
		
		int x = 0;
		int y = 0;
		for (int i = 0; i < result.length; i++) {
			if (x + normalizedSize[0] > atlasDimensions[0]) {
				x = 0;
				y += normalizedSize[1];
				
				if (y > atlasDimensions[1])
					break;
			}
			
			result[i] = new TextureAtlasEntry(x, y, normalizedSize[0], normalizedSize[1], atlasDimensions[0], atlasDimensions[1]);
			
			x += normalizedSize[0];
		}
		
		return result;
	}
	
	private BufferedImage rawPixelsToBufferedImage(ByteBuffer pixels, int width, int height) throws GAGEException {
		pixels.mark();
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				byte r = pixels.get();
				byte g = pixels.get();
				byte b = pixels.get();
				byte a = pixels.get();
				
				int pixel = convertColor(r, g, b, a);
				
				img.setRGB(x, y, pixel);
			}
		}
		
		pixels.reset();
		
		return img;
	}
	
	private int convertColor(byte r, byte g, byte b, byte a) {
		return ((a & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
	}
	
}
