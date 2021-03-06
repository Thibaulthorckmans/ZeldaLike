package fr.zeldalike.sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;

public abstract class InteractiveTileObject {
	// **************************************************
	// Fields
	// **************************************************
	protected World world;
	protected TiledMap map;
	protected TiledMapTile tile;
	protected Rectangle bounds;
	protected Body body;
	protected Fixture fixture;

	// **************************************************
	// Constructors
	// **************************************************
	public InteractiveTileObject(World world, TiledMap map, Rectangle bounds) {
		this.world = world;
		this.map = map;
		this.bounds = bounds;

		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + (bounds.getWidth()/2))/Constants.PPM, (bounds.getY() + (bounds.getHeight()/2))/Constants.PPM);

		this.body = world.createBody(bdef);

		shape.setAsBox((bounds.getWidth()/2)/Constants.PPM, (bounds.getHeight()/2)/Constants.PPM);
		fdef.shape = shape;
		this.fixture = this.body.createFixture(fdef);
	}

	// **************************************************
	// Public Methods
	// **************************************************
	public abstract void onHeadHit();

	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits = filterBit;
		this.fixture.setFilterData(filter);
	}

	public TiledMapTileLayer.Cell getCell() {
		TiledMapTileLayer layer = (TiledMapTileLayer) this.map.getLayers().get("Plant");
		return layer.getCell((int)((this.body.getPosition().x * Constants.PPM) / 16), (int)((this.body.getPosition().y * Constants.PPM) / 16));
	}
}
