package com.accele.gage.config;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.accele.gage.GAGE;
import com.accele.gage.GAGEException;
import com.accele.gage.ResourceLocation;

public class ConfigurationReader {

	private ResourceLocation src;
	
	public ConfigurationReader(ResourceLocation src) {
		this.src = src;
	}
	
	public Configuration read() throws GAGEException {
		try {
			List<String> lines = Files.readAllLines(src.toPath());
			String meta = lines.get(0);
			String[] metaParts = meta.trim().split(",");
			Map<String, Object> properties = new HashMap<>();
			for (String prop : lines.subList(1, lines.size())) {
				String[] parts = prop.trim().split("=");
				properties.put(parts[0].trim(), parts[1].trim());
			}
			Configuration config = new Configuration(metaParts[0].split("=")[1].trim(), properties, Boolean.parseBoolean(metaParts[1].split("=")[1].trim()), 
					Boolean.parseBoolean(metaParts[2].split("=")[1].trim()), Boolean.parseBoolean(metaParts[3].split("=")[1].trim()));
			GAGE.getInstance().getConfigurationRegistry().register(config);
			return config;
		} catch (IOException e) {
			throw new GAGEException(e.getMessage(), e);
		}
	}
	
}
