package entities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

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
	protected Pane configSkin;
	
	@FXML
	protected Pane configWall;
	
	@FXML
	protected Pane configStatus;
	
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
	protected Button readyStatus;
	
	@FXML
	protected Text sPower;
	
	@FXML
	protected Text sMultiCoins;
	
	@FXML
	protected Text sAuto;
	
	@FXML
	protected Text sVelo;
	
	@FXML
	protected Label wall1;
	
	@FXML
	protected Label wall2;
	
	@FXML
	protected Label wall3;
	
	@FXML
	protected Label wall4;
	
	@FXML
	protected Label wall5;
	
	@FXML
	protected Label wall6;
	
	@FXML
	protected Label wallS1;
	
	@FXML
	protected Label wallS2;
	
	@FXML
	protected Label wallS3;

	@FXML
	protected Label skin1;
	
	@FXML
	protected Label skin2;
	
	@FXML
	protected Label skin3;
	
	@FXML
	protected Label skin4;
	
	@FXML
	protected Label skin5;
	
	@FXML
	protected Label skin6;
	
	@FXML
	protected Label skinS1;
	
	@FXML
	protected Label skinS2;
	
	@FXML
	protected Label skinS3;
		
	@FXML
	protected ProgressBar pBar;
	
	@FXML
	protected Button btRoll;
	
	@FXML
	protected Button play;
	
	@FXML
	protected Button readyWall;
	
	@FXML
	protected Button readySkin;
	
	@FXML
	protected Rectangle resume;
	
	@FXML
	public void initialize() {
		loading.setVisible(true);
		startG.setVisible(false);
		match.setVisible(false);
		paneRoll.setVisible(false);
		paneUtils.setVisible(false);
		
		configPause.setVisible(false);
		configWall.setVisible(false);
		configSkin.setVisible(false);
		configStatus.setVisible(false);
		
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
