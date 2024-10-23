package entities;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Game {

	private Controller ct;

	private Label clock = new Label();
	private Label score = new Label();
	private Label meta = new Label();
	private Label plus = new Label();
	
	int i=0;
	
	private int timeRest = 1;
	
	public Game(Controller ct) {
		this.ct = ct;
	}

	public void runGame() {
		startGame();
		loading();
		match();
	}

	private void variableStarter() {
		
		clock.setStyle("-fx-font-size: 36px;");
		clock.setLayoutX(286);
		clock.setLayoutY(192);
		clock.setTextFill(Color.WHITE);
		
		score.setStyle("-fx-font-size: 18px;");
		score.setLayoutX(273);
		score.setLayoutY(10);
		score.setTextFill(Color.WHITE);
		
		meta.setStyle("-fx-font-size: 18px;");
		meta.setLayoutX(290);
		meta.setLayoutY(10);
		meta.setTextFill(Color.WHITE);
		
		plus.setStyle("-fx-font-size: 24px;");
		plus.setLayoutX(120);
		plus.setLayoutY(216);
		plus.setTextFill(Color.WHITE);
		
		ct.farm.setVisible(false);
		
		
	}
	
	private void loading() {

		variableStarter();
		
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
		
		
		
		ct.play.setOnMouseClicked(e -> {
			
			ct.startG.setVisible(false);
			ct.match.setVisible(true);
			
			Timeline go = new Timeline( new KeyFrame(Duration.seconds(1), ev -> {
				if(timeRest > 0) {
					clock.setText("" + timeRest);
					timeRest--;
				}
				else if(timeRest == 0){
					System.out.println("go");
					clock.setText("GO!");
					clock.setLayoutX(275);
					timeRest--;
				}
				else {
					clock.setVisible(false);
					ct.farm.setVisible(true);
					meta.setVisible(true);
					score.setVisible(true);
				}	
			}));
			
			
			
			go.setCycleCount(Timeline.INDEFINITE);
			go.play();
			ct.match.getChildren().add(clock);
			
		});
	}

	private void match() {
		
		score.setText("0/");
		meta.setText("10");
		
		ct.match.getChildren().addAll(meta, score, plus);
		
		
		ct.farm.setOnMouseClicked(e->{
			
			i++;
			score.setText(i + "/");
			plus.setText("+1");
			generatePosition(ct.match, plus);
			System.out.println(plus.getLayoutX() + " \\" + plus.getLayoutY());
			
			if(i == 10) {
				score.setLayoutX(265);
				ct.match.setDisable(true);
				ct.match.setOpacity(0.75);
			}
			
		});
	}
	
	private void generatePosition(Pane pane, Control plus) {

		Random random = new Random();
		
		double maxX = pane.getPrefHeight();
		double maxY = pane.getPrefWidth();
		
		double x = random.nextDouble() * maxX;
        double y = random.nextDouble() * maxY;
        
        if(x > 171 && x < 355 && y > 113 && y < 252 || y < 54) {
        	while(x > 171 && x < 355) {
        		if(x > 171) {
        			x--;
        		}
        		if(x < 355) {
        			x++;
        		}
        		if(y<54) {
        			y++;
        		}
        	}
        	
        	
        }
        else {
        	plus.setLayoutX(x);
        	plus.setLayoutY(y);
        }
	}
	
	public void rainPlus() {
		
	}
}
