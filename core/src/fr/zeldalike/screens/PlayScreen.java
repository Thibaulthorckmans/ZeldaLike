package fr.zeldalike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.game.Main;
//import fr.zeldalike.scenes.Hud;
import fr.zeldalike.sprites.Avatar;
import fr.zeldalike.sprites.Villager;
import fr.zeldalike.tools.B2WorldCreator;
import fr.zeldalike.tools.MusicLoader;
import fr.zeldalike.tools.WorldContactListener;

public class PlayScreen implements Screen {
	private Main game;
	// HUD variables
//	private Hud hud;
	// Camera variables
	private Camera mainCam;
	// Map variables
	private Map mainMap;
	// Box2D variables
	private World world;
	private Box2DDebugRenderer b2dr;
	// Player variables
	private TextureAtlas atlas;
	private Avatar player;
	// NPc variables
	private Villager villager;
	// Music variables
	private Music music;

	
	private TextureAtlas heartAtlas;
	private SpriteBatch HUDBatch;
	private Sprite heart1, heart2, heart3;

	public PlayScreen(Main game) {
		this.game = game;
		mainCam = new Camera();
		mainMap = new Map("Village");
//		hud = new Hud(game.batch);

		// Create our Box2D world, setting no gravity and allow bodies to sleep
		world = new World(new Vector2(0, 0), true);

		// Allows for debug lines of our Box2D world
		b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, mainMap.getMap());

		// Create the avatar in our game world
		atlas = new TextureAtlas("Sprites/Link.pack");
		player = new Avatar(world, this);
		
		world.setContactListener(new WorldContactListener());
		
		// Launch our main theme music, set on looping and is volume
		music = MusicLoader.manager.get("Audio/Music/MainTheme.ogg", Music.class);
		music.setLooping(true);
		music.setVolume(10/Constants.PPM);
		music.play();

		// 
		villager = new Villager(this, 360, 610);
		villager.movePath();
		
		// Define if the avatar is moving or not
		Constants.isMoving = false;

		// Set the layers
		mainMap.setLayers();
		

		
		heartAtlas = new TextureAtlas("Sprites/items.pack");
		
		HUDBatch = new SpriteBatch();
		heart1 = new Sprite(heartAtlas.findRegion("heart", 1));
		heart1.setPosition(5, 760);
		heart1.setSize(60, 60);
		heart1.setScale(0.5f);
		heart2 = new Sprite(heartAtlas.findRegion("heart", 1));
		heart2.setPosition(35, 760);
		heart2.setSize(60, 60);
		heart2.setScale(0.5f);
		heart3 = new Sprite(heartAtlas.findRegion("heart", 2));
		heart3.setPosition(65, 760);
		heart3.setSize(60, 60);
		heart3.setScale(0.5f);
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	@Override
	public void show() {

	}

	public void update(float dt) {
		// Handle user input first
		player.handleInput(dt);
		
		// Takes 1 step in the physics simulation (60 times per second)
		world.step(1/60f, 6, 2);

		player.update(dt);
		villager.update(dt);
		player.isMoving();
		villager.isMoving();

		// Attach our gameCam to our player's coordinates
		mainCam.setPosition(player.b2body.getPosition().x, player.b2body.getPosition().y);
		mainCam.update();

		// Tell our renderer to draw only what our camera can see in our game world
		mainMap.setView(mainCam.getGameCam());
	}

	@Override
	public void render(float delta) {
		update(delta);

		// Clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render our back plan layers
		mainMap.renderLayers(mainMap.getBackPlan());

		// Render our Box2DDebugLines
		b2dr.render(world, mainCam.getGameCam().combined);

		// Render our player
		game.batch.setProjectionMatrix(mainCam.getGameCam().combined);
		game.batch.begin();
		player.draw(game.batch);
		villager.draw(game.batch);
		game.batch.end();

		// Render our first plan layers
		mainMap.renderLayers(mainMap.getFirstPlan());

//		// Set our batch to now draw the HUD camera sees
//		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
//		hud.stage.draw();

		//Display HUD (Score & Lives)
		HUDBatch.begin();
			heart1.draw(HUDBatch);
			heart2.draw(HUDBatch);
			heart3.draw(HUDBatch);
		HUDBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		// Update our game viewport
		mainCam.resize(width, height);
	}
	
	public World getWorld() {
		return world;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		mainMap.dispose();
		world.dispose();
//		hud.dispose();
	}
}
