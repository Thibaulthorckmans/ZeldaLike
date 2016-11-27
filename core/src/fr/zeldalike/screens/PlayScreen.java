package fr.zeldalike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.game.Main;
import fr.zeldalike.scenes.Hud;
import fr.zeldalike.sprites.Avatar;
import fr.zeldalike.sprites.Villager;
import fr.zeldalike.tools.B2WorldCreator;
import fr.zeldalike.tools.MusicLoader;
import fr.zeldalike.tools.WorldContactListener;

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
	private Box2DDebugRenderer b2dr;
	// Player variables
	private TextureAtlas atlas;
	private Avatar player;
	// NPc variables
	private Villager villager;
	// Music variables
	private Music music;

	public PlayScreen(Main game) {
		this.game = game;
		this.mainCam = new Camera();
		this.mainMap = new Map("Village");
		this.hud = new Hud(game.batch);

		// Create our Box2D world, setting no gravity and allow bodies to sleep
		this.world = new World(new Vector2(0, 0), true);

		// Allows for debug lines of our Box2D world
		this.b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(this.world, this.mainMap.getMap());

		// Create the avatar in our game world
		this.atlas = new TextureAtlas("Sprites/Link.pack");
		this.player = new Avatar(this.world, this);

		this.world.setContactListener(new WorldContactListener());

		// Launch our main theme music, set on looping and is volume
		this.music = MusicLoader.manager.get("Audio/Music/ALTTP_Kakariko_Village.ogg", Music.class);
		this.music.setLooping(true);
		this.music.setVolume(10/Constants.PPM);
		this.music.play();

		//
		this.villager = new Villager(this, 360, 610);
		this.villager.movePath();

		// Define if the avatar is moving or not
		Constants.isMoving = false;

		// Set the layers
		this.mainMap.setLayers();
	}

	public TextureAtlas getAtlas() {
		return this.atlas;
	}

	@Override
	public void show() {

	}

	public void update(float dt) {

		// Handle user input first
		this.player.handleInput(dt);

		// Takes 1 step in the physics simulation (60 times per second)
		this.world.step(1/60f, 6, 2);

		this.player.update(dt);
		this.villager.update(dt);
		this.player.isMoving();
		this.villager.isMoving();

		// Attach our gameCam to our player's coordinates
		this.mainCam.setPosition(this.player.b2body.getPosition().x, this.player.b2body.getPosition().y);
		this.mainCam.update();

		// Tell our renderer to draw only what our camera can see in our game world
		this.mainMap.setView(this.mainCam.getGameCam());


		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			this.hud.damage(1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			this.hud.cure(1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			this.hud.setDialogBoxVisibility();
		}
	}

	@Override
	public void render(float delta) {
		this.update(delta);

		if(Gdx.input.isKeyJustPressed(Input.Keys.V)) {
			//this.mainMap.getMap().dispose();
			this.mainMap.setMap("donjonTest");
		}

		// Clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render our back plan layers
		this.mainMap.renderLayers(this.mainMap.getBackPlan());

		// Render our Box2DDebugLines
		this.b2dr.render(this.world, this.mainCam.getGameCam().combined);

		// Render our player
		this.game.batch.setProjectionMatrix(this.mainCam.getGameCam().combined);
		this.game.batch.begin();
		this.player.draw(this.game.batch);
		this.villager.draw(this.game.batch);
		this.game.batch.end();

		// Render our first plan layers
		this.mainMap.renderLayers(this.mainMap.getFirstPlan());

		// Set our batch to now draw the HUD camera sees
		this.game.batch.setProjectionMatrix(this.hud.getStage().getCamera().combined);
		this.hud.getStage().draw();
	}

	@Override
	public void resize(int width, int height) {
		// Update our game viewport
		this.mainCam.resize(width, height);
	}

	public World getWorld() {
		return this.world;
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
		this.mainMap.dispose();
		this.world.dispose();
		this.hud.dispose();
	}
}