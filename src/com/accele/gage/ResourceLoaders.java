package com.accele.gage;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.libc.LibCStdlib;

import com.accele.gage.gfx.CharMeta;
import com.accele.gage.gfx.FontMeta;
import com.accele.gage.gfx.Texture;
import com.accele.gage.gfx.TextureMeta;
import com.accele.gage.sfx.SoundBuffer;
import com.accele.gage.tile.TileMapMeta;

/**
 * A utility class containing predefined instances of {@link com.accele.gage.ResourceLoader ResourceLoader}.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see ResourceLoader
 * @see Resource
 */
public class ResourceLoaders {

	private ResourceLoaders() {}

	/**
	 * A loader that produces a {@link com.accele.gage.gfx.Texture Texture} from a BMP file.
	 */
	public static ResourceLoader<TextureMeta> BMP_TEXTURE_LOADER = (src, args) -> {
		ByteBuffer b = null;

		try {
			SeekableByteChannel c = Files.newByteChannel(src.toPath());
			b = BufferUtils.createByteBuffer((int) c.size());
			while (c.read(b) > 0)
				;
			b.flip();
			c.close();
		} catch (IOException e) {
			throw new GAGEException(e);
		}

		byte[] header = new byte[54];
		b.get(header, 0, header.length);

		ByteBuffer headerBuffer = ByteBuffer.wrap(header).order(ByteOrder.nativeOrder());

		char verification0 = (char) headerBuffer.get();
		char verification1 = (char) headerBuffer.get();

		if (verification0 != 'B' || verification1 != 'M')
			throw new IllegalStateException("Invalid header for BMP file.");

		headerBuffer.getLong();
		headerBuffer.get();

		int dataPos = headerBuffer.getChar(); // 0x0A
		for (int i = 0; i < 5; i++)
			headerBuffer.get();
		int width = headerBuffer.getChar(); // 0x12
		for (int i = 0; i < 2; i++)
			headerBuffer.get();
		int height = headerBuffer.getChar(); // 0x16
		for (int i = 0; i < 9; i++)
			headerBuffer.get();
		int imageSize = headerBuffer.getChar(); // 0x22

		if (imageSize == 0)
			imageSize = width * height * 3;
		if (dataPos == 0)
			dataPos = 54;

		b.position(dataPos);
		ByteBuffer dataBuffer = b.slice();
		dataBuffer.limit(imageSize);

		return new TextureMeta(dataBuffer, width, height, GL12.GL_BGR);
	};

