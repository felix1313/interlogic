package com.felix.game.map.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import com.felix.game.map.Main;
import com.felix.game.map.model.Map;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

public class MapEditorController {

	private Map map;
	private Integer cursorType = 0;
	private Main mainApp;
	private final int brushCoef = 5;

	private int brushSize;
	@FXML
	private Canvas canvas;

	@FXML
	private Rectangle rectangle;
	@FXML
	private RadioButton grassRadio;
	@FXML
	private RadioButton forestRadio;
	@FXML
	private RadioButton waterRadio;
	@FXML
	private Slider brushSizeSlider;

	public void setMainApp(Main main) {
		this.mainApp = main;
	}

	@FXML
	private void handleNewMap() {

		map = new Map();
		drawMap(canvas.getGraphicsContext2D());
	}

	@FXML
	private void handleExit() {
		Action response = Dialogs.create().owner(mainApp.getPrimaryStage())
				.title("Exit").message("Are you sure you want to exit?")
				.showConfirm();
		if (response == Dialog.Actions.YES)
			Platform.exit();
	}

	@FXML
	private void handleSave() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save map");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("MAP", "*.map"),
				new FileChooser.ExtensionFilter("All files", "*.*"));
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		ObjectOutputStream out = null;
		if (file != null) {
			try {
				out = new ObjectOutputStream(new FileOutputStream(file));
				out.writeObject(map);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@FXML
	private void handleOpen() {
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
				drawMap(canvas.getGraphicsContext2D());
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
	private void handleCellTypeChanged() {
		if (grassRadio.isSelected()) {
			cursorType = 0;
		}
		if (forestRadio.isSelected()) {
			cursorType = 1;
		}
		if (waterRadio.isSelected()) {
			cursorType = 2;
		}
		rectangle.setFill(Map.numToColor(cursorType));
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
	private void handleMouseClick(MouseEvent ev) {
		double x = ev.getX();
		double y = ev.getY();
		double size = brushSize * brushCoef;
		x -= (int) (x + 0.0001) % size;
		y -= (int) (y + 0.0001) % size;
		canvas.getGraphicsContext2D().setFill(Map.numToColor(this.cursorType));
		canvas.getGraphicsContext2D().fillRect(x, y, size, size);
		// update map object
		int xindex = ((int) x) / brushCoef;
		int yindex = ((int) y) / brushCoef;
		int cnt = (int) (brushSize + 0.0001);
		for (int i = 0; i < cnt; i++)
			for (int j = 0; j < cnt; j++) {
				map.getMap()[i + xindex][j + yindex] = cursorType;
			}
	}

	@FXML
	private void handleSliderMove() {
		brushSize = (int) (brushSizeSlider.getValue() + 1e-11);
		rectangle.setWidth(brushSize * brushCoef);
		rectangle.setHeight(brushSize * brushCoef);
		System.out.println(brushSize);
	}

}
