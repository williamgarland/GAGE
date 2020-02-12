package com.accele.gage.gfx;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Resource;

public class TextureAtlas extends Texture implements Cleanable {

	private TextureAtlasEntry[] entries;
	
	public TextureAtlas(String registryId, Resource<TextureAtlasMeta> meta) {
		super(registryId, new Resource<>((src, args) -> meta.get().getMeta(), null));
		try {
			this.entries = meta.get().getEntries();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
	}
	
	public TextureAtlasEntry[] getEntries() {
		return entries;
	}
	
	public Texture getEntry(int index) {
		return subRegion(entries[index].getX(), entries[index].getY(), entries[index].getWidth(), entries[index].getHeight());
	}
	
}
