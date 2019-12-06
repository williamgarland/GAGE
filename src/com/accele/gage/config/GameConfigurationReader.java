package com.accele.gage.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.accele.gage.GAGE;
import com.accele.gage.GAGEException;
import com.accele.gage.GameConfiguration;
import com.accele.gage.Resource;

/**
 * A simple {@link com.accele.gage.GameConfiguration GameConfiguration} reader that reads engine properties from an external source.
 * <p>
 * This reader will import a list of engine properties and automatically inject them into the {@code GameConfiguration}.
 * An instance of this class can be created at any point after calling the {@link com.accele.gage.GAGE#init(int, int, String) init(int, int, String)} method, but it is most
 * preferable to use this class after initialization and before calling the {@link com.accele.gage.GAGE#start() start()} method
 * as there are some properties (i.e. {@code ticksPerSecond}) that cannot be injected after the engine has started.
 * </p>
 * <p>
 * The {@code GameConfigurationReader} will only read properties from the specified source if there is a {@code version} property present
 * and the version major matches the current version major specified in the {@code GameConfiguration}.
 * </p>
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameConfigurationReader {

	private Resource<InputStream> src;
	private GameConfiguration config;
	
	/**
	 * Creates a new {@link com.accele.gage.config.GameConfigurationReader GameConfigurationReader} using the specified external source.
	 * @param src the external source containing the game configuration properties
	 */
	public GameConfigurationReader(Resource<InputStream> src) {
		this.src = src;
		this.config = GAGE.getInstance().getConfig();
	}
	
	/**
	 * Retrieves and automatically injects game configuration properties from the specified external source into the {@link com.accele.gage.GameConfiguration GameConfiguration}.
	 * <p>
	 * An instance of this class can be created at any point after calling the {@link com.accele.gage.GAGE#init(int, int, String) init(int, int, String)} method, but it is most
	 * preferable to use this class after initialization and before calling the {@link com.accele.gage.GAGE#start() start()} method
	 * as there are some properties (i.e. {@code ticksPerSecond}) that cannot be injected after the engine has started.
	 * </p>
	 * <p>
	 * The {@code GameConfigurationReader} will only read properties from the specified source if there is a {@code version} property present
	 * and the version major matches the current version major specified in the {@code GameConfiguration}.
	 * </p>
	 * @throws GAGEException if the reader fails to read the properties from the external source
	 */
	public void read() throws GAGEException {
		Properties props = new Properties();
		try {
			props.load(src.get());
		} catch (IOException e) {
			throw new GAGEException(e);
		}
		String version = getProperty(props, "version");
		if (getVersionMajor(version) < getVersionMajor(GAGE.getInstance().getConfig().getVersion()))
			throw new GAGEException("The version major of the specified game configuration file " + getVersionMajor(version) 
				+ " is too low for the current context (" + getVersionMajor(GAGE.getInstance().getConfig().getVersion()) + ")");
		else if (getVersionMajor(version) > getVersionMajor(GAGE.getInstance().getConfig().getVersion()))
			throw new GAGEException("The version major of the specified game configuration file " + getVersionMajor(version) 
			+ " is too high for the current context (" + getVersionMajor(GAGE.getInstance().getConfig().getVersion()) + ")");
		double ticksPerSecond = Double.parseDouble(getProperty(props, "ticksPerSecond"));
		config.setTicksPerSecond(ticksPerSecond);
		boolean doEntityCollision = Boolean.parseBoolean(getProperty(props, "doEntityCollision"));
		config.setEntityCollision(doEntityCollision);
	}
	
	private String getProperty(Properties props, String property) throws GAGEException {
		String value = props.getProperty(property);
		if (value == null)
			throw new GAGEException("Property " + property + " not found in specified game configuration file.");
		return value;
	}
	
	private int getVersionMajor(String version) {
		return Integer.parseInt(version.split("\\.")[0].trim());
	}
	
}
