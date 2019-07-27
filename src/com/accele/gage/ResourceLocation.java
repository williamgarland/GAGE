package com.accele.gage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A class describing the location of a particular {@link com.accele.gage.Resource Resource}.
 * 
 * The default location of a {@code Resource} will be {@value #DEFAULT_LOCATION} if only the name is specified.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class ResourceLocation {

	public static final String DEFAULT_LOCATION = "/res/";
	
	private String location;
	private String name;
	
	/**
	 * Constructs a {@code ResourceLocation} with the specified location and name.
	 * 
	 * @param location the location of the {@code Resource} (typically the directory of the file)
	 * @param name the name of the {@code Resource} (typically the name of the file)
	 */
	public ResourceLocation(String location, String name) {
		this.location = location;
		this.name = name;
	}
	
	/**
	 * Constructs a {@code ResourceLocation} with the specified name and {@value #DEFAULT_LOCATION} as the location.
	 * 
	 * @param name the name of the {@code Resource} (typically the name of the file)
	 */
	public ResourceLocation(String name) {
		this(DEFAULT_LOCATION, name);
	}
	
	/**
	 * Returns the location (containing directory) of the {@code Resource}.
	 * 
	 * @return the location of the {@code Resource}
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Returns the name of the {@code Resource} (typically the name of the file).
	 * 
	 * @return the name of the {@code Resource}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the fully-qualified path of this {@code ResourceLocation}.
	 * For example, if the location is "/assets/" and the name is "data.txt",
	 * then this method will return "/assets/data.txt".
	 * 
	 * @return the fully-qualified path of this {@code ResourceLocation}
	 */
	@Override
	public String toString() {
		return location + name;
	}
	
	/**
	 * Converts this {@code ResourceLocation} to a {@link java.nio.file.Path Path}.
	 * 
	 * @return the {@code Path} representation of this {@code ResourceLocation}
	 */
	public Path toPath() {
		return Paths.get(location + name);
	}
	
}
