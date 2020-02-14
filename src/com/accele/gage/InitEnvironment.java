package com.accele.gage;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class InitEnvironment {

	private static final int DEFAULT_DRAW_BATCH_SIZE = 4096;
	private static final OutputStream DEFAULT_LOGGER_DESTINATION = System.out;
	private static final OutputStream DEFAULT_LOGGER_ERROR_DESTINATION = System.err;
	private static final String DEFAULT_LOGGER_PREFIX = "";
	private static final DateFormat DEFAULT_LOGGER_DATE_FORMAT = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss.SSSS");
	
	private int[] customWindowHints;
	private int drawBatchSize;
	private OutputStream loggerDestination;
	private OutputStream loggerErrorDestination;
	private String loggerPrefix;
	private DateFormat loggerDateFormat;
	
	public InitEnvironment() {
		this.customWindowHints = null;
		this.drawBatchSize = DEFAULT_DRAW_BATCH_SIZE;
		this.loggerDestination = DEFAULT_LOGGER_DESTINATION;
		this.loggerErrorDestination = DEFAULT_LOGGER_ERROR_DESTINATION;
		this.loggerPrefix = DEFAULT_LOGGER_PREFIX;
		this.loggerDateFormat = DEFAULT_LOGGER_DATE_FORMAT;
	}

	public int[] getCustomWindowHints() {
		return customWindowHints;
	}

	/**
	 * Sets custom window hints to use when initializing the {@link com.accele.gage.gfx.Window Window}.
	 * <p>
	 * GAGE uses the programmable pipeline version of the LWJGL API, and as a result also uses the GLFW API as the windowing system, 
	 * so all available window hints can be found in the {@link org.lwjgl.glfw.GLFW GLFW} class.
	 * By default, GAGE uses a predefined set of window hints upon initializing the {@code Window} (see the {@link com.accele.gage.gfx.Window Window} class for details).
	 * Calling this method will override these presets and instead use the specified custom hints.
	 * This method must be called prior to calling the {@link com.accele.gage.GAGE#init(int, int, String)} method; calling it after GAGE has already been initialized will have no effect.
	 * </p>
	 * <p>
	 * Note that this method expects hints to be provided in key-value order; the key for a given window hint is immediately followed by its matching value, 
	 * which then is followed by subsequent key-value pairs.
	 * </p>
	 * <p>Example:</p>
	 * <pre>
	 * setCustomWindowHints(new int[] { GLFW_SAMPLES, 4, GLFW_CONTEXT_VERSION_MAJOR, 3, GLFW_CONTEXT_VERSION_MINOR, 2 });
	 * </pre>
	 * @param hints		the array of window hints to use when initializing the {@code Window} in key-value order
	 * @see com.accele.gage.gfx.Window Window
	 * @see org.lwjgl.glfw.GLFW GLFW
	 */
	public InitEnvironment setCustomWindowHints(int[] customWindowHints) {
		if (customWindowHints.length % 2 != 0)
			throw new IllegalArgumentException("Invalid number of window hints; hints must be specified in key-value pairs.");
		this.customWindowHints = customWindowHints;
		return this;
	}
	
	/**
	 * Tells the engine to use the default window hints when initializing the {@link com.accele.gage.gfx.Window Window}.
	 * <p>
	 * GAGE uses a predefined set of window hints by default, so calling this method without also having called {@link #setCustomWindowHints(int[])} will have no effect.
	 * Additionally, this method must be called prior to calling the {@link com.accele.gage.GAGE#init(int, int, String)} method; calling it after GAGE has already been initialized will have no effect.
	 * </p>
	 * <p>
	 * For the list of default window hints used by GAGE, see the {@link com.accele.gage.gfx.Window Window} class.
	 * </p>
	 * @see com.accele.gage.gfx.Window Window
	 */
	public InitEnvironment useDefaultWindowHints() {
		this.customWindowHints = null;
		return this;
	}

	public int getDrawBatchSize() {
		return drawBatchSize;
	}

	public InitEnvironment setDrawBatchSize(int drawBatchSize) {
		this.drawBatchSize = drawBatchSize;
		return this;
	}
	
	public InitEnvironment useDefaultDrawBatchSize() {
		this.drawBatchSize = DEFAULT_DRAW_BATCH_SIZE;
		return this;
	}

	public OutputStream getLoggerDestination() {
		return loggerDestination;
	}

	public InitEnvironment setLoggerDestination(OutputStream loggerDestination) {
		this.loggerDestination = loggerDestination;
		return this;
	}

	public InitEnvironment useDefaultLoggerDestination() {
		this.loggerDestination = DEFAULT_LOGGER_DESTINATION;
		return this;
	}
	
	public OutputStream getLoggerErrorDestination() {
		return loggerErrorDestination;
	}

	public InitEnvironment setLoggerErrorDestination(OutputStream loggerErrorDestination) {
		this.loggerErrorDestination = loggerErrorDestination;
		return this;
	}
	
	public InitEnvironment useDefaultLoggerErrorDestination() {
		this.loggerErrorDestination = DEFAULT_LOGGER_ERROR_DESTINATION;
		return this;
	}

	public String getLoggerPrefix() {
		return loggerPrefix;
	}

	public InitEnvironment setLoggerPrefix(String loggerPrefix) {
		this.loggerPrefix = loggerPrefix;
		return this;
	}
	
	public InitEnvironment useDefaultLoggerPrefix() {
		this.loggerPrefix = DEFAULT_LOGGER_PREFIX;
		return this;
	}

	public DateFormat getLoggerDateFormat() {
		return loggerDateFormat;
	}

	public InitEnvironment setLoggerDateFormat(DateFormat loggerDateFormat) {
		this.loggerDateFormat = loggerDateFormat;
		return this;
	}
	
	public InitEnvironment useDefaultLoggerDateFormat() {
		this.loggerDateFormat = DEFAULT_LOGGER_DATE_FORMAT;
		return this;
	}
	
}
