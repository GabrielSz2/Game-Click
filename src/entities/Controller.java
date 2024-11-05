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
	protected Pane powers;
	
	@FXML
	protected Pane paneRoll;
	
	@FXML
	protected Pane end;
	
	@FXML
	protected Button play;
	
	@FXML
	protected ImageView farm;

	@FXML
	protected ImageView x2Coin;
	
	@FXML
	protected ImageView x2Power;
	
	@FXML
	protected ImageView roletaMaluca;
	
	@FXML
	protected ImageView roleta;
	
	@FXML
	protected ProgressBar pBar;
	
	@FXML
	protected Button btRoll;
	
	@FXML
	public void initialize() {
		loading.setVisible(true);
		startG.setVisible(false);
		match.setVisible(false);
		powers.setVisible(false);
		paneRoll.setVisible(false);
		end.setVisible(false);
		
		game.runGame();
		
	}
	
	public Controller() {
		game = new Game(this);
	}
	
	
}
