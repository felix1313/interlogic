package com.felix.socket;

import java.io.Serializable;

public enum MessageType implements Serializable{
	CHAT_MESSAGE,
	UNIT_MOVE,
	GAME_START,
	GAME_LOAD,
	OPERATION_SUCCESS,
	INCORRECT_PASSWORD,
	INVALID_USERNAME,
	OPERATION_FAIL,
	USER_REGISTER,
	LOGIN,
	EXIT;
}
