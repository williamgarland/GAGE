package com.accele.gage.callbacks;

import com.accele.gage.sfx.SoundSource;

/**
 * A callback used for detecting changes in a {@link com.accele.gage.sfx.SoundSource SoundSource}.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public interface SoundSourceCallback {

	/**
	 * Invoked whenever a change in a {@link com.accele.gage.sfx.SoundSource SoundSource} occurs.
	 * A {@link com.accele.gage.sfx.SoundBuffer SoundBuffer} finishing playback is the most likely reason for this method to be invoked.
	 * 
	 * @param source the {@code SoundSource} that changed
	 */
	public void call(SoundSource source);
	
}
