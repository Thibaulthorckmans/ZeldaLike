package fr.zeldalike.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;

public class Plant extends InteractiveTileObject {
	// **************************************************
	// Constructors
	// **************************************************
	public Plant(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(Constants.PLANT_BIT);
	}

	// **************************************************
	// Public Methods
	// **************************************************
	@Override
	public void onHeadHit() {
		setCategoryFilter(Constants.DESTROYED_PLANT);
//		getCell().setTile(null);
	}
}
