package com.rajinder.test.skytest.exceptions;

public class FileException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FileException(String msg) {
		super(msg);
	}

	public FileException(String msg, Exception e) {
		super(msg, e);
	}

}
