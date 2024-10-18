package entities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;

public class Controller {

	@FXML
	private Pane loading;
	
	@FXML
	private Pane startG;
	
	@FXML
	private Button play;
	
	@FXML
	private ProgressBar bar;
	
	
	@FXML
	public void initialize() {
		runGame();
	}
	
	private void runGame() {
		startGame();
		loading();
		
	}
	
	private void startGame() {
	
		
		
		play.setOnMouseClicked(e -> {
			

		});
		
	}
	
	private void loading() {
		
		
		
	}
	
}
