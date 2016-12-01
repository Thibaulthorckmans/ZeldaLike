package fr.zeldalike.screens;

/**
 * Define the camera.
 */
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;

public class Camera {
	// **************************************************
	// Fields
	// **************************************************
	private OrthographicCamera gameCam;
	private Viewport gamePort;

	// **************************************************
	// Constructors
	// **************************************************
	public Camera() {
		// Create cam used to follow through the world
		this.gameCam = new OrthographicCamera();
		// Create a FitViewport to maintain virtual aspect ratio despite screen size
		this.gamePort = new FitViewport(Constants.V_WIDTH / Constants.PPM, Constants.V_HEIGHT / Constants.PPM, this.gameCam);
		// Initially set our gameCam to be centered correctly at the start of the game
		this.gameCam.position.set(this.gamePort.getWorldWidth()/2, this.gamePort.getWorldHeight()/2, 0);
	}

	// **************************************************
	// Getters
	// **************************************************
	public Viewport getGamePort() {
		return this.gamePort;
	}

	public OrthographicCamera getGameCam() {
		return this.gameCam;
	}

	// **************************************************
	// Setters
	// **************************************************
	public void setGamePort(Viewport gamePort) {
		this.gamePort = gamePort;
	}

	public void setGameCam(OrthographicCamera gameCam) {
		this.gameCam = gameCam;
	}

	// **************************************************
	// Public Methods
	// **************************************************
	/**
	 * Set the camera's position.
	 * @param x Position on the x axis.
	 * @param y Position on the y axis.
	 */
	public void setPosition(float x, float y) {
		this.gameCam.position.x = x;
		this.gameCam.position.y = y;
	}

	public void update() {
		this.gameCam.update();
	}

	public void resize(int width, int height) {
		this.gamePort.update(width, height);
	}
}