package com.accele.gage.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.accele.gage.GAGE;
import com.accele.gage.GAGEException;
import com.accele.gage.Resource;

/**
 * A simple {@link com.accele.gage.config.Configuration Configuration} reader that reads user-defined properties from an external source.
 * <p>
 * This reader will import a list of user-defined properties, create a new {@code Configuration} to store them in, and register the {@code Configuration} in the configuration registry.
 * The reader expects a minimum of four built-in properties to be defined: {@code __registryId__}, {@code __canSet__}, {@code __canAdd__}, and {@code __canRemove__}.
 * These properties are intrinsic to every {@code Configuration} instance and are required to create new {@code Configuration} instances, so there cannot be user-defined properties in
 * any given {@code Configuration} with these identifiers.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigurationReader {

	private Resource<InputStream> src;
	
	/**
	 * Creates a new {@link com.accele.gage.config.ConfigurationReader ConfigurationReader} using the specified external source.
	 * @param src the external source containing the configuration properties
	 */
	public ConfigurationReader(Resource<InputStream> src) {
		this.src = src;
	}
	
	/**
	 * Retrieves a list of user-defined properties, creates a new {@link com.accele.gage.config.Configuration Configuration} to store them in, 
	 * registers the {@code Configuration} in the configuration registry, and returns the {@code Configuration}.
	 * <p>
	 * The reader expects a minimum of four built-in properties to be defined: {@code __registryId__}, {@code __canSet__}, {@code __canAdd__}, and {@code __canRemove__}.
	 * These properties are intrinsic to every {@code Configuration} instance and are required to create new {@code Configuration} instances.
	 * </p>
	 * @return the {@code Configuration} created from the imported properties
	 * @throws GAGEException if the reader fails to read the properties from the external source
	 * @see com.accele.gage.GAGE#getConfigurationRegistry() getConfigurationRegistry()
	 */
	public Configuration read() throws GAGEException {
		try {
			Properties props = new Properties();
			props.load(src.get());
			Map<String, Object> configProps = new HashMap<>();
			for (Object key : props.keySet())
				configProps.put(key.toString(), props.getProperty(key.toString()));
			Configuration config = new Configuration(configProps.get("__registryId__").toString(), configProps, 
					Boolean.parseBoolean(configProps.get("__canSet__").toString()), 
					Boolean.parseBoolean(configProps.get("__canAdd__").toString()), 
					Boolean.parseBoolean(configProps.get("__canRemove__").toString()));
			GAGE.getInstance().getConfigurationRegistry().register(config);
			return config;
		} catch (IOException e) {
			throw new GAGEException(e);
		}
	}
	
	/**
	 * Retrieves a list of user-defined properties, loads them into the specified {@link com.accele.gage.config.Configuration Configuration}, 
	 * and returns the {@code Configuration}.
	 * <p>
	 * The reader expects a minimum of four built-in properties to be defined: {@code __registryId__}, {@code __canSet__}, {@code __canAdd__}, and {@code __canRemove__}.
	 * These properties are intrinsic to every {@code Configuration} instance and are required to create new {@code Configuration} instances.
	 * </p>
	 * @param dest the destination {@code Configuration} used to store the properties
	 * @return the {@code Configuration} created from the imported properties
	 * @throws GAGEException if the reader fails to read the properties from the external source
	 * @see com.accele.gage.GAGE#getConfigurationRegistry() getConfigurationRegistry()
	 */
	public Configuration read(Configuration dest) throws GAGEException {
		try {
			Properties props = new Properties();
			props.load(src.get());
			for (Object key : props.keySet()) {
				if (!key.toString().startsWith("__") && !key.toString().endsWith("__"))
				dest.add(key.toString(), props.getProperty(key.toString()));
			}
			return dest;
		} catch (IOException e) {
			throw new GAGEException(e);
		}
	}
	
}
