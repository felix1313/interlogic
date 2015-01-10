package com.felix.game;

import java.awt.BorderLayout;
import java.io.IOException;

import com.felix.game.view.WindowController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private BorderPane rootPane;
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/Window.fxml"));
		try {
			rootPane = (BorderPane) loader.load();
			Scene scene = new Scene(rootPane);
			primaryStage.setScene(scene);
			WindowController controller = loader.getController();
			controller.init();
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
