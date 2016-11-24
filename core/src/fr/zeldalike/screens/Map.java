package fr.zeldalike.screens;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import fr.zeldalike.assets.Constants;

public class Map {
	// Tiled map variables
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	// Layers variables
	int[] backPlan, firstPlan;

	public TiledMap getMap() {
		return map;
	}

	public void setMap(String mapName) {
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Maps/" + mapName + ".tmx");
	}
	
	public int[] getBackPlan() {
		return backPlan;
	}

	public int[] getFirstPlan() {
		return firstPlan;
	}

	public void setLayers() {
		backPlan = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
		firstPlan = new int[] {38, 39, 40};
	}

	public Map(String mapName) {
		// Load our map and setup the renderer
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Maps/" + mapName + ".tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/Constants.PPM);
	}

	public void setView(OrthographicCamera cam) {
		renderer.setView(cam);
	}

	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

	public void renderLayers(int[] layer) {
		renderer.render(layer);
	}
}
