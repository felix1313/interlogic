package com.felix.game.server.message;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 5527873243468448345L;
	private MessageType messageType;
	private Serializable data;

	public Message() {
	}

	public Message(MessageType messageType) {
		this.messageType = messageType;
	}

	public Message(MessageType messageType, Serializable data) {
		this.messageType = messageType;
		this.data = data;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public Serializable getData() {
		return data;
	}

	public void setData(Serializable data) {
		this.data = data;
	}

}
