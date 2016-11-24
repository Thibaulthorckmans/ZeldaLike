package fr.zeldalike.scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;

public class Hud implements Disposable{
	private Viewport viewport;
	private Stage stage;
	private Table healthBar;

	private int health = 3;
	
	public Hud(SpriteBatch sb) {
		this.viewport = new FitViewport(Constants.V_WIDTH * 3 , Constants.V_HEIGHT * 3, new OrthographicCamera());
		this.stage = new Stage(this.viewport, sb);
		
		this.healthBar = new Table();

		this.healthBar.top().left();
		this.healthBar.defaults().padTop(5f).padLeft(5f);
		this.healthBar.setFillParent(true);
		
		this.health = this.health > Constants.MAX_HEALTH ? Constants.MAX_HEALTH : this.health;
		for(int i = 0; i < this.health; i++) {
			Stack healthBarStack = new Stack();
			
			if(i == 10) {
				this.healthBar.row();
			}
			Image healthBarBG = new Image(new Texture("HUD/healthbg.png"));
			Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
			Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));
			
			healthBarStack.add(healthBarBG);
			healthBarStack.add(healthBarMG);
			healthBarStack.add(healthBarFG);
			
			this.healthBar.add(healthBarStack);
		}

		this.stage.addActor(this.healthBar);
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void cure(int valSoin) {
		int sizeHealthBar = this.healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;
		
		
		for(int i = 0; i < sizeHealthBar; i++) {
			stack = (Stack) this.healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;
			
			Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
			Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));
			
			if((valSoin >= 2) && (sizeTab == 1)) {
				stack.add(healthBarMG);
				stack.add(healthBarFG);
				valSoin -= 2;
			} else if((valSoin >= 2) && (sizeTab == 2)) {
				stack.add(healthBarFG);
				valSoin -= 1;
			} else if((valSoin == 1) && (sizeTab == 1)) {
				stack.add(healthBarMG);
				valSoin -= 1;
			} else if((valSoin == 1) && (sizeTab == 2)) {
				stack.add(healthBarFG);
				valSoin -= 1;
			}

			if(valSoin == 0) {
				break;
			}
		}
	}
	
	public void damage(int valDegat) {
		int sizeHealthBar = this.healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;
		
		this.health -= valDegat;
		
		for(int i = sizeHealthBar - 1; i >= 0; i--) {
			stack = (Stack) this.healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;
			
			//System.out.println(i + "  " + sizeTab);
			for(int j = sizeTab - 1; j > 0; j--) {
				stack.getChildren().get(j).remove();
				valDegat = valDegat - 1;
				
				if(valDegat == 0) {
					break;
				}
			}
			
			if(valDegat == 0) {
				break;
			}
		}
	}

	@Override
	public void dispose() {
		
	}
}