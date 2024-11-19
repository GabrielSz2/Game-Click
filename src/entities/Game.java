package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
	private Label changeWall = createLabel(286, 360, 18);
	
	private Button ready = new Button();
	private Button stats = new Button();

	private Integer skinSecret = 1;
	private Integer WallSecret = 1;
	private Integer autoClickers = 0;
	private Integer multiCoins = 1;
	private Integer coins = 0;
	private Integer end = 50;
	private Integer currentScore = 0;
	private Integer futureMeta = 10;
	private double clickPower = 1.0;
	private Double xp = 0.0;
	private Integer timeRest = 3;
	private Integer level = 1;

	private boolean panePower = false;
	private boolean repsAuto = false;
	private boolean isRunning = false;
	private long lastUpdate = 0;
	private long veloTurbo = 1_000_000_000L;

	private AnimationTimer gameLoop;
	private AnimationTimer UP = new AnimationTimer() {
		@Override
		public void handle(long now) {
			plusUp(ct.match);
		}
	};

	int p0 = 0;

	Random rd = new Random();
	private List<Text> rain = new ArrayList<>();

	public Game() {
	}
	
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

		ct.pane.getChildren().addAll(ready, changeWall, choosePower);
		ct.match.getChildren().addAll(plus, showCoins, sts, ckPower, porcentageBar, showLevel, meta, stats);
		ct.powers.getChildren().add(gainCoin);
		ct.paneRoll.getChildren().add(resultRoll);
	}

	private void loading() {

		variableStarter();

		Timeline ti = new Timeline(new KeyFrame(Duration.seconds(3), e -> ct.loading.setVisible(false)),
				new KeyFrame(Duration.seconds(3), e -> ct.startG.setVisible(true)));
		ti.play();
	}

	private void startMatch() {

		ct.play.setOnMouseClicked(e -> {

			ct.startG.setVisible(false);
			ct.match.setVisible(true);
			match();

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
		initGameLoop();

		toggleGameLoop();
	}

	private void startGame() {

		currentScore++;
		ckPower.setText("Power: +" + ((int) clickPower));
		meta.setText("" + (int) (futureMeta * clickPower));
		showCoins.setText("Coins: " + coins + "\nCoin multi: " + multiCoins + "x");
		showLevel.setText("Level: " + level);
		gainCoin.setStyle("-fx-background-color: black; " + "-fx-font-family: 'Sitka Test'; " + "-fx-font-size: 16px; "
				+ "-fx-text-fill: white;");
		resultRoll.setStyle("-fx-background-color: white; " + "-fx-font-family: 'Serif'; " + "-fx-font-size: 18px; "
				+ "-fx-text-fill: black;");
		changeWall.setStyle("-fx-background-color: white; " + "-fx-font-family: 'Serif'; " + "-fx-font-size: 18px; "
				+ "-fx-text-fill: black;");
		
		if (level == end) {
			endGame();
			System.out.println("end game");
		}

		
		if (ct.autoClicker.isVisible()) {
			if (ct.autoClicker.getFitHeight() == 65) {
				if (repsAuto) {
					xp = ct.pBar.getProgress();
					xp += (double) ((1.0 / futureMeta + clickPower / 100000) * autoClickers * 2);
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
		
		ct.iconConfig.setOnMouseClicked(e -> pauseGame());
		ct.iconWall.setOnMouseClicked(e -> changeWall());
		ct.iconSkin.setOnMouseClicked(e -> changeSkin());
		ct.farm.setOnMouseClicked(e -> handleFarmClick(UP, e));
		stats.setOnMouseClicked(e -> showStatus());
		
	}

	private void handleFarmClick(AnimationTimer timer, MouseEvent e) {

		double x = ct.farm.getLocalToParentTransform().getTx() + e.getX();
		createPlus(ct.match, x);
		timer.start();
		animationFarmClick(ct.match);

		xp = ct.pBar.getProgress();
		xp += (double) (1.0 / futureMeta + clickPower / 100000);
		ct.pBar.setProgress(xp);
		porcentageBar.setText((String.format("%.0f", xp * 100)) + "%");

	}

	private void panePower() {
		ct.match.setDisable(true);
		ct.match.setOpacity(0.75);
		ct.blackScreen.setVisible(true);
		
		ready.setVisible(true);
		choosePower.setVisible(true);

		ready.setOnMouseClicked(evnt -> {
			System.out.println("pane power nivel " + level);
			level++;
			futureMeta = (int) ((int) futureMeta * 1.75);
			if(clickPower * 10000000 < futureMeta) {futureMeta = (int) ((int) futureMeta * 1.75);}
			
			addCoins(1);
			gainCoin.setText("Você ganhou " + 1 * multiCoins + " coins");
			ready.setVisible(false);
			choosePower.setVisible(false);
			ct.powers.setVisible(true);
			panePower = false;
			
			ct.x2Power.setOnMouseClicked(e -> {
				clickPower = clickPower * 2;
				ct.powers.setVisible(false);

				ct.blackScreen.setVisible(false);
				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				currentScore = 0;
			});

			ct.x2Coin.setOnMouseClicked(e -> {
				clickPower = (clickPower * 1.2);
				multiCoins = multiCoins * 2;
				ct.powers.setVisible(false);
				
				ct.blackScreen.setVisible(false);
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
					roll(ct.roulette, numberForRoll); 
					ct.btRoll.setDisable(true);
					
				});

			});			
		});

	}

	private void endGame() {
		ct.match.setVisible(false);
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
				
				switch (WallSecret) {
				case 1: 
				ct.wallS1.setOpacity(0);
				WallSecret++;
				break;
				
				case 2:
				ct.wallS2.setOpacity(0);
				WallSecret++;
				break;
				
				case 3:
				ct.wallS3.setOpacity(0);
				WallSecret++;
				break;
				}
				
				resultRoll.setText("Parabens \n Você ganhou: Wallpaper");
			} else if (roll.getRotate() > 6 && roll.getRotate() < 13) {
				// skin secret
				switch (skinSecret) {
				case 1: 
				ct.skinS1.setOpacity(0);
				skinSecret++;
				break;
				
				case 2:
				ct.skinS2.setOpacity(0);
				skinSecret++;
				break;
				
				case 3:
				ct.skinS3.setOpacity(0);
				skinSecret++;
				break;
				}
				
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
				veloTurbo =- 100_000_000L;
				resultRoll.setText("Parabens \n Você ganhou: Velocidade Turbo");
			} else if (roll.getRotate() > 303 && roll.getRotate() < 361) {
				if (autoClickers == 0) {
					ct.autoClicker.setVisible(true);
				}
				autoClickers++;
				resultRoll.setText("Parabens \n Você ganhou: +1 autoclick");
			}

			Timeline ti = new Timeline(new KeyFrame(Duration.seconds(3), evs -> {
				ct.powers.setVisible(false);
				ct.paneRoll.setVisible(false);
				roll.setRotate(0);

				ct.match.setDisable(false);
				ct.match.setOpacity(1);
				ct.pBar.setProgress(0);
				ct.blackScreen.setVisible(false);
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
	
	private void pauseGame() {
		toggleGameLoop();
		ct.blackScreen.setVisible(true);
		ct.match.setDisable(true);
		ct.match.setOpacity(0.75);
		ct.configPause.setVisible(true);
		ct.paneUtils.setVisible(true);
		
		ct.resume.setOnMouseClicked(e -> resumeGame(ct.configPause));
	}
	
	private void resumeGame(Pane pane) {
		toggleGameLoop();
		ct.blackScreen.setVisible(false);
		ct.match.setDisable(false);
		ct.match.setOpacity(1);
		ct.paneUtils.setVisible(false);
		pane.setVisible(false);
	}
	
	private void showStatus() {
		toggleGameLoop();
		ct.blackScreen.setVisible(true);
		ct.match.setDisable(true);
		ct.match.setOpacity(0.75);
		ct.paneUtils.setVisible(true);
		ct.configStatus.setVisible(true);		
		ct.sAuto.setText("" + autoClickers);
		ct.sPower.setText("" + (int)clickPower);
		ct.sMultiCoins.setText("" + multiCoins);
		ct.sVelo.setText("" + veloTurbo / 1000000000 + "s");
		
		ct.readyStatus.setOnMouseClicked(e -> resumeGame(ct.configStatus));
	}
	
	private void changeWall() {
		toggleGameLoop();
		ct.blackScreen.setVisible(true);
		ct.match.setDisable(true);
		ct.match.setOpacity(0.75);
		ct.paneUtils.setVisible(true);
		ct.configWall.setVisible(true);
		
		
		ct.wall1.setOnMouseClicked(e -> unlockWall(0));
		ct.wall2.setOnMouseClicked(e -> unlockWall(1));
		ct.wall3.setOnMouseClicked(e -> unlockWall(2));
		ct.wall4.setOnMouseClicked(e -> unlockWall(3));
		ct.wall5.setOnMouseClicked(e -> unlockWall(4));
		ct.wall6.setOnMouseClicked(e -> unlockWall(5));
		ct.wallS1.setOnMouseClicked(e -> unlockWall(6));
		ct.wallS2.setOnMouseClicked(e -> unlockWall(7));
		ct.wallS3.setOnMouseClicked(e -> unlockWall(8));
		
			
		ct.readyWall.setOnMouseClicked(ep -> resumeGame(ct.configWall));
	}
	
	private void unlockWall(Integer wall) {			
			switch(wall){
			case 0:
				if(ct.wall1.getOpacity() == 1) {
					if(coins >= 1000) {
						ct.wall1.setOpacity(0);
						coins = coins - 1000;
					}
				}
				else if(ct.wall1.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\wall1.jpg").toExternalForm()));	
				}
				
				break;
				
			case 1:
				if(ct.wall2.getOpacity() == 1) {
					if(coins >= 10000) {
						ct.wall2.setOpacity(0);
						coins = coins - 10000;
					}
				}
				else if(ct.wall2.getOpacity() == 0 ) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\wall2.jpg").toExternalForm()));	
				}
				
				break;
				
			case 2:
				if(ct.wall3.getOpacity() == 1) {
					if(coins >= 50000) {
						ct.wall3.setOpacity(0);
						coins = coins - 50000;
					}
				}
				else if(ct.wall3.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\wall3.jpg").toExternalForm()));	
				}
				
				break;
				
			case 3:
				if(ct.wall4.getOpacity() == 1) {
					if(coins >= 100000) {
						ct.wall4.setOpacity(0);
						coins = coins - 100000;
					}
				}
				else if(ct.wall4.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\wall4.jpg").toExternalForm()));	
				}
				
				break;
				
			case 4:
				if(ct.wall5.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\Wall5.jpg").toExternalForm()));	
				}
				
				break;
				
			case 5:
				if(ct.wall6.getOpacity() == 1) {
					if(coins >= 1000000) {
						ct.wall6.setOpacity(0);
						coins = coins - 1000000;
					}
				}
				else if(ct.wall6.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\Wall6.jpg").toExternalForm()));	
				}
				
				break;
			
			case 6:
				if(ct.wallS1.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\wallS1.jpg").toExternalForm()));	
				}
				
				break;
				
			case 7:
				if(ct.wallS2.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\WallS2.jpg").toExternalForm()));	
				}
				
				break;
				
			case 8:
				if(ct.wallS3.getOpacity() == 0) {
					ct.wallpaper.setImage(new Image(getClass()
					.getClassLoader().getResource("\\imagens\\imagensWall\\wallS3.jpg").toExternalForm()));	
				}
				
				break;
			}
		
	}
	
	private void changeSkin() {
		toggleGameLoop();
		ct.blackScreen.setVisible(true);
		ct.match.setDisable(true);
		ct.match.setOpacity(0.75);
		ct.paneUtils.setVisible(true);
		ct.configSkin.setVisible(true);
		
		
		ct.skin1.setOnMouseClicked(e -> unlockSkin(0));
		ct.skin2.setOnMouseClicked(e -> unlockSkin(1));
		ct.skin3.setOnMouseClicked(e -> unlockSkin(2));
		ct.skin4.setOnMouseClicked(e -> unlockSkin(3));
		ct.skin5.setOnMouseClicked(e -> unlockSkin(4));
		ct.skin6.setOnMouseClicked(e -> unlockSkin(5));
		ct.skinS1.setOnMouseClicked(e -> unlockSkin(6));
		ct.skinS2.setOnMouseClicked(e -> unlockSkin(7));
		ct.skinS3.setOnMouseClicked(e -> unlockSkin(8));
		
			
		ct.readySkin.setOnMouseClicked(ep -> resumeGame(ct.configSkin));
	}
	
	private void unlockSkin(Integer skin) {
		switch(skin){
		case 0:
			if(ct.skin1.getOpacity() == 1) {
				if(coins >= 1000) {
					ct.skin1.setOpacity(0);
					coins = coins - 1000;
				}
			}
			else if(ct.skin1.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skin1.png").toExternalForm()));	
			}
			
			break;
			
		case 1:
			if(ct.skin2.getOpacity() == 1) {
				if(coins >= 10000) {
					ct.skin2.setOpacity(0);
					coins = coins - 10000;
				}
			}
			else if(ct.skin2.getOpacity() == 0 ) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skin2.png").toExternalForm()));	
			}
			
			break;
			
		case 2:
			if(ct.skin3.getOpacity() == 1) {
				if(coins >= 50000) {
					ct.skin3.setOpacity(0);
					coins = coins - 50000;
				}
			}
			else if(ct.skin3.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skin3.png").toExternalForm()));	
			}
			
			break;
			
		case 3:
			if(ct.skin4.getOpacity() == 1) {
				if(coins >= 100000) {
					ct.skin4.setOpacity(0);
					coins = coins - 100000;
				}
			}
			else if(ct.skin4.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skin4.png").toExternalForm()));	
			}
			
			break;
			
		case 4:
			if(ct.skin5.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skin5.png").toExternalForm()));	
			}
			
			break;
			
		case 5:
			if(ct.skin6.getOpacity() == 1) {
				if(coins >= 1000000) {
					ct.skin6.setOpacity(0);
					coins = coins - 1000000;
				}
			}
			else if(ct.skin6.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skin6.png").toExternalForm()));	
			}
			
			break;
		
		case 6:
			if(ct.skinS1.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skinS1.png").toExternalForm()));	
			}
			
			break;
			
		case 7:
			if(ct.skinS2.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skinS3.png").toExternalForm()));	
			}
			
			break;
			
		case 8:
			if(ct.skinS3.getOpacity() == 0) {
				ct.farm.setImage(new Image(getClass()
				.getClassLoader().getResource("\\imagens\\imagensFarm\\skinS2.png").toExternalForm()));	
			}
			
			break;
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

				if (now - lastUpdate >= veloTurbo) { 
					animationAutoClick();
					if (!ct.match.isDisable()) {
						if (!repsAuto) {
							repsAuto = true;
						}
					}
					lastUpdate = now;
				}
			}
		};
	}

	private void toggleGameLoop() {
		if (isRunning) {
			gameLoop.stop();
			System.out.println("Jogo pausado");
		} else {
			gameLoop.start();
			System.out.println("Jogo iniciado");
		}
		isRunning = !isRunning; 
	}

}