package fr.zeldalike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.game.Main;
//import fr.zeldalike.scenes.Hud;
import fr.zeldalike.sprites.Avatar;
import fr.zeldalike.tools.B2WorldCreator;

public class PlayScreen implements Screen {
	private Main game;
	private TextureAtlas atlas;

	// Camera variables
	private OrthographicCamera gameCam;
	private Viewport gamePort;
	//private Hud hud;

	// Tiled map variables
	private TmxMapLoader mapLoader;
	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;

	// Box2D variables
	private World world;
	private Box2DDebugRenderer b2dr;

	private Avatar player;

	public PlayScreen(Main game) {
		atlas = new TextureAtlas("Sprites/Link.pack");

		this.game = game;

		// Create cam used to follow through the world
		gameCam = new OrthographicCamera();

		// Create a FitViewport to maintain virtual aspect ratio despite screen size
		gamePort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, gameCam);

		// Create our game HUD for scores/times /level info
		//hud = new Hud(game.batch);

		// Load our map and setup our map renderer
		mapLoader = new TmxMapLoader();
		map = mapLoader.load("Maps/villageModifie.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1/Constants.PPM);

		// Initially set our gameCam to be centered correctly at the start of the game
		gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);

		// Create our Box2D world, setting no gravity and allow bodies to sleep
		world = new World(new Vector2(0, 0), true);

		// Allows for debug lines of our Box2D world
		//b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, map);

		// Create the avatar in our game world
		player = new Avatar(world, this);

		// Define if the avatar is moving or not
		Constants.isMoving = false;
	}

	public TextureAtlas getAtlas() {
		return atlas;
	}

	@Override
	public void show() {

	}

	public void handleInput(float dt) {
		// Control our player with immediate impulses when key pressed and stop when nothing is pressed
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y <= 0.5f) {
			player.b2body.applyLinearImpulse(new Vector2(0, 1), player.b2body.getWorldCenter(), true);

		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && player.b2body.getLinearVelocity().y >= -0.5f) {
			player.b2body.applyLinearImpulse(new Vector2(0, -1), player.b2body.getWorldCenter(), true);

		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 0.5f) {
			player.b2body.applyLinearImpulse(new Vector2(1, 0), player.b2body.getWorldCenter(), true);

		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -0.5f) {
			player.b2body.applyLinearImpulse(new Vector2(-1, 0), player.b2body.getWorldCenter(), true);

		} else {
			player.b2body.setLinearVelocity(new Vector2(0, 0));
		}
	}

	public void update(float dt) {
		// Handle user input first
		handleInput(dt);

		// Takes 1 step in the physics simulation (60 times per second)
		world.step(1/60f, 6, 2);

		player.update(dt);
		if(player.b2body.getLinearVelocity().x==0 && player.b2body.getLinearVelocity().y==0)
			Constants.isMoving = false;
		else
			Constants.isMoving = true;

		// Attach our gameCam to our players coordinates
		gameCam.position.x = player.b2body.getPosition().x;
		gameCam.position.y = player.b2body.getPosition().y;

		// Update our gameCam with the correct coordinates after changes
		gameCam.update();

		// Tell our renderer to draw only what our camera can see in our game world
		renderer.setView(gameCam);
	}

	@Override
	public void render(float delta) {
		// Separate our update logic from render
		update(delta);

		// Clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render our game map
		renderer.render();

		// Render our Box2DDebugLines
		//b2dr.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();
		player.draw(game.batch);
		game.batch.end();

		// Set our batch to now drax the HUD camera sees
		//game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		//hud.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
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
		map.dispose();
		renderer.dispose();
		world.dispose();
		b2dr.dispose();
		//hud.dispose();
	}


}
