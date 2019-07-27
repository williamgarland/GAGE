package com.accele.gage;

/**
 * An engine-specific resource used by various components throughout the engine.
 * 
 * Creating an instance of this class will not retrieve the specified resource;
 * instead the necessary address-related information is saved.
 * The actual data can be retrieved at any time using {@link #get()}.
 * 
 * @param <T> the type of data this {@code Resource} will accept and retrieve
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class Resource<T> {

	private ResourceLoader<T> loader;
	private ResourceLocation src;
	private Object[] additionalArgs;
	
	/**
	 * Constructs a {@code Resource} with the specified {@link com.accele.gage.ResourceLoader ResourceLoader}, {@link com.accele.gage.ResourceLocation ResourceLocation},
	 * and any additional loader arguments.
	 * 
	 * Calling this constructor will not retrieve the specified resource;
	 * instead the necessary address-related information is saved.
	 * The actual data can be retrieved at any time using {@link #get()}.
	 * 
	 * @param loader the {@code ResourceLoader} used to retrieve the resource data
	 * @param src the {@code ResourceLocation} at which the data is located
	 * @param loaderArgs any additional arguments the loader may require
	 */
	public Resource(ResourceLoader<T> loader, ResourceLocation src, Object... loaderArgs) {
		this.loader = loader;
		this.src = src;
		this.additionalArgs = loaderArgs;
	}
	
	/**
	 * Calls the {@link com.accele.gage.ResourceLoader ResourceLoader} this {@code Resource} contains using the saved {@link com.accele.gage.ResourceLocation ResourceLocation}
	 * and any additional arguments and returns the result.
	 * 
	 * This method will throw a {@link com.accele.gage.GAGEException GAGEException} if the loader fails to load the data.
	 * 
	 * @return the data provided by the {@code ResourceLoader}
	 * @throws GAGEException if the loader fails to load the data
	 */
	public T get() throws GAGEException {
		return loader.load(src, additionalArgs);
	}
	
	/**
	 * Returns the {@link com.accele.gage.ResourceLocation ResourceLocation} used by this {@code Resource}.
	 * 
	 * @return the {@code ResourceLocation} used by this {@code Resource}
	 */
	public ResourceLocation getSrc() {
		return src;
	}
	
}
