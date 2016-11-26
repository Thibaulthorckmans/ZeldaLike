package fr.zeldalike.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;

public class Hud implements Disposable{
	private Viewport viewport;
	private Stage stage;
	private Table healthBar, Buttons;

	private int health = 6, maxHealth = 6;
	private String action;
	
	private Label buttonA, buttonB;
	
	public Hud(SpriteBatch sb) {
		this.viewport = new FitViewport(Constants.V_WIDTH * 3 , Constants.V_HEIGHT * 3, new OrthographicCamera());
		this.stage = new Stage(this.viewport, sb);
		
		this.healthBar = new Table();
		this.Buttons = new Table();

		this.initHealthBar();
		this.initButton();

		this.stage.addActor(this.healthBar);
		this.stage.addActor(this.Buttons);
	}
	
	public Stage getStage() {
		return this.stage;
	}
	
	public int getHealth() {
		return this.health;
	}
	
	public void setButtonA(String pText) {
		this.buttonA.setText(pText);
	}
	
	private void initHealthBar() {
		this.healthBar.top().left();
		this.healthBar.defaults().padTop(5f).padLeft(5f);
		this.healthBar.setFillParent(true);

		this.maxHealth = this.maxHealth > Constants.MAX_HEALTH ? Constants.MAX_HEALTH : this.maxHealth;
		this.health = this.health > this.maxHealth ? this.maxHealth : this.health;
		
		for(int i = 0; i < (this.maxHealth / 2); i++) {
			Stack healthBarStack = new Stack();
			
			if(i == 10) {
				this.healthBar.row();
			}
			Image healthBarBG = new Image(new Texture("HUD/healthbg.png"));
			Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
			Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));
			
			healthBarStack.add(healthBarBG);
			
			if(i < ((this.health / 2) + (this.health % 2))) {
				healthBarStack.add(healthBarMG);
			}
			
			if(i < (this.health / 2)) {
				healthBarStack.add(healthBarFG);
			}
			
			this.healthBar.add(healthBarStack);
		}
	}
	
	private void initButton() {
		this.Buttons.top().right();
		this.Buttons.padTop(20f).padRight(30f);
		this.Buttons.defaults().width(50f).height(50f);
		this.Buttons.setFillParent(true);
		
		this.Buttons.setDebug(true);
		
		this.action = "Epée";
		this.buttonB = new Label(this.action, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		this.action = "Action";
		this.buttonA = new Label(this.action, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		
		this.Buttons.add(this.buttonB);
		this.Buttons.add();
		this.Buttons.row();
		//this.Buttons.add();
		this.Buttons.add(this.buttonA).colspan(2).right();
	}
	
	public void cure(int valSoin) {
		int sizeHealthBar = this.healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;
		
		for(int i = 0; i < sizeHealthBar; i++) {
			stack = (Stack) this.healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;
			
			if(sizeTab < 3) {
				Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
				Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));
				
				if((valSoin >= 2) && (sizeTab == 1)) {
					stack.add(healthBarMG);
					stack.add(healthBarFG);
					valSoin -= 2;
					this.health += 2;
				} else if((valSoin >= 2) && (sizeTab == 2)) {
					stack.add(healthBarFG);
					valSoin -= 1;
					this.health += 1;
				} else if((valSoin == 1) && (sizeTab == 1)) {
					stack.add(healthBarMG);
					valSoin -= 1;
					this.health += 1;
				} else if((valSoin == 1) && (sizeTab == 2)) {
					stack.add(healthBarFG);
					valSoin -= 1;
					this.health += 1;
				}
			}

			if((valSoin == 0) || (this.health == (sizeTab * 2))) {
				break;
			}
		}
	}
	
	public void damage(int valDegat) {
		int sizeHealthBar = this.healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;
		
		for(int i = sizeHealthBar - 1; i >= 0; i--) {
			stack = (Stack) this.healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;

			if(sizeTab > 1) {
				for(int j = sizeTab - 1; j > 0; j--) {
						stack.getChildren().get(j).remove();
						valDegat = valDegat - 1;
						this.health -= 1;
					
					if((valDegat == 0) || (this.health == 0)) {
						break;
					}
				}
			}

			if((valDegat == 0) || (this.health == 0)) {
				break;
			}
		}
	}

	@Override
	public void dispose() {
		
	}
}