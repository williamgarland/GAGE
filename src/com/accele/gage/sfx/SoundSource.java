package com.accele.gage.sfx;

import org.lwjgl.openal.AL10;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.Indexable;

public class SoundSource implements Indexable, Cleanable {

	private String registryId;
	private int sourceId;
	
	public SoundSource(String registryId) {
		this.registryId = registryId;
		this.sourceId = AL10.alGenSources();
	}
	
	public void linkSound(String registryId) {
		SoundBuffer buffer = GAGE.getInstance().getSoundBufferRegistry().getEntry(registryId);
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer.getBufferId());
		buffer.setLinkedSourceId(sourceId);
	}
	
	public void loop() {
		loop(1, 1);
	}
	
	public void loop(float volume) {
		loop(volume, 1);
	}
	
	public void loop(float volume, float pitch) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_TRUE);
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
		AL10.alSourcePlay(sourceId);
	}
	
	public void play() {
		play(1, 1);
	}
	
	public void play(float volume) {
		play(volume, 1);
	}
	
	public void play(float volume, float pitch) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_FALSE);
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
		AL10.alSourcePlay(sourceId);
	}
	
	public void pause() {
		AL10.alSourcePause(sourceId);
	}
	
	public void resume() {
		play(1, 1);
	}
	
	public void resume(float volume) {
		play(volume, 1);
	}
	
	public void resume(float volume, float pitch) {
		play(volume, pitch);
	}
	
	public void stop() {
		AL10.alSourceStop(sourceId);
	}
	
	@Override
	public void clean() {
		AL10.alSourceStop(sourceId);
		AL10.alDeleteSources(sourceId);
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	public int getSourceId() {
		return sourceId;
	}
	
}
