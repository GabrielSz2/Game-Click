package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
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
	private Integer futureMeta = 5;
	private Integer indexPlus = 1;
	private Boolean boo = false;
	private Text more = new Text();
	private Integer clickPower =1;
	private Double xp= 0.0;
	private int timeRest = 1;
	Random rd = new Random();
	
	private List<Text> rain = new ArrayList<>();

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
		score.setLayoutX(280);
		score.setLayoutY(10);
		score.setTextFill(Color.WHITE);

		plus.setStyle("-fx-font-size: 24px;");
		plus.setLayoutX(120);
		plus.setLayoutY(216);
		plus.setTextFill(Color.WHITE);

		ct.farm.setVisible(false);
		
	}

	private void loading() {

		variableStarter();

		// coloquei 1 para desenvolver o match, assim que terminar, favor colocar 3;
		Timeline ti = new Timeline();
		KeyFrame turnOffLo = new KeyFrame(Duration.seconds(1), event -> {
			ct.loading.setVisible(false);
			System.out.println("sistema desligado");
		});

		KeyFrame turnOnSt = new KeyFrame(Duration.seconds(1), event -> {
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

			// coloquei 0 para desenvolver o match, assim que terminar, favor colocar 3;
			Timeline go = new Timeline(new KeyFrame(Duration.seconds(0.1), ev -> {
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
		ct.match.getChildren().addAll(score, plus);

		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rainDown(ct.match);
			}
		};

		
		
		ct.farm.setOnMouseClicked(e -> {
			currentScore++;
			score.setText(currentScore + "/" + futureMeta);

			double x = ct.farm.getLocalToParentTransform().getTx() + e.getX();
			createPlus(ct.match, x);
			timer.start();
			animationFarmClick(ct.match);
			
			xp = ct.pBar.getProgress();
			xp += (double) (1.0 / futureMeta);
			ct.pBar.setProgress(xp);
			
			
			if (currentScore == futureMeta) {
				ct.match.setDisable(true);
				ct.match.setOpacity(0.75);
				
				
				
			}
		});	
		
		
		
		
	}

	private void createPlus(Pane pane, Double x) {
		Text more = new Text("+" + clickPower);
		more.setFont(Font.font(18));
		more.setFill(Color.WHITE);
		more.setX(x);
		more.setY(200);
		more.setDisable(true);

		rain.add(more);
		pane.getChildren().add(more);
	}

	private void rainDown(Pane pane) {
		try {
			for (Text textoNumero : rain) {
				textoNumero.setY(textoNumero.getY() - 2); // Faz o número "cair"
			}

			rain.removeIf(textoNumero -> textoNumero.getY() < pane.getMinHeight() + 80);
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
					if (tst.getY() < pane.getMinHeight() + 80) {
						pane.getChildren().remove(tst);
					}
				} else if (indexPlus < pane.getChildren().size()) {
					indexPlus++;
				}
			}
		} catch (IndexOutOfBoundsException e) {

		}
	}
	
	private void animationFarmClick(Pane pane) {
		Timeline tm = new Timeline();
		KeyFrame ky = new KeyFrame(Duration.seconds(0.1), ev ->{
			ct.farm.setRadius(52);
		});
		ct.farm.setRadius(48);
		
		tm.getKeyFrames().add(ky);
		tm.play();

	}
	

}
