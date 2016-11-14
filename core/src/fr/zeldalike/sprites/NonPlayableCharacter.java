package fr.zeldalike.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.screens.PlayScreen;
import fr.zeldalike.sprites.Villager.State;

public abstract class NonPlayableCharacter extends Sprite{
	protected World world;
	protected PlayScreen screen;
	public Body b2body;
	
	public NonPlayableCharacter(PlayScreen screen, float x, float y) {
		this.world = screen.getWorld();
		this.screen = screen;
		setPosition(x, y);
		defineNPC();
	}
	
	protected abstract void isMoving();
	
	protected abstract void update(float dt);
	
	protected abstract TextureRegion getFrame(float dt);
	
	protected abstract State getState();
	
	protected abstract void movePath();
	
	protected abstract void defineNPC();
}
