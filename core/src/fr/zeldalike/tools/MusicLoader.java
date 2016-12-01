package fr.zeldalike.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class MusicLoader {
	// **************************************************
	// Fields
	// **************************************************
	public static AssetManager manager;
	
	// **************************************************
	// Public Methods
	// **************************************************
	/**
	 * Load musics and sounds.
	 */
	public void loadMusic() {
		manager = new AssetManager();
		
		manager.load("Audio/Music/MainTheme.ogg", Music.class);
		manager.load("Audio/Music/ALTTP_Kakariko_Village.ogg", Music.class);
		
//		manager.load("Audio/Sound/Attack.wav", Sound.class);
		
		manager.finishLoading();
	}
	
	public void update() {
		manager.update();
	}
}