	/**
	 * A loader that produces a {@link com.accele.gage.gfx.Texture Texture} from a PNG file.
	 */
	public static final ResourceLoader<TextureMeta> PNG_TEXTURE_LOADER = (src, args) -> {
		BufferedImage image = null;
		try {
			image = ImageIO.read(src.toPath().toFile());
		} catch (IOException e) {
			throw new GAGEException(e);
		}

		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		image = newImage;

		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = pixels[i * width + j];

				buffer.put((byte) ((pixel >> 16) & 0xff)); // r
				buffer.put((byte) ((pixel >> 8) & 0xff)); // g
				buffer.put((byte) ((pixel >> 0) & 0xff)); // b
				buffer.put((byte) ((pixel >> 24) & 0xff)); // a
			}
		}

		buffer.flip();

		return new TextureMeta(buffer, width, height, GL11.GL_RGBA);
	};

	/**
	 * A loader that produces a {@link com.accele.gage.gfx.Shader Shader} from a text file.
	 */
	public static final ResourceLoader<String> SHADER_LOADER = (src, args) -> {
		try {
			return Files.readAllLines(src.toPath()).stream().reduce("", (a, b) -> a + "\n" + b);
		} catch (IOException e) {
			throw new GAGEException(e);
		}
	};

	/**
	 * A loader that produces a {@link com.accele.gage.sfx.SoundBuffer.SoundBufferMeta SoundBufferMeta} from an OGG file.
	 */
	public static final ResourceLoader<SoundBuffer.SoundBufferMeta> OGG_SOUND_LOADER = (src, args) -> {
		MemoryStack.stackPush();
		IntBuffer channelsBuffer = MemoryStack.stackMallocInt(1);
		MemoryStack.stackPush();
		IntBuffer sampleRateBuffer = MemoryStack.stackMallocInt(1);

		ShortBuffer rawAudioBuffer = STBVorbis.stb_vorbis_decode_filename(src.toString(), channelsBuffer,
				sampleRateBuffer);

		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		MemoryStack.stackPop();
		MemoryStack.stackPop();

		int format = -1;
		if (channels == 1) {
			format = AL10.AL_FORMAT_MONO16;
		} else if (channels == 2) {
			format = AL10.AL_FORMAT_STEREO16;
		}

		return new SoundBuffer.SoundBufferMeta(format, rawAudioBuffer, sampleRate,
				() -> LibCStdlib.free(rawAudioBuffer));
	};
	
	/**
	 * A loader that produces a {@link com.accele.gage.sfx.SoundBuffer.SoundBufferMeta SoundBufferMeta} from a WAV file.
	 */
	public static final ResourceLoader<SoundBuffer.SoundBufferMeta> WAV_SOUND_LOADER = (src, args) -> {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(src.toPath().toFile());
			AudioFormat sourceFormat = ais.getFormat();
			
			int format = -1;
			int channels = sourceFormat.getChannels();
			int sampleSizeBits = sourceFormat.getSampleSizeInBits();
			
			if (channels == 1 && sampleSizeBits == 8)
				format = AL10.AL_FORMAT_MONO8;
			else if (channels == 1 && sampleSizeBits == 16)
				format = AL10.AL_FORMAT_MONO16;
			else if (channels == 2 && sampleSizeBits == 8)
				format = AL10.AL_FORMAT_STEREO8;
			else if (channels == 2 && sampleSizeBits == 16)
				format = AL10.AL_FORMAT_STEREO16;
			else if (channels != 1 && channels != 2)
				throw new GAGEException("Error loading WAV file " + src + ": GAGE only supports mono and stereo audio formats (channels found: " + channels + ").");
			else
				throw new GAGEException("Error loading WAV file " + src + ": GAGE only supports audio sample sizes of 8 or 16 bits (sample size found: " + sampleSizeBits + ").");
			
			byte[] array = new byte[channels * (int) ais.getFrameLength() * sampleSizeBits / 8];
			int read = 0;
			int total = 0;
			while ((read = ais.read(array, total, array.length - total)) != -1 && total < array.length)
				total += read;
			
			ByteBuffer data = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());
			ByteBuffer buf = ByteBuffer.wrap(array).order(ByteOrder.LITTLE_ENDIAN);
			if (sampleSizeBits == 16) {
				while (buf.hasRemaining())
					data.putShort(buf.getShort());
			} else {
				while (buf.hasRemaining())
					data.put(buf.get());
			}
			data.flip();
			return new SoundBuffer.SoundBufferMeta(format, data.asShortBuffer(), (int) sourceFormat.getSampleRate(), () -> {});
		} catch (UnsupportedAudioFileException | IOException e) {
			throw new GAGEException(e);
		}
	};

	/**
	 * A loader that produces a {@link com.accele.gage.tile.TileMapMeta TileMapMeta} from a text file.
	 * 
	 * <p>
	 * A tile map file is essentially a properties file specifically used for generating tile maps.
	 * Information about the tile map is specified using key-value pairs in the format "key = value".
	 * Some properties are required in every map file, but others are optional.
	 * Each property must be declared on its own line.
	 * </p>
	 * <table>
	 * <caption>TileMap Attribute Information</caption>
	 * <tr>
	 * <th>Key</th>
	 * <th>Description</th>
	 * <th>Value</th>
	 * <th>Required</th>
	 * <th>Example</th>
	 * </tr>
	 * <tr>
	 * <td>visibleTilesX</td>
	 * <td>Specifies how many tiles should be visible on the screen at any one time in the x-direction. By default, this value will be -1 (shows all tiles).</td>
	 * <td>An integer value</td>
	 * <td>No</td>
	 * <td>{@code visibleTilesX = 16}</td>
	 * </tr>
	 * <tr>
	 * <td>visibleTilesY</td>
	 * <td>Specifies how many tiles should be visible on the screen at any one time in the y-direction. By default, this value will be -1 (shows all tiles).</td>
	 * <td>An integer value</td>
	 * <td>No</td>
	 * <td>{@code visibleTilesY = 16}</td>
	 * </tr>
	 * <tr>
	 * <td>tileWidth</td>
	 * <td>Specifies how large the width of a tile should be.</td>
	 * <td>A floating-point value</td>
	 * <td>Yes</td>
	 * <td>{@code tileWidth = 0.5}</td>
	 * </tr>
	 * <tr>
	 * <td>tileHeight</td>
	 * <td>Specifies how large the height of a tile should be.</td>
	 * <td>A floating-point value</td>
	 * <td>Yes</td>
	 * <td>{@code tileHeight = 0.5}</td>
	 * </tr>
	 * <tr>
	 * <td>rowsize</td>
	 * <td>Specifies how many tiles makes up a row on the map.</td>
	 * <td>An integer value</td>
	 * <td>Yes</td>
	 * <td>{@code rowsize = 10}</td>
	 * </tr>
	 * <tr>
	 * <td>key</td>
	 * <td>Specifies the key to use for mapping between texture registry IDs and tile indices. The value can span multiple lines.</td>
	 * <td>An integer-string dictionary</td>
	 * <td>Yes</td>
	 * <td><code>key = { 0:"water_texture", 4:"fire_texture", 3:"grass_texture" }</code></td>
	 * </tr>
	 * <tr>
	 * <td>tiles</td>
	 * <td>Specifies the tile data for the tile map where the integer values correspond to the values specified in the {@code key} property. The value can span multiple lines.</td>
	 * <td>An integer array.</td>
	 * <td>Yes</td>
	 * <td>{@code tiles = [0, 0, 0, 0, 1, 1, 0, 2, 5, 0, 0, 1, 1]}</td>
	 * </tr>
	 * </table>
	 */
	public static final ResourceLoader<TileMapMeta> TILE_MAP_LOADER = (src, args) -> {
		List<String> data = null;
		try {
			data = Files.readAllLines(src.toPath());
		} catch (IOException e) {
			throw new GAGEException(e);
		}

		float tileWidth = -1;
		float tileHeight = -1;
		int rowSize = -1;
		int[] tiles = null;
		Map<Integer, Texture> textures = null;
		int visibleTilesX = -1;
		int visibleTilesY = -1;

		for (int i = 0; i < data.size(); i++) {
			String line = data.get(i);
			if (line.startsWith("#"))
				continue;

			if (line.contains("=")) {
				String[] parts = line.split("=");

				if (parts[0].trim().equalsIgnoreCase("visibleTilesX")) {
					visibleTilesX = Integer.parseInt(parts[1].trim());
				} else if (parts[0].trim().equalsIgnoreCase("visibleTilesY")) {
					visibleTilesY = Integer.parseInt(parts[1].trim());
				} else if (parts[0].trim().equalsIgnoreCase("tilewidth")) {
					tileWidth = Float.parseFloat(parts[1].trim());
				} else if (parts[0].trim().equalsIgnoreCase("tileheight")) {
					tileHeight = Float.parseFloat(parts[1].trim());
				} else if (parts[0].trim().equalsIgnoreCase("rowsize")) {
					rowSize = Integer.parseInt(parts[1].trim());
				} else if (parts[0].trim().equalsIgnoreCase("key")) {
					List<String> subLines = new ArrayList<>();

					for (int j = i; j < data.size(); j++) {
						if (data.get(j).contains("}")) {
							subLines.add(data.get(j));
							textures = parseKey(subLines);
							i = j;
							break;
						} else if (!data.get(j).startsWith("#"))
							subLines.add(data.get(j));
					}
				} else if (parts[0].trim().equalsIgnoreCase("tiles")) {
					List<String> subLines = new ArrayList<>();

					for (int j = i; j < data.size(); j++) {
						if (data.get(j).contains("]")) {
							subLines.add(data.get(j));
							tiles = parseTiles(subLines);
							i = j;
							break;
						} else if (!data.get(j).startsWith("#"))
							subLines.add(data.get(j));
					}
				}
			}
		}

		List<String> errors = new ArrayList<>();

		if (tileWidth == -1)
			errors.add("tileWidth");
		if (tileHeight == -1)
			errors.add("tileHeight");
		if (rowSize == -1)
			errors.add("rowSize");
		if (textures == null)
			errors.add("key");
		if (tiles == null)
			errors.add("tiles");

		if (visibleTilesX == -1)
			visibleTilesX = rowSize;

		if (visibleTilesY == -1 && tiles != null)
			visibleTilesY = tiles.length / rowSize;

		if (!errors.isEmpty()) {
			throw new GAGEException("[" + src
					+ "] TILE_MAP_LOADER_ERROR: The following attributes are required but were not present in the parsed file: "
					+ errors);
		}

		return new TileMapMeta(textures, tiles, rowSize, tileWidth, tileHeight, visibleTilesX, visibleTilesY);
	};

	private static Map<Integer, Texture> parseKey(List<String> lines) {
		Registry<Texture> registry = GAGE.getInstance().getTextureRegistry();

		String str = lines.stream().reduce("", (a, b) -> a + "\n" + b).split("\\{")[1].split("\\}")[0].trim();

		String[] pairs = str.contains(",") ? str.split(",") : new String[] { str };

		Map<Integer, Texture> result = new HashMap<>();

		for (int i = 0; i < pairs.length; i++) {
			String[] parts = pairs[i].trim().split(":");

			int key = Integer.parseInt(parts[0].trim());
			String value = parts[1].trim().replace("\"", "");

			result.put(key, registry.getEntry(value));
		}

		return result;
	}

	private static int[] parseTiles(List<String> lines) {
		String str = lines.stream().reduce("", (a, b) -> a + "\n" + b).split("\\[")[1].split("\\]")[0].trim();

		String[] tiles = str.contains(",") ? str.split(",") : new String[] { str };

		int[] result = new int[tiles.length];

		for (int i = 0; i < tiles.length; i++) {
			int tile = Integer.parseInt(tiles[i].trim());

			result[i] = tile;
		}

		return result;
	}

	/**
	 * A loader that produces a {@link com.accele.gage.gfx.FontMeta FontMeta} from an internal font.
	 * This loader does not require a {@link com.accele.gage.ResourceLocation ResourceLocation} and requires two additional arguments.
	 * The first argument must be an instance of the desired {@link java.awt.Font}, and the second argument must be a boolean describing
	 * whether the font should be antialiased.
	 */
	public static final ResourceLoader<FontMeta> INTERNAL_FONT_LOADER = (src, args) -> {
		java.awt.Font font = (java.awt.Font) args[0];
		boolean antialias = (boolean) args[1];
		CharMeta[] chars = new CharMeta[256];
		Texture texture = generateFont(font, chars, antialias);

		return new FontMeta(font.getSize(), texture, chars);
	};

	private static Texture generateFont(java.awt.Font internalFont, CharMeta[] chars, boolean antialias) {
		int textureWidth = 512;
		int textureHeight = 512;
		BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, 2);
		Graphics2D g = imgTemp.createGraphics();

		g.setColor(new java.awt.Color(0, 0, 0, 0));
		g.fillRect(0, 0, textureWidth, textureHeight);

		int rowHeight = 0;
		int positionX = 0;
		int positionY = 0;

		int fontHeight = 0;

		for (int i = 0; i < 256; i++) {
			char ch = i < 32 || (i >= 127 && i <= 160) ? '?' : (char) i;

			BufferedImage fontImage = createCharImage(internalFont, ch, antialias);

			int width = fontImage.getWidth();
			int height = fontImage.getHeight();
			if (positionX + width >= textureWidth) {
				positionX = 0;
				positionY += rowHeight;
				rowHeight = 0;
			}
			float x = (float) positionX / (float) textureWidth;
			float y = (float) positionY / (float) textureHeight;
			CharMeta meta = new CharMeta(x, 1f - (y + (float) height / (float) textureHeight),
					(float) width / (float) textureWidth, (float) height / (float) textureHeight);
			if (height > fontHeight) {
				fontHeight = height;
			}
			if (height > rowHeight) {
				rowHeight = height;
			}
			g.drawImage(fontImage, positionX, positionY, null);

			positionX += width;
			chars[i] = meta;
		}

		return new Texture(":internal:", new Resource<TextureMeta>(
				(src, args) -> new TextureMeta(imageToByteBuffer(imgTemp), textureWidth, textureHeight, GL11.GL_RGBA),
				null));
	}

	private static BufferedImage createCharImage(java.awt.Font internalFont, char c, boolean antialias) {
		BufferedImage tempfontImage = new BufferedImage(1, 1, 2);

		Graphics2D g = tempfontImage.createGraphics();
		if (antialias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(internalFont);
		FontMetrics fontMetrics = g.getFontMetrics();
		g.dispose();
		int charwidth = fontMetrics.charWidth(c);
		if (charwidth <= 0) {
			charwidth = 1;
		}
		int charheight = fontMetrics.getHeight();
		if (charheight <= 0) {
			charheight = internalFont.getSize();
		}
		BufferedImage fontImage = new BufferedImage(charwidth, charheight, 2);

		Graphics2D gt = fontImage.createGraphics();
		if (antialias) {
			gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		gt.setFont(internalFont);

		gt.setColor(java.awt.Color.WHITE);
		int charx = 0;
		int chary = 0;
		gt.drawString(String.valueOf(c), charx, chary + fontMetrics.getAscent());
		gt.dispose();

		return fontImage;
	}

	private static ByteBuffer imageToByteBuffer(BufferedImage image) {
		AffineTransform at = new AffineTransform();
		at.concatenate(AffineTransform.getScaleInstance(1, -1));
		at.concatenate(AffineTransform.getTranslateInstance(0, -image.getHeight()));
		BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = newImage.createGraphics();
		g.transform(at);
		g.drawImage(image, 0, 0, null);
		g.dispose();
		image = newImage;

		int width = image.getWidth();
		int height = image.getHeight();

		int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = pixels[i * width + j];

				buffer.put((byte) ((pixel >> 16) & 0xff)); // r
				buffer.put((byte) ((pixel >> 8) & 0xff)); // g
				buffer.put((byte) ((pixel >> 0) & 0xff)); // b
				buffer.put((byte) ((pixel >> 24) & 0xff)); // a
			}
		}

		buffer.flip();

		return buffer;
	}

}
