package fr.zeldalike.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.zeldalike.assets.Constants;

/**
 * Hud is the class that allow us to display the player's life
 * as well as the dialogues boxes and the buttons containing the items or the possible actions.
 */
public class Hud implements Disposable {
	// **************************************************
	// Fields
	// **************************************************
	private Viewport viewport;
	private Stage stage;
	private Table healthBar, dialogBox, buttons; // Display health bar, dialogues and action/item's buttons 
	private TextureAtlas itemsAtlas; // Contains the item's textures

	private int health = 6, maxHealth = 6; // Health's attributes, initialize at 6

	private Label buttonA; // Label that describe the actions for the 'A' button
	private TextureRegion buttonB, buttonX, buttonY; // Label that describe the actions for the 'B', 'X' and 'Y' buttons

	// **************************************************
	// Constructors
	// *************************************************
	public Hud(SpriteBatch sb) {
		viewport = new FitViewport(Constants.V_WIDTH * 3, Constants.V_HEIGHT * 3, new OrthographicCamera());
		stage = new Stage(viewport, sb);
		itemsAtlas = new TextureAtlas("Sprites/items.pack");

		initHealthBar();
		initButton();
		initDialogBox();
	}

	// **************************************************
	// Getters
	// **************************************************
	public Stage getStage() {
		return stage;
	}

	public int getHealth() {
		return health;
	}

	public String getButtonA() {
		return buttonA.getText().toString();
	}

	public TextureRegion getButtonB() {
		return buttonB;
	}

	public TextureRegion getButtonX() {
		return buttonX;
	}

	public TextureRegion getButtonY() {
		return buttonY;
	}

	public boolean getDialogBoxVisibility() {
		return dialogBox.isVisible();
	}

	// **************************************************
	// Setters
	// **************************************************
	public void setButtonA(String pItems) {
		buttonA.setText(pItems);
	}

	public void setButtonB(String pItems) {
		buttonB.setRegion(itemsAtlas.findRegion(pItems));
	}

	public void setButtonX(String pItems) {
		buttonX.setRegion(itemsAtlas.findRegion(pItems));
	}

	public void setButtonY(String pItems) {
		buttonY.setRegion(itemsAtlas.findRegion(pItems));
	}

	public void setDialogBoxVisibility() {
		if(dialogBox.isVisible()) {
			dialogBox.setVisible(false);
		} else {
			dialogBox.setVisible(true);
		}
	}

	// **************************************************
	// Private methods
	// **************************************************
	/** 
	 * Initialize the health bar.
	 */
	private void initHealthBar() {
		healthBar = new Table();

		healthBar.top().left();
		healthBar.defaults().padTop(5f).padLeft(5f);
		healthBar.setFillParent(true);

		maxHealth = maxHealth > Constants.MAX_HEALTH ? Constants.MAX_HEALTH : maxHealth;
		health = health > maxHealth ? maxHealth : health;

		for (int i = 0; i < (maxHealth / 2); i++) {
			Stack healthBarStack = new Stack();

			if ((i % 10) == 0) {
				healthBar.row();
			}
			Image healthBarBG = new Image(new Texture("HUD/healthbg.png"));
			Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
			Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));

			healthBarStack.add(healthBarBG);

			if (i < ((health / 2) + (health % 2))) {
				healthBarStack.add(healthBarMG);
			}

			if (i < (health / 2)) {
				healthBarStack.add(healthBarFG);
			}

