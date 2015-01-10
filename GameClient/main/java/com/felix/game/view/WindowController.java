package com.felix.game.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.felix.game.map.model.Map;

import javafx.animation.AnimationTimer;
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
	private int unitX = 100;
	private int unitY = 100;
	private int targetX;
	private int targetY;
	private AnimationTimer timer;

	public void init() {
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(
					"/home/felix/Desktop/sample2.map"));
			this.map = (Map) in.readObject();
			this.drawMap(canvas.getGraphicsContext2D());
			markUnit(unitX, unitY);
			timer = new AnimationTimer() {

				@Override
				public void handle(long now) {
					if (unitX == targetX && unitY == targetY) {
						this.stop();
						return;
					}
					repaintMap(unitX, unitY);
					if (unitX < targetX)
						unitX++;
					else if (unitX > targetX)
						unitX--;

					if (unitY < targetY)
						unitY++;
					else if (unitY > targetY)
						unitY--;
					WindowController.this.markUnit(unitX, unitY);
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

	@FXML
	private void handleCanvasClick(MouseEvent ev) {
		double x = ev.getX();
		double y = ev.getY();
		targetX = (int) (x - ((int) (x + 0.0001)) % brushCoef);
		targetY = (int) (y - ((int) (y + 0.0001)) % brushCoef);
		markTarget(targetX, targetY);
		timer.start();
	}

	private void markTarget(int x, int y) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.BLACK);
		gc.fillOval(x, y, brushCoef, brushCoef);
	}

	private void markUnit(int x, int y) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.RED);
		gc.fillOval(x, y, brushCoef, brushCoef);
	}

	private void repaintMap(int x, int y) {
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.setFill(Map.numToColor(map.getMap()[x/brushCoef][y/brushCoef]));
		gc.fillRect(x , y , brushCoef, brushCoef);
	}
}
