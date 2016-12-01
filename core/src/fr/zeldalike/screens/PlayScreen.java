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
import fr.zeldalike.scenes.Inventory;
import fr.zeldalike.sprites.Avatar;
import fr.zeldalike.sprites.Villager;
import fr.zeldalike.tools.B2WorldCreator;
import fr.zeldalike.tools.MusicLoader;
import fr.zeldalike.tools.WorldContactListener;

public class PlayScreen implements Screen {
	// **************************************************
	// Fields
	// **************************************************
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
	private Inventory inventory;
	// NPC's variables
	private Villager runningVillager;
	// Music variables
	private Music music;

	// **************************************************
	// Constructors
	// **************************************************
	public PlayScreen(Main game) {
		this.game = game;
		this.mainCam = new Camera();
		this.mainMap = new Map("Village");
		this.hud = new Hud(game.batch);
		this.inventory = new Inventory(game.batch);

		// Create our Box2D world, setting no gravity and allow bodies to sleep
		this.world = new World(new Vector2(0, 0), true);

		// Allows for debug lines of our Box2D world
		this.b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(this.world, this.mainMap.getMap());

		// Create the avatar in our game world
		this.atlas = new TextureAtlas("Sprites/Link.pack");
		this.player = new Avatar(this.world, this);
		this.player.setInventory(this.inventory);

		this.world.setContactListener(new WorldContactListener());

		// Launch our main theme music, set on looping and is volume
		this.music = MusicLoader.manager.get("Audio/Music/ALTTP_Kakariko_Village.ogg", Music.class);
		this.music.setLooping(true);
		this.music.setVolume(10/Constants.PPM);
		this.music.play();

		//
		this.runningVillager = new Villager(this, 310, 700);

		// Set the layers
		this.mainMap.setLayers();
	}

	// **************************************************
	// Getters
	// **************************************************
	public TextureAtlas getAtlas() {
		return this.atlas;
	}

	public World getWorld() {
		return this.world;
	}

	// **************************************************
	// Setters
	// **************************************************


	// **************************************************
	// Public Methods
	// **************************************************
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
		this.runningVillager.draw(this.game.batch);
		this.game.batch.end();

		// Render our first plan layers
		this.mainMap.renderLayers(this.mainMap.getFirstPlan());

		// Set our batch to now draw the HUD camera sees
		this.game.batch.setProjectionMatrix(this.hud.getStage().getCamera().combined);
		this.hud.getStage().draw();

		// Set our batch to now draw the HUD camera sees
		this.game.batch.setProjectionMatrix(this.inventory.getStage().getCamera().combined);
		this.inventory.getStage().draw();
	}

	public void update(float dt) {
		// Handle user input first
		this.player.handleInput(dt);

		// Takes 1 step in the physics simulation (60 times per second)
		this.world.step(1/60f, 6, 2);

		this.player.update(dt);
		this.runningVillager.update(dt);
		this.player.setMoving();
		this.runningVillager.setMoving();

		this.runningVillager.movePathSquare(4.6f, 3.1f, 4.6f, 3.1f);

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
			if(this.inventory.getInvetoryIsVisible()) {
				this.hud.setImgButtonY(this.inventory.getNameItem());
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			if(this.inventory.getInvetoryIsVisible()) {
				this.hud.setImgButtonX(this.inventory.getNameItem());
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// Update our game viewport
		this.mainCam.resize(width, height);
	}

	@Override
	public void show() {}

	@Override
	public void pause() {}

	@Override
	public void resume() {}

	@Override
	public void hide() {}

	@Override
	public void dispose() {
		this.mainMap.dispose();
		this.world.dispose();
		this.hud.dispose();
	}
}