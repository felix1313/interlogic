package com.felix.game.view;

import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
	private Logger log = Logger.getLogger(getClass());
	@FXML
	private TextField loginText;
	@FXML
	private PasswordField passText;

	private boolean validateInput() {
		return !(loginText.getText().isEmpty() || passText.getText().isEmpty());
	}

	@FXML
	private void handleLoginClick() {
		if (!validateInput()) {
			log.warn("invalid input");
			return;
		}
		log.info("trying to login");
		System.out.println(loginText.getText() + passText.getText());
	}

	@FXML
	private void handleRegisterClick() {
		if (!validateInput()) {
			log.warn("invalid input");
			return;
		}
		log.info("trying to register");
		System.out.println(loginText.getText() + passText.getText());
	}
}
