package com.felix.game.view;

import org.apache.log4j.Logger;

import com.felix.game.Main;
import com.felix.game.model.User;
import com.felix.game.server.Server;
import com.felix.game.server.message.Message;
import com.felix.game.server.message.MessageType;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

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
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Invalid input");
			alert.showAndWait();
			return;
		}
		log.info("trying to login...");

		Message res = null;
		try {
			res = Server.instance().login(loginText.getText(),
					passText.getText());
			if (res.getMessageType() == MessageType.OPERATION_SUCCESS) {
				log.info("login success");
				mainApp.setUser(new User(loginText.getText(), passText
						.getText()));
				mainApp.setUserId((int) res.getData());
				mainApp.loadGameLoadForm();
			} else {
				log.warn("failed to login " + res);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("login failed");
				alert.setContentText(res.getMessageType().toString());
				alert.showAndWait();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("connection failed ");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
		}

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
			log.info("register success id="+res.getData());
			mainApp.setUser(new User(loginText.getText(), passText
					.getText()));
			mainApp.setUserId((int) res.getData());
			mainApp.loadGameLoadForm();
		} else{
			log.warn("failed to register " + res);
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("registration failed ");
			alert.setContentText(res.getMessageType().toString());
			alert.showAndWait();
		}
	}

}
