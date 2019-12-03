package com.accele.gage.config;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import com.accele.gage.GAGE;
import com.accele.gage.GAGEException;
import com.accele.gage.Resource;

/**
 * Writes a {@link com.accele.gage.config.Configuration Configuration} to a file.
 * <p>
 * This writer will output four built-in properties in addition to the user-defined ones: {@code __registryId__}, {@code __canSet__}, {@code __canAdd__}, and {@code __canRemove__}.
 * These properties are intrinsic to every {@code Configuration} instance and are required to create new {@code Configuration} instances, so there cannot be user-defined properties in
 * any given {@code Configuration} with these identifiers.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigurationWriter {

	private String registryId;
	private Resource<OutputStream> out;
	
	/**
	 * Creates a new {@link com.accele.gage.config.ConfigurationWriter ConfigurationWriter} using a {@link com.accele.gage.config.Configuration Configuration} 
	 * with the specified {@code registryId} and exports them to the specified external destination.
	 * @param registryId the {@code registryId} of the target {@code Configuration}
	 * @param out the external destination to write the {@code Configuration} properties to
	 */
	public ConfigurationWriter(String registryId, Resource<OutputStream> out) {
		this.registryId = registryId;
		this.out = out;
	}
	
	/**
	 * Retrieves a list of user-defined properties from the {@link com.accele.gage.config.Configuration Configuration} with the specified {@code registryId} and exports them to the specified external destination.
	 * <p>
	 * This writer will output four built-in properties in addition to the user-defined ones: {@code __registryId__}, {@code __canSet__}, {@code __canAdd__}, and {@code __canRemove__}.
	 * These properties are intrinsic to every {@code Configuration} instance and are required to create new {@code Configuration} instances, so there cannot be user-defined properties in
	 * any given {@code Configuration} with these identifiers.
	 * </p>
	 * @throws GAGEException if the writer fails to write the properties to the external destination
	 */
	public void write() throws GAGEException {
		try {
			Configuration config = GAGE.getInstance().getConfigurationRegistry().getEntry(registryId);
			Properties props = new Properties();
			Map<String, Object> configProps = config.getProperties();
			props.setProperty("__registryId__", config.getRegistryId());
			props.setProperty("__canSet__", String.valueOf(config.canSetProperties()));
			props.setProperty("__canAdd__", String.valueOf(config.canAddProperties()));
			props.setProperty("__canRemove__", String.valueOf(config.canRemoveProperties()));
			for (String key : configProps.keySet())
				props.setProperty(key, configProps.get(key).toString());
			props.store(out.get(), "Configuration file generated by GAGE version " + GAGE.getInstance().getConfig().getVersion() + ". Do not edit this line.");
		} catch (IOException e) {
			throw new GAGEException(e.getMessage(), e);
		}
	}
	
}
