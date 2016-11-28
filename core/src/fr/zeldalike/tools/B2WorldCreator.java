package fr.zeldalike.tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.sprites.Plant;
//import fr.zeldalike.sprites.Ruby;

public class B2WorldCreator {
	private Body body;
	
	public B2WorldCreator(World world, TiledMap map) {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		
		for(MapObject object : map.getLayers().get("col_Block").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + (rect.getWidth()/2))/Constants.PPM, (rect.getY() + (rect.getHeight()/2))/Constants.PPM);

			this.body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
			fdef.shape = shape;
			this.body.createFixture(fdef);
		}
		
		// Create plants bodies/fixtures
		for(MapObject object : map.getLayers().get("col_Plant").getObjects().getByType(RectangleMapObject.class)) {
				Rectangle rect = ((RectangleMapObject) object).getRectangle();

				new Plant(world, map, rect);
		}

//		// Create rubis bodies/fixtures
//		for(MapObject object : map.getLayers().get("").getObjects().getByType(RectangleMapObject.class)) {
//				Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//				new Ruby(world, map, rect);
//		}
	}
}
