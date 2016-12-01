package fr.zeldalike.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.scenes.Inventory;
import fr.zeldalike.screens.PlayScreen;

/**
 *	Define the player, his movements and his animation.
 */
public class Avatar extends Sprite {
	// **************************************************
	// Fields
	// **************************************************
	private TextureRegion avatarStand;
	private TextureRegion lunkDead;
	private float stateTimer;
	private boolean lunkIsDead;
	// Position variables
	public enum State {UP, DOWN, LEFT, RIGHT, STANDUP, STANDDOWN, STANDLEFT, STANDRIGHT, ATTACKDOWN, ATTACKUP, ATTACKLEFT, ATTACKRIGHT, DEAD};
	public State currentState;
	public State previousState;
	// Animation variables
	private Animation walkRight, walkLeft, walkUp, walkDown;
	private Animation standRight, standLeft, standUp, standDown;
	private Animation attackright, attackleft, attackup, attackdown;
	// Box2D variables
	public World world;
	public Body b2body;
	// Movement variables
	public boolean isMoving = false;
	//Inventory variables
	private Inventory inventory;

	// **************************************************
	// Constructors
	// **************************************************
	/**
	 * Initialize the player's state, animations and his texture.
	 */
	public Avatar(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("Link"));
		this.world = world;

		// Set our current and previous state initial animation
		this.currentState = State.STANDDOWN;
		this.previousState = State.STANDDOWN;

		this.stateTimer = 0;

		this.walkLeft = new Animation(0.1f, this.defineAnimation(0, 6, 50, 20, 60, 70));
		this.walkRight = new Animation(0.1f, this.defineAnimation(7, 13, 50, 20, 60, 70));
		this.walkDown = new Animation(0.1f, this.defineAnimation(14, 20, 50, 20, 60, 70));
		this.walkUp = new Animation(0.1f, this.defineAnimation(21, 27, 50, 20, 60, 70));

		this.standLeft = new Animation(0, new TextureRegion(this.getTexture(), 200, 20, 60, 70));
		this.standRight = new Animation(0, new TextureRegion(this.getTexture(), 500, 20, 60, 70));
		this.standDown = new Animation(0, new TextureRegion(this.getTexture(), 850, 20, 60, 70));
		this.standUp = new Animation(0, new TextureRegion(this.getTexture(), 1250, 20, 60, 70));

		this.attackleft = new Animation(0.1f, this.defineAnimation(28, 35, 50, 20, 60, 70));
		this.attackright = new Animation(0.1f, this.defineAnimation(36, 43, 50, 20, 60, 70));
		this.attackup = new Animation(0.1f, this.defineAnimation(44, 50, 50, 20, 60, 70));
		this.attackdown = new Animation(0.1f, this.defineAnimation(51, 56, 50, 20, 60, 70));

		// Initial texture
		this.avatarStand = new TextureRegion(this.getTexture(), 0, 0, 20, 25);

