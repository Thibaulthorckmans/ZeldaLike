package fr.zeldalike.screens;

import java.util.ArrayList;
import java.util.List;

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

	public Map(String mapName) {
		// Load our map and setup the renderer
		this.mapLoader = new TmxMapLoader();
		this.map = this.mapLoader.load("Maps/" + mapName + ".tmx");
		this.renderer = new OrthogonalTiledMapRenderer(this.map, 1/Constants.PPM);
	}
	
	public TiledMap getMap() {
		return this.map;
	}
	
	public int[] getBackPlan() {
		return this.backPlan;
	}

	public int[] getFirstPlan() {
		return this.firstPlan;
	}

	public void setMap(String mapName) {
		this.mapLoader = new TmxMapLoader();
		this.map = this.mapLoader.load("Maps/" + mapName + ".tmx");
		this.renderer.setMap(this.map);
		this.setLayers();
	}
	public void setRenderer() {
		this.renderer.setMap(this.map);
	}

	public void setLayers() {
		this.setBackPlan();
		this.setFirstPlan();
	}
	
	public void setBackPlan() {
		int nbLayer = this.map.getLayers().getCount();
		List<Integer> layers = new ArrayList<Integer>();

		for(int i = 0; i < nbLayer; i++) {
			String nameLayer = this.map.getLayers().get(i).getName();
			
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
		
		this.backPlan = tab;
	}
	
	public void setFirstPlan() {
		int nbLayer = this.map.getLayers().getCount();
		List<Integer> layers = new ArrayList<Integer>();

		for(int i = 0; i < nbLayer; i++) {
			String nameLayer = this.map.getLayers().get(i).getName();
			
			if(nameLayer.contains("fp_")) {
				layers.add(i);
			}
		}
		
		int[] tab = new int[layers.size()];
		
		for(int i = 0; i < tab.length; i++) {
			tab[i] =  layers.get(i);
		}
		
		this.firstPlan=  tab;
	}

	public void setView(OrthographicCamera cam) {
		this.renderer.setView(cam);
	}

	public void dispose() {
		this.map.dispose();
		this.renderer.dispose();
	}

	public void renderLayers(int[] layer) {
		this.renderer.render(layer);
	}
}
