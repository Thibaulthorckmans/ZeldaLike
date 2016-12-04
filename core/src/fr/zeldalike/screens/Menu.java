package fr.zeldalike.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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

public class Menu implements Screen {
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
	 * Initialize the menu game, animations and his texture.
	 */
	public Menu(Game game) {
		this.game = game;
		this.batch = new SpriteBatch();
		this.viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
		this.stage= new Stage(this.viewport, ((Main)game).batch);
		
		this.texture = new Texture(Gdx.files.internal("Menu/ZeldaLikePres2.png"));
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.RED);
		
		Table table = new Table();
		table.center();
		table.setFillParent(true);
		
		Label menuLabel = new Label("MENU", font);
		Label playGameLabel = new Label("Click to Run", font);
		
		table.add(menuLabel).expandX();
		table.row();
		table.add(playGameLabel).expandX().padTop(10);
		
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
		
		if(Gdx.input.justTouched()){
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
