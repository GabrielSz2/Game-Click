package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Game {

	private Controller ct;

	private Label clock = new Label();
	private Label score = new Label();
	private Label meta = new Label();
	private Label plus = new Label();

	private Integer currentScore = 0;
	private Integer futureMeta = 10;
	private Integer indexPlus = 1;
	private Boolean boo = false;
	private List<Text> rain = new ArrayList<>();
	Random rd = new Random();

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

			Timeline go = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
				if (timeRest > 0) {
					clock.setText("" + timeRest);
					timeRest--;
				} else if (timeRest == 0) {
					System.out.println("go");
					clock.setText("GO!");
					clock.setLayoutX(275);
					timeRest--;
				} else {
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

		score.setText(currentScore + "/" + futureMeta);
		ct.match.getChildren().addAll(meta, score, plus);

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rainDown(ct.match);
			}
		};

		ct.farm.setOnMouseClicked(e -> {
			currentScore++;
			score.setText(currentScore + "/" + futureMeta);

			createPlus(ct.match);
			timer.start();

			if (currentScore == futureMeta) {
				score.setLayoutX(265);
				ct.match.setDisable(true);
				ct.match.setOpacity(0.75);
			}

		});
		
		
		
		
		
	}

	// Resolver a chuva de numeros
	private void createPlus(Pane pane) {
		Double xi = rd.nextDouble(pane.getPrefWidth());
		Text text = new Text("+1");
		text.setFont(Font.font(18));
		text.setFill(Color.WHITE);
		text.setX(xi);
		text.setY(0);
		text.setDisable(true);

		rain.add(text);
		pane.getChildren().add(text);
	}

	private void rainDown(Pane pane) {
		try {
			for (Text textoNumero : rain) {
				textoNumero.setY(textoNumero.getY() + 2); // Faz o número "cair"
			}

			rain.removeIf(textoNumero -> textoNumero.getY() > pane.getPrefHeight());
			rain.forEach(e -> {
				if (e != null) {
					boo = true;
				} else {
					boo = false;
				}
			});

			if (boo == true) {
				Text tst = null;
				if (pane.getChildren().get(indexPlus) instanceof Text) {
					tst = (Text) pane.getChildren().get(indexPlus);
				}

				if (tst instanceof Text) {

					if (tst.getY() > pane.getPrefHeight()) {
						pane.getChildren().remove(tst);
					}
				} else if (indexPlus < pane.getChildren().size()) {
					indexPlus++;
				}
			}
		} catch (IndexOutOfBoundsException e) {

		}
	}
	
	// Resolver ...
	private void animationFarmClick(Pane pane) {
		
	}
	

}
