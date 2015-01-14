package com.felix.game;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;

import com.felix.game.view.LoginController;
import com.felix.game.view.RootController;
import com.felix.game.view.WindowController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	private BorderPane rootPane;
	private Stage primaryStage;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		initRootLayout();
		loadLoginForm();
		primaryStage.show();
	}

	public void initRootLayout() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/RootLayout.fxml"));
		try {
			rootPane = (BorderPane) loader.load();
			Scene scene = new Scene(rootPane);
			primaryStage.setScene(scene);
			// primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadGameForm() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/Window.fxml"));
		try {
			AnchorPane loginPane = (AnchorPane) loader.load();
			rootPane.setCenter(loginPane);
			WindowController controller = loader.getController();
			controller.init();
			// primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadLoginForm() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("view/LoginForm.fxml"));
		try {
			AnchorPane loginPane = (AnchorPane) loader.load();
			rootPane.setCenter(loginPane);
			LoginController controller = loader.getController();
			// primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		PropertyConfigurator.configure("config/log4j.properties");
		launch(args);
	}
}
