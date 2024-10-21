package app;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Program extends Application{
	
	@Override
	public void start(Stage ps) throws Exception {
		URL rc = getClass().getResource("/resources/game.fxml");
		
		Pane gp = new Pane();
		FXMLLoader loader = new FXMLLoader(rc);
		
		gp = loader.load();
		
		Scene scene = new Scene(gp);
		
		ps.setTitle("my game");
		ps.setScene(scene);
		ps.show();
		
		
	}

	public static void main (String[] args) {
		launch(args);
	}

}
