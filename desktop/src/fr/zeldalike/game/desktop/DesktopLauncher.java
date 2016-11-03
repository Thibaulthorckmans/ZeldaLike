package fr.zeldalike.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import fr.zeldalike.game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Zelda - Lonk's adventure !";
		//config.addIcon("Atom.png", FileType.Internal);
		config.width = 1200;
		config.height = 800;
		
		new LwjglApplication(new Main(), config);
	}
}
