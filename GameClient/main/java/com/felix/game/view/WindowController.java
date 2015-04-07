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
import com.felix.game.map.model.Map;
import com.felix.game.model.ChatMessage;
import com.felix.game.model.UnitModel;
import com.felix.game.server.Server;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextArea;
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
	private Map map;
	private int brushCoef = 5;
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
		this.map = mainApp.getGame().getMap();
		mainApp.getPrimaryStage().setTitle(
				"Game id: " + mainApp.getGame().getGameId());
		map.drawMap(canvasBack.getGraphicsContext2D(), brushCoef);
		// markUnit(models.get(me));
	}

	public void move(UnitPathDTO path) {

		UnitModel model = models.get(path.getUserId());
		model.setTargetLocation(path.getPath().get(path.getPath().size() - 1));
		model.startMovement(path.getPath(), canvasFront.getGraphicsContext2D(),
				brushCoef);
	}

	public void stopMovement(int userId) {
		models.get(userId).stopMoving();
	}

	public void addUnit(UserLocationDTO locationDTO) {
		log.info("unit added id=" + locationDTO.getUserId());
		UnitModel model = new UnitModel(locationDTO);
		model.setLocation(model.getLocation().zoomIn(brushCoef));
		models.put(locationDTO.getUserId(), model);
		markUnit(model);
	}

	@FXML
	private void handleCanvasClick(MouseEvent ev) {
		log.info("canvas click");
		double x = ev.getX();
		double y = ev.getY();

		int indX = (int) (x - ((int) (x + 0.0001)) % brushCoef);
		int indY = (int) (y - ((int) (y + 0.0001)) % brushCoef);
		if (!map.canGo(indX / brushCoef, indY / brushCoef)) {
			log.info("invalid target " + indX + " " + indY);
			return;
		}

		UnitModel model = models.get(me);
		model.clearTarget(canvasFront.getGraphicsContext2D());
		model.setTargetLocation(new Location(indX, indY));
		model.paintTarget(canvasFront.getGraphicsContext2D());

		List<Location> path = map.getPath(model.getLocation()
				.zoomOut(brushCoef),
				model.getTargetLocation().zoomOut(brushCoef));
		model.startMovement(path, canvasFront.getGraphicsContext2D(), brushCoef);
		Server.instance().moveUnit(new UnitPathDTO(me, path));
	}

	private void markUnit(UnitModel m) {

		m.paintLocation(canvasFront.getGraphicsContext2D());

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

	public void handleCrashMessage(UnitPathDTO data){
		if(data.getPath()==null)
			models.get(data.getUserId()).rejectCrash();
		else crashMovement(data.getUserId(),data.getPath());
	}
	public void crashMovement(int userId,List<Location> locations) {
		if (locations.size() != 2) {
			log.error("incorrect crash parameter");
			throw new InvalidParameterException();
		}
		log.trace("updating path for "+userId);
		models.get(userId).crash(locations.get(0), locations.get(1));
	}

}
