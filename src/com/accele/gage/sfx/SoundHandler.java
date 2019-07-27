package com.accele.gage.sfx;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;

import com.accele.gage.Cleanable;

public class SoundHandler implements Cleanable {

	private long device;
	private long context;
	
	public SoundHandler() {
		String defaultDeviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
		this.device = ALC10.alcOpenDevice(defaultDeviceName);
		this.context = ALC10.alcCreateContext(device, new int[] {0});
		if (!ALC10.alcMakeContextCurrent(context)) {
			throw new RuntimeException("Failed to create OpenAL context.");
		}
		ALCCapabilities caps = ALC.createCapabilities(device);
		AL.createCapabilities(caps);
	}
	
	@Override
	public void clean() {
		ALC10.alcMakeContextCurrent(0);
		ALC10.alcDestroyContext(context);
		if (!ALC10.alcCloseDevice(device)) {
			throw new RuntimeException("Failed to close OpenAL device.");
		}
	}
	
}
