package entities;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Game {

	private Controller ct;

	private Label clock = new Label();
	
	private int timeRest = 6;
	
	public Game(Controller ct) {
		this.ct = ct;
	}

	public void runGame() {
		startGame();
		loading();

	}

	private void loading() {

		Timeline ti = new Timeline();
		KeyFrame turnOffLo = new KeyFrame(Duration.seconds(3), event -> {
			ct.loading.setVisible(false);
			System.out.println("sistema desligado");
		});

		KeyFrame turnOnSt = new KeyFrame(Duration.seconds(3), event -> {
			ct.startG.setVisible(true);
			System.out.println("Sistema ligado");
		});

		ti.getKeyFrames().addAll(turnOffLo, turnOnSt);
		ti.play();
	}

	private void startGame() {

		
		
		clock.setStyle("-fx-font-size: 24px;");
		clock.setLayoutX(0);
		clock.setLayoutY(0);
		
		ct.play.setOnMouseClicked(e -> {
			ct.startG.setVisible(false);
			ct.match.setVisible(true);
			
			Timeline go = new Timeline( new KeyFrame(Duration.seconds(1), ev -> {
				if(timeRest > 0) {
					clock.setText("" + timeRest);
					timeRest--;
					System.out.println(timeRest);
				}
				else {
					clock.setText("GO!");
				}
			})
			);
			
			go.setCycleCount(Timeline.INDEFINITE);
			go.play();
			
			
			
		});

	}

}