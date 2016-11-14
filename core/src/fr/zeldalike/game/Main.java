package fr.zeldalike.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import fr.zeldalike.screens.PlayScreen;
import fr.zeldalike.tools.MusicLoader;

public class Main extends Game {
	public SpriteBatch batch;
	public MusicLoader music;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		music = new MusicLoader();
		
		music.loadMusic();
		
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
		music.update();
	}
}
