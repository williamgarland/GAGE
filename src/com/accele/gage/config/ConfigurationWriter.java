package com.accele.gage.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import com.accele.gage.GAGE;
import com.accele.gage.GAGEException;
import com.accele.gage.ResourceLocation;

/**
 * Writes a {@link com.accele.gage.config.Configuration Configuration} to a file.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigurationWriter {

	private String registryId;
	private ResourceLocation dest;
	
	public ConfigurationWriter(String registryId, ResourceLocation dest) {
		this.registryId = registryId;
		this.dest = dest;
	}
	
	public void write() throws GAGEException {
		try {
			Configuration config = GAGE.getInstance().getConfigurationRegistry().getEntry(registryId);
			Files.write(dest.toPath(), Arrays.asList(config.toString()));
		} catch (IOException e) {
			throw new GAGEException(e.getMessage(), e);
		}
	}
	
}
