package fr.zeldalike.assets;

/**
 * Contains all the constants.
 */
public class Constants {
	// **************************************************
	// Constants
	// **************************************************
	
	/** Used to scale some parameters, value is {@value #PPM}. */
	public static final float PPM = 100;
	
	/** Used to define the camera's width, value is {@value #V_WIDTH}. */
	public static final int V_WIDTH = 500;
	/** Used to define the camera's height, value is {@value #V_HEIGHT}. */
	public static final int V_HEIGHT = 333;
	
	/** Used to define the collision between elements, value is {@value #DEFAULT_BIT}. */
	public static final short DEFAULT_BIT = 1;
	/** Used to define the collision between elements, value is {@value #LINK_BIT}. */
	public static final short LINK_BIT = 2;
	/** Used to define the collision between elements, value is {@value #PLANT_BIT}. */
	public static final short PLANT_BIT = 4;
	/** Used to define the collision between elements, value is {@value #RUBY_BIT}. */
	public static final short RUBY_BIT = 8;
	/** Used to define the collision between elements, value is {@value #DESTROYED_PLANT}. */
	public static final short DESTROYED_PLANT = 16;
	/** Used to define the collision between elements, value is {@value #NPC_BIT}. */
	public static final short NPC_BIT = 32;

	/** Contains the maximum life value the player can have, value is {@value #MAX_HEALTH}. */
	public static final int MAX_HEALTH = 40;
}