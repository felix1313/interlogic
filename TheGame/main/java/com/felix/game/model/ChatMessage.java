package com.felix.game.model;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class ChatMessage implements Serializable {

	private static final long serialVersionUID = -4829907557832903594L;
	private String text;
	private String author;
	private Date time;

	public String getText() {
		return text;
	}

	public ChatMessage(String text, String author, Date time) {
		this.text = text;
		this.author = author;
		this.time = time;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
}
