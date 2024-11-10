package entities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Controller {
	
	private Game game;
	
	@FXML
	protected Pane pane;
	
	@FXML
	protected Pane loading;
	
	@FXML
	protected Pane startG;
	
	@FXML
	protected Pane match;
	
	@FXML
	protected Pane paneUtils;
	
	@FXML
	protected Pane configPause;
	
	@FXML
	protected Pane configWall;
	
	@FXML
	protected Pane powers;
	
	@FXML
	protected Pane paneRoll;
	
	@FXML
	protected Pane end;
	
	@FXML
	protected ImageView iconConfig;
	
	@FXML
	protected ImageView iconSkin;
	
	@FXML
	protected ImageView iconWall;
	
	@FXML
	protected ImageView wallpaper;
	
	@FXML
	protected ImageView blackScreen;
	
	@FXML
	protected ImageView farm;

	@FXML
	protected ImageView x2Coin;
	
	@FXML
	protected ImageView x2Power;
	
	@FXML
	protected ImageView crazyRoulette;
	
	@FXML
	protected ImageView roulette;
	
	@FXML
	protected ImageView autoClicker;
	
	@FXML
	protected ImageView wall1;
	
	@FXML
	protected ImageView wall2;
	
	@FXML
	protected ImageView wall3;
	
	@FXML
	protected ImageView wall4;
	
	@FXML
	protected ImageView wall5;
	
	@FXML
	protected ImageView wall6;
	
	@FXML
	protected ImageView wallS1;
	
	@FXML
	protected ImageView wallS2;
	
	@FXML
	protected ProgressBar pBar;
	
	@FXML
	protected Button btRoll;
	
	@FXML
	protected Button play;
	
	@FXML
	protected Button resume;
	
	@FXML
	public void initialize() {
		loading.setVisible(true);
		startG.setVisible(false);
		match.setVisible(false);
		paneRoll.setVisible(false);
		paneUtils.setVisible(false);
		
		configPause.setVisible(false);
		configWall.setVisible(false);
		
		powers.setVisible(false);
		end.setVisible(false);
		autoClicker.setVisible(false);
		blackScreen.setVisible(false);
			
		game.runGame();
		
	}
	
	public Controller() {
		game = new Game(this);
	}
	
	
}
