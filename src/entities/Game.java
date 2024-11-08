package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
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
	private Label resultRoll = createLabel(216, 12, 18);

	private Button ready = new Button();
	private Button stats = new Button();

	private Integer autoClickers = 0;
	private Integer multiCoins = 1;
	private Integer coins = 0;
	private Integer end = 5;
	private Integer currentScore = 0;
	private Integer futureMeta = 10;
	private double clickPower = 1.0;
	private Double xp = 0.0;
	private Integer timeRest = 3;
	private Integer level = 1;

	private boolean panePower = false;
	private boolean repsAuto = false;
	private boolean isRunning = false;
	private AnimationTimer gameLoop;
	private long lastUpdate = 0;

	AnimationTimer UP = new AnimationTimer() {
		@Override
		public void handle(long now) {
			plusUp(ct.match);
		}
	};

	int p0 = 0;

	Random rd = new Random();
	private List<Text> rain = new ArrayList<>();

	public Game(Controller ct) {
		this.ct = ct;
	}

	public void runGame() {
		loading();
		startMatch();

	}

	private void variableStarter() {
		ct.farm.setVisible(false);
		choosePower.setVisible(false);
		ready.setVisible(false);
		ct.autoClicker.setVisible(false);

		porcentageBar.setText(xp + "%");
		ready.setLayoutX(230);
		ready.setLayoutY(300);
		ready.setText("Escolher");
		ready.setStyle("-fx-font-size: " + 28 + "px;");

		stats.setLayoutX(492);
		stats.setLayoutY(162);
		stats.setText("Stats");
		stats.setStyle("-fx-background-color: white; " + "-fx-font-family: 'Serif'; " + "-fx-font-size: 18px; "
				+ "-fx-text-fill: black;");

		ct.pane.getChildren().add(ready);
		ct.pane.getChildren().add(choosePower);
		ct.match.getChildren().addAll(plus, showCoins, sts, ckPower, porcentageBar, showLevel, meta, stats);
		ct.powers.getChildren().add(gainCoin);
		ct.paneRoll.getChildren().add(resultRoll);
	}

	private void loading() {

		variableStarter();

		// coloquei 1 seg para desenvolver o match, assim que terminar, favor colocar 3;
		Timeline ti = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> ct.loading.setVisible(false)),
				new KeyFrame(Duration.seconds(0.1), e -> ct.startG.setVisible(true)));
		ti.play();
	}

	private void startMatch() {

		ct.play.setOnMouseClicked(e -> {

			ct.startG.setVisible(false);
			ct.match.setVisible(true);
			match();

			// coloquei 0 para desenvolver o match, assim que terminar, favor colocar 1;
			Timeline go = new Timeline(new KeyFrame(Duration.seconds(0.1), ev -> updateClock()));

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
		initGameLoop();

		toggleGameLoop();

	}

	private void startGame() {

		currentScore++;
		ckPower.setText("Click power: +" + ((int) clickPower));
		meta.setText("" + (int) (futureMeta * clickPower));
		showCoins.setText("Coins: " + coins + "\nCoin multiplier: " + multiCoins + "x");
		showLevel.setText("Level: " + level);
		gainCoin.setStyle("-fx-background-color: black; " + "-fx-font-family: 'Sitka Test'; " + "-fx-font-size: 16px; "
				+ "-fx-text-fill: white;");
		resultRoll.setStyle("-fx-background-color: white; " + "-fx-font-family: 'Serif'; " + "-fx-font-size: 18px; "
				+ "-fx-text-fill: black;");

		if (level == end) {
			endGame();
			System.out.println("end game");
		}

		
		if (ct.autoClicker.isVisible()) {
			if (ct.autoClicker.getFitHeight() == 65) {
				if (repsAuto) {
					xp = ct.pBar.getProgress();
					xp += (double) (1.0 / futureMeta);
					ct.pBar.setProgress(xp);
					porcentageBar.setText((String.format("%.0f", xp * 100)) + "%");
					repsAuto = false;
				}
			}
		}
		

		if (panePower == false) {
			if (ct.pBar.getProgress() > 0.99) {
				panePower();
				panePower = true;
				if(repsAuto) {repsAuto = false;}
				ct.pBar.setProgress(0);
			}
		}

		ct.farm.setOnMouseClicked(e -> handleFarmClick(UP, e));

	}

	private void handleFarmClick(AnimationTimer timer, MouseEvent e) {

		// metodos do click
		double x = ct.farm.getLocalToParentTransform().getTx() + e.getX();
		createPlus(ct.match, x);
		timer.start();
		animationFarmClick(ct.match);

		xp = ct.pBar.getProgress();
		xp += (double) (1.0 / futureMeta);
		ct.pBar.setProgress(xp);
		porcentageBar.setText((String.format("%.0f", xp * 100)) + "%");

	}

	private void panePower() {
		ct.match.setDisable(true);
		ct.match.setOpacity(0.3);

		ready.setVisible(true);
		choosePower.setVisible(true);

		ready.setOnMouseClicked(evnt -> {
			System.out.println("pane power nivel " + level);
			level++;
			futureMeta = (int) ((int) futureMeta * clickPower);

			addCoins(1);
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
				clickPower = (clickPower * 1.2);
				multiCoins = multiCoins * 2;
				ct.powers.setVisible(false);

				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				currentScore = 0;
			});

			ct.crazyRoulette.setOnMouseClicked(e -> {
				clickPower = (clickPower * 1.2);
				ct.powers.setVisible(false);
				ct.paneRoll.setVisible(true);
				ct.btRoll.setDisable(false);

				ct.btRoll.setOnMouseClicked(ev -> {
					Random xyz = new Random();
					int numberForRoll = xyz.nextInt(361);
					roll(ct.roulette, 350);
					ct.btRoll.setDisable(true);
				});

			});
		});

	}

	private void endGame() {
		ct.match.setVisible(false);
		// metodo para stopar a gameplay
		toggleGameLoop();
		ct.end.setVisible(true);
	}

	private void createPlus(Pane pane, Double x) {
		Text more = new Text("+" + (int) (clickPower));
		more.setFont(Font.font(18));
		more.setFill(Color.WHITE);
		more.setX(x);
		more.setY(200);
		more.setDisable(true);

		rain.add(more);
		pane.getChildren().add(more);
	}

	private void plusUp(Pane pane) {
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
			animationFarm(200, 188, 206, 106, ct.farm);
		}));
		animationFarm(165, 170, 218, 118, ct.farm);

		tm.play();
	}

	private void animationFarm(int fitW, int fitH, int x, int y, ImageView farm) {
		farm.setFitWidth(fitW);
		farm.setFitHeight(fitH);
		farm.setLayoutX(x);
		farm.setLayoutY(y);
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

	private void roll(ImageView roll, int angulo) {
		Rotate rotate = new Rotate();
		roll.getTransforms().add(rotate);

		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), roll);
		rotateTransition.setByAngle(angulo * 100);
		rotateTransition.setCycleCount(1);
		rotateTransition.play();

		rotateTransition.setOnFinished(event -> {
			roll.setRotate(roll.getRotate() / 100);

			System.out.println("angle rotransi: " + rotateTransition.getByAngle());
			System.out.println(roll.getRotate());

			if (roll.getRotate() > 0 && roll.getRotate() < 7) {
				// wallpaper secret

				resultRoll.setText("Parabens \n Você ganhou: Wallpaper");
			} else if (roll.getRotate() > 6 && roll.getRotate() < 13) {
				// skin secret

				resultRoll.setText("Parabens \n Você ganhou: Skin secret");
			} else if (roll.getRotate() > 12 && roll.getRotate() < 71) {
				addCoins(100);
				resultRoll.setText("Parabens \n Você ganhou: coins");
			} else if (roll.getRotate() > 70 && roll.getRotate() < 129) {
				resultRoll.setText("Parabens \n Você ganhou: nada kkkk");
			} else if (roll.getRotate() > 128 && roll.getRotate() < 187) {
				resultRoll.setText("Parabens \n Você ganhou: 10x coin");
				multiCoins = multiCoins * 10;
			} else if (roll.getRotate() > 186 && roll.getRotate() < 246) {
				resultRoll.setText("Parabens \n Você ganhou: 5x power");
				clickPower = clickPower * 5;
			} else if (roll.getRotate() > 247 && roll.getRotate() < 304) {
				// Turbo
				resultRoll.setText("Parabens \n Você ganhou: Velocidade Turbo");
			} else if (roll.getRotate() > 303 && roll.getRotate() < 361) {
				// +1 auto
				if (autoClickers == 0) {
					ct.autoClicker.setVisible(true);
				}
				autoClickers++;
				resultRoll.setText("Parabens \n Você ganhou: +1 autoclick");
			}

			Timeline ti = new Timeline(new KeyFrame(Duration.seconds(5), evs -> {
				ct.powers.setVisible(false);
				ct.paneRoll.setVisible(false);
				roll.setRotate(0);

				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				
				panePower = false;
			}));

			ti.play();

		});

	}

	private void addCoins(int amount) {
		if (coins == 0) {
			coins += amount;
		} else {
			coins += amount * multiCoins;
		}
	}

	private void animationAutoClick() {
		Timeline tm = new Timeline(new KeyFrame(Duration.seconds(0.1), ev -> {
			animationFarm(79, 70, 312, 201, ct.autoClicker);
		}));

		animationFarm(79, 65, 312, 201, ct.autoClicker);
		tm.play();
	}

	private void initGameLoop() {
		gameLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				startGame();

				if (now - lastUpdate >= 1_000_000_000) { // 1 bilhão de nanossegundos = 1 segundo
					animationAutoClick();
					if (!ct.match.isDisable()) {
						if (!repsAuto) {
							repsAuto = true;
						}
					}
					lastUpdate = now; // Atualiza o tempo da última execução
				}
			}
		};
	}

	// Método que liga ou desliga o game loop
	private void toggleGameLoop() {
		if (isRunning) {
			gameLoop.stop();
			System.out.println("Jogo pausado");
		} else {
			gameLoop.start();
			System.out.println("Jogo iniciado");
		}
		isRunning = !isRunning; // Alterna o estado
	}

}