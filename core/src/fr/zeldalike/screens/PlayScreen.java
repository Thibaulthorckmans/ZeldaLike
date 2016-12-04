package fr.zeldalike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	private TextureAtlas atlasAvatar, atlasVillager;
	private Avatar player;
	private Inventory inventory;
	// NPC's variables
	private Villager queen, traveller, frogMan, redLady, hoodedLady, oldMan, brownLady, blondLady;
	// Music variables
	private Music music;

	// **************************************************
	// Constructors
	// **************************************************
	public PlayScreen(Main game) {
		this.game = game;
		mainCam = new Camera();
		mainMap = new Map("Village");
		hud = new Hud(game.batch);
		inventory = new Inventory(game.batch);

		// Create our Box2D world, setting no gravity and allow bodies to sleep
		world = new World(new Vector2(0, 0), true);

		// Allows for debug lines of our Box2D world
		b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(world, mainMap.getMap());

		// Create the avatar in our game world
		atlasAvatar = new TextureAtlas("Sprites/Link.pack");
		atlasVillager = new TextureAtlas("Sprites/NPCs.pack");
		player = new Avatar(world, this);
		player.setInventory(inventory);

		world.setContactListener(new WorldContactListener());

		// Launch our main theme music, set on looping and is volume
		music = MusicLoader.manager.get("Audio/Music/ALTTP_Kakariko_Village.ogg", Music.class);
		music.setLooping(true);
		music.setVolume(10/Constants.PPM);
		music.play();

		// Create the NPCs
		traveller = new Villager(this, 310, 700, "Traveller");
		queen = new Villager(this, 210, 520, "Queen");
		frogMan = new Villager(this, 312, 145, "FrogMan");
		redLady = new Villager(this, 513, 793, "RedLady");
		hoodedLady = new Villager(this, 745, 463, "HoodedLady");
		oldMan = new Villager(this, 274, 918, "OldMan");
		brownLady = new Villager(this, 785, 948, "BrownLady");
		blondLady =  new Villager(this, 880, 53, "BlondLady");
				
		// Set the layers
		mainMap.setLayers();
	}

	// **************************************************
	// Getters
	// **************************************************
	public TextureAtlas getAtlasAvatar() {
		return atlasAvatar;
	}
	public TextureAtlas getAtlasVillager() {
		return atlasVillager;
	}

	public World getWorld() {
		return world;
	}

	// **************************************************
	// Private Methods
	// **************************************************
	private void drawNPC(SpriteBatch batch) {
		traveller.draw(batch);
		queen.draw(batch);
		frogMan.draw(batch);
		redLady.draw(batch);
		hoodedLady.draw(batch);
		oldMan.draw(batch);
		brownLady.draw(batch);
		blondLady.draw(batch);
	}
	
	private void updateNPC(float dt) {
		traveller.update(dt);
		queen.update(dt);
		frogMan.update(dt);
		redLady.update(dt);
		hoodedLady.update(dt);
		oldMan.update(dt);
		brownLady.update(dt);
		blondLady.update(dt);
	}
	
	private void setNPCMoving() {
		traveller.setMoving();
		queen.setMoving();
		frogMan.setMoving();
		redLady.setMoving();
		hoodedLady.setMoving();
		oldMan.setMoving();
		brownLady.setMoving();
		blondLady.setMoving();
	}
	
	private void pathNPC() {
		traveller.movePathSquare(4.6f, 3.1f, 4.6f, 3.1f);
		queen.movePathLine(2.2f, 2.2f, true, false);
		frogMan.movePathLine(2, 2, false, true);
		redLady.movePathLine(2.7f, 2.7f, false, true);
		hoodedLady.movePathLine(2.3f, 2.3f, true, false);
		oldMan.movePathLine(1.9f, 1.9f, false, false);
		brownLady.movePathSquare(0.95f, 1.2f, 0.95f, 1.2f);
		blondLady.movePathLine(0.6f, 0.6f, false, true);
	}

	// **************************************************
	// Public Methods
	// **************************************************
	@Override
	public void render(float dt) {
		update(dt);

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
		drawNPC(game.batch);
		game.batch.end();

		// Render our first plan layers
		mainMap.renderLayers(mainMap.getFirstPlan());

		// Set our batch to now draw the HUD camera sees
		game.batch.setProjectionMatrix(hud.getStage().getCamera().combined);
		hud.getStage().draw();

		// Set our batch to now draw the HUD camera sees
		game.batch.setProjectionMatrix(inventory.getStage().getCamera().combined);
		inventory.getStage().draw();
		
		// No heart of link active Game Over
		if(hud.getHealth() == 0){
			gameOver();
				game.setScreen(new GameOver(game));
				//sauvegarder partie DataSaveGame(class), faire appel a la methode save de DataSaveGame
				
				dispose();
		}
	}

	public void update(float dt) {
		// Handle user input first
		player.handleInput(dt);

		// Takes 1 step in the physics simulation (60 times per second)
		world.step(1/60f, 6, 2);

		player.update(dt);
		updateNPC(dt);
		player.setMoving();
		setNPCMoving();

		pathNPC();
		
		// Attach our gameCam to our player's coordinates
		mainCam.setPosition(player.b2body.getPosition().x, player.b2body.getPosition().y);
		mainCam.update();

		// Tell our renderer to draw only what our camera can see in our game world
		mainMap.setView(mainCam.getGameCam());


		if(Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
			hud.damage(1);
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.S)) {
			hud.cure(1);
		}


		if(Gdx.input.isKeyJustPressed(Input.Keys.B)) {
			if(inventory.getInvetoryIsVisible()) {
				hud.setImgButtonY(inventory.getNameItem());
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.N)) {
			if(inventory.getInvetoryIsVisible()) {
				hud.setImgButtonX(inventory.getNameItem());
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		// Update our game viewport
		mainCam.resize(width, height);
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
		mainMap.dispose();
		world.dispose();
		hud.dispose();
	}

	public boolean gameOver(){
		if((player.currentState == Avatar.State.DEAD) && (player.getStateTimer()>3)){
			return true;
		}
		return false;
	}
}