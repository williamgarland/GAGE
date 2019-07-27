package com.accele.gage;

/**
 * An engine object with a unique registry identifier.
 * 
 * <p>
 * All entries of a {@link com.accele.gage.Registry Registry} must implement this interface and
 * return a unique registry ID.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see com.accele.gage.Registry Registry
 */
public interface Indexable {

	/**
	 * Returns the registry ID to use in the {@link com.accele.gage.Registry Registry}.
	 * This ID must be unique within the registry.
	 * 
	 * @return the registry ID to use in the {@code Registry}
	 */
	public String getRegistryId();
	
}
