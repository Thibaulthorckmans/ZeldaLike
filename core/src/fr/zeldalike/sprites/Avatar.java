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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.screens.PlayScreen;

public class Avatar extends Sprite {
	// Position variables
	public enum State { UP, DOWN, LEFT, RIGHT, STANDUP, STANDDOWN, STANDLEFT, STANDRIGHT, ATTACK};
	public State currentState;
	public State previousState;
	// Animation variables
	private Animation walkRight, walkLeft, walkUp, walkDown;
	private Animation standRight, standLeft, standUp, standDown;
	private Animation attack;
	// Box2D variables
	public World world;
	public Body b2body;
	// Other variables
	private TextureRegion avatarStand;
	private float stateTimer;

	public Avatar(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("Link"));
		this.world = world;

		// Set our current and previous state initial animation
		currentState = State.STANDDOWN;
		previousState = State.STANDDOWN;

		stateTimer = 0;

		walkLeft = new Animation(0.1f, defineAnimation(0, 6, 20, 0, 20, 25)); // Define the "walk to the left" animation
		walkRight = new Animation(0.1f, defineAnimation(7, 13, 20, 0, 20, 25)); // Define the "walk to the right" animation
		walkDown = new Animation(0.1f, defineAnimation(14, 20, 20, 0, 20, 25)); // Define the "walk down" animation
		walkUp = new Animation(0.1f, defineAnimation(21, 27, 20, 0, 20, 25)); // Define the "walk up" animation
		standLeft = new Animation(0, new TextureRegion(getTexture(), 60, 0, 20, 25)); // Define the "look left" animation
		standRight = new Animation(0, new TextureRegion(getTexture(), 200, 0, 20, 25)); // Define the "look right" animation
		standDown = new Animation(0, new TextureRegion(getTexture(), 340, 0, 20, 25)); // Define the "look down" animation
		standUp = new Animation(0, new TextureRegion(getTexture(), 480, 0, 20, 25)); // Define the "look up" animation

		attack = new Animation(0.1f, defineAnimation(12, 18, 20, 0, 20, 25)); // Define the "attack" animation

		// Initial texture
		avatarStand = new TextureRegion(getTexture(), 0, 0, 20, 25);

		// Define our avatar and set his sprite bounds
		defineAvatar();
		setBounds(0, 0, 18/Constants.PPM, 23/Constants.PPM);
		setRegion(avatarStand);
	}

	public void handleInput(float dt) {
		// Control our player with immediate impulses when a key is pressed and stop when nothing is pressed
		if (Gdx.input.isKeyPressed(Input.Keys.UP) && b2body.getLinearVelocity().y <= 0.5f) {
			b2body.applyLinearImpulse(new Vector2(0, 1), b2body.getWorldCenter(), true);

		} else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && b2body.getLinearVelocity().y >= -0.5f) {
			b2body.applyLinearImpulse(new Vector2(0, -1), b2body.getWorldCenter(), true);

		} else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && b2body.getLinearVelocity().x <= 0.5f) {
			b2body.applyLinearImpulse(new Vector2(1, 0), b2body.getWorldCenter(), true);

		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && b2body.getLinearVelocity().x >= -0.5f) {
			b2body.applyLinearImpulse(new Vector2(-1, 0), b2body.getWorldCenter(), true);

		} else {
			b2body.setLinearVelocity(new Vector2(0, 0));
		}
	}

	public void isMoving() {
		// Change the constant on false if the player stop moving
		if(b2body.getLinearVelocity().x==0 && b2body.getLinearVelocity().y==0) {
			Constants.isMoving = false;
		} else {
			Constants.isMoving = true;
		}
	}

	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}

	public TextureRegion getFrame(float dt) {
		TextureRegion region;

		// Set our current position
		currentState = getState();

		switch(currentState) {
		case UP:
			region = walkUp.getKeyFrame(stateTimer, true );
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
		case ATTACK:
			region = attack.getKeyFrame(stateTimer, true);
			break;
		default:
			region = standDown.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}

	private State getState() {
		if(b2body.getLinearVelocity().y > 0) {
			return State.UP;
		}
		if(b2body.getLinearVelocity().y < 0) {
			return State.DOWN;
		}
		if(b2body.getLinearVelocity().x > 0) {
			return State.RIGHT;
		}
		if(b2body.getLinearVelocity().x < 0) {
			return State.LEFT;
		}

		if(!Constants.isMoving && previousState==State.UP) {
			return State.STANDUP;
		}
		if(!Constants.isMoving && previousState==State.DOWN) {
			return State.STANDDOWN;
		}
		if(!Constants.isMoving && previousState==State.RIGHT) {
			return State.STANDRIGHT;
		}
		if(!Constants.isMoving && previousState==State.LEFT) {
			return State.STANDLEFT;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			return State.ATTACK;
		}

		return currentState;
	}

	private void defineAvatar() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		
		// Set the player's initial position and the type of body used
		bdef.position.set(375/Constants.PPM, 610/Constants.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		b2body = world.createBody(bdef);

		// Set the body form, a circle with a radius of 7
		shape.setRadius(7f/Constants.PPM);

		fdef.shape = shape;
		b2body.createFixture(fdef);
	}

	public Array<TextureRegion> defineAnimation(int init, int limit, int posX, int posY, int width, int height) {
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for(int i = init; i < limit; i++) {
			frames.add(new TextureRegion(getTexture(), i * posX, posY, width, height));
		}

		return frames;
	}
}
