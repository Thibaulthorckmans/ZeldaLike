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
//import fr.zeldalike.sprites.Brick;
//import fr.zeldalike.sprites.Coin;

public class B2WorldCreator {

	public B2WorldCreator(World world, TiledMap map) {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		
		for(MapObject object : map.getLayers().get("Collision").getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);

			body = world.createBody(bdef);

			shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}
		
//		// Create bricks bodies/fixtures
//		for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
//				Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//				new Brick(world, map, rect);
//		}
//
//		// Create coins bodies/fixtures
//		for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
//				Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//				new Coin(world, map, rect);
//		}
	}
}
