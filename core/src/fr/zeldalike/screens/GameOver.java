package fr.zeldalike.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
		this.viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
		this.stage = new Stage(this.viewport,((Main)game).batch);

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

	@Override
	public void show() {

	}

	@Override
	public void render(float delta) {
		if(Gdx.input.justTouched()){
			this.game.setScreen(new PlayScreen((Main)this.game));

			this.dispose();
		}
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.stage.draw();
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
