package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Game {

	private Controller ct;

	private Label clock = createLabel(286, 192, 36);
	private Label plus = createLabel(120, 216, 24);
	private Label sts = createLabel(469, 15, 36, "Status");
	private Label ckPower = createLabel(450, 77, 16);
	private Label porcentageBar = createLabel(286, 24, 15);
	private Label showLevel = createLabel(444, 200, 22, "Level: 1");
	private Label showCoins = createLabel(450, 100, 16);
	private Label meta = createLabel(252, 48, 12);
	private Label choosePower = createLabel(150, 250, 28, "Escolha sua recompensa!");
	private Label gainCoin = createLabel(230, 380, 16);
	
	private Button ready = new Button();
	
	private Integer multiCoins = 1;
	private Integer coins = 0;
	private Integer end = 100;
	private Integer currentScore = 0;
	private Integer futureMeta = 10;
	private double clickPower = 1.0;
	private Double xp = 0.0;
	private Integer timeRest = 3;
	private Integer level = 1;

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
		ct.farm.setVisible(false);
	}

	private void loading() {

		variableStarter();

		// coloquei 1 seg para desenvolver o match, assim que terminar, favor colocar 3;
		Timeline ti = new Timeline(new KeyFrame(Duration.seconds(3), e -> ct.loading.setVisible(false)),
				new KeyFrame(Duration.seconds(3), e -> ct.startG.setVisible(true)));
		ti.play();
	}

	private void startGame() {

		ct.play.setOnMouseClicked(e -> {

			ct.startG.setVisible(false);
			ct.match.setVisible(true);

			// coloquei 0 para desenvolver o match, assim que terminar, favor colocar 1;
			Timeline go = new Timeline(new KeyFrame(Duration.seconds(1), ev -> updateClock()));

			go.setCycleCount(Timeline.INDEFINITE);
			go.play();
			ct.match.getChildren().add(clock);

		});
	}

	private void updateClock() {
		if (timeRest > 0) {
			clock.setText(String.valueOf(timeRest--));
		} else if (timeRest == 0) {
			clock.setText("GO!");
			clock.setLayoutX(275);
			timeRest--;
		} else {
			clock.setVisible(false);
			ct.farm.setVisible(true);
		}
	}

	private void match() {
		ct.pane.getChildren().add(ready);
		ct.pane.getChildren().add(choosePower);
		choosePower.setVisible(false);
		ready.setVisible(false);
		
		porcentageBar.setText(xp + "%");
		ct.match.getChildren().addAll(plus, showCoins, sts, ckPower, porcentageBar, showLevel, meta);
		ct.powers.getChildren().add(gainCoin);
		ckPower.setText("Click power: +" + ((int)clickPower));
		meta.setText("" + futureMeta);
		showCoins.setText("Coins: " + coins + "\nCoin multiplier: " + multiCoins + "x");
		
		AnimationTimer timer = new AnimationTimer() {
			@Override
			public void handle(long now) {
				rainDown(ct.match);
			}
		};

		ct.farm.setOnMouseClicked(e -> handleFarmClick(timer, e));

	}

	private void handleFarmClick(AnimationTimer timer, MouseEvent e) {
		currentScore++;
		meta.setText("" + (int)(futureMeta * clickPower));
		ckPower.setText("Click power: +" + ((int)clickPower));
		showLevel.setText("Level: " + level);
		showCoins.setText("Coins: " + coins + "\nCoin multiplier: " + multiCoins + "x");
		gainCoin.setStyle("-fx-background-color: black; " +
	               "-fx-font-family: 'Sitka Test'; " +
	               "-fx-font-size: 16px; " +
	               "-fx-text-fill: white;");
		
		double x = ct.farm.getLocalToParentTransform().getTx() + e.getX();
		createPlus(ct.match, x);
		timer.start();
		animationFarmClick(ct.match);

		xp = ct.pBar.getProgress();
		xp += (double) (1.0 / futureMeta);
		ct.pBar.setProgress(xp);
		porcentageBar.setText((String.format("%.0f", xp * 100)) + "%");

		if (ct.pBar.getProgress() > 0.99) {
			panePower();
			System.out.println("pane power nivel " + level);
		}

		if (level == end) {
			endGame();
			System.out.println("end game");
		}

	}

	private void panePower() {
		ct.match.setDisable(true);
		ct.match.setOpacity(0.75);
		level++;
		futureMeta = (int) ((int) futureMeta * 1.5);

		// implementar um botão para setVisible do powers
		ready.setLayoutX(230);
		ready.setLayoutY(300);
		ready.setText("Escolher");
		ready.setStyle("-fx-font-size: " + 28 + "px;");
		ready.setVisible(true);
		choosePower.setVisible(true);
		
		ready.setOnMouseClicked(evnt -> {
			if(coins != 0) {coins += 1 * multiCoins;}
			if(coins == 0) {coins++;}
			gainCoin.setText("Você ganhou " + 1 * multiCoins + " coins");
			ready.setVisible(false);
			choosePower.setVisible(false);
			ct.powers.setVisible(true);

			ct.x2Power.setOnMouseClicked(e -> {
				clickPower = clickPower * 2;
				ct.powers.setVisible(false);

				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				currentScore = 0;
			});

			ct.x2Coin.setOnMouseClicked(e -> {
				clickPower = (clickPower * 1.5);
				multiCoins = multiCoins * 2;
				ct.powers.setVisible(false);

				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				currentScore = 0;
			});

			ct.roleta.setOnMouseClicked(e -> {
				clickPower = (int) (clickPower * 1.2);
				ct.powers.setVisible(false);

				
				
				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				currentScore = 0;
			});

		});

	}

	private void endGame() {
		ct.match.setVisible(false);
		ct.end.setVisible(true);
	}

	private void createPlus(Pane pane, Double x) {
		Text more = new Text("+" + (int)(clickPower));
		more.setFont(Font.font(18));
		more.setFill(Color.WHITE);
		more.setX(x);
		more.setY(200);
		more.setDisable(true);

		rain.add(more);
		pane.getChildren().add(more);
	}

	private void rainDown(Pane pane) {
		List<Text> itemsToRemove = new ArrayList<>();

		for (Text text : rain) {
			text.setY(text.getY() - 2); 

			if (text.getY() < pane.getMinHeight() + 80) {
				itemsToRemove.add(text); 
			}
		}

		
		pane.getChildren().removeAll(itemsToRemove);
		rain.removeAll(itemsToRemove);
	}

	private void animationFarmClick(Pane pane) {
		Timeline tm = new Timeline(new KeyFrame(Duration.seconds(0.1), ev -> {
			animationFarm(200, 188, 206, 106);
		}));
		animationFarm(165, 170, 218, 118);

		tm.play();
	}

	private void animationFarm(int fitW, int fitH, int x, int y) {
		ct.farm.setFitWidth(fitW);
		ct.farm.setFitHeight(fitH);
		ct.farm.setLayoutX(x);
		ct.farm.setLayoutY(y);
	}

	private Label createLabel(double x, double y, int fontSize) {
		Label label = new Label();
		label.setStyle("-fx-font-size: " + fontSize + "px;");
		label.setLayoutX(x);
		label.setLayoutY(y);
		label.setTextFill(Color.WHITE);
		return label;
	}

	private Label createLabel(double x, double y, int fontSize, String text) {
		Label label = createLabel(x, y, fontSize);
		label.setText(text);
		return label;
	}

}
