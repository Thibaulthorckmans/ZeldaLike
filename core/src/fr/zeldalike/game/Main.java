package fr.zeldalike.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.zeldalike.screens.Menu;
import fr.zeldalike.tools.MusicLoader;

public class Main extends Game {
	public static final String TITLE = "ZeldaLike";
	public SpriteBatch batch;
	public MusicLoader music;
	
	@Override
	public void create () {
		this.batch = new SpriteBatch();
		this.music = new MusicLoader();
		
		this.music.loadMusic();
		
		setScreen(new Menu(this));
	}

	@Override
	public void render () {
		super.render();
		this.music.update();
	}
}
