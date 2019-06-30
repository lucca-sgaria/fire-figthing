package br.com.ucs.firecombat.view;

import java.io.IOException;

import br.com.ucs.firecombat.main.AppMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainView extends Application {
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent fxml = FXMLLoader.load(MainView.class.getResource("View.fxml"));
		Scene containsWord = new Scene(fxml);
		primaryStage.setScene(containsWord);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
