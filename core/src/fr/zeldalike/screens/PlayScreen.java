package fr.zeldalike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.game.Main;
import fr.zeldalike.scenes.Hud;
import fr.zeldalike.sprites.Avatar;
import fr.zeldalike.tools.B2WorldCreator;

public class PlayScreen implements Screen {
	private Main game;
	// HUD variables
	private Hud hud;
	// Camera variables
	private Camera mainCam;
	// Map variables
	private Map mainMap;
	// Box2D variables
	private World world;
	// Player variables
	private TextureAtlas atlas;
	private Avatar player;


	public PlayScreen(Main game) {
		this.game = game;
		mainCam = new Camera();
		mainMap = new Map("Village");
		hud = new Hud(game.batch);

		// Create our Box2D world, setting no gravity and allow bodies to sleep
		world = new World(new Vector2(0, 0), true);

		// Allows for debug lines of our Box2D world
		//b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, mainMap.getMap());

		// Create the avatar in our game world
		atlas = new TextureAtlas("Sprites/Link.pack");
		player = new Avatar(world, this);

		// Define if the avatar is moving or not
		Constants.isMoving = false;

		// Set the layers
		mainMap.setLayers();
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
		player.isMoving();

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
		//b2dr.render(world, gameCam.combined);

		// Render our player
		game.batch.setProjectionMatrix(mainCam.getGameCam().combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();

		// Render our first plan layers
		mainMap.renderLayers(mainMap.getFirstPlan());

		// Set our batch to now draw the HUD camera sees
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		mainCam.resize(width, height);
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
		hud.dispose();
	}


}
