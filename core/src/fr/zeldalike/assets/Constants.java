package fr.zeldalike.assets;

public class Constants {
	// **************************************************
	// Constants
	// **************************************************
	public static final float PPM = 100;
	// Camera size variables
	public static final int V_WIDTH = 500;
	public static final int V_HEIGHT = 333;
	// Player's movement variables
	public static boolean isMoving;
	//
	public static final short DEFAULT_BIT = 1;
	public static final short LINK_BIT = 2;
	public static final short PLANT_BIT = 4;
	public static final short RUBY_BIT = 8;
	public static final short DESTROYED_PLANT = 16;
	public static final short NPC_BIT = 32;

	/** Contains the maximum life value the player can have, value is {@value #MAX_HEALTH}. */
	public static final int MAX_HEALTH = 40;
}