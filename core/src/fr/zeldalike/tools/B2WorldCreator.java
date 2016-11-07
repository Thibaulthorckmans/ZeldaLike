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
		// Create body and fixture variables
				BodyDef bdef = new BodyDef();
				PolygonShape shape = new PolygonShape();
				FixtureDef fdef = new FixtureDef();
				Body body;
				
				// Create cliff bodies/fixtures
				for(MapObject object : map.getLayers().get(36).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create tree bodies/fixtures
				for(MapObject object : map.getLayers().get(37).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create hedge bodies/fixtures
				for(MapObject object : map.getLayers().get(38).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create fence bodies/fixtures
				for(MapObject object : map.getLayers().get(39).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create bush bodies/fixtures
				for(MapObject object : map.getLayers().get(40).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create building bodies/fixtures
				for(MapObject object : map.getLayers().get(41).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create object bodies/fixtures
				for(MapObject object : map.getLayers().get(42).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				// Create exit bodies/fixtures
				for(MapObject object : map.getLayers().get(43).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						bdef.type = BodyDef.BodyType.StaticBody;
						bdef.position.set((rect.getX() + rect.getWidth()/2)/Constants.PPM, (rect.getY() + rect.getHeight()/2)/Constants.PPM);
						
						body = world.createBody(bdef);
						
						shape.setAsBox((rect.getWidth()/2)/Constants.PPM, (rect.getHeight()/2)/Constants.PPM);
						fdef.shape = shape;
						body.createFixture(fdef);
				}
				
				/*
				// Create bricks bodies/fixtures
				for(MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						new Brick(world, map, rect);
				}
				
				// Create coins bodies/fixtures
				for(MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
						Rectangle rect = ((RectangleMapObject) object).getRectangle();
						
						new Coin(world, map, rect);
				}
				*/
	}
}
