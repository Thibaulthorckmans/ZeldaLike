package fr.zeldalike.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;
import fr.zeldalike.game.Main;

public class GameOver implements Screen{
	// **************************************************
	// Fields
	// **************************************************
	private Viewport viewport;
	private Stage stage;
	private Texture texture;
	private SpriteBatch batch;
	// Game variable
	private Game game;

	// **************************************************
	// Constructors
	// **************************************************
	/**
	 * Initialize the game over, animations and his texture.
	 */
	public GameOver(Game game){
		this.game = game;
		this.batch = new SpriteBatch();
		this.viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
		this.stage = new Stage(this.viewport,((Main)game).batch);
		
		this.texture = new Texture(Gdx.files.internal("Menu/ZeldaLikeGO.png"));
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);

		Table table = new Table();
		table.center();
		table.setFillParent(true);

		Label gameOverLabel = new Label("GAME OVER", font);
		Label playAgainLabel = new Label("Click to Play Again", font);

		table.add(gameOverLabel).expandX();
		table.row();
		table.add(playAgainLabel).expandX().padTop(10);

		this.stage.addActor(table);
	}
	
	// **************************************************
	// Public Methods
	// **************************************************
	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		this.batch.begin();
		this.batch.draw(this.texture, 0, 0);
		this.batch.end();
		
		if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
			this.game.setScreen(new PlayScreen((Main)this.game));

			this.dispose();
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		this.stage.dispose();
	}
}
