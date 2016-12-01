package fr.zeldalike.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import fr.zeldalike.assets.Constants;

/**
 * Set the map.
 */
public class Map {
	// **************************************************
	// Fields
	// **************************************************
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	int[] backPlan, firstPlan; // Layers variables

	// **************************************************
	// Constructors
	// **************************************************
	public Map(String mapName) {
		// Load our map and setup the renderer
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Maps/" + mapName + ".tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/Constants.PPM);
	}
	
	// **************************************************
	// Getters
	// **************************************************
	public TiledMap getMap() {
		return map;
	}
	
	public int[] getBackPlan() {
		return backPlan;
	}

	public int[] getFirstPlan() {
		return firstPlan;
	}
	
	// **************************************************
	// Setters
	// **************************************************
	public void setMap(String mapName) {
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Maps/" + mapName + ".tmx");
		renderer.setMap(map);
		setLayers();
	}
	public void setRenderer() {
		renderer.setMap(map);
	}

	public void setLayers() {
		setBackPlan();
		setFirstPlan();
	}
	
	public void setBackPlan() {
		int nbLayer = map.getLayers().getCount();
		List<Integer> layers = new ArrayList<Integer>();

		for(int i = 0; i < nbLayer; i++) {
			String nameLayer = map.getLayers().get(i).getName();
			
			if(nameLayer.contains("col_") || nameLayer.contains("fp_")) {
				break;
			} else {
				layers.add(i);
			}
		}
		
		int[] tab = new int[layers.size()];
		
		for(int i = 0; i < tab.length; i++) {
			tab[i] = layers.get(i);
		}
		
		backPlan = tab;
	}
	
	public void setFirstPlan() {
		int nbLayer = map.getLayers().getCount();
		List<Integer> layers = new ArrayList<Integer>();

		for(int i = 0; i < nbLayer; i++) {
			String nameLayer = map.getLayers().get(i).getName();
			
			if(nameLayer.contains("fp_")) {
				layers.add(i);
			}
		}
		
		int[] tab = new int[layers.size()];
		
		for(int i = 0; i < tab.length; i++) {
			tab[i] =  layers.get(i);
		}
		
		firstPlan=  tab;
	}

	public void setView(OrthographicCamera cam) {
		renderer.setView(cam);
	}
	
	// **************************************************
	// Public Methods
	// **************************************************
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}

	public void renderLayers(int[] layer) {
		renderer.render(layer);
	}
}
