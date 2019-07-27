package com.accele.gage;

/**
 * Represents a way to retrieve data for a particular {@link com.accele.gage.Resource Resource}.
 * 
 * For specific resource loaders predefined by GAGE, see the {@link com.accele.gage.ResourceLoaders ResourceLoaders} class.
 * 
 * @param <T> the type of data this {@code ResourceLoader} will retrieve
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see ResourceLoaders
 */
public interface ResourceLoader<T> {

	/**
	 * Loads the data contained at the specified {@link com.accele.gage.ResourceLocation ResourceLocation} using any necessary additional arguments.
	 * 
	 * This method will throw a {@link com.accele.gage.GAGEException GAGEException} if the loader fails to retrieve the data.
	 * 
	 * @param src the {@code ResourceLocation} of the data
	 * @param additionalArgs any additional arguments required by the loader
	 * @return the data retrieved by the loader
	 * @throws GAGEException if the loader fails to retrieve the data
	 */
	public T load(ResourceLocation src, Object[] additionalArgs) throws GAGEException;
	
}
