package fr.zeldalike.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;

public class Plant extends InteractiveTileObject {
	
	public Plant(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(Constants.PLANT_BIT);
	}

	@Override
	public void onHeadHit() {
		Gdx.app.log("Plant", "Collision");
		setCategoryFilter(Constants.DESTROYED_PLANT);
//		getCell().setTile(null);
	}
}