			healthBar.add(healthBarStack);
		}

		stage.addActor(healthBar);
	}

	/** 
	 * Initialize the buttons.
	 */
	private void initButton() {
		TextureAtlas btnAtlas = new TextureAtlas("HUD/buttons.pack");
		buttons = new Table();

		buttons.top().right();
		buttons.padTop(20f).padRight(30f);
		buttons.defaults().width(50f).height(50f).maxWidth(50f).maxHeight(50f).center();
		buttons.setFillParent(true);

		//this.buttons.setDebug(true);

		buttonA = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		buttonA.setAlignment(Align.center);
		buttonA.setFontScale(1.5f);

		buttonB = new TextureRegion(itemsAtlas.findRegion("Sword"));
		Image imgButtonB = new Image(buttonB);

		buttonX = new TextureRegion(itemsAtlas.findRegion("NullItem"));
		Image imgButtonX = new Image(buttonX);

		buttonY = new TextureRegion(itemsAtlas.findRegion("NullItem"));
		Image imgButtonY = new Image(buttonY);

		Stack stackBtnA = new Stack(new Image(btnAtlas.findRegion("buttonA")), buttonA);
		Stack stackBtnB = new Stack(new Image(btnAtlas.findRegion("buttonB")), imgButtonB);
		Stack stackBtnX = new Stack(new Image(btnAtlas.findRegion("buttonX")), imgButtonX);
		Stack stackBtnY = new Stack(new Image(btnAtlas.findRegion("buttonY")), imgButtonY);

		buttons.add(stackBtnX).colspan(3);
		buttons.row();
		buttons.add(stackBtnY);
		buttons.add();
		buttons.add(stackBtnA);
		buttons.row();
		buttons.add(stackBtnB).colspan(3);

		stage.addActor(buttons);
	}

	/** 
	 * Initialize the dialogue box.
	 */
	private void initDialogBox() {
		dialogBox = new Table();

		dialogBox.bottom();
		dialogBox.padBottom(15f);
		dialogBox.defaults().width(900f).height(240f);
		dialogBox.setFillParent(true);
		dialogBox.setVisible(false);

		//this.dialogBox.setDebug(true);

		Stack stackDialog = new Stack();
		Image dialogBG = new Image(new Texture("HUD/dialog_box.png"));

		Table dialog = new Table();
		dialog.top().padTop(20f);
		dialog.defaults().width(860f).maxWidth(860f);
		dialog.setFillParent(true);
		//dialog.setDebug(true);

		Label titre = new Label("Narrateur :", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		titre.setFontScale(2f);
		Label message = new Label("Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		message.setFontScale(1.5f);
		message.setWrap(true);

		dialog.add(titre).top();
		dialog.row();
		dialog.add(message).top().padLeft(25f);

		stackDialog.add(dialogBG);
		stackDialog.add(dialog);

		dialogBox.add(stackDialog);

		stage.addActor(dialogBox);
	}

	// **************************************************
	// Public methods
	// **************************************************
	/** 
	 * Restore the player's health.
	 * 
	 * @param valSoin The amount of life restored.
	 */
	public void cure(int valSoin) {
		int sizeHealthBar = healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;

		for (int i = 0; i < sizeHealthBar; i++) {
			stack = (Stack) healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;

			if (sizeTab < 3) {
				Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
				Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));

				if ((valSoin >= 2) && (sizeTab == 1)) {
					stack.add(healthBarMG);
					stack.add(healthBarFG);
					valSoin -= 2;
					health += 2;
				} else if ((valSoin >= 2) && (sizeTab == 2)) {
					stack.add(healthBarFG);
					valSoin -= 1;
					health += 1;
				} else if ((valSoin == 1) && (sizeTab == 1)) {
					stack.add(healthBarMG);
					valSoin -= 1;
					health += 1;
				} else if ((valSoin == 1) && (sizeTab == 2)) {
					stack.add(healthBarFG);
					valSoin -= 1;
					health += 1;
				}
			}

			if ((valSoin == 0) || (health == (sizeTab * 2))) {
				break;
			}
		}
	}
	
	/** 
	 * Decrease the player's health.
	 * 
	 * @param valDegat The amount of life lost.
	 */
	public void damage(int valDegat) {
		int sizeHealthBar = healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;

		for (int i = sizeHealthBar - 1; i >= 0; i--) {
			stack = (Stack) healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;

			if (sizeTab > 1) {
				for (int j = sizeTab - 1; j > 0; j--) {
					stack.getChildren().get(j).remove();
					valDegat = valDegat - 1;
					health -= 1;

					if ((valDegat == 0) || (health == 0)) {
						break;
					}
				}
			}

			if ((valDegat == 0) || (health == 0)) {
				break;
			}
		}
	}

	@Override
	public void dispose() {

	}
}