package entities;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;

public class Controller {

	@FXML
	private Pane loading;
	
	@FXML
	private Pane startG;
	
	@FXML
	private Button play;
	
	@FXML
	private ProgressIndicator indi;
	
	@FXML
	public void initialize() {
		startGame();
	}
	
	private void startGame() {
		
		if(indi.getProgress() == 0) {
			startG.setVisible(false);
		}
		
		
		
		play.setOnMouseClicked(e -> {
			

		});
		
	}
	
}
