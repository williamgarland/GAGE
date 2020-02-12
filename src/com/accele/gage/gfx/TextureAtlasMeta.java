package com.accele.gage.gfx;

import com.accele.gage.gfx.TextureMeta;

public class TextureAtlasMeta {

	private TextureMeta meta;
	private TextureAtlasEntry[] entries;
	
	public TextureAtlasMeta(TextureMeta meta, TextureAtlasEntry[] entries) {
		this.meta = meta;
		this.entries = entries;
	}
	
	public TextureMeta getMeta() {
		return meta;
	}
	
	public TextureAtlasEntry[] getEntries() {
		return entries;
	}
	
}
