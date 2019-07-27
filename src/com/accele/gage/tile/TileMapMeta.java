package com.accele.gage.tile;

import java.util.Map;

import com.accele.gage.gfx.Texture;

/**
 * A wrapper class for specifying information about a {@link com.accele.gage.tile.TileMap TileMap}.
 * 
 * GAGE comes with a built-in external tile map reader to easily parse large or complicated tile maps.
 * Refer to {@link com.accele.gage.ResourceLoaders#TILE_MAP_LOADER TILE_MAP_LOADER} for more details.
 * 
 * @author William Garland
 * @version 1.0.0
 * @since 1.0.0
 * @see com.accele.gage.ResourceLoaders#TILE_MAP_LOADER TILE_MAP_LOADER
 */
public class TileMapMeta {

	private Map<Integer, Texture> textures;
	private int[] tiles;
	private int rowSize;
	private float tileWidth;
	private float tileHeight;
	private int visibleTilesX;
	private int visibleTilesY;
	
	/**
	 * Constructs a {@code TileMapMeta} with the specified texture map, tiles, row size, tile dimensions, and number of visible tiles.
	 * 
	 * @param textures the mapping between tile indices and {@link com.accele.gage.gfx.Texture Texture} instances
	 * @param tiles the tile layout of the map
	 * @param rowSize the number of tiles per row in the map
	 * @param tileWidth the width of an individual tile
	 * @param tileHeight the height of an individual tile
	 * @param visibleTilesX the number of tiles visible on the screen at any one time in the x-direction
	 * @param visibleTilesY the number of tiles visible on the screen at any one time in the y-direction
	 */
	public TileMapMeta(Map<Integer, Texture> textures, int[] tiles, int rowSize, float tileWidth, float tileHeight, int visibleTilesX, int visibleTilesY) {
		this.textures = textures;
		this.tiles = tiles;
		this.rowSize = rowSize;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.visibleTilesX = visibleTilesX;
		this.visibleTilesY = visibleTilesY;
	}

	/**
	 * Returns the {@link java.util.Map Map} of tile indices and {@link com.accele.gage.gfx.Texture Texture} instances used to relay which texture goes with which tile.
	 * 
	 * @return the mapping between tile indices and {@link com.accele.gage.gfx.Texture Texture} instances
	 */
	public Map<Integer, Texture> getTextures() {
		return textures;
	}

	/**
	 * Returns the tile layout of the map.
	 * 
	 * @return the tile layout of the map
	 */
	public int[] getTiles() {
		return tiles;
	}

	/**
	 * Returns the number of tiles per row in the map.
	 * 
	 * @return the number of tiles per row in the map
	 */
	public int getRowSize() {
		return rowSize;
	}

	/**
	 * Returns the width of an individual tile.
	 * 
	 * @return the width of an individual tile
	 */
	public float getTileWidth() {
		return tileWidth;
	}

	/**
	 * Returns the height of an individual tile.
	 * 
	 * @return the height of an individual tile
	 */
	public float getTileHeight() {
		return tileHeight;
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
