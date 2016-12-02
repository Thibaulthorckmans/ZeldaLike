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
		super(screen.getAtlasAvatar().findRegion("Link"));
		this.world = world;

		// Set our current and previous state initial animation
		currentState = State.STANDDOWN;
		previousState = State.STANDDOWN;

		stateTimer = 0;

		walkLeft = new Animation(0.1f, defineAnimation(0, 6, 50, 20, 60, 70));
		walkRight = new Animation(0.1f, defineAnimation(7, 13, 50, 20, 60, 70));
		walkDown = new Animation(0.1f, defineAnimation(14, 20, 50, 20, 60, 70));
		walkUp = new Animation(0.1f, defineAnimation(21, 27, 50, 20, 60, 70));

		standLeft = new Animation(0, new TextureRegion(getTexture(), 200, 20, 60, 70));
		standRight = new Animation(0, new TextureRegion(getTexture(), 500, 20, 60, 70));
		standDown = new Animation(0, new TextureRegion(getTexture(), 850, 20, 60, 70));
		standUp = new Animation(0, new TextureRegion(getTexture(), 1250, 20, 60, 70));

		attackleft = new Animation(0.1f, defineAnimation(28, 35, 50, 20, 60, 70));
		attackright = new Animation(0.1f, defineAnimation(36, 43, 50, 20, 60, 70));
		attackup = new Animation(0.1f, defineAnimation(44, 50, 50, 20, 60, 70));
		attackdown = new Animation(0.1f, defineAnimation(51, 56, 50, 20, 60, 70));

		// Initial texture
		avatarStand = new TextureRegion(getTexture(), 0, 0, 20, 25);

		// Define our avatar and set his sprite bounds
		defineAvatar();
		setBounds(0, 0, 60 / Constants.PPM, 70 / Constants.PPM);
		this.setRegion(avatarStand);
	}

	// **************************************************
	// Getters
	// **************************************************
	public TextureRegion getFrame(float dt) {
		TextureRegion region;

		// Set our current position
		currentState = getState();

		switch (currentState) {
		case UP:
			region = walkUp.getKeyFrame(stateTimer, true);
			break;
		case RIGHT:
			region = walkRight.getKeyFrame(stateTimer, true);
			break;
		case DOWN:
			region = walkDown.getKeyFrame(stateTimer, true);
			break;
		case LEFT:
			region = walkLeft.getKeyFrame(stateTimer, true);
			break;
		case STANDUP:
			region = standUp.getKeyFrame(stateTimer, true);
			break;
		case STANDRIGHT:
			region = standRight.getKeyFrame(stateTimer, true);
			break;
		case STANDDOWN:
			region = standDown.getKeyFrame(stateTimer, true);
			break;
		case STANDLEFT:
			region = standLeft.getKeyFrame(stateTimer, true);
			break;
		case ATTACKRIGHT:
			region = attackright.getKeyFrame(stateTimer, true);
			if (stateTimer >= 0.7) {
				stateTimer = 0;
				currentState = State.STANDRIGHT;
			}
			break;
		case ATTACKUP:
			region = attackup.getKeyFrame(stateTimer, true);

			if (stateTimer >= 0.7) {
				stateTimer = 0;
				currentState = State.STANDUP;
			}
			break;
		case ATTACKDOWN:
			region = attackdown.getKeyFrame(stateTimer, true);
			if (stateTimer >= 0.5) {
				stateTimer = 0;
				currentState = State.STANDDOWN;

			}
			break;
		case ATTACKLEFT:
			region = attackleft.getKeyFrame(stateTimer, true);
			// animation complete d'attaque
			if (stateTimer >= 0.7) {
				stateTimer = 0;
				currentState = State.STANDLEFT;
			}
			break;
		case DEAD:
			region = lunkDead;
			break;
		default:
			region = standDown.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
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
		b2body.getFixtureList();
		fdef.shape = head;
		fdef.isSensor = false;

		if ((b2body.getLinearVelocity().y > 0)
				&& ((currentState != State.ATTACKUP) || (previousState != State.ATTACKUP))
				&& ((currentState != State.ATTACKRIGHT) || (previousState != State.ATTACKRIGHT))
				&& ((currentState != State.ATTACKLEFT) || (previousState != State.ATTACKLEFT))
				&& ((currentState != State.ATTACKDOWN) || (previousState != State.ATTACKDOWN))) {
			return State.UP;
		}
		if ((b2body.getLinearVelocity().y < 0)
				&& ((currentState != State.ATTACKDOWN) || (previousState != State.ATTACKDOWN))
				&& ((currentState != State.ATTACKRIGHT) || (previousState != State.ATTACKRIGHT))
				&& ((currentState != State.ATTACKLEFT) || (previousState != State.ATTACKLEFT))
				&& ((currentState != State.ATTACKUP) || (previousState != State.ATTACKUP))) {
			return State.DOWN;
		}
		if ((b2body.getLinearVelocity().x > 0)
				&& ((currentState != State.ATTACKRIGHT) || (previousState != State.ATTACKRIGHT))
				&& ((currentState != State.ATTACKLEFT) || (previousState != State.ATTACKLEFT))
				&& ((currentState != State.ATTACKUP) || (previousState != State.ATTACKUP))
				&& ((currentState != State.ATTACKDOWN) || (previousState != State.ATTACKDOWN))) {
			return State.RIGHT;
		}
		if ((b2body.getLinearVelocity().x < 0)
				&& ((currentState != State.ATTACKLEFT) || (previousState != State.ATTACKLEFT))
				&& ((currentState != State.ATTACKUP) || (previousState != State.ATTACKUP))
				&& ((currentState != State.ATTACKDOWN) || (previousState != State.ATTACKDOWN))
				&& ((currentState != State.ATTACKRIGHT) || (previousState != State.ATTACKRIGHT))) {
			return State.LEFT;
		}

		if (!isMoving && (previousState == State.UP)) {
			return State.STANDUP;
		}
		if (!isMoving && (previousState == State.DOWN)) {
			return State.STANDDOWN;
		}
		if (!isMoving && (previousState == State.RIGHT)) {
			return State.STANDRIGHT;
		}
		if (!isMoving && (previousState == State.LEFT)) {
			return State.STANDLEFT;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((currentState == State.LEFT) || (currentState == State.STANDLEFT))) {
			return State.ATTACKLEFT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((currentState == State.RIGHT) || (currentState == State.STANDRIGHT))) {
			return State.ATTACKRIGHT;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((currentState == State.UP) || (currentState == State.STANDUP))) {
			return State.ATTACKUP;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)
				&& ((currentState == State.DOWN) || (currentState == State.STANDDOWN))) {
			return State.ATTACKDOWN;
		}
		if(lunkIsDead){
			return State.DEAD;
		}

		// Creation collision epee
		if ((currentState == State.ATTACKLEFT)) {
			head.set(new Vector2(-20 / Constants.PPM, 10 / Constants.PPM),
					new Vector2(-20 / Constants.PPM, -10 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			b2body.createFixture(fdef).setUserData("head");

		}

		if (((currentState == State.ATTACKRIGHT) && (stateTimer <= 0.7))) {
			head.set(new Vector2(16 / Constants.PPM, -10 / Constants.PPM),
					new Vector2(16 / Constants.PPM, 10 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			b2body.createFixture(fdef).setUserData("head");
			b2body.getFixtureList();
		}

		if (((currentState == State.ATTACKUP) && (stateTimer <= 0.7))) {
			head.set(new Vector2(10 / Constants.PPM, 20 / Constants.PPM),
					new Vector2(-10 / Constants.PPM, 20 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			b2body.createFixture(fdef).setUserData("head");
			b2body.getFixtureList();
		}

		if (((currentState == State.ATTACKDOWN) && (stateTimer <= 0.7))) {
			head.set(new Vector2(-10 / Constants.PPM, -20 / Constants.PPM),
					new Vector2(10 / Constants.PPM, -20 / Constants.PPM));
			fdef.isSensor = true;
			// this.b2body.createFixture(head, this.stateTimer);
			b2body.createFixture(fdef).setUserData("head");
			b2body.getFixtureList();
		}

		return currentState;
	}

	public float getStateTimer() {
		return stateTimer;
	}

	public boolean isLunkIsDead() {
		return lunkIsDead;
	}

	// **************************************************
	// Setters
	// **************************************************
	public void setMoving() {
		if ((b2body.getLinearVelocity().x == 0) && (b2body.getLinearVelocity().y == 0)) {
			isMoving = false;
		} else {
			isMoving = true;
		}
	}

	public void setStateTimer(float stateTimer) {
		this.stateTimer = stateTimer;
	}

	public void setLunkIsDead(boolean lunkIsDead) {
		this.lunkIsDead = lunkIsDead;
	}

	public void setInventory(Inventory pInventory) {
		inventory = pInventory;
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

		b2body = world.createBody(bdef);

		// Set the body form, a circle with a radius of 7
		shape.setRadius(7f / Constants.PPM);

		fdef.filter.categoryBits = Constants.LINK_BIT;
		fdef.filter.maskBits = Constants.DEFAULT_BIT | Constants.NPC_BIT | Constants.RUBY_BIT | Constants.PLANT_BIT;

		fdef.shape = shape;
		b2body.createFixture(fdef);

		EdgeShape head = new EdgeShape();
		head.set(new Vector2(-2 / Constants.PPM, 7 / Constants.PPM), new Vector2(2 / Constants.PPM, 7 / Constants.PPM));
		fdef.shape = head;
		fdef.isSensor = true;

		b2body.createFixture(fdef).setUserData("head");
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
			frames.add(new TextureRegion(getTexture(), i * posX, posY, width, height));
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
			inventory.setInventoryVisibility();
		}

		if(inventory.getInvetoryIsVisible()) {
			if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
				inventory.moveCursor(0, 1);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
				inventory.moveCursor(1, 0);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
				inventory.moveCursor(0, -1);
			}
			if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
				inventory.moveCursor(-1, 0);
			}
		} else {
			// Control our player with immediate impulses when a key is pressed and
			// stop when nothing is pressed
			if (Gdx.input.isKeyPressed(Input.Keys.UP) && (b2body.getLinearVelocity().y <= 0.5f)) {
				b2body.applyLinearImpulse(new Vector2(0, 1), b2body.getWorldCenter(), true);

			} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && (b2body.getLinearVelocity().y >= -0.5f)) {
				b2body.applyLinearImpulse(new Vector2(0, -1), b2body.getWorldCenter(), true);

			} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && (b2body.getLinearVelocity().x <= 0.5f)) {
				b2body.applyLinearImpulse(new Vector2(1, 0), b2body.getWorldCenter(), true);

			} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && (b2body.getLinearVelocity().x >= -0.5f)) {
				b2body.applyLinearImpulse(new Vector2(-1, 0), b2body.getWorldCenter(), true);

			} else {
				b2body.setLinearVelocity(new Vector2(0, 0));
			}

			JsonMove.sendRequest(b2body);
		}
	}

	public void update(float dt) {
		setPosition(b2body.getPosition().x - (getWidth() / 2), b2body.getPosition().y - (getHeight() / 2));
		this.setRegion(getFrame(dt));
	}
}
