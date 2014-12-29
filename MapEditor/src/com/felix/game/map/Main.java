package com.felix.game.map;

import java.awt.BorderLayout;
import java.io.IOException;

import com.felix.game.map.view.MapEditorController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane rootPane;

	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("view/MapEditor.fxml"));
			rootPane = (BorderPane) loader.load();
			MapEditorController controller = loader.getController();
			controller.setMainApp(this);
			Scene scene = new Scene(rootPane);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
}
