package com.felix.game.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Accordion;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.log4j.Logger;

import com.felix.game.Main;
import com.felix.game.map.model.Map;
import com.felix.game.model.Game;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;
import com.felix.game.socket.Server;

public class GameLoadController {
	private Logger log = Logger.getLogger(getClass());
	@FXML
	private TextField gameIdText;
	@FXML
	private PasswordField passTextCreate;
	@FXML
	private PasswordField passTextJoin;
	@FXML
	private Accordion accordion;
	@FXML
	private TitledPane createGamePane;
	@FXML
	private TitledPane joinGamePane;

	@FXML
	private Canvas canvas;
	private Main mainApp;
	private Map map;

	private boolean gameLoaded = false;

	public void setMainApp(Main main) {
		this.mainApp = main;
		accordion.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null)
							oldPane.setCollapsible(true);
						if (newPane != null)
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									newPane.setCollapsible(false);
								}
							});
					}
				});
		// for (TitledPane pane : accordion.getPanes())
		// pane.setAnimated(false);
		accordion.setExpandedPane(accordion.getPanes().get(0));
	}

	@FXML
	private void handleGOClick() {

		if (accordion.getExpandedPane() == createGamePane) {
			Message res;
			log.info("creating game...");
			Game game = new Game(passTextCreate.getText());
			game.setMap(map);
			res = Server.instance().createGame(game);
			if (res.getMessageType() == MessageType.OPERATION_SUCCESS) {
				log.info("game loaded! ");
				mainApp.setGame((Game) res.getData());
				System.out.println(res);
				mainApp.loadGameForm();
			} else
				log.warn("failed to load( " + res);

		} else {
			if (gameLoaded || handleConnectClick())

				mainApp.loadGameForm();

		}

	}

	@FXML
	private void handleCreateMapClick() {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				new com.felix.game.map.Main().start(new Stage());

			}
		});
	}

	@FXML
	private void handleOpenMapClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open map");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("MAP", "*.map"),
				new FileChooser.ExtensionFilter("All files", "*.*"));

		File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
		if (file != null) {
			ObjectInputStream in = null;
			try {
				in = new ObjectInputStream(new FileInputStream(file));
				map = (Map) in.readObject();
				map.drawMap(canvas.getGraphicsContext2D(), 1);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	private boolean handleConnectClick() {

		// validate
		if (gameIdText.getText().isEmpty()) {
			log.warn("invalid input");
			return false;
		}
		Integer gameId = Integer.parseInt(gameIdText.getText());
		log.info("trying to join game... " + gameId);
		Message res = Server.instance()
				.joinGame(gameId, passTextJoin.getText());
		if (res.getMessageType() == MessageType.OPERATION_SUCCESS) {
			log.info("connection success");
			Game game = (Game) res.getData();
			this.map = game.getMap();
			map.drawMap(this.canvas.getGraphicsContext2D(), 1);
			mainApp.setGame(game);
			gameLoaded = true;
			return true;
		} else {
			log.warn("failed to connect" + res.getMessageType());
			return false;
		}
	}
}
