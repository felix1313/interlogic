package com.felix.game.view;

import org.apache.log4j.Logger;

import com.felix.game.Main;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;
import com.felix.game.socket.Server;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	private Logger log = Logger.getLogger(getClass());
	@FXML
	private TextField loginText;
	@FXML
	private PasswordField passText;

	private Main mainApp;

	public void setMainApp(Main main) {
		this.mainApp = main;
	}

	private boolean validateInput() {
		return !(loginText.getText().isEmpty() || passText.getText().isEmpty());
	}

	@FXML
	private void handleLoginClick() {
		if (!validateInput()) {
			log.warn("invalid input");
			return;
		}
		log.info("trying to login...");
		Message res = Server.instance().login(loginText.getText(),
				passText.getText());
		if (res.getMessageType() == MessageType.OPERATION_SUCCESS) {
			log.info("login success");
			mainApp.setUserId((int) res.getData());
			mainApp.loadGameLoadForm();
		} else
			log.warn("failed to login " + res);
	}

	@FXML
	private void handleRegisterClick() {
		if (!validateInput()) {
			log.warn("invalid input");
			return;
		}
		log.info("trying to register");
		Message res = Server.instance().register(loginText.getText(),
				passText.getText());
		if (res.getMessageType() == MessageType.OPERATION_SUCCESS) {
			log.info("register success");
			mainApp.setUserId((int) res.getData());
			mainApp.loadGameLoadForm();
		} else
			log.warn("failed to register " + res);
	}

}
