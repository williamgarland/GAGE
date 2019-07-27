package com.accele.gage.gfx;

import com.accele.gage.Indexable;
import com.accele.gage.Tickable;

public class Animation implements Indexable, Tickable {

	private String registryId;
	private Texture[] frames;
	private int[] frameDurations;
	private int currentFrame;
	private int delta;
	
	public Animation(String registryId, Texture[] frames, int[] frameDurations) {
		this.registryId = registryId;
		this.frames = frames;
		this.frameDurations = frameDurations;
		this.currentFrame = 0;
		this.delta = frameDurations[currentFrame];
	}

	@Override
	public void tick() {
		delta--;
		if (delta <= 0) {
			currentFrame++;
			if (currentFrame >= frames.length)
				currentFrame = 0;
			delta = frameDurations[currentFrame];
		}
	}

	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	public Texture getCurrentFrame() {
		return frames[currentFrame];
	}

}
