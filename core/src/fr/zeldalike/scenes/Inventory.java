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
 * Inventory est la classe permettant d'afficher l'inventaire du joueur,
 * ainsi que de définir les items de l'inventaire sur les boutons.
 *
 * @author Nicolas
 */
public class Inventory implements Disposable {
	// **************************************************
	// Fields
	// **************************************************
	private Viewport viewport;
	private Stage stage;
	private Table inventory, tabItems; // Tableaux affichant les items appartenant au joueur
	private TextureAtlas itemsAtlas; // Attributs contenant la texture des items

	private static final int LINE = 4, COLUMN = 5;

	private String[] items;
	private String nameItem;
	private int posCursorX, posCursorY;
	private Image cursor;

	// **************************************************
	// Constructors
	// **************************************************
	/**
	 * Constructeur par défaut de la Classe Inventory
	 * */
	public Inventory() {
		super();
	}

	/**
	 * Constructeur par de la Classe Inventory
	 * */
	public Inventory(SpriteBatch sb) {
		this.viewport = new FitViewport(Constants.V_WIDTH * 2, Constants.V_HEIGHT * 2, new OrthographicCamera());
		this.stage = new Stage(this.viewport, sb);
		this.itemsAtlas = new TextureAtlas("Sprites/items.pack");

		this.cursor = new Image(new Texture("HUD/cursor.png"));

		this.items = new String[] {
				"Flute", "NullItem", "NullItem", "NullItem", "NullItem",
				"NullItem", "NullItem", "NullItem", "NullItem", "NullItem",
				"NullItem", "NullItem", "NullItem", "NullItem", "NullItem",
				"RedPotion", "RedPotion", "RedPotion", "Bottle", "Bottle"
		};

		this.inventory = new Table();

		this.inventory.top().center();
		this.inventory.setFillParent(true);
		this.inventory.setVisible(false);

		Stack stackInventory = new Stack();

		stackInventory.add(new Image(new Texture("HUD/InventoryBackground.png")));

		this.initTabItems();
		stackInventory.add(this.tabItems);

		this.inventory.add(stackInventory);

		this.stage.addActor(this.inventory);
	}

	// **************************************************
	// Getters
	// **************************************************
	public Stage getStage() {
		return this.stage;
	}

	public boolean getInvetoryIsVisible() {
		return this.inventory.isVisible();
	}

	public String getNameItem() {
		return this.nameItem;
	}

	// **************************************************
	// Setters
	// **************************************************
	public void setInventoryVisibility() {
		if(this.inventory.isVisible()) {
			this.inventory.setVisible(false);
		} else {
			this.inventory.setVisible(true);
		}
	}


	// **************************************************
	// Private methods
	// **************************************************
	private void initTabItems() {
		this.tabItems = new Table();
		//this.tabItems.setDebug(true);

		//		this.tabItems.top().center();
		this.tabItems.defaults().size(50f, 50f).pad(5f);

		this.tabItems.setFillParent(true);

		Label title = new Label("Inventaire", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		title.setFontScale(2f);
		title.setAlignment(Align.center);
		this.tabItems.add(title).colspan(COLUMN).width(200f).pad(0);
		this.tabItems.row();

		int cpt = 0, index = 0;
		boolean cursorPlaced = false;
		for(String item : this.items) {
			if(cpt == COLUMN) {
				this.tabItems.row();
				cpt=0;
			}

			Stack stack = new Stack();
			Image imgItem = new Image(new TextureRegion(this.itemsAtlas.findRegion(item)));
			imgItem.setName(index + ":" + item);

			stack.add(imgItem);

			if(!cursorPlaced) {
				stack.add(this.cursor);
				cursorPlaced = true;
				this.posCursorX = 1;
				this.posCursorY = 1;
				this.nameItem = imgItem.getName();
			}

			this.tabItems.add(stack);
			cpt++;
			index++;
		}
	}

	// **************************************************
	// Public methods
	// **************************************************
	public void moveCursor(int moveX, int moveY) {
		int line;

		if(moveX == 1) {
			this.posCursorX = (this.posCursorX + moveX) > COLUMN ? 1 : (this.posCursorX + moveX);
		} else if(moveX == -1) {
			this.posCursorX = (this.posCursorX + moveX) < 1 ? COLUMN : (this.posCursorX + moveX);
		}

		if(moveY == 1) {
			this.posCursorY = (this.posCursorY + moveY) > LINE ? 1 : (this.posCursorY + moveY);
		} else if(moveY == -1) {
			this.posCursorY = (this.posCursorY + moveY) < 1 ? LINE : (this.posCursorY + moveY);
		}

		switch(this.posCursorY) {
		case 1:
			line = 0;
			break;
		case 2:
			line = 5;
			break;
		case 3:
			line = 10;
			break;
		case 4:
			line = 15;
			break;
		default:
			line = 0;
			break;
		}

		Stack stack = (Stack) this.tabItems.getCells().get(this.posCursorX + line).getActor();
		stack.add(this.cursor);

		this.nameItem = stack.getChildren().get(0).getName();
	}

	@Override
	public void dispose() {

	}
}
