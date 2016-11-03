package fr.zeldalike.sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.screens.PlayScreen;

public class Avatar extends Sprite {
	public enum State { UP, DOWN, LEFT, RIGHT, STANDUP, STANDDOWN, STANDLEFT, STANDRIGHT};
	public State currentState;
	public State previousState;
	public World world;
	public Body b2body;
	private TextureRegion avatarStand;
	private Animation walkRight, walkLeft, walkUp, walkDown;
	private Animation standRight, standLeft, standUp, standDown;
	private float stateTimer;
	
	public Avatar(World world, PlayScreen screen) {
		super(screen.getAtlas().findRegion("Link"));
		this.world = world;
		
		currentState = State.DOWN;
		previousState = State.DOWN;
		
		stateTimer = 0;
		
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for(int i = 0; i < 6; i++)
			frames.add(new TextureRegion(getTexture(), i * 20, 0, 20, 25));
		walkLeft = new Animation(0.1f, frames);
		frames.clear();
		
		for (int i = 7; i < 13; i++)
			frames.add(new TextureRegion(getTexture(), i * 20, 0, 20, 25));
		walkRight = new Animation(0.1f, frames);
		frames.clear();
		
		for(int i = 14; i < 20; i++)
			frames.add(new TextureRegion(getTexture(), i * 20, 0, 20, 25));
		walkDown = new Animation(0.1f, frames);
		frames.clear();
		
		for(int i = 21; i < 27; i++)
			frames.add(new TextureRegion(getTexture(), i * 20, 0, 20, 25));
		walkUp = new Animation(0.1f, frames);
		frames.clear();
		
		frames.add(new TextureRegion(getTexture(), 3 * 20, 0, 20, 25));
		standLeft = new Animation(0.1f, frames);
		frames.clear();
		
		frames.add(new TextureRegion(getTexture(), 10 * 20, 0, 20, 25));
		standRight = new Animation(0.1f, frames);
		frames.clear();
		
		frames.add(new TextureRegion(getTexture(), 17 * 20, 0, 20, 25));
		standDown = new Animation(0.1f, frames);
		frames.clear();
		
		frames.add(new TextureRegion(getTexture(), 24 * 20, 0, 20, 25));
		standUp= new Animation(0.1f, frames);
		
		avatarStand = new TextureRegion(getTexture(), 0, 0, 20, 25);
		
		defineAvatar();
		setBounds(0, 0, 18/Constants.PPM, 23/Constants.PPM);
		setRegion(avatarStand);
	}
	
	public void update(float dt) {
		setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
		setRegion(getFrame(dt));
	}
	
	public TextureRegion getFrame(float dt) {
		currentState = getState();
		
		TextureRegion region;
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
	
	public State getState() {
		if(b2body.getLinearVelocity().y > 0)
			return State.UP;
		else if(b2body.getLinearVelocity().y < 0)
			return State.DOWN;
		else if(b2body.getLinearVelocity().x > 0)
			return State.RIGHT;
		else if(b2body.getLinearVelocity().x < 0)
			return State.LEFT;

		return currentState;
		
	}
	
	public void defineAvatar() {
		BodyDef bdef = new BodyDef();
		bdef.position.set(375/Constants.PPM, 610/Constants.PPM);
		bdef.type = BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(bdef);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(7f/Constants.PPM);
		
		fdef.shape = shape;
		b2body.createFixture(fdef);
		
	}
}
