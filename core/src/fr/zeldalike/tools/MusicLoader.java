package fr.zeldalike.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

public class MusicLoader {
	public static AssetManager manager;
	
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
