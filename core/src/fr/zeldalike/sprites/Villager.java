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
	// Position variables
	public enum State { UP, DOWN, LEFT, RIGHT, STANDUP, STANDDOWN, STANDLEFT, STANDRIGHT, ATTACK};
	public State currentState;
	public State previousState;
	// Animation variables
	private Animation walkRight, walkLeft, walkUp, walkDown;
	private Animation standRight, standLeft, standUp, standDown;
	// Other variables
	private float stateTimer;
	private boolean isMoving;

	public Villager(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		
		// Set our current and previous state initial animation
		currentState = State.STANDDOWN;
		previousState = State.STANDDOWN;
		
		walkLeft = new Animation(0.2f, defineAnimation(0, 6, 20, 0, 20, 25));
		walkRight = new Animation(0.2f, defineAnimation(7, 13, 20, 0, 20, 25));
		walkDown = new Animation(0.2f, defineAnimation(14, 20, 20, 0, 20, 25));
		walkUp = new Animation(0.2f, defineAnimation(21, 27, 20, 0, 20, 25));

		standLeft = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 60, 0, 20, 25));
		standRight = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 200, 0, 20, 25));
		standDown = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 340, 0, 20, 25));
		standUp = new Animation(0, new TextureRegion(screen.getAtlas().findRegion("Link"), 480, 0, 20, 25));
		
		stateTimer = 0;
		setBounds(0, 0, 18/Constants.PPM, 23/Constants.PPM);
	}
	
	@Override
	public void isMoving() {
		// Change the constant on false if the player stop moving
		if(b2body.getLinearVelocity().x==0 && b2body.getLinearVelocity().y==0) {
			isMoving = false;
		} else {
			isMoving = true;
		}
	}
	
	@Override
	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}
	
	@Override
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
		default:
			region = standDown.getKeyFrame(stateTimer, true);
			break;
		}

		stateTimer = currentState == previousState ? stateTimer + dt : 0;
		previousState = currentState;
		return region;
	}
	
	@Override
	public State getState() {
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

		if(isMoving && previousState==State.UP) {
			return State.STANDUP;
		}
		if(isMoving && previousState==State.DOWN) {
			return State.STANDDOWN;
		}
		if(isMoving && previousState==State.RIGHT) {
			return State.STANDRIGHT;
		}
		if(isMoving && previousState==State.LEFT) {
			return State.STANDLEFT;
		}
	
		return currentState;
	}
	
	@Override
	public void movePath() {
		if (b2body.getLinearVelocity().x <= 0.1f) {
			b2body.applyLinearImpulse(new Vector2(0.3f, 0), b2body.getWorldCenter(), true);
		}
	}

	@Override
	public void defineNPC() {
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		
		// Set the player's initial position and the type of body used
		bdef.position.set(375/Constants.PPM, 650/Constants.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		
		b2body = world.createBody(bdef);

		// Set the body form, a circle with a radius of 7
		shape.setRadius(7f/Constants.PPM);
		
		fdef.filter.categoryBits = Constants.NPC_BIT;
		fdef.filter.maskBits = Constants.DEFAULT_BIT | Constants.LINK_BIT | Constants.PLANT_BIT | Constants.NPC_BIT;

		fdef.shape = shape;
		b2body.createFixture(fdef);
	}
	
	public Array<TextureRegion> defineAnimation(int init, int limit, int posX, int posY, int width, int height) {
		Array<TextureRegion> frames = new Array<TextureRegion>();

		for(int i = init; i < limit; i++) {
			frames.add(new TextureRegion(screen.getAtlas().findRegion("Link"), i * posX, posY, width, height));
		}

		return frames;
	}
}
