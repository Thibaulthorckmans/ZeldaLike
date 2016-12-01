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
	private TextureRegion buttonB, buttonX, buttonY; // Texture representing the items defined on the 'B', 'X' and 'Y' buttons
	private Image imgButtonB, imgButtonX, imgButtonY;

	// **************************************************
	// Constructors
	// **************************************************
	public Hud(SpriteBatch sb) {
		this.viewport = new FitViewport(Constants.V_WIDTH * 3, Constants.V_HEIGHT * 3, new OrthographicCamera());
		this.stage = new Stage(this.viewport, sb);
		this.itemsAtlas = new TextureAtlas("Sprites/items.pack");

		this.initHealthBar();
		this.initButton();
		this.initDialogBox();
	}

	// **************************************************
	// Getters
	// **************************************************
	public Stage getStage() {
		return this.stage;
	}

	public int getHealth() {
		return this.health;
	}

	public String getButtonA() {
		return this.buttonA.getText().toString();
	}

	public String getNameImgButtonB() {
		return this.imgButtonB.getName();
	}

	public String getNameImgButtonX() {
		return this.imgButtonX.getName();
	}

	public String getNameImgButtonY() {
		return this.imgButtonY.getName();
	}

	public boolean getDialogBoxVisibility() {
		return this.dialogBox.isVisible();
	}

	// **************************************************
	// Setters
	// **************************************************
	public void setButtonA(String pItems) {
		this.buttonA.setText(pItems);
	}

	public void setImgButtonB(String pItems) {
		this.buttonB.setRegion(this.itemsAtlas.findRegion(pItems));
	}

	public void setImgButtonX(String pItems) {
		if(this.imgButtonX.getName() != pItems) {
			String newItem = pItems.split(":")[1];

			if(this.imgButtonY.getName() == pItems) {
				this.buttonY.setRegion(this.itemsAtlas.findRegion(this.imgButtonX.getName().split(":")[1]));
				this.imgButtonY.setName(this.imgButtonX.getName());
			}

			this.buttonX.setRegion(this.itemsAtlas.findRegion(newItem));
			this.imgButtonX.setName(pItems);
		}
	}

	public void setImgButtonY(String pItems) {
		if(this.imgButtonY.getName() != pItems) {
			String newItem = pItems.split(":")[1];

			if(this.imgButtonX.getName() == pItems) {
				this.buttonX.setRegion(this.itemsAtlas.findRegion(this.imgButtonY.getName().split(":")[1]));
				this.imgButtonX.setName(this.imgButtonY.getName());
			}

			this.buttonY.setRegion(this.itemsAtlas.findRegion(newItem));
			this.imgButtonY.setName(pItems);
		}
	}

	public void setDialogBoxVisibility() {
		if(this.dialogBox.isVisible()) {
			this.dialogBox.setVisible(false);
			System.out.println("Masquer");
		} else {
			this.dialogBox.setVisible(true);
			System.out.println("Afficher");
		}
	}

	// **************************************************
	// Private methods
	// **************************************************
	/**
	 * Initialize the health bar.
	 */
	private void initHealthBar() {
		this.healthBar = new Table();

		this.healthBar.top().left();
		this.healthBar.defaults().padTop(5f).padLeft(5f);
		this.healthBar.setFillParent(true);

		this.maxHealth = this.maxHealth > Constants.MAX_HEALTH ? Constants.MAX_HEALTH : this.maxHealth;
		this.health = this.health > this.maxHealth ? this.maxHealth : this.health;

		for (int i = 0; i < (this.maxHealth / 2); i++) {
			Stack healthBarStack = new Stack();

			if ((i % 10) == 0) {
				this.healthBar.row();
			}
			Image healthBarBG = new Image(new Texture("HUD/healthbg.png"));
			Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
			Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));

			healthBarStack.add(healthBarBG);

			if (i < ((this.health / 2) + (this.health % 2))) {
				healthBarStack.add(healthBarMG);
			}

			if (i < (this.health / 2)) {
				healthBarStack.add(healthBarFG);
			}

			this.healthBar.add(healthBarStack);
		}

		this.stage.addActor(this.healthBar);
	}

	/**
	 * Initialize the buttons.
	 */
	private void initButton() {
		TextureAtlas btnAtlas = new TextureAtlas("HUD/buttons.pack");
		this.buttons = new Table();

		this.buttons.top().right();
		this.buttons.padTop(20f).padRight(30f);
		this.buttons.defaults().width(50f).height(50f).maxWidth(50f).maxHeight(50f).center();
		this.buttons.setFillParent(true);

		this.buttonA = new Label("", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		this.buttonA.setAlignment(Align.center);
		this.buttonA.setFontScale(1.5f);

		this.buttonB = new TextureRegion(this.itemsAtlas.findRegion("Sword"));
		this.imgButtonB = new Image(this.buttonB);

		this.buttonX = new TextureRegion(this.itemsAtlas.findRegion("Bottle"));
		this.imgButtonX = new Image(this.buttonX);

		this.buttonY = new TextureRegion(this.itemsAtlas.findRegion("Flute"));
		this.imgButtonY = new Image(this.buttonY);

		Stack stackBtnA = new Stack(new Image(btnAtlas.findRegion("buttonA")), this.buttonA);
		Stack stackBtnB = new Stack(new Image(btnAtlas.findRegion("buttonB")), this.imgButtonB);
		Stack stackBtnX = new Stack(new Image(btnAtlas.findRegion("buttonX")), this.imgButtonX);
		Stack stackBtnY = new Stack(new Image(btnAtlas.findRegion("buttonY")), this.imgButtonY);

		this.buttons.add(stackBtnX).colspan(3);
		this.buttons.row();
		this.buttons.add(stackBtnY);
		this.buttons.add();
		this.buttons.add(stackBtnA);
		this.buttons.row();
		this.buttons.add(stackBtnB).colspan(3);

		this.stage.addActor(this.buttons);
	}

	/**
	 * Initialize the dialogue box.
	 */
	private void initDialogBox() {
		this.dialogBox = new Table();

		this.dialogBox.bottom();
		this.dialogBox.padBottom(15f);
		this.dialogBox.defaults().width(900f).height(240f);
		this.dialogBox.setFillParent(true);
		this.dialogBox.setVisible(false);

		this.writeDialog("Narrateur :", "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...");

		this.stage.addActor(this.dialogBox);
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
		int sizeHealthBar = this.healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;

		for (int i = 0; i < sizeHealthBar; i++) {
			stack = (Stack) this.healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;

			if (sizeTab < 3) {
				Image healthBarMG = new Image(new Texture("HUD/healthmg.png"));
				Image healthBarFG = new Image(new Texture("HUD/healthfg.png"));

				if ((valSoin >= 2) && (sizeTab == 1)) {
					stack.add(healthBarMG);
					stack.add(healthBarFG);
					valSoin -= 2;
					this.health += 2;
				} else if ((valSoin >= 2) && (sizeTab == 2)) {
					stack.add(healthBarFG);
					valSoin -= 1;
					this.health += 1;
				} else if ((valSoin == 1) && (sizeTab == 1)) {
					stack.add(healthBarMG);
					valSoin -= 1;
					this.health += 1;
				} else if ((valSoin == 1) && (sizeTab == 2)) {
					stack.add(healthBarFG);
					valSoin -= 1;
					this.health += 1;
				}
			}

			if ((valSoin == 0) || (this.health == (sizeTab * 2))) {
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
		int sizeHealthBar = this.healthBar.getCells().size;
		int sizeTab = 0;
		Stack stack;

		for (int i = sizeHealthBar - 1; i >= 0; i--) {
			stack = (Stack) this.healthBar.getCells().get(i).getActor();
			sizeTab = stack.getChildren().size;

			if (sizeTab > 1) {
				for (int j = sizeTab - 1; j > 0; j--) {
					stack.getChildren().get(j).remove();
					valDegat = valDegat - 1;
					this.health -= 1;

					if ((valDegat == 0) || (this.health == 0)) {
						break;
					}
				}
			}

			if ((valDegat == 0) || (this.health == 0)) {
				break;
			}
		}
	}

	/**
	 * Write into the dialogue box.
	 *
	 * @param pTitle Title of the dialog box
	 * @param pMessage Message of the dialog box
	 * */
	public void writeDialog(String pTitle, String pMessage) {
		this.dialogBox.clear();

		Stack stackDialog = new Stack();
		Image dialogBG = new Image(new Texture("HUD/dialog_box.png"));

		Table dialog = new Table();
		dialog.top().padTop(20f);
		dialog.defaults().width(860f).maxWidth(860f);
		dialog.setFillParent(true);
		//dialog.setDebug(true);

		Label lblTitre = new Label("Narrateur :", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		lblTitre.setFontScale(2f);
		Label lblMessage = new Label("Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		lblMessage.setFontScale(1.5f);
		lblMessage.setWrap(true);

		dialog.add(lblTitre).top();
		dialog.row();
		dialog.add(lblMessage).top().padLeft(25f);

		stackDialog.add(dialogBG);
		stackDialog.add(dialog);

		this.dialogBox.add(stackDialog);
	}

	@Override
	public void dispose() {

	}
}