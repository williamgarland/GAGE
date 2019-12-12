package com.accele.gage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
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

	public static final String DEFAULT_LOCATION = "/res";
	
	private String location;
	private String name;
	private InputStream inputStream;
	
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
	 * Constructs a {@code ResourceLocation} with the specified {@link java.io.InputStream InputStream}.
	 * <p>
	 * The {@code location} and {@code name} fields will be set to empty strings.
	 * </p>
	 * 
	 * @param inputStream the {@code InputStream} connected to the target resource
	 */
	public ResourceLocation(InputStream inputStream) {
		this.location = "";
		this.name = "";
		this.inputStream = inputStream;
	}
	
	/**
	 * Returns the location (containing directory) of the {@code Resource}.
	 * <p>
	 * Note that if this {@code ResourceLocation} was constructed with an {@code InputStream} rather than a location, this method will always return an empty string.
	 * </p>
	 * 
	 * @return the location of the {@code Resource}
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Returns the name of the {@code Resource} (typically the name of the file).
	 * <p>
	 * Note that if this {@code ResourceLocation} was constructed with an {@code InputStream} rather than a location, this method will always return an empty string.
	 * </p>
	 * 
	 * @return the name of the {@code Resource}
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the fully-qualified path of this {@code ResourceLocation}.
	 * For example, if the location is "some_folder/assets" and the name is "data.txt",
	 * then this method will return "some_folder/assets/data.txt".
	 * <p>
	 * Note that if this {@code ResourceLocation} was constructed with an {@code InputStream} rather than a location, this method will always return an empty string.
	 * </p>
	 * 
	 * @return the fully-qualified path of this {@code ResourceLocation}
	 */
	@Override
	public String toString() {
		return inputStream != null ? "" : location + (location.endsWith("/") ? "" : "/") + name;
	}
	
	/**
	 * Converts this {@code ResourceLocation} to a {@link java.nio.file.Path Path}.
	 * 
	 * @return the {@code Path} representation of this {@code ResourceLocation}
	 */
	public Path toPath() {
		return Paths.get(location + (location.endsWith("/") ? "" : "/") + name);
	}
	
	/**
	 * Returns whether this {@code ResourceLocation} already contains an {link java.io.InputStream InputStream}.
	 * <p>
	 * This method only returns true if an {@code InputStream} was passed into the constructor upon creating this {@code ResourceLocation}; 
	 * it does not return whether the specified location will produce a valid {@code InputStream}.
	 * To determine if the location is valid, use {@link #isLocationValid()}.
	 * </p>
	 * @return whether this {@code ResourceLocation} already contains an {@code InputStream}
	 */
	public boolean hasInputStream() {
		return inputStream != null;
	}
	
	/**
	 * Returns whether this {@code ResourceLocation} contains a valid path to the target resource.
	 * <p>
	 * The validity of a location is determined by checking if the resource pointed to by the path exists and if it is readable.
	 * </p>
	 * <p>
	 * Note that if this {@code ResourceLocation} has an {@code InputStream} (as determined by {@link #hasInputStream()}), this method will always return true.
	 * </p>
	 * @return whether this {@code ResourceLocation} contains a valid path to the target resource
	 */
	public boolean isLocationValid() {
		if (inputStream != null)
			return true;
		Path path = toPath();
		return Files.exists(path) && Files.isReadable(path);
	}
	
	/**
	 * Returns the {@link java.io.InputStream InputStream} associated with this {@code ResourceLocation}.
	 * <p>
	 * If this {@code ResourceLocation} was initialized with an {@code InputStream}, this method will return that stream.
	 * Otherwise, this method will initialize and open a new {@link java.io.FileInputStream FileInputStream} connected to the target resource 
	 * as specified by {@code location} and {@code name}.
	 * </p>
	 * 
	 * @return the {@code InputStream} associated with this {@code ResourceLocation}
	 * @throws GAGEException if the resource does not exist or cannot be read
	 */
	public InputStream getInputStream() throws GAGEException {
		try {
			return inputStream == null ? new FileInputStream(toString()) : inputStream;			
		} catch (FileNotFoundException e) {
			throw new GAGEException(e);
		}
	}
	
}
