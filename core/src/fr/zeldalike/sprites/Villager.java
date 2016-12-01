package fr.zeldalike.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.screens.PlayScreen;

public class Villager extends NonPlayableCharacter {
	// **************************************************
	// Fields
	// **************************************************
	private float stateTimer;
	// Position variables
	public enum State {UP, DOWN, LEFT, RIGHT, STANDUP, STANDDOWN, STANDLEFT, STANDRIGHT, ATTACK};
	public State currentState;
	public State previousState;
	// Animation variables
	private Animation walkRight, walkLeft, walkUp, walkDown;
	private Animation standRight, standLeft, standUp, standDown;
	// Movement variables
	private boolean isMoving = false;
	// Deplacement variables
	private float timer;
	private boolean verticalAxe = false;
	private boolean stopMovingXPos = true;
	private boolean stopMovingYPos = true;
	private boolean stopMovingXNeg = true;
	private boolean stopMovingYNeg = true;
	
	// **************************************************
	// Constructors
	// **************************************************
	/**
	 * Initialize the villager's state and animations.
	 */
	public Villager(PlayScreen screen, float x, float y) {
		super(screen, x, y);

		// Set our current and previous state initial animation
		currentState = State.STANDDOWN;
		previousState = State.STANDDOWN;

		walkLeft = new Animation(0.2f, defineAnimation(0, 6, 50, 20, 60, 70));
		walkRight = new Animation(0.2f, defineAnimation(7, 13, 50, 20, 60, 70));
		walkDown = new Animation(0.2f, defineAnimation(14, 20, 50, 20, 60, 70));
		walkUp = new Animation(0.2f, defineAnimation(21, 27, 50, 20, 60, 70));

		standLeft = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 200, 20, 60, 70));
		standRight = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 500, 20, 60, 70));
		standDown = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 850, 20, 60, 70));
		standUp = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 1250, 20, 60, 70));

		stateTimer = 0;
		setBounds(0, 0, 60 / Constants.PPM, 70 / Constants.PPM);
	}

	// **************************************************
	// Getters
	// **************************************************
	@Override
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
		default:
			region = standDown.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}
	
	/**
	 * Return the actual state of the villager.
	 * @return
	 * State
	 */
	@Override
	public State getState() {
		if (b2body.getLinearVelocity().y > 0) {
			return State.UP;
		}
		if (b2body.getLinearVelocity().y < 0) {
			return State.DOWN;
		}
		if (b2body.getLinearVelocity().x > 0) {
			return State.RIGHT;
		}
		if (b2body.getLinearVelocity().x < 0) {
			return State.LEFT;
		}

		if (isMoving && previousState == State.UP) {
			return State.STANDUP;
		}
		if (isMoving && previousState == State.DOWN) {
			return State.STANDDOWN;
		}
		if (isMoving && previousState == State.RIGHT) {
			return State.STANDRIGHT;
		}
		if (isMoving && previousState == State.LEFT) {
			return State.STANDLEFT;
		}

		return currentState;
	}
	
	// **************************************************
	// Setters
	// **************************************************
	@Override
	public void setMoving() {
		// Change the constant on false if the player stop moving
		if (b2body.getLinearVelocity().x == 0 && b2body.getLinearVelocity().y == 0) {
			isMoving = false;
		} else {
			isMoving = true;
		}
	}
	
	// **************************************************
	// Private Methods
	// **************************************************
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
			frames.add(new TextureRegion(screen.getAtlas().findRegion("Link"), i * posX, posY, width, height));
		}
		return frames;
	}
	// **************************************************
	// Public Methods
	// **************************************************
	@Override
	protected void defineNPC(float x, float y) {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();

		// Set the player's initial position and the type of body used
		bdef.position.set(x/Constants.PPM, y/Constants.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;

		b2body = world.createBody(bdef);

		// Set the body form, a circle with a radius of 7
		shape.setRadius(7f/Constants.PPM);

		fdef.filter.categoryBits = Constants.NPC_BIT;
		fdef.filter.maskBits = Constants.DEFAULT_BIT | Constants.LINK_BIT | Constants.PLANT_BIT | Constants.NPC_BIT;

		fdef.shape = shape;
		b2body.createFixture(fdef);
	}
	
	@Override
	public void movePathLine(float timer1, float timer2, boolean yAxe) {
		timer += 0.005f;
		
		if (!yAxe) {
			if (timer < timer1 && b2body.getLinearVelocity().x <= 0.1f) {
				b2body.applyLinearImpulse(new Vector2(0.2f, 0), b2body.getWorldCenter(), true);
			} else if (timer > timer1 && timer < timer1 + timer2 && b2body.getLinearVelocity().x >= -0.1f){
				b2body.applyLinearImpulse(new Vector2(-0.2f, 0), b2body.getWorldCenter(), true);
			}
		} else {
			if (timer < timer1 && b2body.getLinearVelocity().y <= 0.1f) {
				b2body.applyLinearImpulse(new Vector2(0, 0.2f), b2body.getWorldCenter(), true);
			} else if (timer > timer1 && timer < timer1 + timer2 && b2body.getLinearVelocity().y >= -0.1f){
				b2body.applyLinearImpulse(new Vector2(0, -0.2f), b2body.getWorldCenter(), true);
			}
		}
		
		if (timer > timer1 + timer2) {
			timer = 0;
		}
	}
	
	@Override
	public void movePathSquare(float timerPosX, float timerNegY, float timerNegX, float timerPosY) {
		timer += 0.005f;
		float yNeg = timerPosX + timerNegY;
		float xNeg = yNeg + timerNegX;
		float yPos = xNeg + timerPosY;

		if (timer < timerPosX && b2body.getLinearVelocity().x <= 0.1f && !verticalAxe) {
			stopMoving(stopMovingXPos);
			b2body.applyLinearImpulse(new Vector2(0.2f, 0), b2body.getWorldCenter(), true);
		} else if (timer < yNeg && b2body.getLinearVelocity().y >= -0.1f && verticalAxe){
			stopMoving(stopMovingYNeg);
			b2body.applyLinearImpulse(new Vector2(0, -0.2f), b2body.getWorldCenter(), true);
		} else if (timer > yNeg && timer < xNeg && b2body.getLinearVelocity().x >= -0.1f && !verticalAxe){
			stopMoving(stopMovingXNeg);
			b2body.applyLinearImpulse(new Vector2(-0.2f, 0), b2body.getWorldCenter(), true);
		} else if (timer > xNeg && timer < yPos && b2body.getLinearVelocity().y <= 0.1f && verticalAxe){
			stopMoving(stopMovingYPos);
			b2body.applyLinearImpulse(new Vector2(0, 0.2f), b2body.getWorldCenter(), true);
		} else if (timer > yPos) {
			timer = 0;
			stopMovingXPos = true;
			stopMovingYPos = true;
			stopMovingXNeg = true;
			stopMovingYNeg = true;
		}

		if (timer < timerPosX || timer > yNeg && timer < xNeg) {
			verticalAxe = false;
		} else if (timer > timerPosX && timer < yNeg || timer > xNeg) {
			verticalAxe = true;
		}
	}
	
	public void stopMoving(boolean stopMoving) {
		if (stopMoving) {
			b2body.setLinearVelocity(new Vector2(0, 0));
		}
		stopMoving = false;
	}

	@Override
	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() / 2,
				b2body.getPosition().y - getHeight() / 2);
		this.setRegion(getFrame(dt));
	}
}
