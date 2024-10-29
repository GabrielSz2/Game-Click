package entities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class Controller {
	
	private Game game;
	
	@FXML
	protected Pane loading;
	
	@FXML
	protected Pane startG;
	
	@FXML
	protected Pane match;
	
	@FXML
	protected Button play;
	
	@FXML
	protected Circle farm;
	
	@FXML
	protected ProgressBar pBar;
	
	@FXML
	public void initialize() {
		loading.setVisible(true);
		startG.setVisible(false);
		match.setVisible(false);
		
		game.runGame();
		
	}
	
	public Controller() {
		game = new Game(this);
	}
	
	
}
