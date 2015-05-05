package com.felix.game.view;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.felix.game.dto.UnitPathDTO;
import com.felix.game.dto.UserLocationDTO;
import com.felix.game.Main;
import com.felix.game.map.model.Location;
import com.felix.game.model.Bullet;
import com.felix.game.model.ChatMessage;
import com.felix.game.model.MapModel;
import com.felix.game.model.UnitModel;
import com.felix.game.server.Server;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class WindowController {
	@FXML
	private Canvas canvasBack;
	@FXML
	private Canvas canvasFront;
	@FXML
	private TextArea messageInput;
	@FXML
	private TextArea messageOutput;
	private MapModel mapModel = new MapModel();
	private final Logger log = Logger.getLogger(getClass());
	private HashMap<Integer, UnitModel> models = new HashMap<Integer, UnitModel>();
	private Integer me;
	private Main mainApp;

	public void setId(int id) {
		log.info("my id=" + id);
		me = (id);
	}

	public void init(Main mainApp) {
		log.info("windowcontroller init");
		this.mainApp = mainApp;
		this.mapModel.setMap(mainApp.getGame().getMap());
		this.mapModel.setMapGraphics(canvasBack.getGraphicsContext2D());
		this.mapModel.setUnitGraphics(canvasFront.getGraphicsContext2D());
		this.mapModel.drawMap();
		mainApp.getPrimaryStage().setTitle(
				"Game id: " + mainApp.getGame().getGameId());
		// mapModel.markUnit(models.get(me));
	}

	public void move(UnitPathDTO path) {
		UnitModel model = models.get(path.getUserId());
		model.setTargetLocation(path.getPath().get(path.getPath().size() - 1));
		long ping = System.currentTimeMillis() - path.getTime();
		model.startMovement(path.getPath(), ping);
	}

	public void stopMovement(int userId) {
		models.get(userId).stopMoving();
	}

	public void addUnit(UserLocationDTO locationDTO) {
		log.info("unit added id=" + locationDTO.getUserId());
		UnitModel model = new UnitModel(locationDTO, mapModel);

		models.put(locationDTO.getUserId(), model);
		mapModel.markUnit(model);
	}
	public void shoot(UserLocationDTO userTarget){
		models.get(userTarget.getUserId()).shoot(userTarget.getLocation());
	}
	private void handleLeftButtonClick(MouseEvent ev) {
		log.info("canvas click");

		double x = ev.getX();
		double y = ev.getY();

		if (!mapModel.canGo(x, y)) {
			log.info("invalid target " + x + " " + y);
			return;
		}

		UnitModel model = models.get(me);
		// model.clearTarget(canvasFront.getGraphicsContext2D());
		model.setTargetLocation(new Location(x, y));
		// model.paintTarget(canvasFront.getGraphicsContext2D());

		int brush = mapModel.getBrushcoef();
		List<Location> path = mapModel.getMap().getPath(
				model.getLocation().shift(brush).zoomOut(brush),
				model.getTargetLocation().shift(brush).zoomOut(brush));
		Server.instance().moveUnit(new UnitPathDTO(me, path));
		model.startMovement(path, 0);
	}

	private void handleRightButtonClick(MouseEvent ev) {
		log.info("right button click");
		Location target = new Location(ev.getX(),ev.getY());
		Server.instance().shoot(target);
		models.get(me).shoot(target);
	}

	@FXML
	private void handleCanvasClick(MouseEvent ev) {
		if (ev.getButton().equals(MouseButton.PRIMARY)) {
			handleLeftButtonClick(ev);
		} else if (ev.getButton().equals(MouseButton.SECONDARY)) {
			handleRightButtonClick(ev);
		}
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
	@Deprecated
	public void handleCrashMessage(UnitPathDTO data) {
		
	}

	@Deprecated
	public void crashMovement(int userId, List<Location> locations) {
		if (locations.size() != 2) {
			log.error("incorrect crash parameter");
			throw new InvalidParameterException();
		}
		log.trace("updating path for " + userId);
		models.get(userId).crash(locations.get(0), locations.get(1));
	}
}
