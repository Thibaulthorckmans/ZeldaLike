package fr.zeldalike.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
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
	//	private Box2DDebugRenderer b2dr;
	// Player variables
	private TextureAtlas atlasAvatar, atlasVillager;
	private Avatar player;
	private Inventory inventory;
	// NPC's variables
	private Villager queen, traveller, frogMan, redLady, hoodedLady, oldMan, brownLady, blondLady, merchant, guard, octopus, fairy, teacher;
	// Music variables
	private Music music, musicOver;

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
		//		b2dr = new Box2DDebugRenderer();

		new B2WorldCreator(this.world, this.mainMap.getMap());

		// Create the avatar in our game world
		this.atlasAvatar = new TextureAtlas("Sprites/Link.pack");
		this.atlasVillager = new TextureAtlas("Sprites/NPCs.pack");
		this.player = new Avatar(this.world, this);
		this.player.setInventory(this.inventory);

		this.world.setContactListener(new WorldContactListener());

		// Launch our game over theme music, set on looping and is volume
		this.musicOver = MusicLoader.manager.get("Audio/Music/GOverZelda.ogg", Music.class);
		this.musicOver.setLooping(false);
		this.musicOver.setVolume(10/Constants.PPM);

		// Launch our main theme music, set on looping and is volume
		this.music = MusicLoader.manager.get("Audio/Music/ALTTP_Kakariko_Village.ogg", Music.class);
		this.music.setLooping(true);
		this.music.setVolume(10/Constants.PPM);
		this.musicOver.stop();
		this.music.play();

		// Create the NPCs
		this.traveller = new Villager(this, 310, 700, "Traveller", 'A');
		this.queen = new Villager(this, 210, 520, "Queen", 'A');
		this.frogMan = new Villager(this, 312, 145, "FrogMan", 'A');
		this.redLady = new Villager(this, 513, 793, "RedLady", 'A');
		this.hoodedLady = new Villager(this, 745, 463, "HoodedLady", 'A');
		this.oldMan = new Villager(this, 274, 918, "OldMan", 'A');
		this.brownLady = new Villager(this, 785, 948, "BrownLady", 'A');
		this.blondLady =  new Villager(this, 880, 53, "BlondLady", 'A');

		this.merchant = new Villager(this, 388, 661, "Merchant", 'B');
		this.guard = new Villager(this, 191, 796, "Guard", 'B');
		this.octopus = new Villager(this, 1000, 818, "Octopus", 'B');
		this.fairy = new Villager(this, 123, 94, "Fairy", 'B');
		this.teacher = new Villager(this, 657, 105, "Teacher", 'B');

		// Set the layers
		this.mainMap.setLayers();
	}

	// **************************************************
	// Getters
	// **************************************************
	public TextureAtlas getAtlasAvatar() {
		return this.atlasAvatar;
	}
	public TextureAtlas getAtlasVillager() {
		return this.atlasVillager;
	}

	public World getWorld() {
		return this.world;
	}

	// **************************************************
	// Private Methods
	// **************************************************
	private void drawNPC(SpriteBatch batch) {
		this.traveller.draw(batch);
		this.queen.draw(batch);
		this.frogMan.draw(batch);
		this.redLady.draw(batch);
		this.hoodedLady.draw(batch);
		this.oldMan.draw(batch);
		this.brownLady.draw(batch);
		this.blondLady.draw(batch);
		this.merchant.draw(batch);
		this.guard.draw(batch);
		this.octopus.draw(batch);
		this.fairy.draw(batch);
		this.teacher.draw(batch);
	}

	private void updateNPC(float dt) {
		this.traveller.update(dt);
		this.queen.update(dt);
		this.frogMan.update(dt);
		this.redLady.update(dt);
		this.hoodedLady.update(dt);
		this.oldMan.update(dt);
		this.brownLady.update(dt);
		this.blondLady.update(dt);
		this.merchant.update(dt);
		this.guard.update(dt);
		this.octopus.update(dt);
		this.fairy.update(dt);
		this.teacher.update(dt);
	}

	private void setNPCMoving() {
		this.traveller.setMoving();
		this.queen.setMoving();
		this.frogMan.setMoving();
		this.redLady.setMoving();
		this.hoodedLady.setMoving();
		this.oldMan.setMoving();
		this.brownLady.setMoving();
		this.blondLady.setMoving();
		this.merchant.setMoving();
		this.guard.setMoving();
		this.octopus.setMoving();
		this.fairy.setMoving();
		this.teacher.setMoving();
	}

	private void pathNPC() {
		this.traveller.movePathSquare(4.6f, 3.1f, 4.6f, 3.1f);
		this.queen.movePathLine(2.2f, 2.2f, true, false);
		this.frogMan.movePathLine(2, 2, false, true);
		this.redLady.movePathLine(2.7f, 2.7f, false, true);
		this.hoodedLady.movePathLine(2.3f, 2.3f, true, false);
		this.oldMan.movePathLine(1.9f, 1.9f, false, false);
		this.brownLady.movePathSquare(0.95f, 1.2f, 0.95f, 1.2f);
		this.blondLady.movePathLine(0.6f, 0.6f, false, true);
	}

	// **************************************************
	// Public Methods
	// **************************************************
	@Override
	public void render(float dt) {
		this.update(dt);

		// Clear the game screen with black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render our back plan layers
		this.mainMap.renderLayers(this.mainMap.getBackPlan());

		// Render our Box2DDebugLines
		//		b2dr.render(world, mainCam.getGameCam().combined);

		// Render our player
		this.game.batch.setProjectionMatrix(this.mainCam.getGameCam().combined);
		this.game.batch.begin();
		this.player.draw(this.game.batch);
		this.drawNPC(this.game.batch);
		this.game.batch.end();

		// Render our first plan layers
		this.mainMap.renderLayers(this.mainMap.getFirstPlan());

		// Set our batch to now draw the HUD camera sees
		this.game.batch.setProjectionMatrix(this.hud.getStage().getCamera().combined);
		this.hud.getStage().draw();

		// Set our batch to now draw the HUD camera sees
		this.game.batch.setProjectionMatrix(this.inventory.getStage().getCamera().combined);
		this.inventory.getStage().draw();

		// No heart of link active Game Over
		if(this.hud.getHealth() == 0){
			this.music.stop();
			this.musicOver.play();
			this.gameOver();
			this.game.setScreen(new GameOver(this.game));

			this.dispose();
		}
	}

	public void update(float dt) {
		// Handle user input first
		this.player.handleInput(dt);

		// Takes 1 step in the physics simulation (60 times per second)
		this.world.step(1/60f, 6, 2);

		this.player.update(dt);
		this.updateNPC(dt);
		this.player.setMoving();
		this.setNPCMoving();

		this.pathNPC();

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

		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_0)) {
			if(this.hud.getDialogBoxVisibility()) {
				this.hud.setDialogBoxVisibility();
				this.hud.setDialog("", "");
			} else {
				this.hud.setDialog("Narrateur :", "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...");
				this.hud.setDialogBoxVisibility();
			}
		}

		if(Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_9)) {
			if(this.mainMap.getMapName() == "Village") {
				this.mainMap.setMap("DonjonTest");
			} else if(this.mainMap.getMapName() == "DonjonTest") {
				this.mainMap.setMap("Village");
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

	public boolean gameOver(){
		if((this.player.currentState == Avatar.State.DEAD) && (this.player.getStateTimer()>3)){
			return true;
		}
		return false;
	}
}