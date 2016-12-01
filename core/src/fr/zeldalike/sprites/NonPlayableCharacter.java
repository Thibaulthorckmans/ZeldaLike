package fr.zeldalike.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.screens.PlayScreen;
import fr.zeldalike.sprites.Villager.State;

public abstract class NonPlayableCharacter extends Sprite{
	// **************************************************
	// Fields
	// **************************************************
	protected World world;
	protected PlayScreen screen;
	public Body b2body;
	
	// **************************************************
	// Constructors
	// **************************************************
	public NonPlayableCharacter(PlayScreen screen, float x, float y) {
		world = screen.getWorld();
		this.screen = screen;
		setPosition(x, y);
		defineNPC(x, y);
	}
	
	// **************************************************
	// Setters
	// **************************************************
	protected abstract TextureRegion getFrame(float dt);
	
	protected abstract State getState();
	
	// **************************************************
	// Setters
	// **************************************************
	protected abstract void setMoving();
	
	// **************************************************
	// Protected Methods
	// **************************************************
	/**
	 * Move a villager with a fixed timing on a line
	 * @param	timer1	Travel time on the positive axis
	 * @param	timer2	Travel time on the negative axis
	 * @param	yAxe	Define if the villager move on the y-axis or not
	 */
	protected abstract void movePathLine(float timer1, float timer2, boolean yAxe);
	
	/**
	 * Move a villager with a fixed timing on a square
	 * @param	timerPosX	Travel time on the positive x-axis
	 * @param	timerNegX	Travel time on the negative x-axis
	 * @param	timerPosY	Travel time on the positive y-axis
	 * @param	timerNegY	Travel time on the negative y-axis
	 */
	protected abstract void movePathSquare(float timerPosX, float timerNegX, float timerNegY, float timerPosY);
	
	/**
	 * Define the player's initial position, his collision body and the elements that interact with him.
	 */
	protected abstract void defineNPC(float x, float y);
	
	protected abstract void update(float dt);
}