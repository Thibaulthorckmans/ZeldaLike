package fr.zeldalike.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;

public class Hud implements Disposable{
	private Viewport viewport;
	private Stage stage;
	private Table healthBar;

	private int healthMax = 3;

	public Hud(SpriteBatch sb) {
		viewport = new FitViewport(Constants.V_WIDTH * 3 , Constants.V_HEIGHT * 3, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		
		healthBar = new Table();

		healthBar.top().left();
		healthBar.defaults().padTop(5f).padLeft(5f);
		healthBar.setFillParent(true);
		
		for(int i = 0; i < healthMax; i++) {
			if(i == 10) {
				healthBar.row();
			}
			Image img = new Image(new Texture("HUD/healthfg.png"));
			
			healthBar.add(img);
		}

		stage.addActor(healthBar);
	}
	
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void dispose() {
		
	}
}