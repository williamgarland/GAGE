package com.accele.gage.callbacks;

import com.accele.gage.Indexable;

/**
 * A callback used for detecting changes in a {@link com.accele.gage.Registry Registry}.
 * 
 * @param <T> the type of {@code Indexable} object in the registry
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see com.accele.gage.Registry Registry
 * @see com.accele.gage.Indexable Indexable
 */
public interface RegistryCallback<T extends Indexable> {

	/**
	 * Invoked whenever a change in the parent {@link com.accele.gage.Registry Registry} occurs.
	 * Adding and removing entries are the most likely reasons for this method to be invoked.
	 * 
	 * @param entry the entry that caused the change in the {@code Registry}
	 */
	public void call(T entry);
	
}
