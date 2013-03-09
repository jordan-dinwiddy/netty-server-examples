package com.dinwiddy.examples.netty_server.object.domain;

import java.io.Serializable;

public class ExampleMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6550060355233260403L;

	private String from; 
	private String subject; 
	private String message;

	public String getFrom() {
		return from;
	}
	public String getSubject() {
		return subject;
	}
	public String getMessage() {
		return message;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String toString() {
		return String.format("[Message from=%s, subject=%s]", from, subject);
	}
}
