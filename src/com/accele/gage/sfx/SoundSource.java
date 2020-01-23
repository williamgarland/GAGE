package com.accele.gage.sfx;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL10;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGE;
import com.accele.gage.Indexable;
import com.accele.gage.Tickable;
import com.accele.gage.callbacks.SoundSourceCallback;

public class SoundSource implements Indexable, Tickable, Cleanable {

	private String registryId;
	private int sourceId;
	private SoundBuffer linkedSound;
	private List<SoundSourceCallback> playbackFinishedCallbacks;
	private int state;
	
	public SoundSource(String registryId) {
		this.registryId = registryId;
		this.sourceId = AL10.alGenSources();
		this.playbackFinishedCallbacks = new ArrayList<>();
	}
	
	/**
	 * Adds a playback-finished callback to this {@code SoundSource}. The callback will be invoked whenever the source finishes playback of its linked {@code SoundBuffer}.
	 * 
	 * @param callback the {@link com.accele.gage.callbacks.SoundSourceCallback SoundSourceCallback} to add
	 */
	public void addPlaybackFinishedCallback(SoundSourceCallback callback) {
		playbackFinishedCallbacks.add(callback);
	}
	
	/**
	 * This method is not designed to be called at the same rate as normal tick methods; 
	 * it should be called as many times as possible per second in order to ensure events are properly handled.
	 */
	@Override
	public void tick() {
		updateSourceState();
	}
	
	private void updateSourceState() {
		int newState = AL10.alGetSourcei(sourceId, AL10.AL_SOURCE_STATE);
		if (state != newState && newState == AL10.AL_STOPPED) {
			state = newState;
			playbackFinishedCallbacks.forEach(c -> c.call(this));
		} else
			state = newState;
	}
	
	public void linkSound(String registryId) {
		this.linkedSound = GAGE.getInstance().getSoundBufferRegistry().getEntry(registryId);
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, linkedSound.getBufferId());
		linkedSound.setLinkedSourceId(sourceId);
	}
	
	public SoundBuffer getLinkedSound() {
		return linkedSound;
	}
	
	public float getVolume() {
		return AL10.alGetSourcef(sourceId, AL10.AL_GAIN);
	}
	
	public void setVolume(float volume) {
		AL10.alSourcef(sourceId, AL10.AL_GAIN, volume);
	}
	
	public float getPitch() {
		return AL10.alGetSourcef(sourceId, AL10.AL_PITCH);
	}
	
	public void setPitch(float pitch) {
		AL10.alSourcef(sourceId, AL10.AL_PITCH, pitch);
	}
	
	public void loop() {
		loop(1, 1);
	}
	
	public void loop(float volume) {
		loop(volume, 1);
	}
	
	public void loop(float volume, float pitch) {
		AL10.alSourcei(sourceId, AL10.AL_LOOPING, AL10.AL_TRUE);
		AL10.alSourcef(sourceId, AL10.AL_GAIN, GAGE.getInstance().getConfig().isMasterVolumeMuted() ? 0 
				: Math.min(GAGE.getInstance().getConfig().getMasterVolume(), volume));
		AL10.alSourcef(sourceId, AL10.AL_PITCH, Math.min(GAGE.getInstance().getConfig().getMasterPitch(), pitch));
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
		AL10.alSourcef(sourceId, AL10.AL_GAIN, GAGE.getInstance().getConfig().isMasterVolumeMuted() ? 0 
				: Math.min(GAGE.getInstance().getConfig().getMasterVolume(), volume));
		AL10.alSourcef(sourceId, AL10.AL_PITCH, Math.min(GAGE.getInstance().getConfig().getMasterPitch(), pitch));
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
	
	public int getState() {
		return state;
	}
	
}
