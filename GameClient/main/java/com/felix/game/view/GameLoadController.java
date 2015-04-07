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
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import org.apache.log4j.Logger;

import com.felix.game.Main;
import com.felix.game.map.model.Map;
import com.felix.game.model.Game;
import com.felix.game.server.Server;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;
import com.felix.game.util.PasswordUtil;

public class GameLoadController {
	private Logger log = Logger.getLogger(getClass());
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

	@FXML
	ComboBox<Game> gameList;
	private Main mainApp;
	private Map map;

	private boolean gameLoaded = false;
	private final int MAP_ZOOM = 1;

	public void setMainApp(Main main) {
		this.mainApp = main;
		// List<Game> testGames = new ArrayList<Game>();
		// // Map map = new Map(128,128);
		// testGames.add(new Game(0, "sss"));
		// testGames.add(new Game(1, "s3s"));
		// testGames.add(new Game(2, "s4s"));
		// testGames.add(new Game(3, "s5s"));
		// testGames.forEach(g -> g.setMap(new Map(128, 128)));
		gameList.getItems().addAll(Server.instance().getGameList());
		gameList.setCellFactory(new Callback<ListView<Game>, ListCell<Game>>() {

			@Override
			public ListCell<Game> call(ListView<Game> param) {
				return new ListCell<Game>() {
					@Override
					protected void updateItem(Game item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setGraphic(null);
						} else {
							String str = "Gameid=" + item.getGameId();
							if (item.getGamePassword() != null
									&& PasswordUtil.instance().passwordEquals(
											"", item.getGamePassword()) == false)
								str += "(password)";
							setText(str);
						}
					}
				};
			}
		});

		gameList.setConverter(new StringConverter<Game>() {

			@Override
			public String toString(Game object) {
				if (object == null)
					return null;
				String str = "Gameid=" + object.getGameId();
				if (object.getGamePassword() != null
						&& PasswordUtil.instance().passwordEquals("",
								object.getGamePassword()) == false)
					str += "(password)";
				return str;
			}

			@Override
			public Game fromString(String string) {
				if (string == null || string.isEmpty())
					return null;
				int from = 0, to;
				if (string.startsWith("Gameid="))
					from = "Gameid=".length();
				to = from;
				while (to < string.length()
						&& Character.isDigit(string.charAt(to)))
					to++;

				Game g = null;
				try {
					int id = Integer.parseInt(string.substring(from, to));
					g = gameList.getItems()
							.filtered(game -> game.getGameId() == id).get(0);
				} catch (Exception e) {
					log.info("incorrect id");
				}
				return g;
			}
		});
		accordion.expandedPaneProperty().addListener(
				new ChangeListener<TitledPane>() {
					@Override
					public void changed(
							ObservableValue<? extends TitledPane> property,
							final TitledPane oldPane, final TitledPane newPane) {
						if (oldPane != null)
							oldPane.setCollapsible(true);
						if (newPane != null)
							Platform.runLater(() -> newPane
									.setCollapsible(false));
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
		if (gameList.getValue() == null) {
			log.warn("invalid input");
			return false;
		}
		Integer gameId = gameList.getValue().getGameId();
		log.info("trying to join game... " + gameId);
		Message res = Server.instance()
				.joinGame(gameId, passTextJoin.getText());
		if (res.getMessageType() == MessageType.OPERATION_SUCCESS) {
			log.info("connection success");
			Game game = (Game) res.getData();
			this.map = game.getMap();
			map.drawMap(this.canvas.getGraphicsContext2D(), MAP_ZOOM);
			mainApp.setGame(game);
			gameLoaded = true;
			return true;
		} else {
			log.warn("failed to connect" + res.getMessageType());
			return false;
		}
	}

	@FXML
	void handleComboboxSelection() {
		log.info("game selected ");
		Game selected = gameList.getValue();
		if (selected != null)
			selected.getMap().drawMap(canvas.getGraphicsContext2D(), MAP_ZOOM);
		else {
			canvas.getGraphicsContext2D().clearRect(0, 0, 9999, 9999);
		}
	}
}
