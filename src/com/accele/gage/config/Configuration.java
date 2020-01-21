package com.accele.gage.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.accele.gage.Indexable;

/**
 * A user-specified configuration class.
 * 
 * <p>
 * This class allows for custom properties to be used in the engine with key-value pairs.
 * Configurations can be set to be read-only or read-write, and can store any type of value. Configurations must be registered
 * with GAGE using the {@link com.accele.gage.Registry Registry} provided by {@link com.accele.gage.GAGE#getConfigurationRegistry getConfigurationRegistry()}
 * and must have unique registry IDs. Configurations can be read from externally using a {@link com.accele.gage.config.ConfigurationReader ConfigurationReader}
 * and can be written to externally using a {@link com.accele.gage.config.ConfigurationWriter ConfigurationWriter}.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class Configuration implements Indexable {

	private String registryId;
	private Map<String, Object> properties;
	private boolean canSetProperties;
	private boolean canAddProperties;
	private boolean canRemoveProperties;
	
	/**
	 * Constructs a read-write {@code Configuration} with the specified {@code registryId}.
	 * 
	 * @param registryId	the unique ID to use in the configuration {@link com.accele.gage.Registry Registry}
	 */
	public Configuration(String registryId) {
		this(registryId, new HashMap<>(), true, true, true);
	}
	
	/**
	 * Constructs a {@code Configuration} with the specified {@code registryId} and initial properties with the option of being read-only.
	 * 
	 * @param registryId	the unique ID to use in the configuration {@link com.accele.gage.Registry Registry}
	 * @param properties	the initial properties to store in the {@code Configuration}
	 * @param readOnly		whether this {@code Configuration} should be read-only
	 */
	public Configuration(String registryId, Map<String, Object> properties, boolean readOnly) {
		this(registryId, properties, !readOnly, !readOnly, !readOnly);
	}
	
	/**
	 * Constructs a {@code Configuration} with the specified {@code registryId} and initial properties with the option of specific actions being read-only.
	 * 
	 * @param registryId	the unique ID to use in the configuration {@link com.accele.gage.Registry Registry}
	 * @param properties	the initial properties to store in the {@code Configuration}
	 * @param canSetProperties	whether this {@code Configuration} should allow properties to be changed
	 * @param canAddProperties	whether this {@code Configuration} should allow more properties to be added
	 * @param canRemoveProperties	whether this {@code Configuration} should allow properties to be removed
	 */
	public Configuration(String registryId, Map<String, Object> properties, boolean canSetProperties, boolean canAddProperties, boolean canRemoveProperties) {
		this.registryId = registryId;
		this.canSetProperties = canSetProperties;
		this.canAddProperties = canAddProperties;
		this.canRemoveProperties = canRemoveProperties;
		if (!canSetProperties && !canAddProperties && !canRemoveProperties)
			this.properties = Collections.unmodifiableMap(properties);
		else
			this.properties = properties;
	}
	
	/**
	 * Adds the specified key-value pair to the {@code Configuration}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not allow adding properties
	 * or if it already contains the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @param value	the value of the property
	 * @throws UnsupportedOperationException	if the {@code Configuration} does not allow adding properties
	 * @throws IllegalArgumentException			if the {@code Configuration} already contains the specified {@code key}
	 */
	public void add(String key, Object value) {
		if (!canAddProperties)
			throw new UnsupportedOperationException("Adding properties to configuration \"" + registryId + "\" is not allowed.");
		if (properties.containsKey(key))
			throw new IllegalArgumentException("Duplicate configuration property " + key + " for configuration \"" + registryId + "\".");
		properties.put(key, value);
	}
	
	/**
	 * Retrieves the property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public Object get(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		return properties.get(key);
	}
	
	/**
	 * Retrieves the {@code byte} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a {@code byte} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public Object getByte(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return Byte.parseByte(properties.get(key).toString());			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Retrieves the {@code short} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a {@code short} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public Object getShort(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return Short.parseShort(properties.get(key).toString());			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Retrieves the {@code int} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain an {@code int} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public int getInt(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return Integer.parseInt(properties.get(key).toString());			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Retrieves the {@code long} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a {@code long} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public long getLong(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return Long.parseLong(properties.get(key).toString());			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Retrieves the {@code float} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a {@code float} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public float getFloat(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return Float.parseFloat(properties.get(key).toString());			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Retrieves the {@code double} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a {@code double} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public double getDouble(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return Double.parseDouble(properties.get(key).toString());			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Retrieves the {@code char} property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not contain a {@code char} property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws IllegalArgumentException	if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the property with the specified {@code key}
	 */
	public char getChar(String key) {
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		try {
			return properties.get(key).toString().charAt(0);			
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	/**
	 * Removes the property with the specified {@code key}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not allow removing properties
	 * or if it does not contain a property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @throws UnsupportedOperationException	if the {@code Configuration} does not allow removing properties
	 * @throws IllegalArgumentException			if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the value of the removed property
	 */
	public Object remove(String key) {
		if (!canRemoveProperties)
			throw new UnsupportedOperationException("Removing properties from configuration \"" + registryId + "\" is not allowed.");
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		return properties.remove(key);
	}
	
	/**
	 * Sets the property with the specified {@code key} to the specified {@code value}.
	 * 
	 * <p>
	 * This operation will fail if the {@code Configuration} does not allow setting properties
	 * or if it does not contain a property with the specified {@code key}.
	 * </p>
	 * 
	 * @param key	the key of the property
	 * @param newValue	the new value of the property
	 * @throws UnsupportedOperationException	if the {@code Configuration} does not allow setting properties
	 * @throws IllegalArgumentException			if the {@code Configuration} does not contain a property with the specified {@code key}
	 * @return the old value of the property
	 */
	public Object set(String key, Object newValue) {
		if (!canSetProperties)
			throw new UnsupportedOperationException("Setting properties in configuration \"" + registryId + "\" is not allowed.");
		if (!properties.containsKey(key))
			throw new IllegalArgumentException("Invalid property key " + key + " for configuration \"" + registryId + "\".");
		return properties.put(key, newValue);
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	/**
	 * Whether the {@code Configuration} allows setting properties.
	 * 
	 * @return whether the {@code Configuration} allows setting properties
	 */
	public boolean canSetProperties() {
		return canSetProperties;
	}
	
	/**
	 * Whether the {@code Configuration} allows adding properties.
	 * 
	 * @return whether the {@code Configuration} allows adding properties
	 */
	public boolean canAddProperties() {
		return canAddProperties;
	}
	
	/**
	 * Whether the {@code Configuration} allows removing properties.
	 * 
	 * @return whether the {@code Configuration} allows removing properties
	 */
	public boolean canRemoveProperties() {
		return canRemoveProperties;
	}
	
	/**
	 * Returns an immutable view of all properties in the {@code Configuration}.
	 * 
	 * @return an unmodifiable {@link java.util.Map Map} of all properties in the {@code Configuration}
	 */
	public Map<String, Object> getProperties() {
		return Collections.unmodifiableMap(properties);
	}
	
}
