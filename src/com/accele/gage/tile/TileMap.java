package com.accele.gage.tile;

import com.accele.gage.Cleanable;
import com.accele.gage.GAGEException;
import com.accele.gage.Indexable;
import com.accele.gage.Renderable;
import com.accele.gage.Resource;
import com.accele.gage.Tickable;
import com.accele.gage.gfx.Graphics;
import com.accele.gage.gfx.Texture;
import com.accele.gage.math.Vector2f;

/**
 * A collection of {@link com.accele.gage.tile.Tile Tile} instances that are rendered seamlessly as one large tiled rectangle.
 * 
 * GAGE comes with a built-in external tile map reader to easily parse large or complicated tile maps.
 * Refer to {@link com.accele.gage.ResourceLoaders#TILE_MAP_LOADER TILE_MAP_LOADER} for more details.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see com.accele.gage.ResourceLoaders#TILE_MAP_LOADER TILE_MAP_LOADER
 */
public class TileMap implements Indexable, Tickable, Renderable, Cleanable {
	
	private String registryId;
	private Tile[] tiles;
	private int width;
	private int height;
	private int visibleTilesX;
	private int visibleTilesY;
	
	/**
	 * Constructs a {@code TileMap} with the specified registry ID and {@code TileMapMeta}.
	 * 
	 * @param registryId the registry ID to use in the tile map {@link com.accele.gage.Registry Registry}
	 * @param meta the metadata about the tile map
	 */
	public TileMap(String registryId, Resource<TileMapMeta> meta) {
		this.registryId = registryId;
		try {
			TileMapMeta data = meta.get();
			
			this.tiles = new Tile[data.getTiles().length];
			this.width = data.getRowSize();
			this.height = tiles.length / data.getRowSize();
			this.visibleTilesX = data.getVisibleTilesX();
			this.visibleTilesY = data.getVisibleTilesY();
			
			for (int y = 0; y < height; y++)
				for (int x = 0; x < width; x++)
					this.tiles[y * width + x] = generateTile(data.getTextures().get(data.getTiles()[y * width + x]), 
							x, y, data.getVisibleTilesX() - 1, data.getVisibleTilesY() - 1, data.getTileWidth(), data.getTileHeight());
		} catch (GAGEException e) {
			e.printStackTrace();
		}
	}
	
	private static Tile generateTile(Texture texture, int x, int y, int xmax, int ymax, float tileWidth, float tileHeight) {
		float oldrangex = xmax;
		float oldrangey = ymax;
		float newrangex = 2f * ((float) xmax / (1f + xmax));
		float newrangey = 2f * ((float) ymax / (1f + ymax));
		float tx = newrangex * x / oldrangex - ((float) xmax / (1f + xmax));
		float ty = newrangey * y / oldrangey - ((float) ymax / (1f + ymax));
		
		return new Tile(texture, new Vector2f(tx, -ty), new Vector2f(tileWidth, tileHeight));
	}
	
	@Override
	public void tick() {
		for (Tile t : tiles)
			t.tick();
	}

	@Override
	public void render(Graphics g, double interpolation) {
		for (Tile t : tiles)
			t.render(g, interpolation);
	}
	
	@Override
	public void clean() {
		for (Tile t : tiles)
			t.clean();
	}
	
	@Override
	public String getRegistryId() {
		return registryId;
	}
	
	/**
	 * Returns the tiles used in the {@code TileMap}.
	 * 
	 * @return the array of {@link com.accele.gage.tile.Tile Tile} instances used in the map
	 */
	public Tile[] getTiles() {
		return tiles;
	}
	
	/**
	 * Returns the width, in tiles, of the {@code TileMap}.
	 * 
	 * @return the width, in tiles, of the {@code TileMap}
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Returns the height, in tiles, of the {@code TileMap}.
	 * 
	 * @return the height, in tiles, of the {@code TileMap}
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the number of tiles visible on the screen at any one time in the x-direction for this {@code TileMap}.
	 * 
	 * @return the number of visible tiles in the x-direction
	 */
	public int getVisibleTilesX() {
		return visibleTilesX;
	}
	
	/**
	 * Returns the number of tiles visible on the screen at any one time in the y-direction for this {@code TileMap}.
	 * 
	 * @return the number of visible tiles in the y-direction
	 */
	public int getVisibleTilesY() {
		return visibleTilesY;
	}

}
