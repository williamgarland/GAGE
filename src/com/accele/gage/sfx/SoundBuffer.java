package com.accele.gage.sfx;

import java.nio.ShortBuffer;

import org.lwjgl.openal.AL10;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Resource;

public class SoundBuffer implements Indexable, Cleanable {

	private String registryId;
	private int bufferId;
	private int linkedSourceId;
	
	public SoundBuffer(String registryId, Resource<SoundBufferMeta> data) {
		this.registryId = registryId;
		SoundBufferMeta meta = null;
		try {
			meta = data.get();
		} catch (GAGEException e) {
			e.printStackTrace();
		}
		
		this.bufferId = AL10.alGenBuffers();
		AL10.alBufferData(bufferId, meta.format, meta.data, meta.frequency);
		meta.cleanupTask.clean();
	}
	
	@Override
	public void clean() {
		if (linkedSourceId != 0) {
			AL10.alSourceStop(linkedSourceId);
			AL10.alSourcei(linkedSourceId, AL10.AL_BUFFER, 0);
		}
		AL10.alDeleteBuffers(bufferId);
	}
	
	public void setLinkedSourceId(int linkedSourceId) {
		this.linkedSourceId = linkedSourceId;
	}
	
	public int getBufferId() {
		return bufferId;
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	public static class SoundBufferMeta {
		private int format;
		private ShortBuffer data;
		private int frequency;
		private Cleanable cleanupTask;

		public SoundBufferMeta(int format, ShortBuffer data, int frequency, Cleanable cleanupTask) {
			this.format = format;
			this.data = data;
			this.frequency = frequency;
			this.cleanupTask = cleanupTask;
		}
		
		public int getFormat() {
			return format;
		}
		
		public ShortBuffer getData() {
			return data;
		}
		
		public int getFrequency() {
			return frequency;
		}
		
		public Cleanable getCleanupTask() {
			return cleanupTask;
		}
	}
	
}
