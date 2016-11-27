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
 * Hud est la classe permettant d'afficher la vie du joueur,
 * ainsi que les boites de dialogues et les boutons contenant les items ou l'action réalisable.
 *
 * @author Nicolas
 */
public class Hud implements Disposable {
	// **************************************************
	// Fields
	// **************************************************
	private Viewport viewport;
	private Stage stage;
	private Table healthBar, buttons; // Tableaux affichant les barres de vie et les boutons d'action et d'items
	private TextureAtlas itemsAtlas; // Attributs contenant la texture des items

	private int health = 6, maxHealth = 6; // Atributs de vie initialisé à 6

	private Label buttonA; // Label représentant les actions du bouton 'A'
	private TextureRegion buttonB, buttonX, buttonY; // Texture représentant les items définit sur les boutons 'B', 'X' et 'Y'

	// **************************************************
	// Constructors
	// **************************************************
	/**
	 * Constructeur de la Classe HUD
	 * */
	public Hud(SpriteBatch sb) {
		this.viewport = new FitViewport(Constants.V_WIDTH * 3, Constants.V_HEIGHT * 3, new OrthographicCamera());
		this.stage = new Stage(this.viewport, sb);
		this.itemsAtlas = new TextureAtlas("Sprites/items.pack");

		this.initHealthBar();
		this.initButton();

	}

	public Stage getStage() {
		return this.stage;
	}

	/**
	 * Returns current health's value.
	 *
	 * @return Current health's value.
	 */
	public int getHealth() {
		return this.health;
	}

	public String getButtonA() {
		return this.buttonA.getText().toString();
	}

	public TextureRegion getButtonB() {
		return this.buttonB;
	}

	public TextureRegion getButtonX() {
		return this.buttonX;
	}

	public TextureRegion getButtonY() {
		return this.buttonY;
	}

	public void setButtonA(String pItems) {
		this.buttonA.setText(pItems);
	}

	public void setButtonB(String pItems) {
		this.buttonB.setRegion(this.itemsAtlas.findRegion(pItems));
	}

	public void setButtonX(String pItems) {
		this.buttonX.setRegion(this.itemsAtlas.findRegion(pItems));
	}

	public void setButtonY(String pItems) {
		this.buttonY.setRegion(this.itemsAtlas.findRegion(pItems));
	}

	// **************************************************
	// Private methods
	// **************************************************
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
		Image imgButtonB = new Image(this.buttonB);

		this.buttonX = new TextureRegion(this.itemsAtlas.findRegion("NullItem"));
		Image imgButtonX = new Image(this.buttonX);

		this.buttonY = new TextureRegion(this.itemsAtlas.findRegion("NullItem"));
		Image imgButtonY = new Image(this.buttonY);

		Stack stackBtnA = new Stack(new Image(btnAtlas.findRegion("buttonA")), this.buttonA);
		Stack stackBtnB = new Stack(new Image(btnAtlas.findRegion("buttonB")), imgButtonB);
		Stack stackBtnX = new Stack(new Image(btnAtlas.findRegion("buttonX")), imgButtonX);
		Stack stackBtnY = new Stack(new Image(btnAtlas.findRegion("buttonY")), imgButtonY);

		this.buttons.add(stackBtnX).colspan(3);
		this.buttons.row();
		this.buttons.add(stackBtnY);
		this.buttons.add();
		this.buttons.add(stackBtnA);
		this.buttons.row();
		this.buttons.add(stackBtnB).colspan(3);

		this.stage.addActor(this.buttons);
	}

	// **************************************************
	// Public methods
	// **************************************************
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

	@Override
	public void dispose() {

	}
}