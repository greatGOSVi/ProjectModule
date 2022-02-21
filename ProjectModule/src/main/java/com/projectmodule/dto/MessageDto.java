package com.projectmodule.dto;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

public class MessageDto {
	
private HttpStatus status;
	
	private String message;
	
	private Timestamp timestamp;
	
	public MessageDto(HttpStatus status, String message, Timestamp timestamp) {
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
