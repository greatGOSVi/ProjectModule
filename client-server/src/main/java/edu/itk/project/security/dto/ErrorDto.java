package edu.itk.project.security.dto;

import java.sql.Timestamp;

import org.springframework.http.HttpStatus;

public class ErrorDto {
	
	private HttpStatus status;
	
	private String message;
	
	private Timestamp timestamp;
	
	public ErrorDto() {}
	
	public ErrorDto(HttpStatus status, String message, Timestamp timestamp) {
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