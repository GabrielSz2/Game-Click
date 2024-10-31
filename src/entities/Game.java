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

	private Label clock = createLabel(286, 192, 36);
	private Label plus = createLabel(120, 216, 24);
	private Label sts = createLabel(469, 15, 36, "Status");
	private Label ckPower = createLabel(450, 77, 16);
	private Label porcentageBar = createLabel(286, 24, 15);
	private Label Slevel = createLabel(444, 200, 22, "Level: 1");

	private Integer fim = 2;
	private Integer currentScore = 0;
	private Integer futureMeta = 10;
	private Integer clickPower = 1;
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

		porcentageBar.setText(xp + "%");
		ct.match.getChildren().addAll(plus, sts, ckPower, porcentageBar, Slevel);
		ckPower.setText("Click power: +" + (clickPower));

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
		ckPower.setText("Click power: +" + (clickPower));
		Slevel.setText("Level: " + level);

		double x = ct.farm.getLocalToParentTransform().getTx() + e.getX();
		createPlus(ct.match, x);
		timer.start();
		animationFarmClick(ct.match);

		xp = ct.pBar.getProgress();
		xp += (double) (1.0 / futureMeta);
		ct.pBar.setProgress(xp);
		porcentageBar.setText((String.format("%.0f", xp * 100)) + "%");

		if (currentScore == futureMeta) {
			ct.match.setDisable(true);
			ct.match.setOpacity(0.75);
			level++;
		}

		if (level == fim) {
			endGame();
		}

	}

	private void endGame() {
			ct.match.setVisible(false);
			ct.end.setVisible(true);
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
		List<Text> itemsToRemove = new ArrayList<>();

		for (Text text : rain) {
			text.setY(text.getY() - 2); // Faz o número "cair"

			if (text.getY() < pane.getMinHeight() + 80) {
				itemsToRemove.add(text); // Adiciona itens que saem da área visível para remoção
			}
		}

		// Remove os elementos fora da área visível do `Pane`
		pane.getChildren().removeAll(itemsToRemove);
		rain.removeAll(itemsToRemove);
	}

	private void animationFarmClick(Pane pane) {
		Timeline tm = new Timeline(new KeyFrame(Duration.seconds(0.1), ev -> ct.farm.setRadius(52)));
		ct.farm.setRadius(48);
		tm.play();
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
