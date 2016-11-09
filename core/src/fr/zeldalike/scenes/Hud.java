package fr.zeldalike.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;

public class Hud implements Disposable{
	public Stage stage;
	private Viewport viewport;

	MapLayer calque;
	TextureAtlas atlasHeart;
	Sprite heart;

	Label heartLabel;

	public Hud(SpriteBatch sb) {		
		viewport = new FitViewport(Constants.V_WIDTH, Constants.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);

		atlasHeart = new TextureAtlas("Sprites/items.pack");






		Table table = new Table();
		table.top();
		table.setFillParent(true);

		heartLabel = new Label("Life <3 <3 <3", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		table.defaults().align(Align.left);
		table.add(heartLabel).expandX().padLeft(5);

		stage.addActor(table);
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
}



























