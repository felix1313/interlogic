package com.felix.game.view;

import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.Main;
import com.felix.game.map.model.Map;
import com.felix.game.model.ChatMessage;
import com.felix.game.model.UnitModel;
import com.felix.game.socket.Server;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class WindowController {
	@FXML
	private Canvas canvasBack;
	@FXML
	private Canvas canvasFront;
	@FXML
	private TextArea messageInput;
	@FXML
	private TextArea messageOutput;
	private Map map;
	private int brushCoef = 5;
	private final Logger log = Logger.getLogger(getClass());
	private HashMap<Integer, UnitModel> models = new HashMap<>();
	private Integer me;
	private AnimationTimer timer;
	private Main mainApp;

	public void setId(int id) {
		log.info("my id=" + id);
		me = (id);
	}

	public void init(Main mainApp) {
		log.info("windowcontroller init");
		this.mainApp = mainApp;
		this.map = mainApp.getGame().getMap();
		mainApp.getPrimaryStage().setTitle(
				"Game id: " + mainApp.getGame().getGameId());
		this.drawMap(canvasBack.getGraphicsContext2D());
		// markUnit(unitX, unitY);
		timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				for (UnitModel model : models.values()) {
					/*
					 * if (unitX == targetX && unitY == targetY) { this.stop();
					 * return; }
					 */
					// System.out.println("tick");
					Integer unitX = model.getUnitX();
					Integer unitY = model.getUnitY();
					Integer targetX = model.getTargetX();
					Integer targetY = model.getTargetY();
					repaintMap(unitX, unitY);
					if (unitX < targetX)
						unitX++;
					else if (unitX > targetX)
						unitX--;

					if (unitY < targetY)
						unitY++;
					else if (unitY > targetY)
						unitY--;
					model.setUnitX(unitX);
					model.setUnitY(unitY);
					WindowController.this.markUnit(model);
				}
			}
		};

	}

	public void drawMap(GraphicsContext gc) {

		for (int i = 0; i < map.getHeight(); i++)
			for (int j = 0; j < map.getWidth(); j++) {
				int type = map.getMap()[i][j];
				Color c = Map.numToColor(type);
				gc.setFill(c);
				gc.fillRect(i * brushCoef, j * brushCoef, brushCoef, brushCoef);
			}
	}

	public void move(UserLocationDTO targetLocation) {
		models.get(targetLocation.getUserId()).setUserLocationDTO(
				targetLocation);
		System.out.println(models);
		timer.start();
	}

	public void addUnit(UserLocationDTO locationDTO) {
		log.info("unit added id=" + locationDTO.getUserId());
		UnitModel model = new UnitModel(locationDTO);
		models.put(locationDTO.getUserId(), model);
		markUnit(model);
	}

	@FXML
	private void handleCanvasClick(MouseEvent ev) {
		log.info("canvas click");
		double x = ev.getX();
		double y = ev.getY();

		UnitModel model = models.get(me);
		repaintMap(model.getTargetX(), model.getTargetY());

		model.setTargetX((int) (x - ((int) (x + 0.0001)) % brushCoef));
		model.setTargetY((int) (y - ((int) (y + 0.0001)) % brushCoef));
		// model.setTargetX((int) (x + 0.000001));
		// model.setTargetY((int) (y + 0.000001));
		markTarget(model.getTargetX(), model.getTargetY());
		timer.start();
		Server.instance().moveUnit(model.getUserLocationDTO());
	}

	private void markTarget(int x, int y) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext gc = canvasFront.getGraphicsContext2D();
				gc.setFill(Color.BLACK);
				gc.fillOval(x, y, brushCoef, brushCoef);
			}
		});
	}

	private void markUnit(UnitModel m) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext gc = canvasFront.getGraphicsContext2D();
				gc.setFill(m.getColor());
				gc.fillOval(m.getUnitX(), m.getUnitY(), brushCoef, brushCoef);
			}
		});

	}

	private void repaintMap(int x, int y) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext gc = canvasFront.getGraphicsContext2D();
				gc.clearRect(x, y, brushCoef, brushCoef);
			}
		});

	}

	@FXML
	private void handleMessageSend() {
		String text = messageInput.getText();
		ChatMessage message = new ChatMessage(text, mainApp.getUser()
				.getLogin(), new Date());
		chat(message);
		messageInput.setText("");
		Server.instance().sendMessage(message);
	}

	public void chat(ChatMessage data) {
		messageOutput.appendText("[" + data.getAuthor() + "]: "
				+ data.getText() + "\r\n");
	}
}