		// Define our avatar and set his sprite bounds
		this.defineAvatar();
		this.setBounds(0, 0, 60 / Constants.PPM, 70 / Constants.PPM);
		this.setRegion(this.avatarStand);
	}

	// **************************************************
	// Getters
	// **************************************************
	public TextureRegion getFrame(float dt) {
		TextureRegion region;

		// Set our current position
		this.currentState = this.getState();

		switch (this.currentState) {
		case UP:
			region = this.walkUp.getKeyFrame(this.stateTimer, true);
			break;
		case RIGHT:
			region = this.walkRight.getKeyFrame(this.stateTimer, true);
			break;
		case DOWN:
			region = this.walkDown.getKeyFrame(this.stateTimer, true);
			break;
		case LEFT:
			region = this.walkLeft.getKeyFrame(this.stateTimer, true);
			break;
		case STANDUP:
			region = this.standUp.getKeyFrame(this.stateTimer, true);
			break;
		case STANDRIGHT:
			region = this.standRight.getKeyFrame(this.stateTimer, true);
			break;
		case STANDDOWN:
			region = this.standDown.getKeyFrame(this.stateTimer, true);
			break;
		case STANDLEFT:
			region = this.standLeft.getKeyFrame(this.stateTimer, true);
			break;
		case ATTACKRIGHT:
			region = this.attackright.getKeyFrame(this.stateTimer, true);
			if (this.stateTimer >= 0.7) {
				this.stateTimer = 0;
				this.currentState = State.STANDRIGHT;
			}
			break;
		case ATTACKUP:
			region = this.attackup.getKeyFrame(this.stateTimer, true);

			if (this.stateTimer >= 0.7) {
				this.stateTimer = 0;
				this.currentState = State.STANDUP;
			}
			break;
		case ATTACKDOWN:
			region = this.attackdown.getKeyFrame(this.stateTimer, true);
			if (this.stateTimer >= 0.5) {
				this.stateTimer = 0;
				this.currentState = State.STANDDOWN;

			}
			break;
		case ATTACKLEFT:
			region = this.attackleft.getKeyFrame(this.stateTimer, true);
			// animation complete d'attaque
			if (this.stateTimer >= 0.7) {
				this.stateTimer = 0;
				this.currentState = State.STANDLEFT;
			}
			break;
		case DEAD:
			region = this.lunkDead;
			break;
		default:
			region = this.standDown.getKeyFrame(this.stateTimer, true);
			break;
		}

		this.stateTimer = this.currentState == this.previousState ? this.stateTimer + dt : 0;
		this.previousState = this.currentState;
		return region;
	}

	/**
	 * Return the actual state of the player.
	 * @return
	 * State
	 */
	private State getState() {
		FixtureDef fdef = new FixtureDef();
		EdgeShape head = new EdgeShape();
		head.set(new Vector2(0 / Constants.PPM, 0 / Constants.PPM), new Vector2(0 / Constants.PPM, 0 / Constants.PPM));
		this.b2body.getFixtureList();
		fdef.shape = head;
		fdef.isSensor = false;

		if ((this.b2body.getLinearVelocity().y > 0)
				&& ((this.currentState != State.ATTACKUP) || (this.previousState != State.ATTACKUP))
				&& ((this.currentState != State.ATTACKRIGHT) || (this.previousState != State.ATTACKRIGHT))
				&& ((this.currentState != State.ATTACKLEFT) || (this.previousState != State.ATTACKLEFT))
				&& ((this.currentState != State.ATTACKDOWN) || (this.previousState != State.ATTACKDOWN))) {
			return State.UP;
		}
		if ((this.b2body.getLinearVelocity().y < 0)
				&& ((this.currentState != State.ATTACKDOWN) || (this.previousState != State.ATTACKDOWN))
				&& ((this.currentState != State.ATTACKRIGHT) || (this.previousState != State.ATTACKRIGHT))
				&& ((this.currentState != State.ATTACKLEFT) || (this.previousState != State.ATTACKLEFT))
				&& ((this.currentState != State.ATTACKUP) || (this.previousState != State.ATTACKUP))) {
			return State.DOWN;
		}
		if ((this.b2body.getLinearVelocity().x > 0)
				&& ((this.currentState != State.ATTACKRIGHT) || (this.previousState != State.ATTACKRIGHT))
				&& ((this.currentState != State.ATTACKLEFT) || (this.previousState != State.ATTACKLEFT))
				&& ((this.currentState != State.ATTACKUP) || (this.previousState != State.ATTACKUP))
				&& ((this.currentState != State.ATTACKDOWN) || (this.previousState != State.ATTACKDOWN))) {
			return State.RIGHT;
		}
		if ((this.b2body.getLinearVelocity().x < 0)
				&& ((this.currentState != State.ATTACKLEFT) || (this.previousState != State.ATTACKLEFT))
				&& ((this.currentState != State.ATTACKUP) || (this.previousState != State.ATTACKUP))
				&& ((this.currentState != State.ATTACKDOWN) || (this.previousState != State.ATTACKDOWN))
				&& ((this.currentState != State.ATTACKRIGHT) || (this.previousState != State.ATTACKRIGHT))) {
			return State.LEFT;
		}

		if (!this.isMoving && (this.previousState == State.UP)) {
			return State.STANDUP;
		}
		if (!this.isMoving && (this.previousState == State.DOWN)) {
			return State.STANDDOWN;
		}
		if (!this.isMoving && (this.previousState == State.RIGHT)) {
			return State.STANDRIGHT;
		}
		if (!this.isMoving && (this.previousState == State.LEFT)) {
			return State.STANDLEFT;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((this.currentState == State.LEFT) || (this.currentState == State.STANDLEFT))) {
			return State.ATTACKLEFT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((this.currentState == State.RIGHT) || (this.currentState == State.STANDRIGHT))) {
			return State.ATTACKRIGHT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((this.currentState == State.UP) || (this.currentState == State.STANDUP))) {
			return State.ATTACKUP;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((this.currentState == State.DOWN) || (this.currentState == State.STANDDOWN))) {
			return State.ATTACKDOWN;
		}
		if(this.lunkIsDead){
			return State.DEAD;
		}

		// Creation collision epee
		if ((this.currentState == State.ATTACKLEFT)) {
			head.set(new Vector2(-20 / Constants.PPM, 10 / Constants.PPM),
					new Vector2(-20 / Constants.PPM, -10 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			this.b2body.createFixture(fdef).setUserData("head");

		}

		if (((this.currentState == State.ATTACKRIGHT) && (this.stateTimer <= 0.7))) {
			head.set(new Vector2(16 / Constants.PPM, -10 / Constants.PPM),
					new Vector2(16 / Constants.PPM, 10 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			this.b2body.createFixture(fdef).setUserData("head");
			this.b2body.getFixtureList();
		}

		if (((this.currentState == State.ATTACKUP) && (this.stateTimer <= 0.7))) {
			head.set(new Vector2(10 / Constants.PPM, 20 / Constants.PPM),
					new Vector2(-10 / Constants.PPM, 20 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			this.b2body.createFixture(fdef).setUserData("head");
			this.b2body.getFixtureList();
		}

		if (((this.currentState == State.ATTACKDOWN) && (this.stateTimer <= 0.7))) {
			head.set(new Vector2(-10 / Constants.PPM, -20 / Constants.PPM),
					new Vector2(10 / Constants.PPM, -20 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			this.b2body.createFixture(fdef).setUserData("head");
			this.b2body.getFixtureList();
		}

		return this.currentState;
	}

	public float getStateTimer() {
		return this.stateTimer;
	}

	public boolean isLunkIsDead() {
		return this.lunkIsDead;
	}

	// **************************************************
	// Setters
	// **************************************************
	public void setMoving() {
		if ((this.b2body.getLinearVelocity().x == 0) && (this.b2body.getLinearVelocity().y == 0)) {
			this.isMoving = false;
		} else {
			this.isMoving = true;
		}
	}

	public void setStateTimer(float stateTimer) {
		this.stateTimer = stateTimer;
	}

	public void setLunkIsDead(boolean lunkIsDead) {
		this.lunkIsDead = lunkIsDead;
	}

	public void setInventory(Inventory pInventory) {
		this.inventory = pInventory;
	}

	// **************************************************
	// Private Methods
	// **************************************************
	/**
	 * Define the player's initial position, his collision body and the elements that interact with him.
	 */
	private void defineAvatar() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();

		// Set the player's initial position and the type of body used
		bdef.position.set(375 / Constants.PPM, 610 / Constants.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;

		this.b2body = this.world.createBody(bdef);

		// Set the body form, a circle with a radius of 7
		shape.setRadius(7f / Constants.PPM);

		fdef.filter.categoryBits = Constants.LINK_BIT;
		fdef.filter.maskBits = Constants.DEFAULT_BIT | Constants.NPC_BIT | Constants.RUBY_BIT | Constants.PLANT_BIT;

		fdef.shape = shape;
		this.b2body.createFixture(fdef);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / Constants.PPM, 7 / Constants.PPM), new Vector2(2 / Constants.PPM, 7 / Constants.PPM));
		fdef.shape = head;
		fdef.isSensor = true;

		this.b2body.createFixture(fdef).setUserData("head");
	}

	/**
	 * Define the player's animation
	 * @param init Position of the first image.
	 * @param limit Position of the last image.
	 * @param posX Position of the image's lower left corner.
	 * @param posY Position of the image's upper left corner.
	 * @param width Image's width.
	 * @param height Image's height.
	 * @return
	 * frames
	 */
	private Array<TextureRegion> defineAnimation(int init, int limit, int posX, int posY, int width, int height) {
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i = init; i < limit; i++) {
			frames.add(new TextureRegion(this.getTexture(), i * posX, posY, width, height));
		}
		return frames;
	}
	// **************************************************
	// Public Methods
	// **************************************************
	/**
	 * Used to control the player with the keyboard inputs.
	 * <br>
	 * Actually diriged by the directional arrows
	 */
	public void handleInput(float dt) {
		if(Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)) {
			this.inventory.setInventoryVisibility();
		}

		if(this.inventory.getInvetoryIsVisible()) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				this.inventory.moveCursor(0, 1);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				this.inventory.moveCursor(1, 0);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				this.inventory.moveCursor(0, -1);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				this.inventory.moveCursor(-1, 0);
			}
		} else {
			// Control our player with immediate impulses when a key is pressed and
			// stop when nothing is pressed
			if (Gdx.input.isKeyPressed(Input.Keys.UP) && (this.b2body.getLinearVelocity().y <= 0.5f)) {
				this.b2body.applyLinearImpulse(new Vector2(0, 1), this.b2body.getWorldCenter(), true);

			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && (this.b2body.getLinearVelocity().y >= -0.5f)) {
				this.b2body.applyLinearImpulse(new Vector2(0, -1), this.b2body.getWorldCenter(), true);

			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (this.b2body.getLinearVelocity().x <= 0.5f)) {
				this.b2body.applyLinearImpulse(new Vector2(1, 0), this.b2body.getWorldCenter(), true);

			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (this.b2body.getLinearVelocity().x >= -0.5f)) {
				this.b2body.applyLinearImpulse(new Vector2(-1, 0), this.b2body.getWorldCenter(), true);

			} else {
				this.b2body.setLinearVelocity(new Vector2(0, 0));
			}

			JsonMove.sendRequest(this.b2body);
		}
	}

	public void update(float dt) {
		this.setPosition(this.b2body.getPosition().x - (this.getWidth() / 2), this.b2body.getPosition().y - (this.getHeight() / 2));
		this.setRegion(this.getFrame(dt));
	}
}
