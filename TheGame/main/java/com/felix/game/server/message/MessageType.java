package com.felix.game.server.message;

import java.io.Serializable;

public enum MessageType implements Serializable{
	CHAT_MESSAGE,
	SHOOT_MESSAGE,
	UNIT_MOVE,
	UNIT_STOP,
	UNIT_ADD,
	UNIT_CRASH,
	GAME_START,
	GAME_LOAD,
	GET_ACTIVE_GAMES,
	OPERATION_SUCCESS,
	INCORRECT_PASSWORD,
	INVALID_USERNAME,
	INVALID_GAMEID,
	OPERATION_FAIL,
	USER_REGISTER,
	LOGIN,
	EXIT;
	

}
