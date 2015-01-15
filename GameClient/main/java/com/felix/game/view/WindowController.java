package com.felix.game.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.felix.game.dto.UserLocationDTO;
import com.felix.game.map.model.Map;
import com.felix.game.model.UnitModel;
import com.felix.game.socket.Server;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class WindowController {
	@FXML
	private Canvas canvas;
	private Map map;
	private int brushCoef = 5;
	private final Logger log = Logger.getLogger(getClass());
	private HashMap<Integer, UnitModel> models = new HashMap<>();
	private Integer me;

	private AnimationTimer timer;

	public void setId(int id) {
		log.info("my id=" + id);
		me = (id);
	}

	public void init() {
		log.info("windowcontroller init");
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(
					"/home/felix/Desktop/sample2.map"));
			this.map = (Map) in.readObject();
			this.drawMap(canvas.getGraphicsContext2D());
			// markUnit(unitX, unitY);
			timer = new AnimationTimer() {

				@Override
				public void handle(long now) {
					for (UnitModel model : models.values()) {
						/*
						 * if (unitX == targetX && unitY == targetY) {
						 * this.stop(); return; }
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
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		System.out.println(x + " " + y);
		UnitModel model = models.get(me);
		// repaintMap(model.getTargetX(), model.getTargetY());
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
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.setFill(Color.BLACK);
				gc.fillOval(x, y, brushCoef, brushCoef);
			}
		});
	}

	private void markUnit(UnitModel m) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.setFill(m.getColor());
				gc.fillOval(m.getUnitX(), m.getUnitY(), brushCoef, brushCoef);
			}
		});

	}

	private void repaintMap(int x, int y) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.setFill(Map.numToColor(map.getMap()[x / brushCoef][y
						/ brushCoef]));
				gc.fillRect(x, y, brushCoef, brushCoef);
			}
		});

	}
}
