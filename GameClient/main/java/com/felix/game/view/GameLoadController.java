package com.felix.game.view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import org.apache.log4j.Logger;

import com.felix.game.Main;
import com.felix.game.server.message.MessageType;
import com.felix.game.socket.Server;

public class GameLoadController {
	private Logger log = Logger.getLogger(getClass());
	@FXML
	private TextField gameIdText;
	@FXML
	private PasswordField passText;
	@FXML
	private RadioButton createRadio;
	@FXML
	private RadioButton joinRadio;
	private Main mainApp;

	public void setMainApp(Main main) {
		this.mainApp = main;
	}

	private boolean validateInput() {
		return !(passText.getText().isEmpty());
	}

	@FXML
	private void handleGOClick() {
		if (!validateInput()) {
			log.warn("invalid input");
			return;
		}

		MessageType res;
		if (createRadio.isSelected()) {
			log.info("trying to create game");

			res = Server.instance().createGame(passText.getText());
		} else {
			Integer gameId = Integer.parseInt(gameIdText.getText());
			log.info("trying to join game... " + gameId);
			res = Server.instance().joinGame(gameId, passText.getText());
		}
		if (res == MessageType.OPERATION_SUCCESS) {
			log.info("game loaded! ");
			mainApp.loadGameForm();
		} else
			log.warn("failed to load( " + res);
	}

	@FXML
	private void handleRadioClick() {
		if (createRadio.isSelected()) {
			gameIdText.setText("");
			gameIdText.setDisable(true);
		} else {
			gameIdText.setDisable(false);
		}
	}
}
